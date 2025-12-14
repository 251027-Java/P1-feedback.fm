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

  // Handle OAuth callback if token is in URL (from backend redirect)
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const listenerId = urlParams.get('listenerId');
    const spotifyToken = urlParams.get('spotifyToken');
    const error = urlParams.get('error');
    
    if (error) {
      setError(error);
      setLoading(false);
      // Clean up URL
      window.history.replaceState({}, document.title, '/');
      return;
    }
    
    if (token) {
      // Token is already provided by backend redirect
      setLoading(true);
      setAccessToken(token);
      if (listenerId) {
        localStorage.setItem('userId', listenerId);
      }
      // Store Spotify access token for API calls
      if (spotifyToken) {
        localStorage.setItem('spotifyAccessToken', spotifyToken);
      }
      // Clean up URL and navigate
      window.history.replaceState({}, document.title, '/');
      navigate('/dashboard');
      return;
    }
    
    // Fallback: if code is present but no token, call the API (legacy support)
    const code = urlParams.get('code');
    if (code) {
      handleCallback(code);
    }
  }, [navigate]);

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
