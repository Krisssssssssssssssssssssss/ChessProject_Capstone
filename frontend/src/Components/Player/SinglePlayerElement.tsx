import UserResponse from "../../Types/UserResponse.ts";

interface SinglePlayerProps {
    user: UserResponse;
    setSelectedUser: (user: UserResponse) => void;
}

export default function SinglePlayerElement({ user, setSelectedUser }: SinglePlayerProps) {
    return (
        <div className="single_player">
            <div>{user.name}</div>
            <button onClick={() => setSelectedUser(user)} className="fa-solid fa-chess-king">
            </button>
        </div>
    );
}
