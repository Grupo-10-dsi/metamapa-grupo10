import React from 'react';
import './hecho-card.css';
import { Link } from 'react-router-dom'; // <-- 1. Importa Link

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

            {/* --- CORRECCIÓN 2: Usa Link para ir al detalle del hecho --- */}
            <Link to={`/hecho/${hecho.id}`} className="hecho-card-title">
              {hecho.titulo}
            </Link>

            <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>

              {/* --- CORRECCIÓN 3: Accede a la propiedad del objeto --- */}
              {hecho.contribuyente && (
                  <span className="hecho-card-autor">
                por {hecho.contribuyente.contribuyente_nombre || 'Anónimo'}
              </span>
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
                <span className="hecho-card-chip-categoria">{hecho.categoria.detalle}</span>
            )}
            {hecho.ubicacion.latitud && (
                <span className="hecho-card-chip-ubicacion">{hecho.ubicacion.latitud}</span>
            )}
              {hecho.ubicacion.longitud && (
                  <span className="hecho-card-chip-ubicacion">{hecho.ubicacion.longitud}</span>
              )}
          </div>
        </div>
      </div>
  );
}