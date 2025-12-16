import React, {useEffect} from 'react';
import {Container, Row, Col, Card, Button, Spinner, Pagination} from 'react-bootstrap';
import { FaLayerGroup } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import ApiAgregador from "../../api/api-agregador.jsx";

// Estilo para el ícono (reemplaza tu "Image cap")
const cardIconStyle = {
    textAlign: 'center', // Centra el ícono
    paddingTop: '1.5rem', // Espacio superior
    paddingBottom: '1rem', // Espacio inferior
    backgroundColor: '#f8f9fa', // Un fondo gris claro
    color: '#6e2a34', // Color del ícono (tu marrón/rojo)
    fontSize: '3rem' // Tamaño del ícono
};

function ColeccionesPage() {
    const navigate = useNavigate();

    const [colecciones, setColecciones] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    // Estado para la paginación
    const [paginacion, setPaginacion] = useState({
        pageNumber: 0,
        pageSize: 10,
        totalElements: 0,
        totalPages: 0,
        first: true,
        last: false
    });

    // Función que se llama al hacer clic en el botón
    const handleVerHechos = (coleccionId) => {
        navigate(`/colecciones/${coleccionId}/hechos`);
    };

    // Función para cargar colecciones con paginación
    const fetchColecciones = async (page = 0, size = 10) => {
        try {
            setLoading(true);
            setError(false);

            const response = await ApiAgregador.obtenerColecciones({ page, size });

            // Extraer datos de la respuesta paginada
            setColecciones(response.content);
            setPaginacion({
                pageNumber: response.pageNumber,
                pageSize: response.pageSize,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
                first: response.first,
                last: response.last
            });

        } catch (err) {
            setError(true);
            console.error('Error al cargar colecciones:', err);
        } finally {
            setLoading(false);
        }
    };

    // Función para cambiar de página
    const handlePageChange = (newPage) => {
        fetchColecciones(newPage, paginacion.pageSize);
    };

    // Función para cambiar el tamaño de página
    const handlePageSizeChange = (newSize) => {
        fetchColecciones(0, parseInt(newSize));
    };

    useEffect(() => {
        fetchColecciones();
    }, []);


    return (
        // Usamos el fondo verde pálido de tu imagen
        <div style={{ backgroundColor: '#f0fdf4', minHeight: '100vh', padding: '2rem 0' }}>
            <Container>
                <h1 className="text-center mb-5">Colecciones de MetaMapa</h1>

                {/* Información de paginación y controles superiores */}
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <span className="text-muted">
                            Mostrando {colecciones.length} de {paginacion.totalElements} colecciones
                        </span>
                    </div>
                    <div>
                        <select
                            className="form-select form-select-sm"
                            value={paginacion.pageSize}
                            onChange={(e) => handlePageSizeChange(e.target.value)}
                            style={{ width: 'auto', display: 'inline-block' }}
                        >
                            <option value="6">6 por página</option>
                            <option value="10">10 por página</option>
                            <option value="20">20 por página</option>
                            <option value="50">50 por página</option>
                        </select>
                    </div>
                </div>

                {/* Mostrar error si existe */}
                {error && (
                    <div className="alert alert-danger" role="alert">
                        Error al cargar las colecciones. Por favor, intenta nuevamente.
                    </div>
                )}

                {/* Mostrar spinner mientras carga */}
                {loading ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" role="status" variant="primary">
                            <span className="visually-hidden">Cargando...</span>
                        </Spinner>
                        <p className="mt-3">Cargando colecciones...</p>
                    </div>
                ) : (
                    <>
                        {/* 4. Grilla responsiva. Se ajustará a 2 columnas en pantallas medianas */}
                        <Row xs={1} md={2} className="g-4">
                            {colecciones.map((coleccion) => (
                                <Col key={coleccion.id}>
                                    <Card className="shadow-sm h-100">
                                        {/* 5. El ícono reemplaza a la imagen */}
                                        <div style={cardIconStyle}>
                                            <FaLayerGroup />
                                        </div>

                                        <Card.Body className="d-flex flex-column">
                                            <Card.Title className="fw-bold">{coleccion.titulo}</Card.Title>
                                            <Card.Text>
                                                {coleccion.descripcion}
                                            </Card.Text>

                                            {/* 6. El botón llama a la función de navegación */}
                                            <Button
                                                variant="warning"
                                                className="mt-auto" // Empuja el botón al final de la card
                                                onClick={() => handleVerHechos(coleccion.id)}
                                            >
                                                Ver Hechos
                                            </Button>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))}
                        </Row>

                        {/* Mensaje si no hay colecciones */}
                        {colecciones.length === 0 && !loading && (
                            <div className="text-center py-5">
                                <p className="text-muted">No hay colecciones disponibles.</p>
                            </div>
                        )}

                        {/* Controles de paginación */}
                        {paginacion.totalPages > 1 && (
                            <div className="d-flex justify-content-center mt-5">
                                <Pagination>
                                    {/* Botón Primera página */}
                                    <Pagination.First
                                        onClick={() => handlePageChange(0)}
                                        disabled={paginacion.first}
                                    />

                                    {/* Botón Anterior */}
                                    <Pagination.Prev
                                        onClick={() => handlePageChange(paginacion.pageNumber - 1)}
                                        disabled={paginacion.first}
                                    />

                                    {/* Números de página */}
                                    {[...Array(paginacion.totalPages)].map((_, index) => {
                                        // Mostrar solo algunas páginas alrededor de la actual
                                        const currentPage = paginacion.pageNumber;
                                        const showPage =
                                            index === 0 || // Primera página
                                            index === paginacion.totalPages - 1 || // Última página
                                            (index >= currentPage - 2 && index <= currentPage + 2); // Páginas cercanas

                                        if (!showPage) {
                                            // Mostrar ellipsis
                                            if (index === currentPage - 3 || index === currentPage + 3) {
                                                return <Pagination.Ellipsis key={index} disabled />;
                                            }
                                            return null;
                                        }

                                        return (
                                            <Pagination.Item
                                                key={index}
                                                active={index === paginacion.pageNumber}
                                                onClick={() => handlePageChange(index)}
                                            >
                                                {index + 1}
                                            </Pagination.Item>
                                        );
                                    })}

                                    {/* Botón Siguiente */}
                                    <Pagination.Next
                                        onClick={() => handlePageChange(paginacion.pageNumber + 1)}
                                        disabled={paginacion.last}
                                    />

                                    {/* Botón Última página */}
                                    <Pagination.Last
                                        onClick={() => handlePageChange(paginacion.totalPages - 1)}
                                        disabled={paginacion.last}
                                    />
                                </Pagination>
                            </div>
                        )}

                        {/* Información adicional de paginación */}
                        <div className="text-center mt-3">
                            <small className="text-muted">
                                Página {paginacion.pageNumber + 1} de {paginacion.totalPages}
                            </small>
                        </div>
                    </>
                )}
            </Container>
        </div>
    );
}

export default ColeccionesPage;