import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import userApi from '../api/UserApi';
import api from '../api/api';

const initialState = {
  isLoggedIn: false,
  user: null,
  status: 'idle',
  error: null,
  errorTranslationKey: null,
  authInitialized: false,
};

export const loginUser = createAsyncThunk(
  'auth/loginUser',
  async ({ username, password }, { rejectWithValue }) => {
    try {
      const response = await userApi.login({ username, password });
      return response.data;
    } catch (error) {
      // Pass the translation key and status to the UI
      return rejectWithValue({
        translationKey: error.translationKey || 'login.loginFailed',
        message: error.message || 'Login failed',
        status: error.status || 500,
      });
    }
  }
);

export const attemptReconnect = createAsyncThunk(
  'auth/attemptReconnect',
  async (_, { dispatch }) => {
    const token = localStorage.getItem('jwt') || sessionStorage.getItem('jwt');
    const username = localStorage.getItem('username') || sessionStorage.getItem('username');
    console.log('attempting reconnect', token, username);
    if (token && username) {
      try {
        const response = await api.get(`/users/${username}`);
        if (response.status === 200) {
          return response.data;
        }
      } catch (error) {
        console.error('Reconnect failed:', error);
      }
    }
    throw new Error('Reconnect failed');
  }
);

export const refreshUserData = createAsyncThunk(
  'auth/refreshUserData',
  async (_, { getState, rejectWithValue }) => {
    const state = getState();
    if (state.auth.isLoggedIn && state.auth.user) {
      try {
        const response = await api.get(`/users/${state.auth.user.username}`);
        return response.data;
      } catch (error) {
        // Provide more detailed error information for better handling
        if (error.response?.status === 401) {
          return rejectWithValue({
            status: 401,
            message: 'Authentication failed',
          });
        }
        // Don't reject with value for network errors - let them be handled as regular rejections
        throw error;
      }
    }
    throw new Error('Not logged in');
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    logout(state) {
      console.log('logging out');
      state.isLoggedIn = false;
      state.user = null;
      sessionStorage.removeItem('jwt');
      sessionStorage.removeItem('username');
      sessionStorage.removeItem('refreshToken');
      localStorage.removeItem('jwt');
      localStorage.removeItem('username');
      localStorage.removeItem('refreshToken');
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginUser.pending, (state) => {
        state.status = 'loading';
        state.error = null;
        state.errorTranslationKey = null;
      })
      .addCase(loginUser.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.isLoggedIn = true;
        state.user = action.payload;
      })
      .addCase(loginUser.rejected, (state, action) => {
        state.status = 'failed';
        // Handle both rejectWithValue and regular errors
        if (action.payload) {
          state.error = action.payload.message;
          state.errorTranslationKey = action.payload.translationKey;
        } else {
          state.error = action.error.message || 'Login failed';
          state.errorTranslationKey = 'login.loginFailed';
        }
      })
      .addCase(attemptReconnect.fulfilled, (state, action) => {
        state.status = 'succeeded';
        state.isLoggedIn = true;
        state.user = action.payload;
        state.authInitialized = true;
      })
      .addCase(attemptReconnect.rejected, (state) => {
        state.status = 'idle';
        state.authInitialized = true;
      })
      .addCase(refreshUserData.fulfilled, (state, action) => {
        state.user = action.payload;
      })
      .addCase(refreshUserData.rejected, (state, action) => {
        console.log('refreshUserData rejected:', action.error.message);
        // Only clear storage if it's an authentication error (401) or "Not logged in"
        // Don't clear on network errors or other temporary issues
        if (
          action.error.message === 'Not logged in' ||
          (action.meta?.rejectedWithValue && action.payload?.status === 401)
        ) {
          console.log('Authentication error - clearing storage');
          state.isLoggedIn = false;
          state.user = null;
          sessionStorage.removeItem('jwt');
          sessionStorage.removeItem('username');
          sessionStorage.removeItem('refreshToken');
          localStorage.removeItem('jwt');
          localStorage.removeItem('username');
          localStorage.removeItem('refreshToken');
        } else {
          console.log('Network or temporary error - keeping session');
        }
      });
  },
});

export const { logout } = authSlice.actions;
export default authSlice.reducer;
