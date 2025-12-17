import ColeccionCard from './coleccion-card/coleccion-card'
import { useEffect, useState } from 'react'
import api from "../../../api/api-agregador";
import './coleccion-card/coleccion-card.css'
import { Collection } from 'react-bootstrap-icons';
import { Pagination, Spinner, Form } from 'react-bootstrap';
import ApiAgregador from "../../../api/api-agregador";

const MostrarColecciones = () => {
    const [colecciones, setColecciones] = useState([])
    const [loading, setLoading] = useState(true)
    const [paginacion, setPaginacion] = useState({
        pageNumber: 0,
        pageSize: 10,
        totalElements: 0,
        totalPages: 0,
        first: true,
        last: false
    });


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

    const handlePageChange = (newPage) => {
        fetchColecciones(newPage, paginacion.pageSize);
    };

    const handlePageSizeChange = (newSize) => {
        fetchColecciones(0, parseInt(newSize));
    };

    useEffect(() => {
        fetchColecciones()
    }, [])


    const handleActualizarColeccionLocal = (coleccionId, datosNuevos) => {
        const nuevasFuentes = datosNuevos.fuentesSeleccionadas.map(url => {
            return { url_fuente: url };
        });

        setColecciones(coleccionesActuales =>
            coleccionesActuales.map(col => {
                // Si encontramos la colección, la reemplazamos con la nueva data
                if (col.id === coleccionId) {
                    return {
                        ...col,
                        algoritmo_consenso: datosNuevos.algoritmo,
                        // Actualizamos también el string 'algoritmo' para la UI
                        algoritmo: datosNuevos.algoritmo ? datosNuevos.algoritmo.replace('_', ' ') : null,
                        fuentes: nuevasFuentes
                    };
                }
                // Si no es, la devolvemos como estaba
                return col;
            })
        );
    };

    const handleEliminarColeccionLocal = (id) => {
        setColecciones(coleccionesActuales =>
            coleccionesActuales.filter(col => col.id !== id)
        );
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Colecciones</h2>

                {!loading && colecciones.length > 0 && (
                    <div className="d-flex align-items-center">
                        <span className="me-2 text-muted small">Mostrar:</span>
                        <Form.Select
                            size="sm"
                            style={{ width: 'auto' }}
                            value={paginacion.pageSize}
                            onChange={(e) => handlePageSizeChange(e.target.value)}
                        >
                            <option value="5">5</option>
                            <option value="10">10</option>
                            <option value="20">20</option>
                        </Form.Select>
                    </div>
                )}
            </div>

            {loading ? (
                <div className="text-center py-5">
                    <Spinner animation="border" variant="primary" />
                </div>
            ) : (
                <>
                    {colecciones.length > 0 ? (
                        <>
                            <div className="fondo-gris">
                                {colecciones.map((result) => (
                                    <ColeccionCard
                                        key={result.id}
                                        coleccion={result}
                                        coleccionId={result.id}
                                        onColeccionActualizada={handleActualizarColeccionLocal}
                                        onColeccionEliminada={handleEliminarColeccionLocal}
                                    />
                                ))}
                            </div>

                            {paginacion.totalPages > 1 && (
                                <div className="d-flex justify-content-center mt-4">
                                    <Pagination>
                                        <Pagination.First
                                            onClick={() => handlePageChange(0)}
                                            disabled={paginacion.first}
                                        />
                                        <Pagination.Prev
                                            onClick={() => handlePageChange(paginacion.pageNumber - 1)}
                                            disabled={paginacion.first}
                                        />

                                        {[...Array(paginacion.totalPages)].map((_, index) => {
                                            const currentPage = paginacion.pageNumber;
                                            if (
                                                index === 0 ||
                                                index === paginacion.totalPages - 1 ||
                                                (index >= currentPage - 1 && index <= currentPage + 1)
                                            ) {
                                                return (
                                                    <Pagination.Item
                                                        key={index}
                                                        active={index === currentPage}
                                                        onClick={() => handlePageChange(index)}
                                                    >
                                                        {index + 1}
                                                    </Pagination.Item>
                                                );
                                            } else if (
                                                index === currentPage - 2 ||
                                                index === currentPage + 2
                                            ) {
                                                return <Pagination.Ellipsis key={index} disabled />
                                            }
                                            return null;
                                        })}

                                        <Pagination.Next
                                            onClick={() => handlePageChange(paginacion.pageNumber + 1)}
                                            disabled={paginacion.last}
                                        />
                                        <Pagination.Last
                                            onClick={() => handlePageChange(paginacion.totalPages - 1)}
                                            disabled={paginacion.last}
                                        />
                                    </Pagination>
                                </div>
                            )}
                            <div className="text-center mt-2">
                                <small className="text-muted">
                                    Mostrando página {paginacion.pageNumber + 1} de {paginacion.totalPages}
                                </small>
                            </div>
                        </>
                    ) : (
                        <div
                            className="text-center p-4 p-md-5 text-secondary"
                            style={{
                                backgroundColor: '#f8f9fa',
                                borderRadius: '12px',
                                marginTop: '1.5rem'
                            }}
                        >
                            <Collection size={48} className="mb-3" />
                            <h4 className="fw-normal">No hay colecciones</h4>
                            <p className="mb-0">Todavía no tenés colecciones creadas.</p>
                        </div>
                    )}
                </>
            )}
        </>
    )
}

export default MostrarColecciones