import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ColeccionInfo from './components/coleccion-info/ColeccionInfo';
import ConsensoSwitch from './components/consenso-switch/ConsensoSwitch';
import FiltrosModal from './components/filtros-modal/FiltrosModal';
import HechoCard from './components/hecho-card/HechoCard';
import Paginacion from './paginacion';
import { getHechosPorColeccion } from '../../api';

export default function ColeccionHechosPage(props) {
  const { id } = useParams();
  const [mostrarConsensuados, setMostrarConsensuados] = useState(false);
  const [filtros, setFiltros] = useState({
    categoria: '',
    fecha_reporte_desde: '',
    fecha_reporte_hasta: '',
    fecha_acontecimiento_desde: '',
    fecha_acontecimiento_hasta: '',
    latitud: '',
    longitud: ''
  });
  const [modalAbierto, setModalAbierto] = useState(false);
  const [hechos, setHechos] = useState([]);  // esto deberia estar en la clase padre pasa



  // Función para buscar hechos filtrados
  const buscarHechos = async (filtrosActuales = filtros, consensoActual = mostrarConsensuados) => {
    try {
      const data = await getHechosPorColeccion({ id, filtros: filtrosActuales, consenso: consensoActual });
      setHechos(data);
    } catch (e) {
      setHechos([]);
    }
  };

  // Buscar hechos al aplicar filtros
  const handleApplyFiltros = () => {
    setModalAbierto(false);
    buscarHechos();
  };

  // Buscar hechos al cambiar consenso
  const handleConsensoChange = (valor) => {
    setMostrarConsensuados(valor);
    buscarHechos(filtros, valor);
  };

  // Inicial: cargar hechos al montar el componente
  useEffect(() => {
    buscarHechos();
    // eslint-disable-next-line
  }, [id]);

  // Simulación: agrego la propiedad consenso a los hechos mockeados
  const hechosConConsenso = hechos.map((h, i) => ({ ...h, consenso: i % 2 === 0 }));
  let hechosFiltrados = mostrarConsensuados
    ? hechosConConsenso.filter(h => h.consenso)
    : hechosConConsenso;

  return (
    <div style={{ background: '#E8E8E8', minHeight: '100vh' }}>
      <div style={{ background: '#E8E8E8', height: 32 }}></div>
      <div style={{ maxWidth: 900, margin: '2rem auto 1.5rem auto', background: '#FFF9D6', borderRadius: 10, boxShadow: '0 2px 8px #99A88C', padding: '1.5rem 2rem' }}>
        <ColeccionInfo coleccion={props} />
        <ConsensoSwitch
          mostrarConsensuados={mostrarConsensuados}
          setMostrarConsensuados={handleConsensoChange}
          onOpenFiltros={() => setModalAbierto(true)}
        />
        {modalAbierto && (
          <FiltrosModal
            filtros={filtros}
            setFiltros={setFiltros}
            onClose={() => setModalAbierto(false)}
            onApply={handleApplyFiltros}
          />
        )}
        {hechosFiltrados.map(hecho => (
          <HechoCard key={hecho.id} hecho={hecho} />
        ))}
        <Paginacion />
      </div>
    </div>
  );
}
