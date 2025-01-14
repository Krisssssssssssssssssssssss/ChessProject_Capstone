import { Route, Routes } from 'react-router-dom';
import RegisterUser from "./Components/RegisterUser.tsx";
import Login from "./Components/Home/Login.tsx";
import Header from "./Components/Header.tsx";
import Footer from "./Components/Footer.tsx";
import { useState, useEffect } from "react";
import Home from "./Components/Home/Home.tsx";
import UserResponse from "./Types/UserResponse.ts";

function App() {
    const [userName, setUserName] = useState("");
    const [user, setUser] = useState<UserResponse | null>(null);

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            const parsedUser = JSON.parse(storedUser) as UserResponse;
            setUser(parsedUser);
            setUserName(parsedUser.name);
        }
    }, []);

    return (
        <div className="app-container">
            <Header/>
            <Routes>
                <Route
                    path="/"
                    element={<Login userName={userName} setUserName={setUserName} user={user} setUser={setUser}/>}
                />
                <Route path="/register" element={<RegisterUser/>}/>
                <Route path="/home" element={<Home user={user} setUser={setUser} setUserName={setUserName}/>}/>
            </Routes>
            <Footer/>
        </div>
    );
}

export default App;
