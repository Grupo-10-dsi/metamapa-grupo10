import SolicitudCard from "./solicitud-card/solicitud-card"
import { useEffect, useState } from 'react'
import api from "../../../api/api-agregador";
import './solicitud-card/solicitud-card.css'
import './MostrarSolicitudes.css'
import { Inbox } from 'react-bootstrap-icons';

const MostrarSolicitudes = () => {
    const [solicitudes, setSolicitudes] = useState([])
    const [loading, setLoading] = useState(true)

    const [filtroActivo, setFiltroActivo] = useState('PENDIENTE')

    const fetchSolicitudes = async () => {
        try {
            // Asumimos que esto trae TODAS (pendientes, aceptadas, etc.)
            const response = await api.obtenerSolicitudes()
            setSolicitudes(response)
        } catch (error) {
            console.log(error.message)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchSolicitudes()
    }, [])

    const handleEliminarSolicitud = async () => {
        fetchSolicitudes()
    }

    const solicitudesFiltradas = solicitudes.filter(
        (s) => s.estado === filtroActivo
    )

    // Mensaje dinámico para cuando no hay solicitudes
    let textoSinSolicitudes = "No hay solicitudes pendientes."
    if (filtroActivo === 'ACEPTADA') {
        textoSinSolicitudes = "No hay solicitudes que hayas aceptado."
    } else if (filtroActivo === 'RECHAZADA') {
        textoSinSolicitudes = "No hay solicitudes que hayas rechazado."
    }


    return (
        <>
            <div className="d-flex justify-content-between align-items-center">
                <h2>Solicitudes de eliminacion</h2>
            </div>

            {/* --- 4. MENÚ DE FILTROS (COMO EN TU DIBUJO) --- */}
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


            {loading ? (
                <div
                    style={{
                        position: 'fixed',
                        top: '50%',
                        left: '55%',
                        transform: 'translate(-50%, -50%)',
                        zIndex: 1000,
                    }}
                >
                </div>
            ) : (
                <>
                    {solicitudesFiltradas.length > 0 && (
                        <div className="fondo-gris">
                            {solicitudesFiltradas.map((result) => (
                                <SolicitudCard
                                    key={result.id} // Añadir key es buena práctica
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
                            {/* Usamos el texto dinámico */}
                            <p className="mb-0">{textoSinSolicitudes}</p>
                        </div>
                    )}
                </>
            )}
        </>
    )
}

export default MostrarSolicitudes