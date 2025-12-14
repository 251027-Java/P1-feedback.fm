import { useEffect, useState } from 'react';
import { userAPI } from '../services/api';

function Dashboard() {
  const [dashboardData, setDashboardData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [imageError, setImageError] = useState(false);
  const refreshInterval = 60; // Fixed to 1 minute (60 seconds)

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
        console.log('Fetching dashboard data at:', new Date().toISOString());
        const response = await userAPI.getDashboard(userId);
        console.log('Dashboard data received:', response.data);
        console.log('Profile image URL:', response.data?.profileImage);
        setDashboardData(response.data);
        setImageError(false); // Reset image error on new data fetch
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
      const intervalId = setInterval(() => {
        console.log('Auto-refreshing dashboard at:', new Date().toISOString());
        fetchDashboard();
      }, refreshInterval * 1000);
      return () => clearInterval(intervalId);
    }
  }, [refreshInterval]);

  if (loading) return <div style={{ padding: '20px', color: 'white' }}>Loading...</div>;

  if (error) return <div style={{ padding: '20px', color: '#ff6b6b' }}>Error: {error}</div>;

  return (
    <div style={{ 
      padding: '0 20px 20px 20px', 
      color: 'white',
      minHeight: '100%',
      width: '100%',
      boxSizing: 'border-box'
    }}>
      <h1 style={{ marginBottom: '20px', color: 'white' }}>Dashboard</h1>
      
      {dashboardData && (
        <div>
          <h2 style={{ marginBottom: '20px', color: 'white' }}>User Info</h2>
          <div
            style={{
              display: 'flex',
              flexDirection: 'row',
              alignItems: 'flex-start',
              gap: '2rem',
              marginBottom: '40px'
            }}
          >
            {/* Profile Picture */}
            <div
              style={{
                width: '120px',
                height: '120px',
                minWidth: '120px',
                borderRadius: '50%',
                overflow: 'hidden',
                backgroundColor: 'rgba(255, 255, 255, 0.1)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)'
              }}
            >
              {dashboardData.profileImage && !imageError ? (
                <img
                  src={dashboardData.profileImage}
                  alt={dashboardData.username || 'User'}
                  style={{
                    width: '100%',
                    height: '100%',
                    objectFit: 'cover'
                  }}
                  onError={() => {
                    console.error('Failed to load profile image:', dashboardData.profileImage);
                    setImageError(true);
                  }}
                />
              ) : (
                <div style={{ color: 'rgba(255, 255, 255, 0.5)', fontSize: '3rem' }}>
                  👤
                </div>
              )}
            </div>

            {/* Username and Stats */}
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                flex: 1,
                gap: '1rem'
              }}
            >
              <div>
                <h3 style={{ margin: '0 0 8px 0', color: 'white', fontSize: '1.5rem' }}>
                  {dashboardData.username || 'N/A'}
                </h3>
                {dashboardData.email && (
                  <p style={{ margin: 0, color: 'rgba(255, 255, 255, 0.7)', fontSize: '0.9rem' }}>
                    {dashboardData.email}
                  </p>
                )}
              </div>

              {dashboardData.stats && (
                <div
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '0.75rem'
                  }}
                >
                  <div>
                    <strong style={{ color: 'rgba(255, 255, 255, 0.8)' }}>Total Listening Time:</strong>{' '}
                    <span style={{ color: 'white' }}>{dashboardData.stats.totalListeningTime || '0 hours'}</span>
                  </div>
                  <div>
                    <strong style={{ color: 'rgba(255, 255, 255, 0.8)' }}>Songs Played:</strong>{' '}
                    <span style={{ color: 'white' }}>{dashboardData.stats.songsPlayed || 0}</span>
                  </div>
                  <div>
                    <strong style={{ color: 'rgba(255, 255, 255, 0.8)' }}>Current Streak:</strong>{' '}
                    <span style={{ color: 'white' }}>{dashboardData.stats.currentStreak || 0} days</span>
                  </div>
                </div>
              )}
            </div>
          </div>

          <h2>Top Artists</h2>
          {dashboardData.topArtists && dashboardData.topArtists.length > 0 ? (
            <ul>
              {dashboardData.topArtists.slice(0, 5).map((artist: any, index: number) => (
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
              {dashboardData.topSongs.slice(0, 5).map((song: any, index: number) => (
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
