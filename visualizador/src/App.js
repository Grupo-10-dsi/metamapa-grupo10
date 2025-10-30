import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from "./features/home-page/home-page.jsx";
import Layout from "./features/layout/layout.jsx";
import Perfil from "./features/perfil-page/perfil.jsx";
import DetailPage from "./features/detail-page/detail-page.jsx";
import Busqueda from "./features/busqueda-page/busqueda.jsx";
import RegistrarHecho from "./features/registrar-hecho/registrar-hecho.jsx";
import ColeccionesPage  from "./features/colecciones/ColeccionesPage";
import ColeccionesHechoPage from "./features/viewHechos-page/coleccion-page.jsx";
import './App.css';
import Estadisticas from "./features/estadisticas-page/estadisticas";
import CrearColeccion from "./features/crear-coleccion/crear-coleccion.jsx";
import RequireAuth from "./RequireAuth.jsx";
import RequireAdmin from "./RequireAdmin.jsx";

// --- CAMBIOS EN IMPORTS ---
import Keycloak from "keycloak-js";
import { ReactKeycloakProvider, useKeycloak } from "@react-keycloak/web";
import ApiAgregador from "./api/api-agregador";
import { useEffect } from "react";


const kcConfig = {
    url: "http://localhost:9090/",
    realm: "MetaMapa",
    clientId: "metamapa-frontend"
};

export const kc = new Keycloak(kcConfig);


function AppRouter() {
    const { keycloak, initialized } = useKeycloak();

    useEffect(() => {
        if (initialized && keycloak.authenticated) {

            ApiAgregador.setToken(keycloak.token);

            keycloak.onTokenExpired = () => {
                console.log("Token expirado, intentando refrescar...");
                keycloak.updateToken(5)
                    .then(refreshed => {
                        if (refreshed) {
                            console.log("Token refrescado con éxito.");
                            // Pasa el NUEVO token a tu API
                            ApiAgregador.setToken(keycloak.token);
                        }
                    })
                    .catch(() => {
                        console.error("Fallo al refrescar el token.");
                    });
            }
        }
    }, [initialized, keycloak, keycloak.authenticated, keycloak.token]);


    // 5. No renderizar la app hasta que Keycloak esté listo
    if (!initialized) {
        return <div>Cargando Keycloak...</div>;
    }

    // 6. renderiza todas las rutas
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout/>}>
                    {/* rutas públicas */}
                    <Route path="/home" element={<HomePage />} />
                    <Route path="/" element={<Navigate to="/home" replace />} />
                    <Route path="*" element={<Navigate to="/home" replace />} />
                    <Route path="/hecho/:hechoId" element={<DetailPage />} />
                    <Route path="/busqueda" element={<Busqueda />} />
                    <Route path="/registrar-hecho" element={<RegistrarHecho/>} />
                    <Route path="/colecciones" element={<ColeccionesPage/>} />
                    <Route path="/hechos" element={<ColeccionesHechoPage/>}/>
                    <Route path="/colecciones/:id/hechos" element={<ColeccionesHechoPage/>} />

                    {/* rutas usuario */}
                    <Route element={<RequireAuth/>} >
                        <Route path="/perfil" element={<Perfil/> } />
                        <Route path="perfil/solicitudes" element={<Perfil mostrarEnPantalla={'solicitudes'} /> }/>
                        <Route path="perfil/colecciones" element={<Perfil mostrarEnPantalla={'colecciones'}/> } />
                    </Route>

                    {/* rutas admin */}
                    <Route element={<RequireAdmin/>} >
                        <Route path="/crear-coleccion" element={<CrearColeccion/>} />
                        <Route path="/estadisticas" element={<Estadisticas/>} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}


// --- COMPONENTE APP MODIFICADO ---
function App() {
    return (
        <ReactKeycloakProvider authClient={kc}>
            <AppRouter />
        </ReactKeycloakProvider>
    );
}

export default App;