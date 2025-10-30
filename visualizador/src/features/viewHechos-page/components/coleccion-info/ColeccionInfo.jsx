import React from 'react';
import styles from './ColeccionInfo.module.css';

export default function ColeccionInfo({ coleccion }) {

    if (!coleccion) {
        return null;
    }

    // Creamos una cadena de texto con los nombres de las fuentes
    const nombresFuentes = coleccion.fuentes
        .map(fuente => fuente.nombre) // <-- Usamos .map()
        .join(', '); // <-- Unimos los nombres con ", "

    return (
        <div className={styles.container}>
            <h2 className={styles.titulo}>{coleccion.titulo}</h2>
            <div className={styles.descripcion}>{coleccion.descripcion}</div>
            <div className={styles.fuente}>Fuentes: {nombresFuentes}</div>
        </div>
    );
}