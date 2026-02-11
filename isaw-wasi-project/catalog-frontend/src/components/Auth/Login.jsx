import React, { useState } from 'react';
import {
    Box,
    Card,
    CardContent,
    TextField,
    Button,
    Typography,
    Alert,
    Container,
    Tab,
    Tabs,
} from '@mui/material';
import { Movie } from '@mui/icons-material';
import { authAPI } from '../../services/api';

const AuthForm = ({ onLogin }) => {
    const [tab, setTab] = useState(0); // 0 = login, 1 = register
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    // Login form state
    const [loginData, setLoginData] = useState({
        username: '',
        password: '',
    });

    // Register form state
    const [registerData, setRegisterData] = useState({
        username: '',
        email: '',
        password: '',
    });

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await authAPI.login(loginData);
            const { token, username, email } = response.data;

            localStorage.setItem('token', token);
            localStorage.setItem('username', username);

            onLogin({ token, username, email });
        } catch (err) {
            setError(err.response?.data || 'Login failed');
        } finally {
            setLoading(false);
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            const response = await authAPI.register(registerData);
            setSuccess('Registration successful! You can now login.');
            setTab(0); // Switch to login tab
            setRegisterData({ username: '', email: '', password: '' });
        } catch (err) {
            setError(err.response?.data || 'Registration failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
                    <Movie sx={{ fontSize: 40, mr: 2, color: 'primary.main' }} />
                    <Typography variant="h3" component="h1" color="primary">
                        I saw, was I
                    </Typography>
                </Box>

                <Card sx={{ width: '100%', maxWidth: 400 }}>
                    <Tabs value={tab} onChange={(e, newValue) => setTab(newValue)} centered>
                        <Tab label="Login" />
                        <Tab label="Register" />
                    </Tabs>

                    <CardContent sx={{ p: 3 }}>
                        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
                        {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

                        {tab === 0 ? (
                            // Login Form
                            <Box component="form" onSubmit={handleLogin}>
                                <TextField
                                    fullWidth
                                    label="Username"
                                    margin="normal"
                                    value={loginData.username}
                                    onChange={(e) => setLoginData({ ...loginData, username: e.target.value })}
                                    required
                                />
                                <TextField
                                    fullWidth
                                    label="Password"
                                    type="password"
                                    margin="normal"
                                    value={loginData.password}
                                    onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
                                    required
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2 }}
                                    disabled={loading}
                                >
                                    {loading ? 'Logging in...' : 'Login'}
                                </Button>
                            </Box>
                        ) : (
                            // Register Form
                            <Box component="form" onSubmit={handleRegister}>
                                <TextField
                                    fullWidth
                                    label="Username"
                                    margin="normal"
                                    value={registerData.username}
                                    onChange={(e) => setRegisterData({ ...registerData, username: e.target.value })}
                                    required
                                />
                                <TextField
                                    fullWidth
                                    label="Email"
                                    type="email"
                                    margin="normal"
                                    value={registerData.email}
                                    onChange={(e) => setRegisterData({ ...registerData, email: e.target.value })}
                                    required
                                />
                                <TextField
                                    fullWidth
                                    label="Password"
                                    type="password"
                                    margin="normal"
                                    value={registerData.password}
                                    onChange={(e) => setRegisterData({ ...registerData, password: e.target.value })}
                                    required
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2 }}
                                    disabled={loading}
                                >
                                    {loading ? 'Registering...' : 'Register'}
                                </Button>
                            </Box>
                        )}
                    </CardContent>
                </Card>
            </Box>
        </Container>
    );
};

export default AuthForm;