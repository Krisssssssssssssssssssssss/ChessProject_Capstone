import UserResponse from "../../Types/UserResponse.ts";

interface AdminProps {
    selectedUser: UserResponse | null;
}

export default function Admin ({ selectedUser }: AdminProps) {
    return (
        <div className={"main_inner"}>
            <div className={"main_inner"}>Admin Page</div>
            <div>Selected user: {selectedUser?.name}</div>
        </div>
    )
}