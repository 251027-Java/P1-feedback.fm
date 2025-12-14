import { Link, useNavigate } from 'react-router-dom';
import { removeAccessToken } from '../services/api';

function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Clear all tokens and user data
    removeAccessToken();
    localStorage.removeItem('spotifyAccessToken');
    localStorage.removeItem('userId');
    
    // Redirect to login page
    navigate('/');
  };

  return (
    <nav style={{ 
      display: 'flex', 
      justifyContent: 'space-between', 
      alignItems: 'center',
      padding: '10px 20px',
      borderBottom: '1px solid #ccc'
    }}>
      <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
        <Link to="/dashboard" style={{ textDecoration: 'none', marginRight: '10px' }}>Dashboard</Link>
        <Link to="/top-artists" style={{ textDecoration: 'none', marginRight: '10px' }}>Top Artists</Link>
        <Link to="/top-songs" style={{ textDecoration: 'none', marginRight: '10px' }}>Top Songs</Link>
        <Link to="/history" style={{ textDecoration: 'none', marginRight: '10px' }}>History</Link>
        <Link to="/currently-playing" style={{ textDecoration: 'none' }}>Now Playing</Link>
      </div>
      <button 
        onClick={handleLogout}
        style={{
          padding: '8px 16px',
          backgroundColor: '#dc3545',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        Logout
      </button>
    </nav>
  );
}

export default Navbar;
