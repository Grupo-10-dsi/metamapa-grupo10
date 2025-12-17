 import React, { useState } from 'react';
import {
    Container,
    Row,
    Col,
    Nav,
} from 'react-bootstrap';

import './perfil.css';

import MostrarSolicitudes from './components/mostrar-solicitudes.jsx';
import MostrarColecciones from "./components/mostrar-colecciones.jsx"

function PerfilPage() {
    const [activeView, setActiveView] = useState('solicitudes');


    return (
        <Container fluid="lg" className="mt-4 perfil-container">
            <Row>
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

                <Col md={9}>
                    <div className="perfil-content">
                        {activeView === 'solicitudes' && <MostrarSolicitudes />}
                        {activeView === 'colecciones' && <MostrarColecciones />}
                    </div>
                </Col>
            </Row>
        </Container>
    );
}

export default PerfilPage;