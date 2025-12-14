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
          <pre>{JSON.stringify(dashboardData, null, 2)}</pre>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
