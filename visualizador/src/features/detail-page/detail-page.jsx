import React, { useState, useEffect } from 'react';
// 1. Importamos Button
import { Spinner, Carousel, Row, Col, Badge, Button } from 'react-bootstrap';
import { hechomockeado } from "./hechomockeado";
import VentanaFlotante from './components/ventana-flotante/ventana-flotante.jsx';
import MapaInteractivo from './components/mapa-interactivo/mapa-interactivo.jsx';

function DetailPage() {
    const [hecho, setHecho] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        setTimeout(() => {
            setHecho(hechomockeado);
            setLoading(false);
        }, 500);
    }, []);

    if (loading) {
        return (
            <div className="d-flex justify-content-center mt-5 ">
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

    const formatOrigen = (origen) => {
        if (origen === 'Estatica') return 'Carga Manual';
        return origen || 'No especificado';
    };

    const handleSolicitudSubmit = async (data) => {


        // Simula una llamada a la API
        await new Promise(resolve => setTimeout(resolve, 1500));


    };


    return (
        <div className="container-xl my-5 bg-white p-4 p-md-5 rounded shadow-sm">
            <div className="row">

                {/* ============================================= */}
                {/* COLUMNA IZQUIERDA: Información  */}
                {/* ============================================= */}
                <div className="col-md-7">
                    <h1 className="display-5 fw-bold">{hecho.titulo}</h1>
                    <p className="h5 text-muted mb-3">{hecho.categoria?.detalle}</p>

                    <div className="d-flex flex-wrap mb-3">
                        {hecho.etiquetas?.map((tag) => (
                            <Badge
                                key={tag.id}
                                bg="secondary-subtle"
                                text="secondary-emphasis"
                                className="rounded-pill me-2 mb-2 p-2 fs-6"
                            >
                                {tag.descripcion}
                            </Badge>
                        ))}
                    </div>
                    {}

                    <p className="lead" style={{ whiteSpace: 'pre-line' }}>
                        {hecho.descripcion}
                    </p>

                    <hr className="my-4" />

                    {/* ================================================== */}
                    {/* --- NUEVA SECCIÓN "INFORMACIÓN ADICIONAL" --- */}
                    {/* ================================================== */}
                    <div className="mt-2">
                        <h4>Detalles del Reporte</h4>

                        <Row className="mt-3">
                            <Col md={6} className="mb-3">
                                <p className="mb-0 fs-5">
                                    <span title="Fecha de Carga">📅</span> <strong>Fecha de Carga:</strong>
                                </p>
                                <p className="text-muted fs-5">
                                    {formatFecha(hecho.fechaCarga)}
                                </p>
                            </Col>

                            <Col md={6} className="mb-3">
                                <p className="mb-0 fs-5">
                                    <span title="Contribuyente">👤</span> <strong>Reportado por:</strong>
                                </p>
                                <p className="text-muted fs-5">
                                    {hecho.contribuyente || 'No especificado'}
                                </p>
                            </Col>

                            {/* Columna 3: Fuente (Origen) */}
                            <Col md={6} className="mb-3 mt-md-3">
                                <p className="mb-0 fs-5">
                                    <span title="Fuente del Reporte">💻</span> <strong>Fuente:</strong>
                                </p>
                                <p className="text-muted fs-5">
                                    {(hecho.origen)}
                                </p>
                            </Col>

                            {/* Columna 4: Ubicación (Texto) */}
                            <Col md={6} className="mb-3 mt-md-3">
                                <p className="mb-0 fs-5">
                                    <span title="Referencia de Ubicación">📍</span> <strong>Ubicación (Ref.):</strong>
                                </p>
                                <p className="text-muted fs-5">
                                    {hecho.ubicacion?.texto || 'Sin descripción textual'}
                                </p>
                            </Col>
                        </Row>

                    </div>
                </div>

                {/* ============================================= */}
                {/* COLUMNA DERECHA: Mapa + Coordenadas */}
                {/* ============================================= */}
                <div className="col-md-5">
                    {/* ... (contenido del mapa sin cambios) ... */}
                    <h1 className="display-5 fw-bold invisible" aria-hidden="true">
                    </h1>

                    <h4 className="mt-3">Ubicacion del hecho</h4>

                    <div
                        className="ratio ratio-4x3 rounded shadow-sm"
                        style={{ overflow: 'hidden', border: '1px solid #ddd' }}
                    >
                        <iframe
                            src={`https://maps.google.com/maps?q=${hecho.ubicacion?.latitud},${hecho.ubicacion?.longitud}&z=15&output=embed`}
                            width="100%"
                            height="100%"
                            style={{ border: 0 }}
                            allowFullScreen=""
                            loading="lazy"
                            referrerPolicy="no-referrer-when-downgrade"
                            title="Ubicación del hecho"
                        ></iframe>
                    </div>

                    <p className="text-muted text-center mt-2 mb-0 small">
                        Coordenadas:
                        <code> {hecho.ubicacion?.latitud}, {hecho.ubicacion?.longitud}</code>
                    </p>

                </div>

            </div>


            {/* ============================================= */}
            {/* NUEVA FILA: Carousel  */}
            {/* ============================================= */}
            <hr className="my-5" />

            <div className="row">
                <div className="col-12">

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

                                <Carousel
                                    id="carouselHecho"
                                    className="mt-3"
                                    interval={null}
                                >
                                    {hecho.contenidoMultimedia.map((url, index) => (
                                        <Carousel.Item key={index}>
                                            <img
                                                className="d-block w-100"
                                                src={url}
                                                alt={`Multimedia ${index + 1}`}
                                                style={carouselImageStyle}
                                            />
                                        </Carousel.Item>
                                    ))}
                                </Carousel>
                            </>
                        )
                    )}

                </div>
            </div>

            {/* ============================================= */}
            {/* 3. Botón de Solicitud */}
            {/* ============================================= */}

            <hr className="my-5" />

            <div className="row">
                <div className="col-12 text-center">
                    <h3>Acciones del Hecho</h3>
                    <p className="text-muted mb-3">
                        ¿Consideras que este hecho es incorrecto o viola alguna norma?
                    </p>

                    <Button
                        variant="warning"
                        size="lg"
                        onClick={() => setShowModal(true)}
                    >
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