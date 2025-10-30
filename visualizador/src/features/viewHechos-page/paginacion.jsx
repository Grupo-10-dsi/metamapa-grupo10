import React from 'react';

// Componente de paginación básico
export default function Paginacion({ paginaActual = 1, totalPaginas = 1, onChange }) {
  if (totalPaginas <= 1) return null;

  const paginas = [];
  for (let i = 1; i <= totalPaginas; i++) {
    paginas.push(i);
  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', margin: '1.5rem 0' }}>
      {paginas.map(num => (
        <button
          key={num}
          style={{
            margin: '0 4px',
            padding: '6px 14px',
            borderRadius: 6,
            border: num === paginaActual ? '2px solid #335C67' : '1px solid #ccc',
            background: num === paginaActual ? '#FFF9D6' : '#fff',
            color: num === paginaActual ? '#335C67' : '#444',
            fontWeight: num === paginaActual ? 'bold' : 'normal',
            cursor: 'pointer'
          }}
          onClick={() => onChange && onChange(num)}
        >
          {num}
        </button>
      ))}
    </div>
  );
}
