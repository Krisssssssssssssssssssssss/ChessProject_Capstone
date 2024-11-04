import { useState } from 'react';
import axios from "axios";

export default function RegisterUser() {
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccessMessage('');

        if (password !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        const userRequest = {
            name,
            password
        };

        try {
            const response = await axios.post("/api/users", userRequest);
            setSuccessMessage("User created successfully!");
            console.log("Response Data:", response.data);
        } catch (err) {
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
        <div>
            <h2>Register User</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Name:
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
    );
}
