import { useKeycloak } from "@react-keycloak/web";
import { Navigate, Outlet, useLocation } from "react-router-dom";

const RequireAuth = () => {
    const { keycloak, initialized } = useKeycloak();
    const location = useLocation();

    if (!initialized) {
        return <div>Cargando...</div>;
    }

    if (keycloak.authenticated) {
        return <Outlet />;
    }

    keycloak.login();

};

export default RequireAuth;