import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './Style/index.css'
import './Style/Main.css'
import './Style/HomePage.css'
import './Style/AdminComponent.css'
import App from './App.tsx'
import {BrowserRouter} from "react-router-dom";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <BrowserRouter>
            <App/>
        </BrowserRouter>
    </StrictMode>,
)
