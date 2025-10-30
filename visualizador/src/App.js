import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
// Todos tus imports de páginas
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
import Keycloak from "keycloak-js"
import {ReactKeycloakProvider} from "@react-keycloak/web";
import RequireAdmin from "./RequireAdmin.jsx";

const kcConfig = {
    url: "http://localhost:9090/",
    realm: "MetaMapa",
    clientId: "metamapa-frontend"
}

const kc = new Keycloak(kcConfig);


function App() {
    return (
        <ReactKeycloakProvider authClient={kc}>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Layout/>}>
                        {/* rutas públicas */}
                        <Route path="/home" element={<HomePage />} />
                        <Route path="/" element={<Navigate to="/home" replace />} />
                        <Route path="*" element={<Navigate to="/home" replace />} />
                        <Route path="/hecho/:hechoId" element={<DetailPage />} />
                        <Route path="/busqueda" element={<Busqueda />} />
                        {/* rutas usuario */}
                        <Route element={<RequireAuth/>} >
                            <Route path="/perfil" element={<Perfil/> } />
                            <Route path="perfil/solicitudes" element={<Perfil mostrarEnPantalla={'solicitudes'} /> }/>
                            <Route path="perfil/colecciones" element={<Perfil mostrarEnPantalla={'colecciones'}/> } />
                            <Route path="/registrar-hecho" element={<RegistrarHecho/>} />
                        </Route>

                        {/* rutas admin */}
                        <Route element={<RequireAdmin/>} >
                            <Route path="/crear-coleccion" element={<CrearColeccion/>} />
                            <Route path="/estadisticas" element={<Estadisticas/>} />
                        </Route>
                    </Route>
                </Routes>
            </BrowserRouter>
        </ReactKeycloakProvider>
    );
}



export default App;