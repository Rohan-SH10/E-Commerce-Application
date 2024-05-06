import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAuth } from '../Auth/AuthProvider';
import useRefreshAuth from '../Auth/useRefreshAuth';

const Home = () => {
    const { user } = useAuth();
    const { refresh } = useRefreshAuth();
    const { state } = useLocation();

    useEffect(() => {
        if (user.authenticated && user.accessExpiration < new Date()) {
            refresh(); // Trigger refresh when the user's access token has expired
        }
    }, []);


    return (
        <div className="flex justify-center bg-gray-300 items-start h-dvh">
            <div id="homebody" className="w-full h-[100px] flex flex-col">
                <div id="home" className="w-full bg-gradient-to-t from-blue-200 to-slate-50 rounded-b-2xl flex flex-col justify-evenly items-start pl-2 h-[150px]">
                    {user.authenticated ? (
                        <div>{user.username} is logged in successfully</div>
                    ) : (
                        <div>Homepage</div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Home;
