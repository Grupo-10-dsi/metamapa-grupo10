import React from 'react';
import './consenso-switch.css';

export default function ConsensoSwitch({
                                           mostrarConsensuados,
                                           setMostrarConsensuados,
                                           onOpenFiltros,
                                           mostrarSwitch = true
                                       }) {
    return (
        <div className="consenso-switch-row">

            {mostrarSwitch ? (
                <div className="consenso-switch-label-row">
                    <span className="consenso-switch-label">Mostrar solo hechos consensuados</span>
                    <label className="consenso-switch-checkbox-label">
                        <input
                            type="checkbox"
                            checked={mostrarConsensuados}
                            onChange={e => setMostrarConsensuados(e.target.checked)}
                            className="consenso-switch-checkbox"
                        />
                        <span className={"consenso-switch-checkbox-text" + (mostrarConsensuados ? " active" : "") }>
              {mostrarConsensuados ? 'Consensuados' : 'Todos'}
            </span>
                    </label>
                </div>
            ) : (
                <div></div>
            )}

            <button type="button" onClick={onOpenFiltros} className="consenso-switch-btn">
                Filtros
            </button>
        </div>
    );
}