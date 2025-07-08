import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import React from 'react';
import NavbarComponent from '../../../pages/components/navBar/navbar';

jest.mock('react-redux', () => ({
    useSelector: jest.fn(),
    useDispatch: () => jest.fn(),
}));

jest.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key) => key,
    }),
}));

const mockOnLocationChange = jest.fn();

beforeAll(() => {
    // Removed warnings
    jest.spyOn(console, 'warn').mockImplementation(() => {});
    jest.spyOn(console, 'error').mockImplementation(() => {});
});

describe('NavbarComponent', () => {
    it('renders login link when not logged in', () => {
        require('react-redux').useSelector.mockReturnValueOnce({ isLoggedIn: false, user: null });

        render(
            <BrowserRouter>
                <NavbarComponent onLocationChange={mockOnLocationChange} />
            </BrowserRouter>
        );

        expect(screen.getByText('navBar.login')).toBeInTheDocument();
    });

    it('renders user dropdown when logged in', () => {
        require('react-redux').useSelector.mockReturnValueOnce({
            isLoggedIn: true,
            user: {
                username: 'john_doe',
                role: 1,
                url: '/images/user.png',
            },
        });

        render(
            <BrowserRouter>
                <NavbarComponent onLocationChange={mockOnLocationChange} />
            </BrowserRouter>
        );

        expect(screen.getByText('john_doe')).toBeInTheDocument();
    });
});
