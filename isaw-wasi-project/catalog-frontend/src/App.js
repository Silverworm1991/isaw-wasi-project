import React, { useState, useEffect } from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { CssBaseline, Box, CircularProgress } from '@mui/material';
import AuthForm from './components/Auth/Login';
import Navbar from './components/Layout/Navbar';

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#0a0a0a',
      paper: '#1a1a1a',
    },
  },
});

function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    if (token && username) {
      setUser({ username, token });
    }
    setLoading(false);
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setUser(null);
  };

  if (loading) {
    return (
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
            <CircularProgress />
          </Box>
        </ThemeProvider>
    );
  }

  return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {user ? (
            <>
              <Navbar user={user} onLogout={handleLogout} />
              <Box sx={{ p: 3 }}>
                <h1>Welcome to your catalog, {user.username}!</h1>
                <p>Catalog functionality coming next...</p>
              </Box>
            </>
        ) : (
            <AuthForm onLogin={handleLogin} />
        )}
      </ThemeProvider>
  );
}

export default App;