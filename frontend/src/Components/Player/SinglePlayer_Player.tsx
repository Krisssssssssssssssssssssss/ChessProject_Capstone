import UserResponse from "../../Types/UserResponse.ts";

interface SimglePlayerProps {
    key: string;
    user: UserResponse;
}

export default function SinglePlayer_Player ({key, user}: SimglePlayerProps){
    return (
        <div className={"main_inner"}>
            <div>Player</div>
            <div className={"main_inner"}>{user.name}</div>
        </div>
    )
}