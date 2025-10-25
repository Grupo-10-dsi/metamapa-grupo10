import React, { useState } from "react";
import { Container } from "react-bootstrap";
import { FaSearch } from "react-icons/fa";
import "./busqueda.css";
import Mapa from "../home-page/components/mapa.jsx";

function Busqueda() {
    const [busqueda, setBusqueda] = useState("");
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
            <Container style={{ paddingTop: '50px', paddingBottom: '50px' }}>
                <Mapa />
            </Container>
        </section>

    </>
    );
}

export default Busqueda;