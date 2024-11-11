import UserResponse from "../../Types/UserResponse.ts";
import ChessboardLocal from "../ChessboardLocal.tsx";
import { Chessboard } from "react-chessboard";


interface PlayerProps {
    currentUserName: string;
    selectedUser: UserResponse | null;
}

export default function Player({ currentUserName, selectedUser }: PlayerProps) {
    return (
        <div className="main_inner">
            {selectedUser ? (
                <div className="top-left-player">
                    <i className="fa-solid fa-user fa-2xl" style={{ color: "#000000" }}></i> {selectedUser.name}
                </div>
            ) : (
                <p className="select-player-text">Select a player you want to play against!</p>
            )}

            <div className="chessboard-container">
                <div className="chessboard-container" style={{maxHeight: '80vh'}}>
                    <Chessboard id="BasicBoard"/>
                </div>

            </div>

            {selectedUser && (
                <div className="bottom-left-player">
                <i className="fa-solid fa-user fa-2xl"></i> {currentUserName}
                </div>
            )}
        </div>
    );
}
