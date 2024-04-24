import React from 'react'
import Header from './Util/Header'
import { Outlet } from 'react-router-dom'
import { useAuth } from './Auth/AuthProvider'

const App = (props) => {
  const { user, updateUser } = useAuth()
  console.log(user)
  const userAuth = props?.userAuth;
  return (
      <div>
        <Header userAuth={userAuth} />
        <Outlet />
      </div>
  )
}

export default App
