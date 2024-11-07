import { Route, Routes } from 'react-router-dom';
import RegisterUser from "./Components/RegisterUser.tsx";
import Login from "./Components/Login.tsx";
import Header from "./Components/Header.tsx";
import Footer from "./Components/Footer.tsx";
import {  useState } from "react";
import Home from "./Components/Home/Home.tsx";

function App() {
    const [userName, setUserName] = useState("");

    return (
        <>
            <Header />
            <Routes>
                <Route
                    path="/"
                    element={<Login userName={userName} setUserName={setUserName} />}
                />
                <Route path="/register" element={<RegisterUser />} />
                <Route path="/home" element={<Home userName={userName} setUserName={setUserName}/>} />
            </Routes>
            <Footer />
        </>
    );
}

export default App;
