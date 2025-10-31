import React, { useState, useEffect } from 'react';
import homeBG from "../../images/home_background.jpg";
import {Container, Spinner} from "react-bootstrap";
import SourceCard from "./components/source-card.jsx";
import Mapa from "./components/mapa.jsx";
import ApiAgregador from "../../api/api-agregador";
import { FaDatabase, FaUsers, FaNetworkWired } from "react-icons/fa";

function HomePage() {
    const [scale, setScale] = useState(1);
    const [mapOpacity, setMapOpacity] = useState(0);

    const [sourceScrollOffset, setSourceScrollOffset] = useState(0);
    const sourcesSectionRef = React.useRef(null);

    const overlayColor = 'rgba(217,210,181,0.45)';

    const [hechosMapa, setHechosMapa] = useState([])
    const [loadingHechos, setLoadingHechos] = useState(true)

    useEffect(() => {
        ApiAgregador.obtenerUbicaciones()
                .then((data) => {
                        setHechosMapa(data)
                    }
                ).finally( () => {
            setLoadingHechos(false);
            console.log('done')
        })
    }, [])

    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    useEffect(() => {
        const handleScroll = () => {
            const scrollPosition = window.scrollY;
            const maxScroll = 400;

            const newScale = Math.max(0.8, 1 - scrollPosition / maxScroll * 0.2);
            setScale(newScale);
            const newOpacity = Math.min(1, scrollPosition / maxScroll);
            setMapOpacity(newOpacity);

            if (sourcesSectionRef.current) {
                const sectionTop = sourcesSectionRef.current.getBoundingClientRect().top;
                const triggerPoint = window.innerHeight - 200;
                const scrollDistance = Math.min(150, Math.max(0, triggerPoint - sectionTop));
                setSourceScrollOffset(scrollDistance);
            }
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);


    // TODO: Reemplazar con datos de fuentes
    // ... tu código ...

    // TODO: Reemplazar con datos de fuentes
    const sourceData = [
        {
            // Aumenta el 'size' y añade 'color'
            icon: <FaDatabase size={45} color="#6e2a34" />,
            text: (
                <>
                    <h5 className="fw-bold mb-1" style={{color: '#6e2a34'}}>
                        Datos Históricos y Oficiales -- Estatica
                    </h5>
                    <p className="fs-6 mb-0" style={{lineHeight: '1.4'}}>
                        Procesamos grandes lotes de datos (como CSV) de fuentes confiables,
                        como registros públicos u organismos gubernamentales...
                    </p>
                </>
            ),
            alignRight: false,
            delay: 0,
        },
        {
            // Aumenta el 'size' y añade 'color'
            icon: <FaUsers size={45} color="#6e2a34" />,
            text: (
                <>
                    <h5 className="fw-bold mb-1" style={{color: '#6e2a34'}}>
                        El Poder de la Comunidad -- Dinamica
                    </h5>
                    <p className="fs-6 mb-0" style={{lineHeight: '1.4'}}>
                        ¡Aquí es donde tú participas! Hechos reportados en tiempo real
                        por contribuyentes y usuarios como tú...
                    </p>
                </>
            ),
            alignRight: true,
            delay: 0.3,
        },
        {
            // Aumenta el 'size' y añade 'color'
            icon: <FaNetworkWired size={45} color="#6e2a34" />,
            text: (
                <>
                    <h5 className="fw-bold mb-1" style={{color: '#6e2a34'}}>
                        Una Red Conectada -- Proxy
                    </h5>
                    <p className="fs-6 mb-0" style={{lineHeight: '1.4'}}>
                        Nos integramos con otras instancias de MetaMapa para traerte
                        una visión más amplia. Accedemos a hechos más allá de...
                    </p>
                </>
            ),
            alignRight: false,
            delay: 0.6,
        },
    ];

    // ... el resto de tu componente ...


    return (
        <div>
            {/* --- HEADER --- */}
            <div
                style={{
                    backgroundImage: `url(${homeBG})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    minHeight: '80vh',
                    padding: '20px',
                    position: 'relative',

                }}
                className="d-flex flex-column justify-content-center align-items-center text-center"
            >
                {/* Overlay y Contenido del Header */}
                <div
                    style={{
                        position: 'absolute', top: 0, left: 0, width: '100%', height: '100%',
                        backgroundColor: overlayColor, zIndex: 1,
                    }}
                />
                <div
                    style={{
                        position: 'relative',
                        zIndex: 2,
                        backgroundColor: 'rgba(0, 0, 0, 0.4)',
                        padding: '20px 40px',
                        borderRadius: '10px',
                        backdropFilter: 'blur(3px)',
                        transform: `scale(${scale})`,
                        transition: 'transform 0.1s ease-out',
                        transformOrigin: 'top center',
                    }}
                >
                    <h1 style={{ color: '#F5F5DC', fontWeight: 'bold' }}>
                        ¿Que es MetaMapa?
                    </h1>
                    <p style={{ color: '#F5F5DC', fontSize: '1.25rem', maxWidth: '700px', fontWeight: '500' }}>
                        MetaMapa es una plataforma de visualización que centraliza incidentes y eventos de múltiples fuentes. Combinamos la precisión de los datos históricos y oficiales con la inmediatez de los reportes de la comunidad, todo enriquecido con información de una red conectada, para darte la visión más completa de lo que sucede a tu alrededor.
                    </p>
                </div>
            </div>

            {/* --- SECCIÓN 2: MAPA --- */}
            <Container style={{ opacity: mapOpacity, transition: 'opacity 0.5s ease-in' }}>
                {loadingHechos ?
                    <div className="text-center mt-5">
                        <Spinner animation="border" role="status" />
                        <p className="mt-3">Cargando hechos...</p>
                    </div>
                    : <Mapa hechosMapa={hechosMapa} />}
            </Container>

            {/* --- SECCIÓN 3: FUENTES --- */}
            <div ref={sourcesSectionRef} style={{ backgroundColor: 'white', padding: '60px 0' }}>
                <h2 className="text-center mb-5" style={{ color: '#9d3b48', fontWeight: 'bold' }}>
                    ¿De que fuentes obtenemos los hechos?
                </h2>
                <Container>
                    <div className="row">
                        {sourceData.map((data, index) => (
                            <SourceCard
                                key={index}
                                text={data.text}
                                alignRight={data.alignRight}
                                scrollOffset={sourceScrollOffset}
                                delay={data.delay}
                                icon={data.icon}
                            />
                        ))}
                    </div>
                </Container>
            </div>
        </div>
    );
}

export default HomePage;