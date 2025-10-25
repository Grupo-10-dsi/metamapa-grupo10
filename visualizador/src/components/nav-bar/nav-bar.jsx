import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

import {Button, Form, NavDropdown} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

function NavBar() {
    const navigate = useNavigate()

    return (
        <Navbar expand="lg" data-bs-theme='prueba' >
            <Container fluid >
                <Navbar.Brand href="/home">MetaMapa</Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarScroll" />
                <Navbar.Collapse id="navbarScroll" >
                    <Nav
                        className="ms-auto my-2 my-lg-0"
                        style={{ maxHeight: '150px' }}
                        navbarScroll
                        activeKey="/home"
                        onSelect={(selectedKey) => navigate( `${selectedKey}`)}
                    >
                        <Nav.Item>
                            <Nav.Link href="/busqueda" >Buscar</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/estadisticas" >Estadisticas</Nav.Link>
                        </Nav.Item>
                        <Nav.Item className='sesion'>
                            <Nav.Link href="/home" >Iniciar Sesion</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;