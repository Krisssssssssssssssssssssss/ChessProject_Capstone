import UserResponse from "../../Types/UserResponse.ts";

interface PlayerProps {
    currentUserName: string;
    selectedUser: UserResponse | null;
}
export default function Player ({ currentUserName, selectedUser }: PlayerProps) {

    return (
        <div className={"main_inner"}>
            <div className={"main_inner"}>Player Page</div>
            <div>Current name: {currentUserName}</div>
            <div>Selected user: {selectedUser?.name}</div>
        </div>

    )
}