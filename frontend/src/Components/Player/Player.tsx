import UserResponse from "../../Types/UserResponse.ts";
import {Chessboard} from "react-chessboard";
import {useState, useEffect} from "react";
import GameResponse from "../../Types/GameResponse.ts";
import axios from "axios";

interface PlayerProps {
    currentUser: UserResponse | null;
    selectedUser: UserResponse | null;
}

export default function Player({currentUser, selectedUser}: PlayerProps) {
    const [fen, setFen] = useState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1.");

    function onDrop(sourceSquare: any, targetSquare: any) {
        console.log(sourceSquare);
        console.log(targetSquare);
        return true;
    }

    useEffect(() => {
        if (currentUser?.id && selectedUser?.id) {
            axios.get<boolean>(`api/game/doesGameExist/${currentUser.id}/${selectedUser.id}`)
                .then((response: { data: boolean }) => {
                    if (response.data) {
                        axios.get<GameResponse>(`api/game/getGame/${currentUser.id}/${selectedUser.id}`)
                            .then((response: { data: GameResponse }) => {
                                setFen(response.data.fenString);
                            })
                            .catch(() => {
                                console.log("Something went wrong! The game couldn't be loaded.");
                            });
                    } else {
                        axios.post<GameResponse>(`api/game`, { playerOneId: currentUser.id, playerTwoId: selectedUser.id })
                            .then((response: { data: GameResponse }) => {
                                setFen(response.data.fenString);
                            })
                            .catch(() => {
                                console.log("Something went wrong! The game couldn't be loaded.");
                            });
                    }
                })
                .catch((error: any) => {
                    console.log("Error checking if the game exists:", error);
                });
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
                    <i className="fa-solid fa-user fa-2xl"></i> {currentUser?.name}
                </div>
            )}
        </div>
    );
}
