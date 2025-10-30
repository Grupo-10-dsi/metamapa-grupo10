
import ColeccionCard from './coleccion-card/coleccion-card'
import { useEffect, useState } from 'react'
import api from "../../../api/api-agregador";
import './coleccion-card/coleccion-card.css'



const MostrarColecciones= () => {
    const [colecciones, setColecciones] = useState([])
    const [loading, setLoading] = useState(true)


    const fetchColecciones = async () => {
        try {
            const response = await api.obtenerColecciones()
            setColecciones(response)
            console.log('colecciones:', colecciones)
            console.log(colecciones.length)
        } catch (error) {
            console.log(error.message)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchColecciones()
    }, [])



    const handleEliminarColeccion = async (id) => {
        console.log(id)
        await api.eliminarColeccion(id)
        const response = await api.obtenerColecciones()
        setColecciones(response)
    }

    return (
        <>
            <h2>Colecciones</h2>
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
                    {colecciones.length > 0 && (
                        <div className="fondo-gris">
                            {colecciones.map((result) => (
                                <ColeccionCard key={result.id} coleccion={result} coleccionId={result.id}  handleEliminarColeccion={handleEliminarColeccion} />
                            ))}
                        </div>
                    )}
                    {colecciones.length === 0 && <p>Todavia no tenes colecciones.</p>}
                </>
            )}
        </>
    )
}

export default MostrarColecciones