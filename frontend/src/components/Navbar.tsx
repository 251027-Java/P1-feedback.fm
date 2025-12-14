import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav>
      <Link to="/">Login</Link>
      <Link to="/dashboard">Dashboard</Link>
      <Link to="/top-artists">Top Artists</Link>
      <Link to="/top-songs">Top Songs</Link>
      <Link to="/history">History</Link>
      <Link to="/currently-playing">Now Playing</Link>
    </nav>
  );
}

export default Navbar;
