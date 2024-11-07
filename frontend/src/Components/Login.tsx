import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Home from "./Home/Home.tsx";

interface LoginProps {
    userName: string;
    setUserName: (name: string) => void;
}

export default function Login({userName, setUserName}: LoginProps) {
    const [userInputValue, setUserInputValue] = useState("");
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleNavigate = () => {
        navigate("/register");
    };

    function login() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(host + '/oauth2/authorization/github', '_self');
        axios.get('/api/auth/me')
    }

    const loadUser = () => {
        axios.get('/api/auth/me')
            .then(response => {
                const githubUserName = response.data;
                setUserName(githubUserName);
            })
            .catch(() => {
                console.log("Error: User login failed. Please try again.");
            });
    };

    const handleLoginGitHub = () => {
        login();
    };

    const handleLoginDatabase = () => {
        setUserName(userInputValue);
    };

    useEffect(() => {
        loadUser();
    }, []);

    if (!userName) {
        return (
            <>
                <div className="loginFormOuter">
                    <h2>Please log in:</h2>
                    <form onSubmit={(e) => e.preventDefault()}>
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            value={userInputValue}
                            onChange={(e) => setUserInputValue(e.target.value)}
                            required
                        />

                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <button type="button" onClick={handleLoginDatabase}>Login</button>
                    </form>
                    <button type="button" onClick={handleLoginGitHub}>Login with GitHub   <i
                        className="fa-brands fa-github"></i></button>

                </div>
                <div className="noAccount_PleaseRegister">
                    <p>Don't have an account yet? Click
                        <a className="RegisterSpan"
                            onClick={handleNavigate}
                            tabIndex={0}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') handleNavigate();
                            }}
                            role="button"> here </a>
                        to register.
                    </p>
                </div>
            </>
        );
    } else {
        return (
            <Home userName={userName} setUserName={setUserName}/>
        );
    }
}
