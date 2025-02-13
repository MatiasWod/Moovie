import {useSelector} from "react-redux";
import Error403 from "../../views/errorViews/error403";
import UserRoles from "../../../api/values/UserRoles"

const RoleGate = ({children, role}) => {
    const {isLoggedIn, user} = useSelector(state => state.auth);
    switch (role) {
        case UserRoles.MODERATOR:
            if (!isLoggedIn || user.role !== UserRoles.MODERATOR) {
                return <Error403/>
            }
            break;
        default:
            break;
    }
    return <>{children}</>
}

export default RoleGate;