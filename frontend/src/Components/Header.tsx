import {Link} from "react-router-dom";

export default function Header() {
    return (
        <div className={"header"}>
            <Link to="/">
                <h1>JuSt a cHesS gAme!</h1>
            </Link>
            <hr></hr>
        </div>
    )
}