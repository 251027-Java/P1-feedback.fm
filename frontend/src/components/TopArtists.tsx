import { useEffect, useState, type ChangeEvent } from 'react';
import { artistsAPI } from '../services/api';

function TopArtists() {
  const [artists, setArtists] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [timeRange, setTimeRange] = useState('medium_term');
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearchChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleTimeRangeChange = (e: ChangeEvent<HTMLSelectElement>) => {
    setTimeRange(e.target.value);
  };

  useEffect(() => {
    const fetchTopArtists = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await artistsAPI.getTopArtists(timeRange);
        setArtists(response.data || []);
      } catch (err: any) {
        console.error('Error fetching top artists:', err);
        setError(err.response?.data?.message || 'Failed to load top artists');
      } finally {
        setLoading(false);
      }
    };

    fetchTopArtists();
  }, [timeRange]);

  const filteredArtists = artists.filter((artist: any) =>
    artist.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) return <div style={{ padding: '20px', color: 'white' }}>Loading...</div>;

  if (error) return <div style={{ padding: '20px', color: '#ff6b6b' }}>Error: {error}</div>;

  return (
    <div style={{ 
      padding: '0 20px 20px 20px', 
      color: 'white',
      minHeight: '100%'
    }}>
      <h1 style={{ marginBottom: '20px', color: 'white' }}>Top Artists</h1>
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
          value={searchTerm}
          onChange={handleSearchChange}
          placeholder="Search artists..."
        />
      </div>
      {filteredArtists.length > 0 ? (
        <ul>
          {filteredArtists.map((artist: any, index: number) => (
            <li key={artist.id || index}>
              {artist.name || 'Unknown Artist'}
            </li>
          ))}
        </ul>
      ) : (
        <p>No artists found</p>
      )}
    </div>
  );
}

export default TopArtists;
