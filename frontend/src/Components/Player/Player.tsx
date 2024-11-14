import UserResponse from "../../Types/UserResponse.ts";
import {Chessboard} from "react-chessboard";
import {useState, useEffect} from "react";

interface PlayerProps {
    currentUserName: string;
    selectedUser: UserResponse | null;
}

export default function Player({currentUserName, selectedUser}: PlayerProps) {
    const [fen, setFen] = useState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

    function onDrop(sourceSquare: any, targetSquare: any) {
        console.log(sourceSquare);
        console.log(targetSquare);
    }

    useEffect(() => {
        if (selectedUser) {
            setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
    }, [selectedUser]);

    return (
        <div className="main_inner">
            {selectedUser && (
                <div className="top-left-player">
                    <i className="fa-solid fa-user fa-2xl" style={{color: "#000000"}}></i> {selectedUser.name}
                </div>
            )}

            <div className="chessboard-container">
                {selectedUser ? (
                    <div className="chessboard-container">
                        <Chessboard position={fen} onPieceDrop={onDrop}/>
                    </div>
                ) : (
                    <p className="select-player-text">Select a player you want to play against!</p>
                )}
            </div>

            {selectedUser && (
                <div className="bottom-left-player">
                    <i className="fa-solid fa-user fa-2xl"></i> {currentUserName}
                </div>
            )}
        </div>
    );
}
