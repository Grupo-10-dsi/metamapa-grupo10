import { Card, Button, Stack, Badge } from "react-bootstrap";
import { useState } from 'react';
import VentanaEmergente from "../ventana-emergente/ventana-emergente.jsx";
import api from "../../../../api/api-agregador";
import './solicitud-card.css';
import { useNavigate } from 'react-router-dom';
import { CheckCircle, XCircle, Eye } from 'react-bootstrap-icons';
// 1. Importa los iconos
import { CheckCircle, XCircle } from 'react-bootstrap-icons';

const SolicitudCard = ({
                           solicitud,
                           solicitudId,
                           solicitudEstado,
                           justificacion,
                           idHecho,
                           handleEliminarSolicitud,
                           vistaActual
                       }) => {
    const [showRechazarSolicitud, setShowRechazarSolicitud] = useState(false);
    const [showConfirmarSolicitud, setShowConfirmarSolicitud] = useState(false);

    const mostrarVentanaRechazar = () => setShowRechazarSolicitud(true);
    const mostrarVentanaConfirmar = () => setShowConfirmarSolicitud(true);
    const navigate = useNavigate();

    // Tu lógica de API (sin cambios)
    const eliminarSolicitud = async () => {
        setShowRechazarSolicitud(false);
        await api.eliminarSolicitud(solicitudId);
        await handleEliminarSolicitud(solicitudId);
    };

    const irAlHecho = () => {
        if (idHecho) {
            navigate(`/hecho/${idHecho}`);
        }
    };

    const confirmarSolicitudD = async () => {
        setShowConfirmarSolicitud(false);
        await api.confirmarSolicitud(solicitudId);
        await handleEliminarSolicitud(solicitudId);
    };

    const hecho = solicitud?.hecho;

    const getBadgeVariant = (estado) => {
        switch (estado?.toUpperCase()) {
            case 'ACEPTADA':
                return 'success';
            case 'RECHAZADA':
                return 'danger';
            case 'PENDIENTE':
            default:
                return 'warning';
        }
    };


    return (
        <>
            <Card className="solicitud-card shadow-sm">
                <Card.Body>
                    <div className="d-flex justify-content-between align-items-center">

                        <div className="solicitud-info">
                            <div className="d-flex align-items-center mb-1">
                                <Badge
                                    pill
                                    bg={getBadgeVariant(solicitudEstado)}
                                    className="me-2 solicitud-badge"
                                >
                                    {solicitudEstado || 'PENDIENTE'}
                                </Badge>
                                <Card.Title className="solicitud-titulo mb-0">
                                    {hecho?.titulo || `Solicitud #${solicitudId}`}
                                </Card.Title>
                            </div>
                            <Card.Text className="solicitud-descripcion text-muted">
                                <strong>Justificación:</strong> {justificacion || "Sin justificación."}
                            </Card.Text>
                        </div>

                        <Stack direction="horizontal" gap={2} className="solicitud-acciones">

                            <Button
                                variant="outline-primary"
                                size="sm"
                                onClick={irAlHecho}
                                title="Ver detalle del hecho"
                            >
                                <Eye size={18} />
                                <span className="ms-2">Ver Hecho</span>
                            </Button>

                            {/* Botones de acción (Solo si es PENDIENTE) */}
                            { vistaActual === 'PENDIENTE' && (
                                <>
                                    <div className="vr" />
                                    <Button
                                        variant="outline-success"
                                        size="sm"
                                        onClick={mostrarVentanaConfirmar}
                                        title="Aceptar solicitud"
                                    >
                                        <CheckCircle size={18} />
                                        <span className="ms-2">Aceptar</span>
                                    </Button>

                                    <Button
                                        variant="outline-danger"
                                        size="sm"
                                        onClick={mostrarVentanaRechazar}
                                        title="Rechazar solicitud"
                                    >
                                        <XCircle size={18} />
                                        <span className="ms-2">Rechazar</span>
                                    </Button>
                                </>
                            )}
                        </Stack>
                    </div>
                </Card.Body>
            </Card>

            {showRechazarSolicitud && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas rechazar la solicitud?"
                    onConfirm={eliminarSolicitud}
                    onCancel={() => setShowRechazarSolicitud(false)}
                />
            )}

            {showConfirmarSolicitud && (
                <VentanaEmergente
                    mensaje="¿Estás seguro de que deseas confirmar la solicitud?"
                    onConfirm={confirmarSolicitudD}
                    onCancel={() => setShowConfirmarSolicitud(false)}
                />
            )}
        </>
    );
};

export default SolicitudCard;
