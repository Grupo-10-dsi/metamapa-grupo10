import React, { useState, useRef, useEffect } from "react";
import { Container, Card, Button, Form, ListGroup } from "react-bootstrap";
import "./estadisticas.css";
import { FaSearch } from "react-icons/fa";

function Estadisticas() {
    const [seleccionada, setSeleccionada] = useState(null);
    const [busqueda, setBusqueda] = useState("");
    const [sugerencias, setSugerencias] = useState([]);
    const [campoSeleccionado, setCampoSeleccionado] = useState("");

    // Esto lo uso para que cuando clickeas afuera del formulario, se cierre el desplegable de sugerencias
    const contenedorRef = useRef(null);

    // Mock solo para ver como se veria el autocompletado
    const colecciones = [
        "Hechos ambientales 2024",
        "Violencia de género",
        "Deforestación Chaco",
        "Contaminación del Riachuelo",
        "Emergencias urbanas"
    ];

    const categorias = [
        "Contaminación",
        "Violencia",
        "Transporte",
        "Seguridad",
        "Incendios"
    ];

    const estadisticas = [
        { id: 1, titulo: "Provincia con más hechos reportados", descripcion: "De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?" },
        { id: 2, titulo: "Categoría más reportada", descripcion: "¿Cuál es la categoría con mayor cantidad de hechos reportados?" },
        { id: 3, titulo: "Provincia con más hechos por categoría", descripcion: "¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?" },
        { id: 4, titulo: "Hora del día con más hechos", descripcion: "¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?" },
        { id: 5, titulo: "Solicitudes de eliminación spam", descripcion: "¿Cuántas solicitudes de eliminación son spam?" }
    ];

    const handleBusqueda = (valor, tipo) => {
        setBusqueda(valor);
        setCampoSeleccionado(tipo);

        let listaFiltrada = [];
        if (tipo === "coleccion") {
            listaFiltrada = colecciones.filter(c =>
                c.toLowerCase().includes(valor.toLowerCase())
            );
        } else if (tipo === "categoria") {
            listaFiltrada = categorias.filter(c =>
                c.toLowerCase().includes(valor.toLowerCase())
            );
        }
        setSugerencias(listaFiltrada);
    };

    const seleccionarSugerencia = (item) => {
        setBusqueda(item);
        setSugerencias([]);
    };

    useEffect(() => {
        const manejarClickFuera = (event) => {
            if (contenedorRef.current && !contenedorRef.current.contains(event.target)) {
                setSugerencias([]); // Cierra el desplegable
            }
        };
        document.addEventListener("mousedown", manejarClickFuera);
        return () => document.removeEventListener("mousedown", manejarClickFuera);
    }, []);

    const renderFormulario = (id) => {
        switch (id) {
            case 1:
                return (
                    <Form ref={contenedorRef}>
                        <Form.Group style={{ position: "relative", overflow: "visible" }}>
                            <Form.Label>Seleccioná la colección:</Form.Label>
                            <div className="input-con-icono">
                                <FaSearch className="icono-formulario" />
                                <Form.Control
                                    type="text"
                                    placeholder="Buscar o seleccionar una colección"
                                    value={campoSeleccionado === "coleccion" ? busqueda : ""}
                                    onChange={(e) => handleBusqueda(e.target.value, "coleccion")}
                                />
                            </div>
                            {sugerencias.length > 0 && (
                                <ListGroup className="sugerencias-lista">
                                    {sugerencias.map((sug, index) => (
                                        <ListGroup.Item
                                            key={index}
                                            action
                                            onClick={() => seleccionarSugerencia(sug)}
                                        >
                                            {sug}
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            )}
                        </Form.Group>
                        <Button variant="primary" className="mt-3">Consultar</Button>
                    </Form>
                );

            case 3:
            case 4:
                return (
                    <Form ref={contenedorRef}>
                        <Form.Group style={{ position: "relative", overflow: "visible" }}>
                            <Form.Label>Categoría:</Form.Label>
                            <div className="input-con-icono">
                                <FaSearch className="icono-formulario" />
                                <Form.Control
                                    type="text"
                                    placeholder="Buscar o seleccionar una categoría"
                                    value={campoSeleccionado === "categoria" ? busqueda : ""}
                                    onChange={(e) => handleBusqueda(e.target.value, "categoria")}
                                />
                            </div>
                            {sugerencias.length > 0 && (
                                <ListGroup className="sugerencias-lista">
                                    {sugerencias.map((sug, index) => (
                                        <ListGroup.Item
                                            key={index}
                                            action
                                            onClick={() => seleccionarSugerencia(sug)}
                                        >
                                            {sug}
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            )}
                        </Form.Group>
                        <Button variant="primary" className="mt-3">Consultar</Button>
                    </Form>
                );

            default:
                return <Button variant="primary" className="mt-2">Consultar</Button>;
        }
    };

    return (
        <div className="fondo-estadisticas">
            <Container className="estadisticas-container">
                <h2 className="titulo-estadisticas">Explorá algunas estadisticas de MetaMapa</h2>

                <div className="lista-estadisticas">
                    {estadisticas.map((item) => (
                        <Card key={item.id} className={`card-estadistica shadow-sm ${seleccionada === item.id ? 'activa' : ''}`}>
                            <Card.Body>
                                <Card.Title>{item.titulo}</Card.Title>
                                <Card.Text>{item.descripcion}</Card.Text>

                                <Button
                                    variant={seleccionada === item.id ? "secondary" : "outline-primary"}
                                    onClick={() =>
                                        setSeleccionada(seleccionada === item.id ? null : item.id)
                                    }
                                    className="me-2"
                                >
                                    {seleccionada === item.id ? "Cerrar" : "Ver más"}
                                </Button>

                                {seleccionada === item.id && (
                                    <>
                                        <div className="formulario-estadistica mt-3">
                                            {renderFormulario(item.id)}
                                        </div>

                                        <Button
                                            variant="outline-secondary"
                                            className="mt-2"
                                            onClick={() => console.log(`Generando CSV de la estadística ${item.id}`)}
                                        >
                                            Generar CSV
                                        </Button>
                                    </>
                                )}
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </Container>
        </div>
    );
}

export default Estadisticas;
