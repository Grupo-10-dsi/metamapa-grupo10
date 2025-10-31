import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import {Card} from "react-bootstrap";
import { useState } from 'react';
import VentanaEmergente from "../../components/ventana-emergente/ventana-emergente.jsx";
import api from "../../../../api/api-agregador";
import './coleccion-card.css'
import VentanaConfiguracion from "../../components/ventana-configuracion/ventana-configuracion.jsx";

const ColeccionCard = ({coleccion, coleccionId, handleEliminarColeccion}) => {
    const [showEliminarColeccion, setShowEliminarColeccion] = useState(false)
    const [showConfig, setShowConfig] = useState(false)
    const mostrarVentana = () => {
        setShowEliminarColeccion(true)
    }

    const eliminarColeccion = async () => {
        setShowEliminarColeccion(false)
        console.log(coleccionId)
        await api.eliminarColeccion(coleccionId)
        await handleEliminarColeccion(coleccionId)

    }

    const abrirConfiguracion = () => {
        setShowConfig(true)
    }

    const handleConfirmConfig = async ({ algoritmo, fuentesSeleccionadas }) => {
        try {
            await api.actualizarColeccion(coleccionId,fuentesSeleccionadas, algoritmo);
            setShowConfig(false)
            window.location.reload()

        } catch (error) {
            console.error("Error al actualizar colección:", error)
            setShowConfig(false)
        }
    }

    const tiposDeFuente = ["ESTATICA", "DINAMICA", "PROXY"];


    return (<>
            <Card as="div" className="contenedor-tarjeta-coleccion">
                <h1 className="coleccion-titulo">{coleccion.titulo} </h1>
                <h2 className="coleccion-descripcion">{coleccion.descripcion}</h2>

                <Container as="div" className="contenedor-botones-coleccion">
                    <Button className="boton-aprobar" variant="secondary" onClick={abrirConfiguracion}> Configurar coleccion </Button>
                    <Button className="boton-rechazar" variant="danger"
                            onClick={mostrarVentana}> Eliminar</Button>
                </Container>
            </Card>

            {showEliminarColeccion && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas eliminar la coleccion?"
                    onConfirm={eliminarColeccion}
                    onCancel={() => setShowEliminarColeccion(false)}
                    //setMotivo={setMotivo}
                />
            )}
            {showConfig && (
            <VentanaConfiguracion
                show={showConfig}
                onClose={() => setShowConfig(false)}
                onConfirm={handleConfirmConfig}
                fuentes={tiposDeFuente}
            />
            )}
        </>
    );
};

export default ColeccionCard;