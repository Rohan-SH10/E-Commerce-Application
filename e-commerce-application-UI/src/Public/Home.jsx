import React, { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom';
import { useAuth } from '../Auth/AuthProvider';

const Home = () => {
  const { user, updateUser } = useAuth()
  const { state } = useLocation();
  const users = state && state?.data?.data;
  console.log(users)
  return (
    <div>
      Home
    </div>
  )
}

export default Home
