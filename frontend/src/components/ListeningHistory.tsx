import { useEffect, useState } from 'react';
import { historyAPI } from '../services/api';

function ListeningHistory() {
  const [history, setHistory] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [limit, setLimit] = useState(50);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await historyAPI.getHistory(limit);
        setHistory(response.data || []);
      } catch (err: any) {
        console.error('Error fetching listening history:', err);
        setError(err.response?.data?.message || 'Failed to load listening history');
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, [limit]);

  if (loading) return <div style={{ padding: '20px', color: 'white' }}>Loading...</div>;

  if (error) return <div style={{ padding: '20px', color: '#ff6b6b' }}>Error: {error}</div>;

  return (
    <div style={{ 
      padding: '0 20px 20px 20px', 
      color: 'white',
      minHeight: '100%'
    }}>
      <h1 style={{ marginBottom: '20px', color: 'white' }}>Listening History</h1>
      <div>
        <label>
          Limit:
          <input
            type="number"
            value={limit}
            onChange={(e) => setLimit(parseInt(e.target.value, 10) || 50)}
            min="1"
            max="100"
          />
        </label>
      </div>
      {history.length > 0 ? (
        <ul>
          {history.map((item: any, index: number) => (
            <li key={item.id || index}>
              {item.songName || 'Unknown Song'} - {item.artistName || 'Unknown Artist'}
              {item.playedAt && (
                <span> ({(() => {
                  try {
                    return new Date(item.playedAt).toLocaleString();
                  } catch {
                    return item.playedAt;
                  }
                })()})</span>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p>No listening history found</p>
      )}
    </div>
  );
}

export default ListeningHistory;
