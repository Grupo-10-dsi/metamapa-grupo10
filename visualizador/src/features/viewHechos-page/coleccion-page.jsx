import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Spinner, Pagination } from 'react-bootstrap';
import ColeccionInfo from './components/coleccion-info/ColeccionInfo';
import ConsensoSwitch from './components/consenso-switch/ConsensoSwitch';
import FiltrosModal from './components/filtros-modal/FiltrosModal';
import HechoCard from './components/hecho-card/HechoCard';
import api from '../../api/api-agregador';

export default function ColeccionHechosPage() {
    const { id } = useParams();
    const isModoColeccion = Boolean(id);

    const [coleccion, setColeccion] = useState(null);
    const [tituloPagina, setTituloPagina] = useState("Cargando...");
    const [hechos, setHechos] = useState([]);
    const [modalAbierto, setModalAbierto] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);

    const [mostrarConsensuados, setMostrarConsensuados] = useState(false);
    const [filtros, setFiltros] = useState({
        categoria: '',
        fecha_reporte_desde: '',
        fecha_reporte_hasta: '',
        fecha_acontecimiento_desde: '',
        fecha_acontecimiento_hasta: '',
        latitud: '',
        longitud: ''
    });

    // Estado para la paginación
    const [paginacion, setPaginacion] = useState({
        pageNumber: 0,
        pageSize: 10,
        totalElements: 0,
        totalPages: 0,
        first: true,
        last: false
    });

    // Función para buscar hechos con paginación
    const buscarHechos = async (page = 0, size = 10, filtrosActuales = filtros, consenso = mostrarConsensuados) => {
        try {
            setLoading(true);
            setError(false);
            
            let response;

            if (isModoColeccion) {
                response = await api.getHechosPorColeccion(id, filtrosActuales, consenso, page, size);
            } else {
                // --- MODO GENERAL ---
                const paramsGenerales = {
                    ...filtrosActuales,
                    tipoNavegacion: consenso ? 'curada' : 'irrestricta'
                };
                response = await api.obtenerHechos(paramsGenerales, page, size);
            }

            // Extraer datos de la respuesta paginada
            setHechos(response.content || response);
            
            // Si la respuesta tiene metadata de paginación, usarla
            if (response.content) {
                setPaginacion({
                    pageNumber: response.pageNumber,
                    pageSize: response.pageSize,
                    totalElements: response.totalElements,
                    totalPages: response.totalPages,
                    first: response.first,
                    last: response.last
                });
            } else {
                // Si no hay paginación (retrocompatibilidad), establecer valores por defecto
                setPaginacion({
                    pageNumber: 0,
                    pageSize: response.length,
                    totalElements: response.length,
                    totalPages: 1,
                    first: true,
                    last: true
                });
            }
        } catch (e) {
            console.error("Error al buscar hechos:", e);
            setError(true);
            setHechos([]);
        } finally {
            setLoading(false);
        }
    };

    // --- Handlers ---

    const handleApplyFiltros = (nuevosFiltros) => {
        setFiltros(nuevosFiltros);
        setModalAbierto(false);
        // Volver a la primera página al aplicar filtros
        buscarHechos(0, paginacion.pageSize, nuevosFiltros, mostrarConsensuados);
    };

    const handleConsensoChange = (valor) => {
        setMostrarConsensuados(valor);
        // Volver a la primera página al cambiar el consenso
        buscarHechos(0, paginacion.pageSize, filtros, valor);
    };

    // Función para cambiar de página
    const handlePageChange = (newPage) => {
        buscarHechos(newPage, paginacion.pageSize, filtros, mostrarConsensuados);
    };

    // Función para cambiar el tamaño de página
    const handlePageSizeChange = (newSize) => {
        buscarHechos(0, parseInt(newSize), filtros, mostrarConsensuados);
    };

    useEffect(() => {
        if (isModoColeccion) {
            const cargarInfoColeccion = async () => {
                try {
                    const info = await api.obtenerColeccion(id);
                    setColeccion(info);
                    console.log(info);
                } catch (e) {
                    console.error("Error al cargar la info de la colección", e);
                }
            };
            cargarInfoColeccion();
        } else {
            setTituloPagina("Todos los Hechos");
        }
    }, [id, isModoColeccion]);

    useEffect(() => {
        buscarHechos();
    }, [id, isModoColeccion]);

    return (
        <div style={{ background: '#E8E8E8', minHeight: '100vh' }}>
            <div style={{ background: '#E8E8E8', height: 32 }}></div>
            <div style={{ maxWidth: 900, margin: '2rem auto 1.5rem auto', background: '#FFF9D6', borderRadius: 10, boxShadow: '0 2px 8px #99A88C', padding: '1.5rem 2rem' }}>

                {isModoColeccion ? (
                    <ColeccionInfo coleccion={coleccion} />
                ) : (
                    <h1 className="text-center mb-4">{tituloPagina}</h1>
                )}

                <ConsensoSwitch
                    mostrarConsensuados={mostrarConsensuados}
                    setMostrarConsensuados={handleConsensoChange}
                    onOpenFiltros={() => setModalAbierto(true)}
                    mostrarSwitch={isModoColeccion}
                />
                {modalAbierto && (
                    <FiltrosModal
                        filtros={filtros}
                        onClose={() => setModalAbierto(false)}
                        onApply={handleApplyFiltros}
                    />
                )}

                {/* Información de paginación y controles */}
                <div className="d-flex justify-content-between align-items-center mb-3 mt-3">
                    <div>
                        <span className="text-muted" style={{ fontSize: '0.9rem' }}>
                            Mostrando {hechos.length} de {paginacion.totalElements} hechos
                        </span>
                    </div>
                    <div>
                        <select
                            className="form-select form-select-sm"
                            value={paginacion.pageSize}
                            onChange={(e) => handlePageSizeChange(e.target.value)}
                            style={{ width: 'auto', display: 'inline-block' }}
                        >
                            <option value="5">5 por página</option>
                            <option value="10">10 por página</option>
                            <option value="20">20 por página</option>
                            <option value="50">50 por página</option>
                        </select>
                    </div>
                </div>

                {/* Mostrar error si existe */}
                {error && (
                    <div className="alert alert-danger" role="alert">
                        Error al cargar los hechos. Por favor, intenta nuevamente.
                    </div>
                )}

                {/* Mostrar spinner mientras carga */}
                {loading ? (
                    <div className="text-center py-5">
                        <Spinner animation="border" role="status" variant="primary">
                            <span className="visually-hidden">Cargando...</span>
                        </Spinner>
                        <p className="mt-3">Cargando hechos...</p>
                    </div>
                ) : (
                    <>
                        {/* Lista de hechos */}
                        {hechos.map(hecho => (
                            <HechoCard key={hecho.id} hecho={hecho} coleccion={isModoColeccion ? coleccion : null} />
                        ))}

                        {/* Mensaje si no hay hechos */}
                        {hechos.length === 0 && !loading && (
                            <div className="text-center py-5">
                                <p className="text-muted">No hay hechos disponibles con los filtros seleccionados.</p>
                            </div>
                        )}

                        {/* Controles de paginación */}
                        {paginacion.totalPages > 1 && (
                            <>
                                <div className="d-flex justify-content-center mt-4">
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
                                            const currentPage = paginacion.pageNumber;
                                            const showPage =
                                                index === 0 ||
                                                index === paginacion.totalPages - 1 ||
                                                (index >= currentPage - 2 && index <= currentPage + 2);

                                            if (!showPage) {
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

                                {/* Información adicional de paginación */}
                                <div className="text-center mt-2 mb-3">
                                    <small className="text-muted">
                                        Página {paginacion.pageNumber + 1} de {paginacion.totalPages}
                                    </small>
                                </div>
                            </>
                        )}
                    </>
                )}

            </div>
        </div>
    );
}