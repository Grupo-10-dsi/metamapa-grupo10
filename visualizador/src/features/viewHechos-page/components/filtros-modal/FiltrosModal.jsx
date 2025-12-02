import React, { useState } from 'react';
import './filtros-modal.css';
import MapaSelector from './MapaSelector';

export default function FiltrosModal({ filtros, onClose, onApply }) {

    const [filtrosLocales, setFiltrosLocales] = useState(filtros);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFiltrosLocales(f => ({ ...f, [name]: value }));
    };

    // --- NUEVA FUNCIÓN: Resetea el estado local a vacío ---
    const handleLimpiar = () => {
        setFiltrosLocales({
            categoria: '',
            fecha_reporte_desde: '',
            fecha_reporte_hasta: '',
            fecha_acontecimiento_desde: '',
            fecha_acontecimiento_hasta: '',
            latitud: '', // Al vaciar esto, el MapaSelector debería quitar el marcador
            longitud: ''
        });
    };

    return (
        <div className="filtros-modal-bg">
            <div className="filtros-modal-box">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h3 className="filtros-modal-title">Filtros</h3>
                    {/* Opción A: Un enlace pequeño arriba */}
                    {/* <button type="button" onClick={handleLimpiar} style={{background:'none', border:'none', color:'#007bff', cursor:'pointer'}}>Limpiar todo</button> */}
                </div>

                <form className="filtros-modal-form" onSubmit={e => { e.preventDefault(); onApply(filtrosLocales); }}>
                    <label className="filtros-modal-label">Categoría</label>
                    <input
                        type="text"
                        placeholder="Categoría"
                        name="categoria"
                        value={filtrosLocales.categoria}
                        onChange={handleChange}
                        className="filtros-modal-input"
                    />

                    {/* ... (El resto de tus inputs de fecha siguen igual) ... */}
                    <div className="filtros-modal-row">
                        <div style={{ flex: 1 }}>
                            <label className="filtros-modal-label">Fecha reporte desde</label>
                            <input
                                type="date"
                                name="fecha_reporte_desde"
                                value={filtrosLocales.fecha_reporte_desde}
                                onChange={handleChange}
                                className="filtros-modal-input"
                            />
                        </div>
                        <div style={{ flex: 1 }}>
                            <label className="filtros-modal-label">Fecha reporte hasta</label>
                            <input
                                type="date"
                                name="fecha_reporte_hasta"
                                value={filtrosLocales.fecha_reporte_hasta}
                                onChange={handleChange}
                                className="filtros-modal-input"
                            />
                        </div>
                    </div>

                    <div className="filtros-modal-row">
                        <div style={{ flex: 1 }}>
                            <label className="filtros-modal-label">Fecha acontecimiento desde</label>
                            <input
                                type="date"
                                name="fecha_acontecimiento_desde"
                                value={filtrosLocales.fecha_acontecimiento_desde}
                                onChange={handleChange}
                                className="filtros-modal-input"
                            />
                        </div>
                        <div style={{ flex: 1 }}>
                            <label className="filtros-modal-label">Fecha acontecimiento hasta</label>
                            <input
                                type="date"
                                name="fecha_acontecimiento_hasta"
                                value={filtrosLocales.fecha_acontecimiento_hasta}
                                onChange={handleChange}
                                className="filtros-modal-input"
                            />
                        </div>
                    </div>

                    <label className="filtros-modal-label" style={{marginTop:18}}>Ubicación en el mapa</label>
                    <MapaSelector
                        latitud={filtrosLocales.latitud}
                        longitud={filtrosLocales.longitud}
                        setFiltros={setFiltrosLocales}
                    />

                    {/* --- BOTONERA --- */}
                    <div className="filtros-modal-btn-row">
                        {/* Opción B: Botón dedicado en la fila de botones */}
                        <button
                            type="button"
                            onClick={handleLimpiar}
                            className="filtros-modal-btn-clear" // <--- Clase nueva
                        >
                            Limpiar
                        </button>

                        <div style={{ flex: 1 }}></div> {/* Espaciador para empujar los otros a la derecha */}

                        <button type="button" onClick={onClose} className="filtros-modal-btn-cancel">Cancelar</button>
                        <button type="submit" className="filtros-modal-btn-apply">Aplicar</button>
                    </div>
                </form>
                <button onClick={onClose} className="filtros-modal-close" title="Cerrar">×</button>
            </div>
        </div>
    );
}