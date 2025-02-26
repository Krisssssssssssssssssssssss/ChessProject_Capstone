type UserModel = {
    id: string;
    name: string;
    password: string;
    rating: string;
    isAdmin: boolean | null;
    isGitHubUser: boolean | null;
};
export default UserModel;