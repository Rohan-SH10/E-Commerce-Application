import React, { useEffect, useState } from "react";
import useRefreshAuth from "./useRefreshAuth";

export const authContext = React.createContext({});

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser
      ? JSON.parse(storedUser)
      : {
          userId: "",
          userRole: "CUSTOMER",
          username: "",
          authenticated: false,
          accessExpiration: 0,
          refreshExpiration: 0,
        };
  });

  const { Auth } = useRefreshAuth();
  useEffect(() => {
    if (Auth !== null || Auth !== undefined)
      localStorage.setItem("user", JSON.stringify(Auth));
    console.log(Auth);
  }, [Auth]);

  return (
    <authContext.Provider value={{ user, setUser }}>
      {children}
    </authContext.Provider>
  );
};

export default AuthProvider;

export const useAuth = () => React.useContext(authContext);
