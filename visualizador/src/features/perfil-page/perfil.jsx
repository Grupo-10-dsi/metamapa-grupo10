import Container from 'react-bootstrap/Container';
import MostrarSolicitudes from './components/mostrar-solicitudes.jsx';
import MostrarColecciones from "./components/mostrar-colecciones";
import Button from 'react-bootstrap/Button';
import './perfil.css';
import BotonesOpciones from './components/botones-opciones/botones-opciones.jsx';

/*import {Card} from "react-bootstrap";
import { useState } from 'react';
import ButtonGroup from 'react-bootstrap/ButtonGroup';
import ToggleButton from 'react-bootstrap/ToggleButton';*/



export default function Perfil ({mostrarEnPantalla}){

    return (
        <Container as="div" fluid className="principal" >
            <Container as="div" fluid className="contenedor-botones">
                <BotonesOpciones activo={mostrarEnPantalla}></BotonesOpciones>
            </Container>
            <Container as="div"  fluid className="contenedor-informacion">
                <Container as="div" fluid className="contenedor-tarjetas">
                    {mostrarEnPantalla === 'solicitudes' ? (
                        <MostrarSolicitudes />
                    ) : mostrarEnPantalla === 'colecciones' ? (
                        <MostrarColecciones/>
                    ) : null}
                </Container>
            </Container>
        </Container>

    )}
