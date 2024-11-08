import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Home from "./Home/Home.tsx";
import UserResponse from "../Types/UserResponse.ts";

interface LoginProps {
    userName: string;
    setUserName: (name: string) => void;
    user: UserResponse | null;
    setUser: (user: UserResponse | null) => void;
}

export default function Login({userName, setUserName, user, setUser}: LoginProps) {
    const [userInputValue, setUserInputValue] = useState("");
    const [passwordInputValue, setPasswordInputValue] = useState('');
    const navigate = useNavigate();

    const navigateToRegisterPage = () => {
        navigate("/register");
    };


    const loadUser = () => {
        axios.get('/api/auth/me')
            .then(response => {
                setUserName(response.data);
            })
            .catch(() => {
                console.log("Error: User login failed. Please try again.");
                setUserName(user!.name);
            });
    };

    const handleLogin = (isGitHubUser : boolean) => {
        if (isGitHubUser) {
            handleLoginGitHub()
        }
        else {
            handleLoginDatabase();
        }
    }

    const handleLoginGitHub = () => {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin;
        window.open(host + '/oauth2/authorization/github', '_self');
        axios.get('/api/auth/me')
            .then(response => {
                setUserName(response.data);
            })
            .catch(() => {
                console.log("Error: User login failed. Please try again.");
            });
    };

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            const parsedUser = JSON.parse(storedUser) as UserResponse;
            setUser(parsedUser);
            setUserName(parsedUser.name);
            navigate("/home");
        }
    }, [navigate, setUser, setUserName]);

    const handleLoginDatabase = async () => {
        try {
            const userResponse = await axios.post<UserResponse>('/api/users/login', {
                name: userInputValue,
                password: passwordInputValue,
                isGitHubUser: false
            });

            if (userResponse.data) {
                setUser(userResponse.data);
                setUserName(userResponse.data.name);

                // Store user data in localStorage
                localStorage.setItem("user", JSON.stringify(userResponse.data));

                setUserInputValue("");
                setPasswordInputValue("");
                navigate("/home");
            }
        } catch (error) {
            console.error("Login failed: ", error);
        }
    };


    const generateRandomPassword = (length: number) => {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+';
        const charactersLength = characters.length;
        const array = new Uint32Array(length);
        crypto.getRandomValues(array);

        return Array.from(array, (num) => characters.charAt(num % charactersLength)).join('');
    };

    const createUserIfNotExists = async (name: string) => {
        try {
            const userResponse = await axios.get(`/api/users/find_by_name/${name}`);
            if (userResponse.data) {
                setUser(userResponse.data);
                return;
            }

            const newUserResponse = await axios.post<UserResponse>('/api/users', {
                name,
                password: generateRandomPassword(12),
                isGitHubUser: true
            });
            setUser(newUserResponse.data);
            console.log(`${name} created`);
        } catch (err) {
            console.error("Error checking or creating GitHub user:", err);
        }
    };

    useEffect(() => {
        if (userName) {
            createUserIfNotExists(userName);
        }
    }, [userName]);


    useEffect(() => {
        if(userName){
            navigate("/home")
        }
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
                            value={passwordInputValue}
                            onChange={(e) => setPasswordInputValue(e.target.value)}
                            required
                        />

                        <button type="button" onClick={() => handleLogin(false)}>Login</button>
                    </form>
                    <button type="button" onClick={() => handleLogin(true)}>Login with GitHub <i
                        className="fa-brands fa-github"></i></button>

                </div>
                <div className="noAccount_PleaseRegister">
                    <p>Don't have an account yet? Click
                        <a className="RegisterSpan"
                           onClick={navigateToRegisterPage}
                           tabIndex={0}
                           onKeyDown={(e) => {
                               if (e.key === 'Enter') navigateToRegisterPage();
                           }}
                           role="button"> here </a>
                        to register.
                    </p>
                </div>
            </>
        );
    } else {
        return (
            <Home user={user} setUser={setUser} setUserName={setUserName}/>
        );
    }
}