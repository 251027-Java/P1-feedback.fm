import { useEffect, useState, type ChangeEvent } from 'react';
import { userAPI } from '../services/api';

function Dashboard() {
  const [dashboardData, setDashboardData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [refreshInterval, setRefreshInterval] = useState(30);

  const handleIntervalChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value, 10);
    if (!isNaN(value) && value > 0) {
      setRefreshInterval(value);
    }
  };

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        setLoading(true);
        setError(null);
        const userId = localStorage.getItem('userId');
        if (!userId) {
          setError('User ID not found. Please login.');
          setLoading(false);
          return;
        }
        const response = await userAPI.getDashboard(userId);
        setDashboardData(response.data);
      } catch (err: any) {
        console.error('Error fetching dashboard:', err);
        setError(err.response?.data?.message || 'Failed to load dashboard');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();

    // Set up auto-refresh if interval is set
    if (refreshInterval > 0) {
      const intervalId = setInterval(fetchDashboard, refreshInterval * 1000);
      return () => clearInterval(intervalId);
    }
  }, [refreshInterval]);

  if (loading) return <div>Loading...</div>;

  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <h1>Dashboard</h1>
      <div>
        <label>
          Auto-refresh interval (seconds):
          <input
            type="number"
            value={refreshInterval}
            onChange={handleIntervalChange}
            min="10"
            max="300"
          />
        </label>
        <p>Dashboard will refresh every {refreshInterval} seconds</p>
      </div>
      
      {dashboardData && (
        <div>
          <h2>User Info</h2>
          <div>
            <p><strong>Username:</strong> {dashboardData.username || 'N/A'}</p>
            <p><strong>Email:</strong> {dashboardData.email || 'N/A'}</p>
          </div>

          <h2>Statistics</h2>
          {dashboardData.stats && (
            <div>
              <p><strong>Total Listening Time:</strong> {dashboardData.stats.totalListeningTime || '0 hours'}</p>
              <p><strong>Songs Played:</strong> {dashboardData.stats.songsPlayed || 0}</p>
              <p><strong>Current Streak:</strong> {dashboardData.stats.currentStreak || 0} days</p>
            </div>
          )}

          <h2>Top Artists</h2>
          {dashboardData.topArtists && dashboardData.topArtists.length > 0 ? (
            <ul>
              {dashboardData.topArtists.slice(0, 10).map((artist: any, index: number) => (
                <li key={artist.id || artist.artistId || index}>
                  {index + 1}. {artist.name || artist.artistName || 'Unknown Artist'}
                </li>
              ))}
            </ul>
          ) : (
            <p>No top artists data available</p>
          )}

          <h2>Top Songs</h2>
          {dashboardData.topSongs && dashboardData.topSongs.length > 0 ? (
            <ul>
              {dashboardData.topSongs.slice(0, 10).map((song: any, index: number) => (
                <li key={song.id || song.songId || index}>
                  {index + 1}. {song.name || song.songName || 'Unknown Song'} - {song.artistName || song.artist?.name || 'Unknown Artist'}
                </li>
              ))}
            </ul>
          ) : (
            <p>No top songs data available</p>
          )}
        </div>
      )}
    </div>
  );
}

export default Dashboard;
