import { useState, useEffect, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI, setAccessToken } from '../services/api';
import LiquidEther from './LiquidEther';

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
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      overflow: 'hidden',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      background: '#0a0a0f'
    }}>
      {/* Liquid Ether Background */}
      <LiquidEther 
        colors={['#5227FF', '#FF9FFC', '#B19EEF']}
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%',
          height: '100%',
          zIndex: 0
        }}
      />

      {/* Login Card */}
      <div style={{
        position: 'relative',
        zIndex: 10,
        backgroundColor: 'rgba(255, 255, 255, 0.05)',
        backdropFilter: 'blur(10px)',
        border: '1px solid rgba(255, 255, 255, 0.1)',
        borderRadius: '20px',
        padding: '3rem 4rem',
        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.3)',
        minWidth: '400px',
        textAlign: 'center'
      }}>
        <h1 style={{
          fontSize: '2.5rem',
          fontWeight: '700',
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          backgroundClip: 'text',
          marginBottom: '0.5rem'
        }}>
          feedback.fm
        </h1>
        <p style={{
          color: 'rgba(255, 255, 255, 0.7)',
          marginBottom: '2rem',
          fontSize: '1.1rem'
        }}>
          Your Spotify Statistics
        </p>

        {error && (
          <div style={{
            color: '#ff6b6b',
            backgroundColor: 'rgba(255, 107, 107, 0.1)',
            border: '1px solid rgba(255, 107, 107, 0.3)',
            borderRadius: '8px',
            padding: '0.75rem',
            marginBottom: '1.5rem',
            fontSize: '0.9rem'
          }}>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            disabled={loading}
            style={{
              padding: '1rem',
              borderRadius: '10px',
              border: '1px solid rgba(255, 255, 255, 0.2)',
              backgroundColor: 'rgba(255, 255, 255, 0.05)',
              color: 'white',
              fontSize: '1rem',
              outline: 'none',
              transition: 'all 0.3s ease'
            }}
            onFocus={(e) => {
              e.target.style.borderColor = 'rgba(147, 51, 234, 0.5)';
              e.target.style.backgroundColor = 'rgba(255, 255, 255, 0.1)';
            }}
            onBlur={(e) => {
              e.target.style.borderColor = 'rgba(255, 255, 255, 0.2)';
              e.target.style.backgroundColor = 'rgba(255, 255, 255, 0.05)';
            }}
          />
          <button
            type="submit"
            disabled={loading}
            style={{
              padding: '1rem 2rem',
              borderRadius: '10px',
              border: 'none',
              background: loading
                ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white',
              fontSize: '1rem',
              fontWeight: '600',
              cursor: loading ? 'not-allowed' : 'pointer',
              opacity: loading ? 0.7 : 1,
              transition: 'all 0.3s ease',
              boxShadow: '0 4px 15px rgba(102, 126, 234, 0.4)'
            }}
            onMouseEnter={(e) => {
              if (!loading) {
                e.currentTarget.style.transform = 'translateY(-2px)';
                e.currentTarget.style.boxShadow = '0 6px 20px rgba(102, 126, 234, 0.6)';
              }
            }}
            onMouseLeave={(e) => {
              if (!loading) {
                e.currentTarget.style.transform = 'translateY(0)';
                e.currentTarget.style.boxShadow = '0 4px 15px rgba(102, 126, 234, 0.4)';
              }
            }}
          >
            {loading ? 'Loading...' : 'Login with Spotify'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
