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

          <h2 style={{ marginBottom: '20px', color: 'white' }}>Top Artists</h2>
          {dashboardData.topArtists && dashboardData.topArtists.length > 0 ? (
            <div
              className="artists-grid"
              style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(5, 1fr)',
                gap: '2rem',
                padding: '0 0 40px 0'
              }}
            >
              {dashboardData.topArtists.map((artist: any, index: number) => (
                <div
                  key={artist.id || index}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    textAlign: 'center'
                  }}
                >
                  <div
                    style={{
                      width: '100%',
                      aspectRatio: '1',
                      borderRadius: '50%',
                      overflow: 'hidden',
                      marginBottom: '12px',
                      backgroundColor: 'rgba(255, 255, 255, 0.1)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)'
                    }}
                  >
                    {artist.image ? (
                      <img
                        src={artist.image}
                        alt={artist.name || 'Artist'}
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      />
                    ) : (
                      <div style={{ color: 'rgba(255, 255, 255, 0.5)', fontSize: '3rem' }}>🎵</div>
                    )}
                  </div>
                  <p style={{ color: 'white', fontSize: '0.9rem', fontWeight: '500', margin: 0, wordBreak: 'break-word' }}>
                    {artist.name || 'Unknown Artist'}
                  </p>
                </div>
              ))}
            </div>
          ) : (
            <p>No top artists data available</p>
          )}

          <h2 style={{ marginBottom: '20px', color: 'white' }}>Top Songs</h2>
          {dashboardData.topSongs && dashboardData.topSongs.length > 0 ? (
            <div
              className="songs-grid"
              style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(5, 1fr)',
                gap: '2rem',
                padding: '0 0 40px 0'
              }}
            >
              {dashboardData.topSongs.map((song: any, index: number) => (
                <div
                  key={song.id || index}
                  style={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    textAlign: 'center'
                  }}
                >
                  <div
                    style={{
                      width: '100%',
                      aspectRatio: '1',
                      borderRadius: '8px',
                      overflow: 'hidden',
                      marginBottom: '12px',
                      backgroundColor: 'rgba(255, 255, 255, 0.1)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      boxShadow: '0 4px 8px rgba(0, 0, 0, 0.3)'
                    }}
                  >
                    {song.image ? (
                      <img
                        src={song.image}
                        alt={song.name || 'Song'}
                        style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      />
                    ) : (
                      <div style={{ color: 'rgba(255, 255, 255, 0.5)', fontSize: '3rem' }}>🎵</div>
                    )}
                  </div>
                  <p style={{ color: 'white', fontSize: '0.9rem', fontWeight: '500', margin: '0 0 4px 0', wordBreak: 'break-word' }}>
                    {song.name || 'Unknown Song'}
                  </p>
                  <p style={{ color: 'rgba(255, 255, 255, 0.7)', fontSize: '0.75rem', margin: 0, wordBreak: 'break-word' }}>
                    {song.artistName || 'Unknown Artist'}
                  </p>
                </div>
              ))}
            </div>
          ) : (
            <p>No top songs data available</p>
          )}
        </div>
      )}
    </div>
  );
}

export default Dashboard;
