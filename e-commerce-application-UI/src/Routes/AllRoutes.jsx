import React from "react";
import { Route, Routes } from "react-router-dom";
import App from "../App.jsx";
import Login from "../Public/Login.jsx";
import SellerDashBoard from "../Private/Seller/SellerDashboard.jsx";
import AddProduct from "../Private/Seller/AddProduct.jsx";
import Register from "../Public/Register.jsx";
import Home from "../Public/Home.jsx";
import Cart from "../Private/Customer/Cart.jsx";
import Whishlist from "../Private/Customer/Wishlist.jsx";
import Explore from "../Private/Customer/Explore.jsx";
import AddAddress from "../Private/Common/AddAdress.jsx";
import EditProfile from "../Private/Common/EditProfile.jsx";
import Orders from "../Private/Seller/Orders.jsx";
const AllRoutes = () => {
  const user = {
    userId: "1",
    userName: "Hari",
    role: "CUSTOMER",
    authenticated: true,
    accessExpiration: "3600",
    refreshExpiration: "1296000",
  };
  const { role, authenticated } = user;
  let routes = [];
  if (authenticated) {
    role == "SELLER"
      ? routes.push(
          <Route
            key={"sellerdashboard"}
            path="/sellerdashboard"
            element={<SellerDashBoard />}
          />,
          <Route
            key={"addproduct"}
            path="/addproduct"
            element={<AddProduct />}
          />,
          <Route key={"orders"} path="/orders" element={<Orders />} />
        )
      : role == "CUSTOMER" &&
        routes.push(
          <Route key={"explore"} path="/explore" element={<Explore />} />,
          <Route key={"cart"} path="/cart" element={<Cart />} />,
          <Route key={"wishlist"} path="/wishlist" element={<Whishlist />} />
        );
    routes.push(
      <Route key={"home"} path="/" element={<Home />} />,
      <Route key={"addaddress"} path="/addaddress" element={<AddAddress />} />,
      <Route
        key={"editprofile"}
        path="/editprofile"
        element={<EditProfile />}
      />
    );
  } else {
    role === "CUSTOMER" &&
      routes.push(
        <Route key={"home"} path="/" element={<Home />} />,
        <Route key={"login"} path="/login" element={<Login />} />,
        <Route key={"explore"} path="/explore" element={<Explore />} />,
        <Route key={"register"} path="/register" element={<Register />} />
      );
  }
  return (
    <Routes>
      <Route path="/" element={<App />}>
        {routes}
      </Route>
      ;
    </Routes>
  );
};

export default AllRoutes;
