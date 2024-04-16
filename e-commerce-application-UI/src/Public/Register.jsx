import React from "react";
import img1 from "../image/img1.jpg";
import { useState } from "react";
const Register = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const errors = {};

    if (!name) {
      errors.name = "Name is required";
    } else if (!/^[A-Z][a-zA-Z0-9]*$/.test(name)) {
      errors.name = "Name should be in upper camel case";
    }

    if (!email) {
      errors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      errors.email = "Invalid email address";
    } else if (!email.endsWith("@gmail.com")) {
      errors.email = "Email should end with @gmail.com";
    }

    if (!password) {
      errors.password = "Password is required";
    } else if (
      !/(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}/.test(
        password
      )
    ) {
      errors.password =
        "Password must contain at least one alphabet, one number, and one special character";
    }

    setErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
    }
  };
  return (
    <form onSubmit={handleSubmit}>
      <div className="flex justify-center items-center mt-32">
        <div id="loginbody" className="w-[800px] h-[400px]  flex">
          <div
            id="loginsec1"
            className="w-[400px] h-[400px]   flex flex-col justify-center   items-center bg-blue-700 "
          >
            <div>
              <img src={img1} alt="" height="150px" width="150px" />
            </div>
            <div id="logoandname">
              <p className="italic text-white text-2xl">Flipkart</p>
            </div>
          </div>
          <div
            id="loginsec2"
            className="w-[400px] bg-gray-100 flex justify-around items-center h-[400px] "
          >
            <div
              id="logsubsec"
              className=" h-[300px] w-[300px] flex flex-col justify-around "
            >
              <div id="loginpara" className=" text-center text-lg font-bold">
                <p>Sign Up</p>
              </div>
              <div id="textbox">
                <div>
                  <p className="font-mono">Enter the Name</p>
                  <input
                    type="text"
                    placeholder="name"
                    className="w-full rounded-md  border-blue-300 border-2 focus:outline-blue-500 sm:text-sm h-10 placeholder:italic placeholder:text-gray-400 block pr-3 shadow-sm font-mono text-2"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                  {errors.name && (
                    <p className="text-red-500 text-xs italic">{errors.name}</p>
                  )}
                </div>
                <div>
                  <p className="mt-2 font-mono">Enter the Mail</p>
                  <input
                    type="text"
                    className="w-full rounded-md  border-blue-300 border-2 focus:outline-blue-500 sm:text-sm h-10 placeholder:italic placeholder:text-gray-400 block pr-3 shadow-sm"
                    placeholder="example@gmail.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                  {errors.email && (
                    <p className="text-red-500 text-xs italic">
                      {errors.email}
                    </p>
                  )}
                </div>

                <p className="mt-2 font-mono">Enter the Password</p>
                <input
                  type="text"
                  className="w-full rounded-md  border-blue-300 border-2 focus:outline-blue-500 sm:text-sm h-10 placeholder:italic placeholder:text-gray-400 block pr-3 shadow-sm"
                  placeholder="***********"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                {errors.password && (
                  <p className="text-red-500 text-xs italic">
                    {errors.password}
                  </p>
                )}
              </div>
              <div className="flex justify-around items-center">
                <button className="bg-blue-600 hover:bg-blue-800 font-mono rounded-full w-[70px] text-white text-xl">
                  Save
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  );
};

export default Register;
