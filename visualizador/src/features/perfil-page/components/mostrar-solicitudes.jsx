import SolicitudCard from "./solicitud-card/solicitud-card"
import { useEffect, useState } from 'react'
import api from "../../../api/api-agregador";
import './solicitud-card/solicitud-card.css'

const MostrarSolicitudes = () => {
    const [solicitudes, setSolicitudes] = useState([])
    const [loading, setLoading] = useState(true)

    const fetchSolicitudes = async () => {
        try {
            const response = await api.obtenerSolicitudes()
            setSolicitudes(response)
            console.log('colecciones:', solicitudes)
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

    return (
        <>
            <h2>Solicitudes de eliminacion</h2>
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
                    {/*}  <CircularIndeterminate />*/}
                </div>
            ) : (
                <>
                    {solicitudes.length > 0 && (
                        <div className="fondo-gris">
                            {solicitudes.map((result) => (
                                <SolicitudCard solicitud={result} solicitudId={result.id} solicitudEstado={result.estadoSolicitud} justificacion={result.justificacion}  handleEliminarSolicitud={handleEliminarSolicitud}/>
                            ))}
                        </div>
                    )}

                    {/* --- 👇 AQUÍ LA MEJORA --- */}
                    {solicitudes.length === 0 && (
                        <div
                            className="text-center p-4 p-md-5 text-secondary"
                            style={{
                                backgroundColor: '#f8f9fa', // Fondo gris muy claro (Bootstrap gray-100)
                                borderRadius: '12px',      // Bordes redondeados
                                marginTop: '1.5rem'        // Margen superior
                            }}
                        >
                            <Inbox size={48} className="mb-3" />
                            <h4 className="fw-normal">No hay solicitudes</h4>
                            <p className="mb-0">Todavía no tenes solicitudes pendientes.</p>
                        </div>
                    )}
                    {/* --- 👆 FIN DE LA MEJORA --- */}
                </>
            )}
        </>
    )
}

export default MostrarSolicitudes