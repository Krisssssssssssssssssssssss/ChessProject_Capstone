import axios from "axios";
import UserResponse from "../../Types/UserResponse.ts";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import Admin from "../Users/Admin.tsx";
import Player from "../Users/Player.tsx";
import AllPlayers from "../Users/AllPlayers.tsx";

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
            localStorage.removeItem("user");

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
        <div className="home_page">
            <div className="user_details">
                <h5>{user.name}</h5>
                <button onClick={logout}>Logout</button>
            </div>

            <div className="main">
                {user.isAdmin ? <Admin /> : <Player />}
            </div>

            <AllPlayers />
        </div>
    );
}
