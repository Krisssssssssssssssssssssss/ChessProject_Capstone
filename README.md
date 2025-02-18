![Chess](https://github.com/user-attachments/assets/34bce289-504a-48d7-92db-d19606c9235b)

# Welcome to My Chess Game! ♟️

Hey you! 🎉\
Chess has always been my passion, and that's why I developed this chess game software to bring strategy and excitement to life. Built with a robust stack for seamless performance:

- **Backend:** Java (Spring Boot)
- **Database:** MongoDB
- **Frontend:** React with TypeScript

Try it out here: [Kristijan's Chess Game](https://kristijans-chess-game.onrender.com/)

---

## How to Play:

1. **Create a username** to get started.
2. **Choose an opponent** by clicking the icon next to their name.
3. **Move your pieces** by dragging them to their destination.
   - Currently, this is a **single-player game** where two players can play on the same device (or one player can try both sides).
   - **Coming soon:** Multiplayer functionality! 🚀

---

## Installation Guide

### Clone the Repo:

```bash
git clone https://github.com/your-repo/chess-game.git
cd chess-game
```

### Backend Setup:

1. Build and run the Java project for the backend (Spring Boot).

2. Set up environment variables for backend functionality. Add the following to your `.env` file (edit to an actual Mongo URL so that you can run it):

   ```
   MONGODB_URI=mongodb+srv://<Username>:<password>@testcluster.c4csk.mongodb.net/ClusterName
   APP_URL=http://localhost:5173
   ID_GITHUB=asdfsdfwas123
   OAUTH_SECRET_GITHUB=asdasf123
   ```

   *(Note: Replace ****`ID_GITHUB`**** and ****`OAUTH_SECRET_GITHUB`**** with your actual GitHub credentials if you want to work on the code and have Sonar on your pull requests.)*

3. Create a dummy MongoDB cluster with two collections:

   - **Game Collection example:**
     ```json
     {
       "_id": "deb808d4-4979-407f-9d5f-136335aec254",
       "fenString": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
       "playerOneId": "a9153648-776b-4d60-8a50-ddcc639bee40",
       "playerTwoId": "84ffdec6-2c0b-4d0b-8c78-0a7a20e2716c",
       "isWhite": true,
       "enPassant": {},
       "castlingModel": {},
       "_class": "com.example.backend.model.GameModel"
     }
     ```
   - **Player Collection:**
     ```json
     {
       "_id": "1",
       "name": "admin",
       "password": "admin",
       "rating": "1500",
       "isAdmin": true,
       "isGitHubUser": false,
       "_class": "com.example.backend.model.UserModel"
     }
     ```

4. Run the backend to enable the API and game logic.

---

### Frontend Setup:

1. Navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies and build the frontend:
   ```bash
   npm install
   npm run build
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. Open the application in your browser at [http://localhost:5173](http://localhost:5173).

---

### Customizing for GitHub Integration:

If you fork this repository and want to enable GitHub OAuth and CI tools like SonarCloud for pull requests:

1. Replace `ID_GITHUB` and `OAUTH_SECRET_GITHUB` with your GitHub credentials.
2. Update your CI/CD configuration for integration with your setup.

---

### Enjoy the Game! 🏆

This project is a labor of love for chess and tech. Feel free to explore, learn, and enhance it. Let’s make it better together!

