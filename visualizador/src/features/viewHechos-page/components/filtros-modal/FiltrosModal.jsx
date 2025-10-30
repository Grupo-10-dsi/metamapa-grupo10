import React, { useState } from 'react'; // <-- 1. Importa useState
import './filtros-modal.css';
import MapaSelector from './MapaSelector';

// 2. Ya no necesitamos 'setFiltros' como prop
export default function FiltrosModal({ filtros, onClose, onApply }) {

  // 3. Creamos un estado local, inicializado con los filtros del padre
  const [filtrosLocales, setFiltrosLocales] = useState(filtros);

  // 4. Un solo handler para todos los inputs de texto/fecha
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFiltrosLocales(f => ({ ...f, [name]: value }));
  };

  return (
      <div className="filtros-modal-bg">
        <div className="filtros-modal-box">
          <h3 className="filtros-modal-title">Filtros</h3>

          {/* 5. onSubmit ahora pasa 'filtrosLocales' a 'onApply' */}
          <form className="filtros-modal-form" onSubmit={e => { e.preventDefault(); onApply(filtrosLocales); }}>
            <label className="filtros-modal-label">Categoría</label>
            <input
                type="text"
                placeholder="Categoría"
                name="categoria" // <-- 6. Añade 'name'
                value={filtrosLocales.categoria} // <-- Usa 'filtrosLocales'
                onChange={handleChange} // <-- Usa el handler genérico
                className="filtros-modal-input"
            />
            <div className="filtros-modal-row">
              <div style={{ flex: 1 }}>
                <label className="filtros-modal-label">Fecha reporte desde</label>
                <input
                    type="date"
                    title="Fecha reporte desde"
                    name="fecha_reporte_desde" // <-- Añade 'name'
                    value={filtrosLocales.fecha_reporte_desde} // <-- Usa 'filtrosLocales'
                    onChange={handleChange}
                    className="filtros-modal-input"
                />
              </div>
              <div style={{ flex: 1 }}>
                <label className="filtros-modal-label">Fecha reporte hasta</label>
                <input
                    type="date"
                    title="Fecha reporte hasta"
                    name="fecha_reporte_hasta" // <-- Añade 'name'
                    value={filtrosLocales.fecha_reporte_hasta} // <-- Usa 'filtrosLocales'
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
                    title="Fecha acontecimiento desde"
                    name="fecha_acontecimiento_desde" // <-- Añade 'name'
                    value={filtrosLocales.fecha_acontecimiento_desde} // <-- Usa 'filtrosLocales'
                    onChange={handleChange}
                    className="filtros-modal-input"
                />
              </div>
              <div style={{ flex: 1 }}>
                <label className="filtros-modal-label">Fecha acontecimiento hasta</label>
                <input
                    type="date"
                    title="Fecha acontecimiento hasta"
                    name="fecha_acontecimiento_hasta" // <-- Añade 'name'
                    value={filtrosLocales.fecha_acontecimiento_hasta} // <-- Usa 'filtrosLocales'
                    onChange={handleChange}
                    className="filtros-modal-input"
                />
              </div>
            </div>
            <label className="filtros-modal-label" style={{marginTop:18}}>Ubicación en el mapa</label>
            <MapaSelector
                latitud={filtrosLocales.latitud}
                longitud={filtrosLocales.longitud}
                setFiltros={setFiltrosLocales} // <-- 7. Pasa el setter local
            />
            <div className="filtros-modal-btn-row">
              <button type="button" onClick={onClose} className="filtros-modal-btn-cancel">Cancelar</button>
              <button type="submit" className="filtros-modal-btn-apply">Aplicar</button>
            </div>
          </form>
          <button onClick={onClose} className="filtros-modal-close" title="Cerrar">×</button>
        </div>
      </div>
  );
}