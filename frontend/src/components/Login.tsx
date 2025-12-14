import { useState, useEffect, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI, setAccessToken } from '../services/api';

function Login() {
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleCallback = async (code: string) => {
    try {
      setLoading(true);
      const response = await authAPI.handleCallback(code);
      if (response.data.token) {
        setAccessToken(response.data.token);
        if (response.data.listenerId) {
          localStorage.setItem('userId', response.data.listenerId);
        }
        navigate('/dashboard');
      }
    } catch (err: any) {
      console.error('Error handling callback:', err);
      setError(err.response?.data?.message || 'Failed to complete login');
      setLoading(false);
    }
  };

  // Handle OAuth callback if code is in URL
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    if (code) {
      handleCallback(code);
    }
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await authAPI.getAuthUrl();
      const authUrl = response.data.authUrl;
      if (authUrl) {
        window.location.href = authUrl;
      }
    } catch (err: any) {
      console.error('Error getting auth URL:', err);
      setError(err.response?.data?.message || 'Failed to initiate login');
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Login</h1>
      {error && <div style={{ color: 'red' }}>Error: {error}</div>}
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Username"
          disabled={loading}
        />
        <button type="submit" disabled={loading}>
          {loading ? 'Loading...' : 'Login with Spotify'}
        </button>
      </form>
    </div>
  );
}

export default Login;
