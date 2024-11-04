import {Route, Routes} from 'react-router-dom'
import './App.css'
import Home from "./Components/Home/Home.tsx";
import RegisterUser from "./Components/RegisterUser.tsx";

function App() {

  return (
    <>
        <Routes>
            <Route path="/" element={<Home />}/>
            <Route path="/register" element={<RegisterUser />}/>
        </Routes>
    </>
  )
}

export default App
