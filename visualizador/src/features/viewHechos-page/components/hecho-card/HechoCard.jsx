import React from 'react';
import './hecho-card.css';
import { Link } from 'react-router-dom';

// ... imports

export default function HechoCard({ hecho, coleccion }) {
    return (
        <Link
            to={`/hecho/${hecho.id}`}
            state={{
                coleccionId: coleccion?.id,
                coleccionNombre: coleccion?.titulo
            }}
            className="hecho-card-link-wrapper"
            style={{ textDecoration: 'none' }} // Agregado para quitar subrayado azul del link general
        >
            <div className="hecho-card">
                {hecho.imagen && (
                    <div className="hecho-card-img-box">
                        <img src={hecho.imagen} alt={hecho.titulo} className="hecho-card-img" />
                    </div>
                )}

                {/* AQUI ESTA EL CAMBIO IMPORTANTE: flex: 1 para ocupar el resto */}
                <div style={{ flex: 1, display: 'flex', flexDirection: 'column', padding: '10px' }}>

                    {/* 1. Título arriba (sin row que lo limite) */}
                    <div className="mb-2"> {/* Margen abajo para separar del autor */}
                        <span className="hecho-card-title" style={{ display: 'block', width: '100%' }}>
                            {hecho.titulo}
                        </span>
                    </div>

                    {/* 2. Autor y Consenso abajo del título */}
                    <div style={{ display: 'flex', alignItems: 'center', gap: 10, fontSize: '0.9rem', color: '#666', marginBottom: '8px' }}>
                        {hecho.contribuyente && (
                            <span className="hecho-card-autor">
                                👤 {hecho.contribuyente.contribuyente_nombre || 'Anónimo'}
                            </span>
                        )}

                        {hecho.consenso && (
                            <span className="hecho-card-consenso" title="Hecho verificado">
                                {/* Mantuve tu SVG pero ajusté el margen */}
                                <svg width="16" height="16" viewBox="0 0 20 20" fill="none" style={{ verticalAlign: 'text-bottom', marginRight: 4 }}>
                                    <path d="M7.5 13.5L4 10L5.41 8.59L7.5 10.67L14.59 3.59L16 5L7.5 13.5Z" fill="#7d4f1e"/>
                                </svg>
                                Consensuado
                            </span>
                        )}
                    </div>

                    {/* 3. Descripción */}
                    <div className="hecho-card-desc mb-3" style={{ color: '#333' }}>
                        {hecho.descripcion}
                    </div>

                    {/* 4. Chips (Categoría y ubicación) al final */}
                    {/* Usamos marginTop: 'auto' para que si la card crece, esto quede siempre abajo */}
                    <div className="hecho-card-chips" style={{ marginTop: 'auto' }}>
                        {hecho.categoria && (
                            <span className="hecho-card-chip-categoria">{hecho.categoria.detalle}</span>
                        )}
                        {/* Agregué una validación extra para no mostrar chips vacíos de ubicación */}
                        {(hecho.ubicacion?.latitud || hecho.ubicacion?.longitud) && (
                            <span className="hecho-card-chip-ubicacion" style={{backgroundColor: '#e9f5e9', color: '#2e7d32'}}>
                                📍 {hecho.ubicacion.latitud?.toFixed(4)}, {hecho.ubicacion.longitud?.toFixed(4)}
                            </span>
                        )}
                    </div>
                </div>
            </div>
        </Link>
    );
}