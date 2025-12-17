// En tu archivo Login.jsx
import { useEffect } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import {Navigate} from "react-router-dom";

const Login = () => {
    const { keycloak } = useKeycloak();

    useEffect(() => {
        if (!keycloak.authenticated) {
            keycloak.login();
        }
    }, [keycloak]);

    if (keycloak.authenticated) {
        return <Navigate to="/home" replace />;
    }

    return <div>Redirigiendo a la página de login...</div>;
};

export default Login;