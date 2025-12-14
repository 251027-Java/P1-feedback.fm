import { useEffect, useState, type ChangeEvent } from 'react';
import { songsAPI } from '../services/api';

function TopSongs() {
  const [songs, setSongs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [timeRange, setTimeRange] = useState('medium_term');
  const [filterText, setFilterText] = useState('');

  const handleTimeRangeChange = (e: ChangeEvent<HTMLSelectElement>) => {
    setTimeRange(e.target.value);
  };

  const handleFilterChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFilterText(e.target.value);
  };

  useEffect(() => {
    const fetchTopSongs = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await songsAPI.getTopSongs(timeRange);
        setSongs(response.data || []);
      } catch (err: any) {
        console.error('Error fetching top songs:', err);
        setError(err.response?.data?.message || 'Failed to load top songs');
      } finally {
        setLoading(false);
      }
    };

    fetchTopSongs();
  }, [timeRange]);

  const filteredSongs = songs.filter((song: any) =>
    song.name?.toLowerCase().includes(filterText.toLowerCase()) ||
    song.artist?.toLowerCase().includes(filterText.toLowerCase())
  );

  if (loading) return <div style={{ padding: '20px', color: 'white' }}>Loading...</div>;

  if (error) return <div style={{ padding: '20px', color: '#ff6b6b' }}>Error: {error}</div>;

  return (
    <div style={{ 
      padding: '0 20px 20px 20px', 
      color: 'white',
      minHeight: '100%'
    }}>
      <h1 style={{ marginBottom: '20px', color: 'white' }}>Top Songs</h1>
      <div>
        <label>
          Time Range:
          <select value={timeRange} onChange={handleTimeRangeChange}>
            <option value="short_term">Last 4 weeks</option>
            <option value="medium_term">Last 6 months</option>
            <option value="long_term">All time</option>
          </select>
        </label>
        <input
          type="text"
          value={filterText}
          onChange={handleFilterChange}
          placeholder="Filter songs..."
        />
      </div>
      {filteredSongs.length > 0 ? (
        <ul>
          {filteredSongs.map((song: any, index: number) => (
            <li key={song.id || index}>
              {song.name || 'Unknown Song'} - {song.artist || 'Unknown Artist'}
            </li>
          ))}
        </ul>
      ) : (
        <p>No songs found</p>
      )}
    </div>
  );
}

export default TopSongs;
