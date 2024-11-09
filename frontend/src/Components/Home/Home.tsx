import axios from "axios";
import UserResponse from "../../Types/UserResponse.ts";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import AllPlayers from "../Users/AllPlayers.tsx";
import Admin from "../Admin/Admin.tsx";
import Player from "../Player/Player.tsx";

interface HomeProps {
    user: UserResponse | null;
    setUser: (user: UserResponse | null) => void;
    setUserName: (name: string) => void;
}

export default function Home({ user, setUser, setUserName }: HomeProps) {
    const navigate = useNavigate();
    const [allUsers, setAllUsers] = useState<UserResponse[]>([]);
    const [selectedUser, setSelectedUser] = useState<UserResponse | null>(null);
    const [userGotUpdated, setUserGotUpdated] = useState(false);

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
        axios.get<UserResponse[]>("/api/users")
            .then((result) => setAllUsers(result.data))
            .catch(() => console.log("Something went wrong"));
    }, [userGotUpdated]);

    if (!user) return null;

    return (
        <div className="home_page">
            <div className="user_details">
                <h5>{user.name}</h5>
                <button onClick={logout}>Logout</button>
            </div>

            <div className="main">
                {user.isAdmin ? (
                    <Admin selectedUser={selectedUser} setSelectedUser={setSelectedUser} setAllUsers={setAllUsers} setUserGotUpdated={setUserGotUpdated} userGotUpdated={userGotUpdated}/>
                ) : (
                    <Player currentUserName={user.name} selectedUser={selectedUser} />
                )}
            </div>

            <AllPlayers currentUser={user} allUsers={allUsers} setSelectedUser={setSelectedUser} />
        </div>
    );
}
