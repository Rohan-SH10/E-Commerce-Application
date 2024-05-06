//my useRefreshAuth
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

const useRefreshAuth = () => {
    const navigate = useNavigate()
    const [Auth, setAuth] = useState({
        userId:"",
        userRole:"CUSTOMER",
        username:"",
        authenticated:false,
        accessExpiration:0,
        refreshExpiration:0
    })
    let refreshAuth = false

    const doRefresh = async () => {
        console.log('refreshed tokens')
        //api call
        let { data: data } = await axios.get(`http://localhost:8080/api/re-v1/refresh-access`, {
            headers: {
                "Content-Type": "application/json",
            },
            withCredentials: true,
        })
        if (Auth!==null|| Auth!== undefined) localStorage.setItem("user", JSON.stringify(Auth))
        console.log(Auth)
        return data
    }

    const refresh = async () => {
        const currentUser = localStorage.getItem("user");
        if (currentUser === null || currentUser === undefined) navigate("/")
        const user = JSON.parse(currentUser)
        const accessExpiry = new Date(new Date().getTime()+(user.accessExpiration*1000))
        const refreshExpiry = new Date(new Date().getTime()+(user.refreshExpiration*1000))
        if (refreshExpiry > new Date()) {
            if (accessExpiry > new Date()) {
                const newAuth = await doRefresh()
                setAuth(newAuth)
            }
            else {
                const newAuth = await doRefresh()
                setAuth(newAuth)
            }
        }
        //navigate to home or login page
        else navigate("/")
    }

    useEffect(() => {
        if (!refreshAuth) {
            refresh()
            refreshAuth = true
        }
    },[])

    return { Auth, refresh };
}

export default useRefreshAuth