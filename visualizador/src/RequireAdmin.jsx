// RequireAdmin.jsx
import { useKeycloak } from "@react-keycloak/web";
import {Navigate, Outlet, useLocation} from "react-router-dom";

const RequireAdmin = ({ children }) => {
    const { keycloak, initialized } = useKeycloak();
    const location = useLocation();

    if (!initialized) {
        return <div>Cargando...</div>;
    }


    const hasAdminRole = keycloak.authenticated && keycloak.hasRealmRole("admin");

    if (!hasAdminRole) {
        if (keycloak.authenticated) {
            return <Navigate to="/home" state={{ from: location }} replace />;
        }


        return <Navigate to="/" state={{ from: location }} replace />;
    }

    return <Outlet />;
};

export default RequireAdmin;