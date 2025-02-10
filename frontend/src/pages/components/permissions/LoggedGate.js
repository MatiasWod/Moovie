import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";


const LoggedGate = ({children}) => {
    const {isLoggedIn, user} = useSelector(state => state.auth);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoggedIn) {
            navigate('/login')
        }
    }, [isLoggedIn, navigate]);

    return <>{children}</>
}

export  default LoggedGate