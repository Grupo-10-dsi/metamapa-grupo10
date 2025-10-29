import React, { useState, useEffect } from 'react';
import homeBG from "../../images/home_background.jpg";
import { Container } from "react-bootstrap";
import SourceCard from "./components/source-card.jsx";
import Mapa from "./components/mapa.jsx";
import ApiAgregador from "../../api/api-agregador";

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
                    setLoadingHechos(false)
                    console.log(data)
                }
            )
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
    const sourceData = [
        { text: 'SALEMALECOMALECONSALAAAAAAAAAAAAAAAAA 🗣️🗣️🗣️🗣️', alignRight: false, delay: 0, imgeSrc: "https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84bc50248b734f65b96d66a0a4" },
        { text: 'UN VIDEO MAS MI GENTE PA PERDER EL TIEMPO', alignRight: true, delay: 0.3, imgeSrc: "https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84bc50248b734f65b96d66a0a4" },
        { text: 'LINGANGULIGULIGULIWACHA LINGANGU LINGANGU', alignRight: false, delay: 0.6, imgeSrc: "https://image-cdn-ak.spotifycdn.com/image/ab67706c0000da84bc50248b734f65b96d66a0a4" },
    ];


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
                    transform: `scale(${scale})`,
                    transition: 'transform 0.1s ease-out',
                    transformOrigin: 'top center',
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
                        backdropFilter: 'blur(3px)'
                    }}
                >
                    <h1 style={{ color: '#F5F5DC', fontWeight: 'bold' }}>
                        ¿Que es MetaMapa?
                    </h1>
                    <p style={{ color: '#F5F5DC', fontSize: '1.25rem', maxWidth: '600px', fontWeight: '500' }}>
                        MetaMapa es un meta pero mapa, que no es ni pinch ni punch, osea
                        digamos, tiene hechos y colecciones
                    </p>
                </div>
            </div>

            {/* --- SECCIÓN 2: MAPA --- */}
            <Container style={{ opacity: mapOpacity, transition: 'opacity 0.5s ease-in' }}>
                {loadingHechos ? <p> cargando hechos </p> : <Mapa hechosMapa={hechosMapa} />}
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
                                imageSrc={data.imgeSrc}
                            />
                        ))}
                    </div>
                </Container>
            </div>
        </div>
    );
}

export default HomePage;