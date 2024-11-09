import { useState, useEffect } from "react";
import UserResponse from "../../Types/UserResponse.ts";
import axios from "axios";
import {useNavigate} from "react-router-dom";

interface AdminProps {
    selectedUser: UserResponse | null;
    setSelectedUser: (user: UserResponse | null) => void;
    setAllUsers: (users: UserResponse[]) => void;
    userGotUpdated: boolean;
    setUserGotUpdated: (updated: boolean) => void;
}

export default function Admin({ selectedUser, setSelectedUser, setAllUsers, userGotUpdated, setUserGotUpdated }: AdminProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [editedUser, setEditedUser] = useState<UserResponse | null>(null);
    const [originalUser, setOriginalUser] = useState<UserResponse | null>(null);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();
    useEffect(() => {
        setEditedUser(selectedUser);
        setOriginalUser(selectedUser);
    }, [selectedUser]);

    const handleDelete  = async () => {
        const confirmResult = confirm("Are you sure you want to delete this user?");
        if (confirmResult) {
            try {
                await axios.delete(`/api/users/${editedUser!.id}`);
                axios.get<UserResponse[]>("/api/users")
                    .then((result) => {
                        setAllUsers(result.data);
                        setSelectedUser(null);
                    })
                    .catch(() => console.log("Something went wrong"));

            }
            catch (error) {
                console.log(error);
            }
        }
    };

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleUpdate = async () => {
        setIsEditing(false);
        setError('');

        const userRequest = {
            name: editedUser!.name,
            password: editedUser!.password,
            isGitHubUser: editedUser!.isGitHubUser,
            rating: editedUser!.rating,
            isAdmin: editedUser!.isAdmin
        };

        try {
            await axios.put(`/api/users/${editedUser!.id}`, userRequest);
            setSuccessMessage("User updated successfully!");
            setOriginalUser(editedUser);
            setUserGotUpdated(!userGotUpdated);

        } catch (err) {
            setError("Something went wrong. Please try again.");
            setEditedUser(originalUser);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.target;
        setEditedUser((prevUser) =>
            prevUser ? { ...prevUser, [name]: type === "checkbox" ? checked : value } : null
        );
    };
    const handleBack = () => {
        setSelectedUser(null);
        setIsEditing(false);
        setError('');
        setSuccessMessage('');
        navigate("/home");
    };

    if (!editedUser) return <p>Select the player you want to manage</p>;

    return (
        <div className="main_inner">
            <div className="details">
                <div className="detail_row">
                    <label>Selected user:</label>
                    {isEditing ? (

                            editedUser.isGitHubUser ? <span>{editedUser.name}</span> : <input
                                type="text"
                                name="name"
                                value={editedUser.name}
                                onChange={handleChange}
                            />

                    ) : (
                        <span>{editedUser.name}</span>
                    )}
                </div>
                <div className="detail_row">
                    <label>Rating:</label>
                    {isEditing ? (
                        <input
                            type="text"
                            name="rating"
                            value={editedUser.rating}
                            onChange={handleChange}
                        />
                    ) : (
                        <span>{editedUser.rating}</span>
                    )}
                </div>
                <div className="detail_row">
                    <label>Admin role:</label>
                    {isEditing ? (
                        <input
                            type="checkbox"
                            name="isAdmin"
                            checked={editedUser.isAdmin ?? false}
                            onChange={handleChange}
                        />
                    ) : (
                        <span>{editedUser.isAdmin ? "Yes" : "No"}</span>
                    )}
                </div>
                <div className="detail_row">
                    <label>Password:</label>
                    {isEditing ? (
                        <input
                            type="text"
                            name="password"
                            value={editedUser.password}
                            onChange={handleChange}
                        />
                    ) : (
                        <span>{editedUser.password}</span>
                    )}
                </div>
            </div>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
            <div className="actions">
                {!isEditing ? (
                    <button className="edit_button" onClick={handleEdit}>Edit</button>
                ) : (
                    <button className="update_button" onClick={handleUpdate}>Update</button>
                )}
                {!isEditing ? (
                    <button className="delete_button" onClick={handleDelete}>Delete</button>
                ) : (
                    <button className="update_button" onClick={handleBack}>Back</button>
                )}
            </div>
        </div>
    );
}
