import React from 'react';
import './filtros-modal.css';
import MapaSelector from './MapaSelector';

export default function FiltrosModal({ filtros, setFiltros, onClose, onApply }) {
  return (
    <div className="filtros-modal-bg">
      <div className="filtros-modal-box">
        <h3 className="filtros-modal-title">Filtros</h3>
        <form className="filtros-modal-form" onSubmit={e => { e.preventDefault(); onApply(); }}>
          <label className="filtros-modal-label">Categoría</label>
          <input
            type="text"
            placeholder="Categoría"
            value={filtros.categoria}
            onChange={e => setFiltros(f => ({ ...f, categoria: e.target.value }))}
            className="filtros-modal-input"
          />
          <div className="filtros-modal-row">
            <div style={{ flex: 1 }}>
              <label className="filtros-modal-label">Fecha reporte desde</label>
              <input
                type="date"
                title="Fecha reporte desde"
                value={filtros.fecha_reporte_desde}
                onChange={e => setFiltros(f => ({ ...f, fecha_reporte_desde: e.target.value }))}
                className="filtros-modal-input"
              />
            </div>
            <div style={{ flex: 1 }}>
              <label className="filtros-modal-label">Fecha reporte hasta</label>
              <input
                type="date"
                title="Fecha reporte hasta"
                value={filtros.fecha_reporte_hasta}
                onChange={e => setFiltros(f => ({ ...f, fecha_reporte_hasta: e.target.value }))}
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
                value={filtros.fecha_acontecimiento_desde}
                onChange={e => setFiltros(f => ({ ...f, fecha_acontecimiento_desde: e.target.value }))}
                className="filtros-modal-input"
              />
            </div>
            <div style={{ flex: 1 }}>
              <label className="filtros-modal-label">Fecha acontecimiento hasta</label>
              <input
                type="date"
                title="Fecha acontecimiento hasta"
                value={filtros.fecha_acontecimiento_hasta}
                onChange={e => setFiltros(f => ({ ...f, fecha_acontecimiento_hasta: e.target.value }))}
                className="filtros-modal-input"
              />
            </div>
          </div>
          <label className="filtros-modal-label" style={{marginTop:18}}>Ubicación en el mapa</label>
          <MapaSelector
            latitud={filtros.latitud}
            longitud={filtros.longitud}
            setFiltros={setFiltros}
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

