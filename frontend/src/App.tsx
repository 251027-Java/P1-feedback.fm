import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import TopArtists from './components/TopArtists';
import TopSongs from './components/TopSongs';
import ListeningHistory from './components/ListeningHistory';
import CurrentlyPlaying from './components/CurrentlyPlaying';

function AppContent() {
  const location = useLocation();
  const isLoginPage = location.pathname === '/';

  return (
    <div className="App">
      {!isLoginPage && <Navbar />}
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/top-artists" element={<TopArtists />} />
        <Route path="/top-songs" element={<TopSongs />} />
        <Route path="/history" element={<ListeningHistory />} />
        <Route path="/currently-playing" element={<CurrentlyPlaying />} />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
