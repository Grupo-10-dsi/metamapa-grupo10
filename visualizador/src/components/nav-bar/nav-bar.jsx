import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

import {Button, Form, NavDropdown} from "react-bootstrap";

function NavBar() {
    return (
        <Navbar expand="lg" data-bs-theme='prueba' >
            <Container fluid >
                <Navbar.Brand href="#">MetaMapa</Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarScroll" />
                <Navbar.Collapse id="navbarScroll" >
                    <Nav
                        className="ms-auto my-2 my-lg-0"
                        style={{ maxHeight: '100px' }}
                        navbarScroll
                    >
                        <Nav.Link href="#action1" variant='flat'>Home</Nav.Link>
                        <Nav.Link href="#action2" >Estadisticas</Nav.Link>
                        <Nav.Link href="#action3" >Iniciar Sesion</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavBar;