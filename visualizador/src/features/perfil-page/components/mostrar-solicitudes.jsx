import SolicitudCard from "./solicitud-card/solicitud-card"
import { useEffect, useState } from 'react'
import api from "../../../api/api-agregador";
import './solicitud-card/solicitud-card.css'
import './MostrarSolicitudes.css'
import { Inbox } from 'react-bootstrap-icons';
import { Pagination, Spinner } from 'react-bootstrap';

const MostrarSolicitudes = () => {
    const [solicitudes, setSolicitudes] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(false)

    const [filtroActivo, setFiltroActivo] = useState('PENDIENTE')

    const [paginacion, setPaginacion] = useState({
        pageNumber: 0,
        pageSize: 10,
        totalElements: 0,
        totalPages: 0,
        first: true,
        last: false
    })

    const fetchSolicitudes = async (page = 0, size = 10, estado = filtroActivo) => {
        try {
            setLoading(true)
            setError(false)
            // Pedimos página paginada del backend con filtro de estado
            const response = await api.obtenerSolicitudesPaged({ page, size, estado })
            setSolicitudes(response?.content ?? [])
            setPaginacion({
                pageNumber: response?.pageNumber ?? 0,
                pageSize: response?.pageSize ?? size,
                totalElements: response?.totalElements ?? 0,
                totalPages: response?.totalPages ?? 0,
                first: response?.first ?? true,
                last: response?.last ?? false
            })
        } catch (error) {
            console.log(error.message)
            setError(true)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchSolicitudes(0, paginacion.pageSize, filtroActivo)
    }, [filtroActivo])

    const handleEliminarSolicitud = async () => {
        // Refrescar la página actual con el filtro activo
        fetchSolicitudes(paginacion.pageNumber, paginacion.pageSize, filtroActivo)
    }

    const solicitudesFiltradas = solicitudes

    // Mensaje dinámico para cuando no hay solicitudes
    let textoSinSolicitudes = "No hay solicitudes pendientes."
    if (filtroActivo === 'ACEPTADA') {
        textoSinSolicitudes = "No hay solicitudes que hayas aceptado."
    } else if (filtroActivo === 'RECHAZADA') {
        textoSinSolicitudes = "No hay solicitudes que hayas rechazado."
    }

    // Handlers de paginación
    const handlePageChange = (newPage) => {
        if (newPage < 0 || newPage >= paginacion.totalPages) return
        fetchSolicitudes(newPage, paginacion.pageSize, filtroActivo)
    }

    const handlePageSizeChange = (newSize) => {
        const sizeInt = parseInt(newSize)
        fetchSolicitudes(0, sizeInt, filtroActivo)
    }

    return (
        <>
            <div className="d-flex justify-content-between align-items-center">
                <h2>Solicitudes de eliminacion</h2>
            </div>

            {/* Menú de filtros */}
            <div className="solicitud-filtro-menu mb-4">
                <span
                    className={`filtro-opcion ${filtroActivo === 'ACEPTADA' ? 'activo' : ''}`}
                    onClick={() => setFiltroActivo('ACEPTADA')}
                >
                    Aceptadas
                </span>
                <span className="filtro-separador">|</span>
                <span
                    className={`filtro-opcion ${filtroActivo === 'PENDIENTE' ? 'activo' : ''}`}
                    onClick={() => setFiltroActivo('PENDIENTE')}
                >
                    Pendientes
                </span>
                <span className="filtro-separador">|</span>
                <span
                    className={`filtro-opcion ${filtroActivo === 'RECHAZADA' ? 'activo' : ''}`}
                    onClick={() => setFiltroActivo('RECHAZADA')}
                >
                    Rechazadas
                </span>
            </div>

            {/* Controles superiores: cantidad y tamaño de página */}
            <div className="d-flex justify-content-between align-items-center mb-3">
                <div>
                    <span className="text-muted">
                        Mostrando {solicitudesFiltradas.length} de {paginacion.totalElements} solicitudes
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
                    Error al cargar las solicitudes. Por favor, intenta nuevamente.
                </div>
            )}

            {loading ? (
                <div className="text-center py-5">
                    <Spinner animation="border" role="status" variant="primary">
                        <span className="visually-hidden">Cargando...</span>
                    </Spinner>
                    <p className="mt-3">Cargando solicitudes...</p>
                </div>
            ) : (
                <>
                    {solicitudesFiltradas.length > 0 && (
                        <div className="fondo-gris">
                            {solicitudesFiltradas.map((result) => (
                                <SolicitudCard
                                    key={result.id}
                                    solicitud={result}
                                    solicitudId={result.id}
                                    solicitudEstado={result.estado}
                                    justificacion={result.justificacion}
                                    idHecho={result.idHecho}
                                    handleEliminarSolicitud={handleEliminarSolicitud}
                                    vistaActual={filtroActivo}
                                />
                            ))}
                        </div>
                    )}

                    {/* --- 6. MENSAJE DE "VACÍO" DINÁMICO --- */}
                    {solicitudesFiltradas.length === 0 && (
                        <div
                            className="text-center p-4 p-md-5 text-secondary"
                            style={{
                                backgroundColor: '#f8f9fa',
                                borderRadius: '12px',
                                marginTop: '1.5rem'
                            }}
                        >
                            <Inbox size={48} className="mb-3" />
                            <h4 className="fw-normal">No hay solicitudes</h4>
                            <p className="mb-0">{textoSinSolicitudes}</p>
                        </div>
                    )}

                    {/* Controles de paginación */}
                    {paginacion.totalPages > 1 && (
                        <div className="d-flex justify-content-center mt-5">
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
                                    const currentPage = paginacion.pageNumber
                                    const showPage =
                                        index === 0 ||
                                        index === paginacion.totalPages - 1 ||
                                        (index >= currentPage - 2 && index <= currentPage + 2)

                                    if (!showPage) {
                                        if (index === currentPage - 3 || index === currentPage + 3) {
                                            return <Pagination.Ellipsis key={index} disabled />
                                        }
                                        return null
                                    }

                                    return (
                                        <Pagination.Item
                                            key={index}
                                            active={index === paginacion.pageNumber}
                                            onClick={() => handlePageChange(index)}
                                        >
                                            {index + 1}
                                        </Pagination.Item>
                                    )
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

                    {/* Info de página actual */}
                    <div className="text-center mt-3">
                        <small className="text-muted">
                            Página {paginacion.pageNumber + 1} de {paginacion.totalPages}
                        </small>
                    </div>
                </>
            )}
        </>
    )
}

export default MostrarSolicitudes