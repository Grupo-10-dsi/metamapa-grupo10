import { Card, Button, Stack } from "react-bootstrap";
import { useState } from 'react';
import VentanaEmergente from "../../components/ventana-emergente/ventana-emergente.jsx";
import api from "../../../../api/api-agregador";
import './coleccion-card.css';
import VentanaConfiguracion from "../../components/ventana-configuracion/ventana-configuracion.jsx";
import { Gear, Trash } from 'react-bootstrap-icons';

const ColeccionCard = ({ coleccion, coleccionId, onColeccionActualizada, onColeccionEliminada }) => {

    const [showEliminarColeccion, setShowEliminarColeccion] = useState(false);
    const [showConfig, setShowConfig] = useState(false);

    const mostrarVentana = () => setShowEliminarColeccion(true);
    const abrirConfiguracion = () => setShowConfig(true);

    const eliminarColeccion = async () => {
        setShowEliminarColeccion(false);
        try {
            await api.eliminarColeccion(coleccionId);
            // --- 👇 CAMBIO IMPORTANTE ---
            // Notificamos al 'abuelo' para que elimine la card de la UI
            onColeccionEliminada(coleccionId);
        } catch (error) {
            console.error("Error al eliminar colección:", error);
        }
    };

    const handleConfirmConfig = async ({ algoritmo, fuentesSeleccionadas }) => {
        try {
            await api.actualizarColeccion(coleccionId, fuentesSeleccionadas, algoritmo);

            console.log("fuentes seleccionadas:", fuentesSeleccionadas);

            onColeccionActualizada(coleccionId, { algoritmo, fuentesSeleccionadas });

            setShowConfig(false);

        } catch (error) {
            console.error("Error al actualizar colección:", error);
            setShowConfig(false);
        }
    };

    return (
        <>
            <Card className="coleccion-card shadow-sm">
                <Card.Body>
                    <div className="d-flex justify-content-between align-items-center">
                        <div className="coleccion-info">
                            <Card.Title className="coleccion-titulo mb-1">
                                {coleccion.titulo}
                            </Card.Title>
                            <Card.Text className="coleccion-descripcion text-muted">
                                {coleccion.descripcion}
                            </Card.Text>

                        </div>

                        <Stack direction="horizontal" gap={2} className="coleccion-acciones">
                            <Button
                                variant="outline-secondary"
                                size="sm"
                                onClick={abrirConfiguracion}
                                title="Configurar colección"
                            >
                                <Gear size={18} />
                                <span className="d-none d-md-inline ms-2">Configurar</span>
                            </Button>

                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={mostrarVentana}
                                title="Eliminar colección"
                            >
                                <Trash size={18} />
                                <span className="d-none d-md-inline ms-2">Eliminar</span>
                            </Button>
                        </Stack>
                    </div>
                </Card.Body>
            </Card>

            {showEliminarColeccion && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas eliminar la coleccion?"
                    onConfirm={eliminarColeccion}
                    onCancel={() => setShowEliminarColeccion(false)}
                />
            )}
            {showConfig && (
                <VentanaConfiguracion
                    show={showConfig}
                    onClose={() => setShowConfig(false)}
                    onConfirm={handleConfirmConfig}
                    fuentes={coleccion.fuentes || []}
                    algoritmoActual={coleccion.algoritmo_consenso || null}
                />
            )}
        </>
    );
};

export default ColeccionCard;