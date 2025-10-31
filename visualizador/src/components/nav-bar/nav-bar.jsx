import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { Button, NavDropdown } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useKeycloak } from '@react-keycloak/web';

function NavBar() {
    const navigate = useNavigate();
    const { keycloak } = useKeycloak();

    return (
        <Navbar
            expand="lg"
            data-bs-theme='metamapa'
            className="metamapa-navbar"
            sticky="top"
        >
            <Container fluid>
                <Navbar.Brand href="/home" className="metamapa-brand">MetaMapa</Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarScroll" />
                <Navbar.Collapse id="navbarScroll">

                    <Nav
                        className="ms-auto my-2 my-lg-0 align-items-center"
                        navbarScroll
                        onSelect={(selectedKey) => navigate(`${selectedKey}`)}
                    >
                        <Nav.Link href="/colecciones" className="nav-link-metamapa">Colecciones</Nav.Link>
                        <Nav.Link href="/estadisticas" className="nav-link-metamapa">Estadisticas</Nav.Link>
                        <Nav.Link href="/busqueda" className="nav-link-metamapa">Buscar</Nav.Link>
                        <Nav.Link href="/hechos" className="nav-link-metamapa">Navegar Hechos</Nav.Link>
                        <Nav.Link
                            href="/registrar-hecho"
                            className="nav-link-metamapa"
                        >
                            Registrar Hecho
                        </Nav.Link>

                        {!keycloak.authenticated && (
                            <Button
                                variant="outline-primary" // <-- Estilo intercambiado
                                className="ms-3"
                                onClick={() => keycloak.login()}
                            >
                                Iniciar Sesion
                            </Button>
                        )}

                        {keycloak.authenticated && (
                            <>
                                <div className="nav-vertical-separator"></div>

                                <NavDropdown
                                    title={`Hola, ${keycloak.tokenParsed.preferred_username}`}
                                    id="basic-nav-dropdown"
                                    align="end"
                                >
                                    <NavDropdown.Item onClick={() => navigate('/perfil')}>
                                        Mi Perfil
                                    </NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item onClick={() => keycloak.logout()}>
                                        Cerrar Sesión
                                    </NavDropdown.Item>
                                </NavDropdown>
                            </>
                        )}
                    </Nav>

                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;