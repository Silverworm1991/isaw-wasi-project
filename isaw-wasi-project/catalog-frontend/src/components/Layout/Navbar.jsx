import React from 'react';
import {
    AppBar,
    Toolbar,
    Typography,
    Button,
    Box,
} from '@mui/material';
import { Movie, ExitToApp } from '@mui/icons-material';

const Navbar = ({ user, onLogout }) => {
    return (
        <AppBar position="static">
            <Toolbar>
                <Movie sx={{ mr: 2 }} />
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    I saw, Was I - Welcome, {user.username}!
                </Typography>
                <Box>
                    <Button
                        color="inherit"
                        startIcon={<ExitToApp />}
                        onClick={onLogout}
                    >
                        Logout
                    </Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;