import React, {useState, useEffect, useRef} from "react";
import {Button, Col, Container, Row, Spinner} from "react-bootstrap";
import {FaMapMarkedAlt, FaSearch} from "react-icons/fa";
import "./busqueda.css";
import Mapa from "../home-page/components/mapa.jsx";
import ApiAgregador from "../../api/api-agregador";
import HechoCard from "../viewHechos-page/components/hecho-card/HechoCard";

function Busqueda() {
    const [busqueda, setBusqueda] = useState("");
    const [hechos, setHechos] = useState([]);
    const [loading, setLoading] = useState(true);

    const mapaRef = useRef(null);

    const irAlMapa = () => {
        mapaRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const [hechosObtenido, setHechosObtenidos] = useState([]);

    const buscar = async () => {
        try {
            const resultados = await ApiAgregador.buscarPorTextoLibre(busqueda)
            setHechosObtenidos(resultados)
        } catch (error) {

        }
    }

    useEffect(() => {
        const fetchUbicaciones = async () => {
            try {
                const data = await ApiAgregador.obtenerUbicaciones();
                setHechos(data);
            } catch (error) {
                console.error("Error al obtener las ubicaciones:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchUbicaciones();
    }, []);

    return (
        <>
            <section className="fondo-busqueda">
                <Container className="text-center contenido-busqueda">
                    <FaMapMarkedAlt
                        className="icono-mapa-principal"
                        onClick={irAlMapa}
                        style={{ cursor: "pointer" }}
                    />
                    <h2 className="titulo-animado">
                        Encontrá los hechos que te interesen
                    </h2>

                    <div className="caja-busqueda animacion-busqueda">
                        <FaSearch className="icono-busqueda" />
                        <input
                            type="text"
                            className="input-busqueda"
                            placeholder="Buscar hechos por palabra clave, categoría o lugar..."
                            value={busqueda}
                            onChange={(e) => setBusqueda(e.target.value)}
                        />
                        <Button onClick={() => buscar()} >Buscar</Button>
                    </div>
                </Container>
            </section>

            <section className="fondo-mapa" ref={mapaRef}>
                <Container className="seccion-mapa">
                    {loading ? (
                        <div className="text-center mt-5">
                            <Spinner animation="border" role="status" />
                            <p className="mt-3">Cargando hechos...</p>
                        </div>
                    ) : (
                        <>
                            <Row className="justify-content-center mb-4">
                                <Col xs="auto">
                                    <h3 className="titulo-mapa">
                                        Resultados en el mapa
                                    </h3>
                                </Col>
                            </Row>
                            <Mapa hechosMapa={hechos} />
                        </>
                    )}
                </Container>
            </section>

            <section>
                <div style={{ maxWidth: 900, margin: '2rem auto 1.5rem auto', background: '#FFF9D6', borderRadius: 10, boxShadow: '0 2px 8px #99A88C', padding: '1.5rem 2rem' }}>
                    {hechosObtenido.map((aHecho) => (

                    <HechoCard key={aHecho.id} hecho={aHecho}/>))}
                </div>
            </section>

        </>
    );
}

export default Busqueda;