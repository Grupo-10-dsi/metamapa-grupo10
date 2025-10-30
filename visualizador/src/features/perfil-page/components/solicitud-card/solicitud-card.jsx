import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import {Card} from "react-bootstrap";
import { useState } from 'react';
import VentanaEmergente from "../../components/ventana-emergente/ventana-emergente.jsx";
import api from "../../../../api/api-agregador";
import './solicitud-card.css'


const SolicitudCard = ({solicitud, solicitudId, solicitudEstado,justificacion, handleEliminarSolicitud}) => {
    const [showRechazarSolicitud, setShowRechazarSolicitud] = useState(false)
    const  [showConfirmarSolicitud, setShowConfirmarSolicitud] = useState(false)


    const mostrarVentanaRechazar = () => {
        setShowRechazarSolicitud(true)
    }

    const mostrarVentanaConfirmar = () => {
        setShowConfirmarSolicitud(true)
    }

    const eliminarSolicitud = async () => {
        setShowRechazarSolicitud(false)
        console.log(solicitudId)
        await api.eliminarSolicitud(solicitudId)
        await handleEliminarSolicitud(solicitudId)
    }


    const confirmarSolicitudD = async () => {
        setShowConfirmarSolicitud(false)
        console.log(solicitudId)
        await api.confirmarSolicitud(solicitudId)
        await handleEliminarSolicitud(solicitudId)
    }

    const hecho = solicitud?.hecho

    return (
        //TARJETAS DE SOLICITUDES
        <>
            <Card  as="div"  className="contenedor-tarjeta-solicitud">
                {/*<h1>{solicitud.id}</h1>*/}
                <h2>{hecho?.titulo}</h2>
                <h3>{justificacion}</h3>
                <Container as="div" className="contenedor-botoness">
                    <Button className="boton-aprobar"  variant = "success" onClick={mostrarVentanaConfirmar} > Aprobar</Button>
                    <Button className="boton-rechazar" variant = "danger" onClick={mostrarVentanaRechazar}> Rechazar</Button>
                </Container>
            </Card>

            {showRechazarSolicitud && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas rechazar la solicitud?"
                    onConfirm={eliminarSolicitud}
                    onCancel={() => setShowRechazarSolicitud(false)}
                    //setMotivo={setMotivo}
                />
            )}

            {showConfirmarSolicitud && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas confirmar la solicitud?"
                    onConfirm={confirmarSolicitudD}
                    onCancel={() => setShowConfirmarSolicitud(false)}
                    //setMotivo={setMotivo}
                />
            )}
        </>
    );
};

export default SolicitudCard