import { useState } from 'react';
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function RegisterUser() {
    const [name, setName] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [successMessage, setSuccessMessage] = useState<string>('');

    const navigate = useNavigate();
    const handleNavigate = () => {
        navigate("/");
    };

    const isValidUsername = (username: string): boolean => {
        const minLength = 6;
        const hasUpperCase = /[A-Z]/.test(username);
        const hasLowerCase = /[a-z]/.test(username);
        return username.length >= minLength && hasUpperCase && hasLowerCase;
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');
        setSuccessMessage('');

        if (!isValidUsername(name)) {
            setError("Username must be at least 6 characters long and include both uppercase and lowercase letters.");
            return;
        }

        if (password !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        const userRequest = {
            name,
            password,
            isGitHubUser: false,
        };

        try {
            const response = await axios.post("/api/users", userRequest);
            setSuccessMessage("User created successfully!");
            console.log("Response Data:", response.data);
            setName('');
            setPassword('');
            setConfirmPassword('');
        } catch (err: any) {
            if (err.response && err.response.status === 409) {
                console.log("Error: Username already exists");
                setError("Username already exists. Please choose a different username.");
            } else {
                console.log("Error: Something went wrong", err);
                setError("Something went wrong. Please try again.");
            }
        }
    };

    return (
        <>
            <div className={"loginFormOuter register"}>
                <h2>Register User</h2>
                <form onSubmit={handleSubmit} className={"registerUserForm"}>
                    <label>
                        Username:
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <label>
                        Password:
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <label>
                        Confirm Password:
                        <input
                            type="password"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    {error && <p style={{ color: 'red' }}>{error}</p>}
                    {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
                    <button type="submit">Register</button>
                </form>
            </div>
            <div className={"noAccount_PleaseRegister"}>
                <p>Registering was successful? Click
                    <a className="RegisterSpan"
                       onClick={handleNavigate}
                       tabIndex={0}
                       onKeyDown={(e) => {
                           if (e.key === 'Enter') handleNavigate();
                       }}
                       role="button"> here </a>
                    to go to the login page
                </p>
            </div>
        </>
    );
}
