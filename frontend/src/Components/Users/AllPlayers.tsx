import UserResponse from "../../Types/UserResponse.ts";
import SinglePlayerElement from "../Player/SinglePlayerElement.tsx";

interface AllPlayersProps {
    currentUser: UserResponse;
    allUsers: UserResponse[];
    setSelectedUser: (user: UserResponse | null) => void;
}

export default function AllPlayers({ currentUser, allUsers, setSelectedUser }: AllPlayersProps) {
    return (
        <div className="all_players">
            {allUsers
                .filter(user => user.name !== "admin" && user.name !== currentUser.name)
                .map(user => (
                    <SinglePlayerElement
                        key={user.id}
                        user={user}
                        setSelectedUser={setSelectedUser}
                    />
                ))
            }
        </div>
    );
}