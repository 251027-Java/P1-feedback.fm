import { useEffect, useState } from 'react';
import { songsAPI } from '../services/api';

function CurrentlyPlaying() {
  const [currentTrack, setCurrentTrack] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchCurrentlyPlaying = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await songsAPI.getCurrentlyPlaying();
        setCurrentTrack(response.data);
      } catch (err: any) {
        console.error('Error fetching currently playing:', err);
        setError(err.response?.data?.message || 'Failed to load currently playing track');
      } finally {
        setLoading(false);
      }
    };

    fetchCurrentlyPlaying();

    // Poll every 5 seconds for updates
    const intervalId = setInterval(fetchCurrentlyPlaying, 5000);
    return () => clearInterval(intervalId);
  }, []);

  if (loading) return <div>Loading...</div>;

  if (error) return <div>Error: {error}</div>;

  if (!currentTrack) {
    return (
      <div>
        <h1>Currently Playing</h1>
        <p>No track is currently playing</p>
      </div>
    );
  }

  return (
    <div>
      <h1>Currently Playing</h1>
      <div>
        <h2>{currentTrack.name || 'Unknown Track'}</h2>
        <p>Artist: {currentTrack.artist || 'Unknown Artist'}</p>
        <p>Album: {currentTrack.album || 'Unknown Album'}</p>
        {currentTrack.isPlaying !== undefined && (
          <p>Status: {currentTrack.isPlaying ? 'Playing' : 'Paused'}</p>
        )}
      </div>
    </div>
  );
}

export default CurrentlyPlaying;
