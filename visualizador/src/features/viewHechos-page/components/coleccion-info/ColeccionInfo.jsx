import React from 'react';
import styles from './ColeccionInfo.module.css';

export default function ColeccionInfo({ coleccion }) {
    if (!coleccion) return null;

    // Determinar la fuente de datos disponible
    const fuentesArray = Array.isArray(coleccion.fuentes) ? coleccion.fuentes : null;
    const urlsArray = !fuentesArray && Array.isArray(coleccion.urls_fuente) ? coleccion.urls_fuente : null;

    const nombresFuentes = fuentesArray
        ? fuentesArray.map(f => f?.nombre ?? f?.url ?? 'Fuente').join(', ')
        : urlsArray
            ? urlsArray.join(', ')
            : 'Sin fuentes';

    return (
        <div className={styles.container}>
            <h2 className={styles.titulo}>{coleccion.titulo}</h2>
            {coleccion.descripcion && (
                <div className={styles.descripcion}>{coleccion.descripcion}</div>
            )}
            <div className={styles.fuente}>Fuentes: {nombresFuentes}</div>
        </div>
    );
}