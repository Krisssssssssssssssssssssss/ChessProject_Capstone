import axios from "axios";
import UserResponse from "../../Types/UserResponse.ts";
import {useEffect, useState} from "react";

interface HomeProps {
    userName: string;
    setUserName: (name: string) => void;
}

export default function Home({userName, setUserName}: HomeProps) {
    const [user, setUser] = useState<UserResponse | null>(null);

    const logoutGitHub = async () => {
        console.log("Logout button clicked");

        try {
            await axios.post(`/api/auth/logout`, {});
            console.log("Logged out successfully");

            setUserName("");

            const response = await axios.get('/api/auth/me');
            if (!response.data) {
                console.log("Session cleared on server.");
            } else {
                console.warn("Session might not be cleared; user still found:", response.data);
            }
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };

    const generateRandomPassword = (length: number) => {
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+';
        return Array.from({ length }, () => characters.charAt(Math.floor(Math.random() * characters.length))).join('');
    };

    const createUserIfNotExists = async (name: string) => {
        try {
            const userResponse = await axios.get(`/api/users/find_by_name/${name}`);
            if (userResponse.data) {
                console.log(`${name} already exists`);
                setUser(userResponse.data);
                console.log(userResponse.data);
                return;
            }

            const newUserResponse = await axios.post<UserResponse>('/api/users', {
                name,
                password: generateRandomPassword(12),
                isGitHubUser: true
            });
            setUserName(newUserResponse.data.name);
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

    return (
        <div className={"loginFormOuter"}>
            <div>Welcome! {userName}</div>
            <button onClick={logoutGitHub}>Logout</button>
        </div>
    );
}
