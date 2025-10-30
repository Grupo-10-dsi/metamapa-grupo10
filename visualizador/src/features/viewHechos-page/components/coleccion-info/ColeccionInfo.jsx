import React from 'react';
import styles from './ColeccionInfo.module.css';

export default function ColeccionInfo({ coleccion }) {
  return (
    <div className={styles.container}>
      <h2 className={styles.titulo}>{coleccion.tituloColeccion}</h2>
      <div className={styles.fuente}>Fuente: {coleccion.fuenteColeccion}</div>
      <div className={styles.descripcion}>{coleccion.descripcionColeccion}</div>
    </div>
  );
}
