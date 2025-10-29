import React, { useState, useEffect} from "react";
import { Container } from "react-bootstrap";
import { FaSearch } from "react-icons/fa";
import "./busqueda.css";
import Mapa from "../home-page/components/mapa.jsx";
import ApiAgregador from "../../api/api-agregador";

function Busqueda() {
    const [busqueda, setBusqueda] = useState("");
    const [hechos, setHechos] = useState([]);
    const [loading, setLoading] = useState(true);

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
                    <h2>Encontrá los hechos que te interesen</h2>

                    <div className="caja-busqueda">
                        <FaSearch className="icono-busqueda" />
                        <input
                            type="text"
                            className="input-busqueda"
                            placeholder="Buscar hechos..."
                            value={busqueda}
                            onChange={(e) => setBusqueda(e.target.value)}
                        />
                    </div>
                </Container>
            </section>

            <section className="fondo-mapa">
                <Container style={{ paddingTop: "50px", paddingBottom: "50px" }}>
                    {loading ? (
                        <p className="text-center">Cargando hechos...</p>
                    ) : (
                        <Mapa hechosMapa={hechos} />
                    )}
                </Container>
            </section>
        </>
    );
}

export default Busqueda;