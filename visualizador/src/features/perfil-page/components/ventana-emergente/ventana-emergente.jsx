import './ventana-emergente.css'
import {useState} from 'react';

const VentanaEmergente = ({ mensaje, onConfirm, onCancel, setMotivo }) => {

    const cerrarVentana = () => {
        onCancel();
    }
    return (
        <div className="ventana-flotante-confirmacion-overlay">
            <div className="ventana-flotante-confirmacion">
                <h2>{mensaje}</h2>
                <div className="motivo-texto">
                    { /*
                    <TextField
                        label="Motivo (opcional)"
                        variant="outlined"
                        fullWidth
                        onChange={(e) => setMotivo(e.target.value)}
                    />
                    */}
                </div>
                <button className="boton-confirmar-reserva" onClick={onConfirm}>
                    CONFIRMAR
                </button>
                <button className="boton-cerrar-ventana" onClick={cerrarVentana}
                >
                    CANCELAR
                </button>
            </div>
        </div>
    )
}

export default VentanaEmergente