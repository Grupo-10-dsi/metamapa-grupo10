import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from "./features/home-page/home-page.jsx";
import Layout from "./features/layout/layout.jsx";
import Perfil from "./features/perfil-page/perfil.jsx";
import DetailPage from "./features/detail-page/detail-page.jsx";
import Busqueda from "./features/busqueda-page/busqueda.jsx";
import RegistrarHecho from "./features/registrar-hecho/registrar-hecho.jsx";
import './App.css';
import Estadisticas from "./features/estadisticas-page/estadisticas";
import CrearColeccion from "./features/crear-coleccion/crear-coleccion.jsx";
import RequireAuth from "./RequireAuth.jsx";
import RequireAdmin from "./RequireAdmin.jsx";
import Keycloak from "keycloak-js";
import { ReactKeycloakProvider, useKeycloak } from "@react-keycloak/web";
import ApiAgregador from "./api/api-agregador";
import { useEffect, useState } from "react";
import { Spinner } from "react-bootstrap";
import ColeccionesPage from "./features/colecciones/ColeccionesPage";
import ColeccionesHechoPage from "./features/viewHechos-page/coleccion-page.jsx";

//const keyCloakBaseUrl = "http://3.15.225.114:9090";

const kcConfig = {
    url: "https://localhost",
    realm: "MetaMapa",
    clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID
};

export const kc = new Keycloak(kcConfig);


function AppRouter() {
    const { keycloak, initialized } = useKeycloak();

    const [showLoader, setShowLoader] = useState(true);
    const [isFadingOut, setIsFadingOut] = useState(false);

    useEffect(() => {
        if (initialized && keycloak.authenticated) {
            ApiAgregador.setToken(keycloak.token);
            keycloak.onTokenExpired = () => {
                console.log("Token expirado, intentando refrescar...");
                keycloak.updateToken(5)
                    .then(refreshed => {
                        if (refreshed) {
                            console.log("Token refrescado con éxito.");
                            ApiAgregador.setToken(keycloak.token);
                        }
                    })
                    .catch(() => {
                        console.error("Fallo al refrescar el token.");
                    });
            }
        }
    }, [initialized, keycloak, keycloak.authenticated, keycloak.token]);

    useEffect(() => {
        if (initialized) {
            setIsFadingOut(true);
            const timer = setTimeout(() => {
                setShowLoader(false);
            }, 500);
            return () => clearTimeout(timer);
        }
    }, [initialized]);


    return (
        <>
            {showLoader && (
                <div className={`loader-container ${isFadingOut ? 'fade-out' : ''}`}>
                    <Spinner animation="grow" variant="primary" className="loader-spinner" />
                    <div className="loader-text">Autenticando...</div>
                </div>
            )}

            {initialized && (
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<Layout />}>
                            {/* rutas públicas */}
                            <Route path="/home" element={<HomePage />} />
                            <Route path="/" element={<Navigate to="/home" replace />} />
                            <Route path="*" element={<Navigate to="/home" replace />} />
                            <Route path="/hecho/:hechoId" element={<DetailPage />} />
                            <Route path="/busqueda" element={<Busqueda />} />
                            <Route path="/registrar-hecho" element={<RegistrarHecho />} />
                            <Route path="/colecciones" element={<ColeccionesPage />} />
                            <Route path="/hechos" element={<ColeccionesHechoPage />} />
                            <Route path="/colecciones/:id/hechos" element={<ColeccionesHechoPage />} />

                            {/* rutas usuario */}
                            <Route element={<RequireAuth />} >
                                <Route path="perfil/solicitudes" element={<Perfil mostrarEnPantalla={'solicitudes'} />} />
                                <Route path="perfil/colecciones" element={<Perfil mostrarEnPantalla={'colecciones'} />} />
                            </Route>

                            {/* rutas admin */}
                            <Route element={<RequireAdmin />} >
                                <Route path="/panel-control" element={<Perfil />} />
                                <Route path="/crear-coleccion" element={<CrearColeccion />} />
                                <Route path="/estadisticas" element={<Estadisticas />} />
                            </Route>
                        </Route>
                    </Routes>
                </BrowserRouter>
            )}
        </>
    );
}


// Opciones de init pensadas para HTTP y sin tanto "magia" de iframes
const keycloakInitOptions = {
    onLoad: "check-sso",       // ni bien entra, fuerza login
    checkLoginIframe: false,        // desactiva iframe de sesión (molesta en HTTP)
    silentCheckSsoFallback: false,  // no intentes check-sso silencioso
    pkceMethod: "S256",             // suele ser el default, lo explicitamos igual
};

function App() {
    return (
        <ReactKeycloakProvider
            authClient={kc}
            initOptions={keycloakInitOptions}
            // Para ver si kc.init falla por algo (lo verías en la consola del navegador)
            onInitError={(error) => {
                console.error("Keycloak init error:", error);
                try {
                    // último recurso: forzá un login manual
                    kc.login();
                } catch (e) {
                    console.error("Keycloak manual login error:", e);
                }
            }}
        >
            <AppRouter />
        </ReactKeycloakProvider>
    );
}

export default App;