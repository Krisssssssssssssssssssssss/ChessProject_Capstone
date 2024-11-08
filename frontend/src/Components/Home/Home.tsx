import axios from "axios";
import UserResponse from "../../Types/UserResponse.ts";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

interface HomeProps {
    user: UserResponse | null;
    setUser: (user: UserResponse | null) => void;
    setUserName: (name: string) => void;
}

export default function Home({ user, setUser, setUserName }: HomeProps) {
    const navigate = useNavigate();

    const logout = async () => {
        try {
            await axios.post(`/api/auth/logout`, {});
            setUserName("");
            setUser(null);
            navigate("/");
        } catch (error) {
            console.error("Error logging out:", error);
        }
    };

    useEffect(() => {
        console.log("User in Home:", user);
    }, [user]);

    if (!user) return null;

    return (
        <div className="loginFormOuter">
            <div>Welcome! {user.name}</div>
            <div>Admin: {user.isAdmin ? "Yes" : "No"}</div>
            <div>GitHub User: {user.isGitHubUser ? "Yes" : "No"}</div>
            <button onClick={logout}>Logout</button>
        </div>
    );
}
