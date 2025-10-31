import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Button from 'react-bootstrap/Button'
import './botones-opciones.css'



function BotonesOpciones ({ mostrarEnPantalla }) {
    const [activo, setActivo] = useState(mostrarEnPantalla)
    const navigate = useNavigate()
    const handleClick = (nombreBoton) => {
        setActivo(nombreBoton)
        navigate(`/perfil/${nombreBoton}`)
    }


    const path = window.location.pathname.split('/').pop()

    return (
        <div className="botones-opcion-container">
            <Button
                className={
                    path === 'solicitudes' ? 'opcion-button-style-activo reset-button': 'opcion-button-style reset-button'
                }
                onClick={() => handleClick('solicitudes')}
            >
                Solicitudes
            </Button>
            <Button
                className={
                    path === 'colecciones' ? 'opcion-button-style-activo reset-button' : 'opcion-button-style reset-button'
                }
                onClick={() => handleClick('colecciones')}
            >
                Colecciones
            </Button>
        </div>
    )
}

export default BotonesOpciones