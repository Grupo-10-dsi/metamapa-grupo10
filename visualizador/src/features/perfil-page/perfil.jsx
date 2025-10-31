import React, { useState } from 'react';
import {
    Container,
    Row,
    Col,
    Nav,
} from 'react-bootstrap';

import './perfil.css';

import MostrarSolicitudes from './components/mostrar-solicitudes.jsx'; // Ajusta la ruta si es necesario
import MostrarColecciones from "./components/mostrar-colecciones.jsx" //Ajusta la ruta si es necesario

function PerfilPage() {
    const [activeView, setActiveView] = useState('solicitudes'); // 'solicitudes' o 'colecciones'


    return (
        <Container fluid="lg" className="mt-4 perfil-container">
            <Row>
                {/* --- COLUMNA IZQUIERDA (Menú) --- */}
                <Col md={3}>
                    <Nav
                        className="flex-column perfil-nav"
                        activeKey={activeView}
                        onSelect={(selectedKey) => setActiveView(selectedKey)}
                    >
                        <Nav.Link eventKey="solicitudes" className="perfil-nav-link">
                            Solicitudes
                        </Nav.Link>
                        <Nav.Link eventKey="colecciones" className="perfil-nav-link">
                            Colecciones
                        </Nav.Link>
                    </Nav>
                </Col>

                {/* --- COLUMNA DERECHA (Contenido) --- */}
                <Col md={9}>
                    <div className="perfil-content">
                        {/* Aquí está la magia.
                          Simplemente renderizamos un componente o el otro.
                          Cada uno de ellos mostrará su propio spinner
                          y manejará sus propios datos.
                        */}
                        {activeView === 'solicitudes' && <MostrarSolicitudes />}
                        {activeView === 'colecciones' && <MostrarColecciones />}
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default PerfilPage;