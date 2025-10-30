import React from 'react';
import './hecho-card.css';

export default function HechoCard({ hecho }) {
  return (
    <div className="hecho-card">
      {hecho.imagen && (
        <div className="hecho-card-img-box">
          <img src={hecho.imagen} alt={hecho.titulo} className="hecho-card-img" />
        </div>
      )}
      <div style={{ flex: 1 }}>
        <div className="hecho-card-title-row">
          <a href="#" className="hecho-card-title">{hecho.titulo}</a>
          <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
            {hecho.contribuyente && (
              <span className="hecho-card-autor">por {hecho.contribuyente}</span>
            )}
            {hecho.consenso && (
              <span className="hecho-card-consenso">
                <svg width="18" height="18" viewBox="0 0 20 20" fill="none" style={{ verticalAlign: 'middle' }}><path d="M7.5 13.5L4 10L5.41 8.59L7.5 10.67L14.59 3.59L16 5L7.5 13.5Z" fill="#7d4f1e"/></svg>
                Consensuado
              </span>
            )}
          </div>
        </div>
        <div className="hecho-card-desc">{hecho.descripcion}</div>
        <div className="hecho-card-chips">
          {hecho.categoria && (
            <span className="hecho-card-chip-categoria">{hecho.categoria}</span>
          )}
          {hecho.ubicacion && (
            <span className="hecho-card-chip-ubicacion">{hecho.ubicacion}</span>
          )}
        </div>
      </div>
    </div>
  );
}
