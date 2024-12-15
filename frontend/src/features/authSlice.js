import {createSlice, createAsyncThunk} from '@reduxjs/toolkit';
import userApi from '../api/UserApi';
import api from '../api/api';

const initialState = {
    isLoggedIn: false,
    user: null,
    status: 'idle',
    error: null,
};

export const loginUser = createAsyncThunk('auth/loginUser', async ({username, password}) => {
    const response = await userApi.login({username, password});
    return response.data;
});

export const fetchUserData = createAsyncThunk('auth/fetchUserData', async (username) => {
    const response = await api.get(`/users/username/${username}`);
    return response.data;
});

export const attemptReconnect = createAsyncThunk('auth/attemptReconnect', async (_, {dispatch}) => {
    const token = sessionStorage.getItem('jwtToken');
    const username = sessionStorage.getItem('username');

    console.log("ABOUT TO RECONNECT ",token, username);
    
    if (token && username) {
        try {
            const response = await api.get(`/users/username/${username}`);
            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Reconnect failed:', error);
        }
    }
    throw new Error('Reconnect failed');
});

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        logout(state) {
            state.isLoggedIn = false;
            state.user = null;
            sessionStorage.removeItem('jwtToken');
            sessionStorage.removeItem('username');
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('username');
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(loginUser.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.isLoggedIn = true;
                state.user = action.payload;
                sessionStorage.setItem('username', action.payload.username);
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(attemptReconnect.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.isLoggedIn = true;
                state.user = action.payload;
            })
            .addCase(attemptReconnect.rejected, (state) => {
                state.status = 'idle';
            })
            .addCase(fetchUserData.fulfilled, (state, action) => {
                state.user = action.payload;
            });
    },
});

export const {logout} = authSlice.actions;

export default authSlice.reducer;