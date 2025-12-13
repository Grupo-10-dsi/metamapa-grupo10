import React, { useState, useEffect } from 'react';
// 1. Importamos Button
import { Spinner, Carousel, Row, Col, Badge, Button } from 'react-bootstrap';
import { hechomockeado } from "./hechomockeado";
import VentanaFlotante from './components/ventana-flotante/ventana-flotante.jsx';
import MapaInteractivo from './components/mapa-interactivo/mapa-interactivo.jsx';
import ApiAgregador from '../../api/api-agregador.jsx';
import useUbicacionFromCoords from "./hooks/useUbicacionFromCoords.jsx";
import Mapa from '../home-page/components/mapa.jsx';
import { useParams, useLocation, Link } from "react-router-dom";

function DetailPage() {
    const { hechoId } = useParams()
    //const hechoId = 1;
    const [hecho, setHecho] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const location = useLocation();
    const coleccionState = location.state;
    useEffect(() => {
        const fetchHecho = async () => {
            try {
                const data = await ApiAgregador.obtenerHecho(hechoId);
                setHecho(data);
            } catch (error) {
                console.error("Error al obtener el hecho:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchHecho();
    }, [hechoId]);

    //Esto pega a una API de maps que te trae la ciudad y provincia segun la latitud y longitud
    const { provincia, ciudad } = useUbicacionFromCoords(
        hecho?.ubicacion?.latitud ?? null,
        hecho?.ubicacion?.longitud ?? null
    );

    if (loading || !hecho) {
        return (
            <div className="d-flex justify-content-center mt-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </Spinner>
            </div>
        );
    }


    const carouselImageStyle = {
        height: '500px',
        objectFit: 'cover',
        borderRadius: 'var(--bs-border-radius-lg)',
        border: '1px solid var(--bs-border-color)'
    };

    const formatFecha = (fechaISO) => {
        if (!fechaISO) return 'No especificada';
        return new Date(fechaISO).toLocaleString();
    };

    const handleSolicitudSubmit = async (data) => {
        try {
            console.log(data)
            await ApiAgregador.enviarSolicitudEliminacion(data);
            setShowModal(false);
        } catch (error) {
            console.error("Error al enviar la solicitud de eliminación:", error);
        }
    };

    return (
        <div className="container-xl my-5 bg-white p-4 p-md-5 rounded shadow-sm">
            <div className="row">
                <div className="col-md-7">
                    {coleccionState && coleccionState.coleccionId && (
                        <Link
                            to={`/colecciones/${coleccionState.coleccionId}/hechos`}
                            className="d-block mb-3 text-muted text-decoration-none"
                        >
                            &larr; Volver a {coleccionState.coleccionNombre || 'la colección'}
                        </Link>
                    )}
                    <h1 className="display-5 fw-bold">{hecho.titulo}</h1>
                    <p className="h5 text-muted mb-3">{hecho.categoria?.detalle}</p>

                    <div className="d-flex flex-wrap mb-3">
                        {hecho.etiquetas?.map((tag) => (
                            <Badge key={tag.id} bg="secondary-subtle" text="secondary-emphasis" className="rounded-pill me-2 mb-2 p-2 fs-6">
                                {tag.descripcion}
                            </Badge>
                        ))}
                    </div>

                    <p className="lead" style={{ whiteSpace: 'pre-line' }}>{hecho.descripcion}</p>

                    <hr className="my-4" />

                    <div className="mt-2">
                        <h4>Detalles del Reporte</h4>
                        <Row className="mt-3">
                            <Col md={6} className="mb-3">
                                <p className="mb-0 fs-5"><span title="Fecha de Acontecimiento">📅</span> <strong>Fecha del acontecimiento:</strong></p>
                                <p className="text-muted fs-5">{formatFecha(hecho.fechaAcontecimiento)}</p>
                            </Col>

                            <Col md={6} className="mb-3">
                                <p className="mb-0 fs-5"><span title="Contribuyente">👤</span> <strong>Reportado por:</strong></p>
                                <p className="text-muted fs-5">{hecho.contribuyente.contribuyente_nombre}</p>
                            </Col>

                            <Col md={6} className="mb-3 mt-md-3">
                                <p className="mb-0 fs-5"><span title="Fuente del Reporte">💻</span> <strong>Fuente:</strong></p>
                                <p className="text-muted fs-5 text-break lh-base">
                                    {hecho.origenMapeado?.nombreArchivo
                                        ? `${hecho.origenMapeado?.nombre} - Archivo: ${hecho.origenMapeado?.nombreArchivo}`
                                        : (hecho.origenMapeado?.nombre || "Fuente desconocida")
                                    }
                                </p>
                            </Col>

                            <Col md={6} className="mb-3 mt-md-3">
                                <p className="mb-0 fs-5"><span title="Referencia de Ubicación">📍</span> <strong>Ubicación (Ref.):</strong></p>
                                <p className="text-muted fs-5 lh-base">
                                    {[provincia, ciudad].filter(Boolean).join(', ') || 'Sin descripción textual'}
                                </p>
                            </Col>
                        </Row>
                    </div>
                </div>

                <div className="col-md-5">
                    <h4 className="mt-3">Ubicacion del hecho</h4>
                    <Mapa hechosMapa={[{
                        latitud: hecho.ubicacion?.latitud,
                        longitud: hecho.ubicacion?.longitud,
                        descripcion: hecho.titulo
                    }]} />
                    <p className="text-muted text-center mt-2 mb-0 small">
                        Coordenadas: <code>{hecho.ubicacion?.latitud}, {hecho.ubicacion?.longitud}</code>
                    </p>
                </div>
            </div>

            <hr className="my-5" />

            {hecho.cuerpo ? (
                <>
                    <h2>Cuerpo</h2>
                    <div className="card p-3 mb-3">
                        <p style={{ whiteSpace: 'pre-line', margin: 0 }}>{hecho.cuerpo}</p>
                    </div>
                </>
            ) : (
                hecho.contenidoMultimedia?.length > 0 && (
                    <>
                        <h2>Contenido Multimedia</h2>
                        <Carousel id="carouselHecho" className="mt-3" interval={null}>
                            {hecho.contenidoMultimedia.map((url, index) => (
                                <Carousel.Item key={index}>
                                    <img
                                        className="d-block w-100"
                                        src={(url.startsWith('http://') || url.startsWith('https://'))
                                            ? url
                                            : "https://ycewwqszmnadqvimhdpx.supabase.co/storage/v1/object/public/multimedia/" + url
                                        }
                                        alt={`Multimedia ${index + 1}`}
                                        style={carouselImageStyle}
                                    />                                </Carousel.Item>
                            ))}
                        </Carousel>
                    </>
                )
            )}

            <hr className="my-5" />

            <div className="row">
                <div className="col-12 text-center">
                    <h3>Acciones del Hecho</h3>
                    <p className="text-muted mb-3">¿Consideras que este hecho es incorrecto o viola alguna norma?</p>

                    <Button variant="warning" size="lg" onClick={() => setShowModal(true)}>
                        Solicitar eliminación
                    </Button>
                </div>
            </div>

            <VentanaFlotante
                show={showModal}
                handleClose={() => setShowModal(false)}
                hecho={hecho}
                onSubmit={handleSolicitudSubmit}
            />
        </div>
    );
}

export default DetailPage;