import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ColeccionInfo from './components/coleccion-info/ColeccionInfo';
import ConsensoSwitch from './components/consenso-switch/ConsensoSwitch';
import FiltrosModal from './components/filtros-modal/FiltrosModal';
import HechoCard from './components/hecho-card/HechoCard';
import api from '../../api/api-agregador';

export default function ColeccionHechosPage() {
    const { id } = useParams();
    const isModoColeccion = Boolean(id);

    const [coleccion, setColeccion] = useState(null);
    const [tituloPagina, setTituloPagina] = useState("Cargando...");
    const [hechos, setHechos] = useState([]);
    const [modalAbierto, setModalAbierto] = useState(false);

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

    // --- Handlers (Solo actualizan el estado) ---

    const handleApplyFiltros = (nuevosFiltros) => {
        setFiltros(nuevosFiltros);
        setModalAbierto(false);
    };

    const handleConsensoChange = (valor) => {
        setMostrarConsensuados(valor);
    };

    useEffect(() => {
        if (isModoColeccion) {
            const cargarInfoColeccion = async () => {
                try {
                    const info = await api.obtenerColeccion(id);
                    setColeccion(info);
                    console.log(info);
                } catch (e) {
                    console.error("Error al cargar la info de la colección", e);
                }
            };
            cargarInfoColeccion();
        } else {
            setTituloPagina("Todos los Hechos");
        }
    }, [id, isModoColeccion]);


    useEffect(() => {


        const buscarHechos = async () => {
            try {
                let data;

                if (isModoColeccion) {
                    data = await api.getHechosPorColeccion(id, filtros, mostrarConsensuados);
                } else {
                    // --- MODO GENERAL ---
                    const paramsGenerales = {
                        ...filtros,
                        tipoNavegacion: mostrarConsensuados ? 'curada' : 'irrestricta'
                    };
                    data = await api.obtenerHechos(paramsGenerales);
                }
                setHechos(data);
            } catch (e) {
                console.error("Error al buscar hechos:", e);
                setHechos([]);
            }
        };

        buscarHechos();

    }, [id, isModoColeccion, mostrarConsensuados, filtros, coleccion]);

    return (
        <div style={{ background: '#E8E8E8', minHeight: '100vh' }}>
            <div style={{ background: '#E8E8E8', height: 32 }}></div>
            <div style={{ maxWidth: 900, margin: '2rem auto 1.5rem auto', background: '#FFF9D6', borderRadius: 10, boxShadow: '0 2px 8px #99A88C', padding: '1.5rem 2rem' }}>

                {isModoColeccion ? (
                    <ColeccionInfo coleccion={coleccion} />
                ) : (
                    <h1 className="text-center mb-4">{tituloPagina}</h1>
                )}

                <ConsensoSwitch
                    mostrarConsensuados={mostrarConsensuados}
                    setMostrarConsensuados={handleConsensoChange}
                    onOpenFiltros={() => setModalAbierto(true)}
                    mostrarSwitch={isModoColeccion}
                />
                {modalAbierto && (
                    <FiltrosModal
                        filtros={filtros} // Pasa los filtros actuales
                        onClose={() => setModalAbierto(false)}
                        onApply={handleApplyFiltros} // Pasa el handler de aplicar
                    />
                )}

                {hechos.map(hecho => (
                    <HechoCard key={hecho.id} hecho={hecho} coleccion={isModoColeccion ? coleccion : null} />
                ))}

            </div>
        </div>
    );
}