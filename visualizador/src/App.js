import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from "./features/home-page/home-page.jsx";
import Layout from "./features/layout/layout.jsx";
import Perfil from "./features/perfil-page/perfil.jsx";
import Login from "./features/login-page/log-in.jsx";
import DetailPage from "./features/detail-page/detail-page.jsx";
import Busqueda from "./features/busqueda-page/busqueda.jsx";
import RegistrarHecho from "./features/registrar-hecho/registrar-hecho.jsx";
import './App.css';
import Estadisticas from "./features/estadisticas-page/estadisticas";

function App() {

  return (
      <BrowserRouter>
        <Routes>
            <Route path="/" element={<Layout/>}>
              <Route path="/home" element={<HomePage />} />
              <Route path="/" element={<Navigate to="/home" replace />} />
              <Route path="*" element={<Navigate to="/home" replace />} />
                <Route path="/hecho" element={<DetailPage />} />
                <Route path="/perfil" element={<Perfil />} />
                <Route path="/login" element={<Login />} />
                <Route path="/busqueda" element={<Busqueda />} />
                <Route path="/registrar-hecho" element={<RegistrarHecho/>} />
                <Route path="/estadisticas" element={<Estadisticas/>} />
            </Route>

        </Routes>
      </BrowserRouter>
  );
}

export default App;
