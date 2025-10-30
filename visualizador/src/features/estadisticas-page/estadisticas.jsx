import React, { useState, useRef, useEffect } from "react";
import { Container, Card, Button, Form, ListGroup } from "react-bootstrap";
import { FaSearch } from "react-icons/fa";
import ContadorElementos from "./contadorElementos.jsx";
import "./estadisticas.css";
import ApiEstadistica from "../../api/api-estadistica.jsx";
import ApiAgregador from "../../api/api-agregador";
import { useKeycloak } from '@react-keycloak/web';

function Estadisticas() {
    const [seleccionada, setSeleccionada] = useState(null);
    const [busqueda, setBusqueda] = useState("");
    const [sugerencias, setSugerencias] = useState([]);
    const [campoSeleccionado, setCampoSeleccionado] = useState("");
    const contenedorRef = useRef(null);
    const [cantidades, setCantidades] = useState({});
    const [resultados, setResultados] = useState([]);
    const [categorias, setCategorias] = useState([])
    const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null)
    const { keycloak, initialized } = useKeycloak();
    const idColeccion = 1;
    const idCategoria = 4;
    const [colecciones, setColecciones] = useState([])
    const [coleccionSeleccionada, setColeccionSeleccionada] = useState(null);


    const estadisticas = [
        { id: 1, titulo: "Provincia con más hechos reportados", descripcion: "De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?" },
        { id: 2, titulo: "Categoría más reportada", descripcion: "¿Cuál es la categoría con mayor cantidad de hechos reportados?" },
        { id: 3, titulo: "Provincia con más hechos por categoría", descripcion: "¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?" },
        { id: 4, titulo: "Hora del día con más hechos", descripcion: "¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?" },
        { id: 5, titulo: "Solicitudes de eliminación spam", descripcion: "¿Cuántas solicitudes de eliminación son spam?" }
    ];

    useEffect(() => {
        const cargarCategorias = async () => {
            try {
                const data = await ApiAgregador.obtenerCategorias();
                setCategorias(data);
            } catch (error) {
                console.error("Error al cargar categorías:", error);
            }
        }
        cargarCategorias()
    }, [])

    useEffect(() => {
        const cargarColecciones = async () => {
            try {
                const data = await ApiAgregador.obtenerColecciones();
                setColecciones(data);
            } catch (error) {
                console.error("Error al cargar colecciones:", error);
            }
        }
        cargarColecciones()
    }, [])

    const handleBusqueda = (valor, tipo) => {
        setBusqueda(valor);
        setCampoSeleccionado(tipo);

        let listaFiltrada = [];
        if (tipo === "coleccion") {
            listaFiltrada = colecciones.filter(c =>
                c.titulo.toLowerCase().includes(valor.toLowerCase())
            );
        } else if (tipo === "categoria") {
            listaFiltrada = categorias.filter(cat =>
                cat.detalle.toLowerCase().includes(valor.toLowerCase())
            );
        }

        setSugerencias(listaFiltrada);
    };

    const seleccionarSugerencia = (item) => {
        if (typeof item === "object" && item.id) {
            if (campoSeleccionado === "categoria") {
                setCategoriaSeleccionada(item);
                setBusqueda(item.detalle || "");
            } else if (campoSeleccionado === "coleccion") {
                setColeccionSeleccionada(item);
                setBusqueda(item.titulo || "");
            }
        } else {
            setBusqueda(item);
        }
        setSugerencias([]);
    };

    useEffect(() => {
        const manejarClickFuera = (event) => {
            if (contenedorRef.current && !contenedorRef.current.contains(event.target)) {
                setSugerencias([]);
            }
        };
        document.addEventListener("mousedown", manejarClickFuera);
        return () => document.removeEventListener("mousedown", manejarClickFuera);
    }, []);

    const handleCantidadChange = (id, nuevaCantidad) => {
        setCantidades(prev => ({ ...prev, [id]: nuevaCantidad }));
    };

    useEffect(() => {
        if (initialized && keycloak.token) {
            ApiEstadistica.setToken(keycloak.token);
        }
    }, [initialized, keycloak.token]);

    const handleConsultar = async (id) => {
        const cantidad = cantidades[id] || 5;
        let datos = [];
        try {
            switch (id) {
                case 1:
                    datos = await ApiEstadistica.obtenerProvinciaPorColeccion({ id: coleccionSeleccionada.id, formato: null, cantidadProvincias: cantidad });
                    break;
                case 2:
                    datos = await ApiEstadistica.obtenerCategoriaMaxHechos({formato: null, cantidadCategorias: cantidad});
                    break;
                case 3:
                    datos = await ApiEstadistica.obtenerProvinciaPorCategoria({ id: categoriaSeleccionada.id, formato: null, cantidadProvincias: cantidad });
                    break;
                case 4:
                    datos = await ApiEstadistica.obtenerHoraMaxHechos({ id: categoriaSeleccionada.id, cantidadHoras: cantidad });
                    break;
                case 5:
                    datos = await ApiEstadistica.obtenerSolicitudesSpam({ mostrar:null, formato: null });
                    break;
                default:
                    break;
            }
            setResultados(datos);
            console.log("Resultados:", datos);
        } catch (error) {
            console.error("Error al consultar la estadística:", error);
        }
    };



    const renderFormulario = (id) => {
        return (
            <Form ref={contenedorRef}>
                {(id === 1 || id === 3 || id === 4) && (
                    <Form.Group style={{ position: "relative", overflow: "visible" }}>
                        <Form.Label>{id === 1 ? "Seleccioná la colección:" : "Categoría:"}</Form.Label>
                        <div className="input-con-icono">
                            <FaSearch className="icono-formulario" />
                            <Form.Control
                                type="text"
                                placeholder={id === 1 ? "Buscar o seleccionar una colección" : "Buscar o seleccionar una categoría"}
                                value={campoSeleccionado === (id === 1 ? "coleccion" : "categoria") ? busqueda : ""}
                                onChange={(e) => handleBusqueda(e.target.value, id === 1 ? "coleccion" : "categoria")}
                            />
                        </div>
                        {sugerencias.length > 0 && (
                            <ListGroup className="sugerencias-lista">
                                {sugerencias.map((item) => (
                                    <ListGroup.Item
                                        key={item.id}
                                        action
                                        onClick={() => seleccionarSugerencia(item)}
                                    >
                                        {campoSeleccionado === "coleccion"
                                            ? item.titulo
                                            : item.detalle}
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                        )}
                    </Form.Group>
                )}

                {(id !== 5) && (
                    <ContadorElementos
                        cantidadInicial={cantidades[id] || 5}
                        onChange={(nuevaCantidad) => handleCantidadChange(id, nuevaCantidad)}
                    />
                )}

                <div className={id === 5 ? "text-center mt-3" : "mt-3"}>
                    <Button variant="primary" onClick={() => handleConsultar(id)}>
                        Consultar
                    </Button>
                </div>
            </Form>
        );
    };

    return (
        <div className="fondo-estadisticas">
            <Container className="estadisticas-container">
                <h2 className="titulo-estadisticas">Explorá algunas estadísticas de MetaMapa</h2>

                <div className="lista-estadisticas">
                    {estadisticas.map((item) => (
                        <Card key={item.id} className={`card-estadistica shadow-sm ${seleccionada === item.id ? 'activa' : ''}`}
                              style={item.id === 5 ? { marginBottom: "3rem" } : {}}>
                            <Card.Body>
                                <Card.Title>{item.titulo}</Card.Title>
                                <Card.Text>{item.descripcion}</Card.Text>

                                <Button
                                    variant={seleccionada === item.id ? "secondary" : "outline-primary"}
                                    onClick={() => {
                                        if (seleccionada === item.id) {
                                            setSeleccionada(null);
                                            setResultados([]);
                                        } else {
                                            setSeleccionada(item.id);
                                            setResultados([]);
                                            setCantidades(prev => ({ ...prev, [item.id]: prev[item.id] || 5 }));
                                        }
                                    }}
                                    className="me-2"
                                >
                                    {seleccionada === item.id ? "Cerrar" : "Ver más"}
                                </Button>

                                {seleccionada === item.id && (
                                    <>
                                        <div className="formulario-estadistica mt-3">
                                            {renderFormulario(item.id)}
                                        </div>

                                        {resultados.length > 0 && (
                                            <Card className="mt-3 shadow-sm border-0">
                                                <Card.Header className="bg-light fw-semibold">
                                                    Resultados obtenidos
                                                </Card.Header>
                                                <Card.Body>
                                                    <ListGroup variant="flush">
                                                        {resultados.map((resultado, index) => (
                                                            <ListGroup.Item key={index} className="px-3 py-2">
                                                                {seleccionada === 2 ? (
                                                                    <>
                                                                        {index + 1}. <strong>Categoría:</strong> {resultado.detalle}
                                                                    </>
                                                                ) : seleccionada === 1 || seleccionada === 3 ? (
                                                                    // Para estadística 1 y 3 (provincias)
                                                                    <>
                                                                        {index + 1}. <strong>Provincia:</strong> {resultado.provincia || resultado}
                                                                    </>
                                                                ) : seleccionada === 4 ? (
                                                                    <>
                                                                        {index + 1}. <strong>Hora: </strong>{resultado.split(":")[0]}:00
                                                                    </>
                                                                ) :
                                                                    seleccionada === 5 ? (
                                                                        <>
                                                                            {index + 1}. <strong>Cantidad de solicitudes spam: </strong>{resultado.split(":")[0]}:00
                                                                        </>
                                                                    ) : (
                                                                    typeof resultado === "object" ? (
                                                                        <div>
                                                                            {index + 1}.{" "}
                                                                            {Object.entries(resultado).map(([k, v]) => (
                                                                                <div key={k}>
                                                                                    <strong>{k}: </strong>{v}
                                                                                </div>
                                                                            ))}
                                                                        </div>
                                                                    ) : (
                                                                        `${index + 1}. ${resultado}`
                                                                    )
                                                                )}
                                                            </ListGroup.Item>
                                                        ))}
                                                    </ListGroup>

                                                    <Button
                                                        variant="outline-secondary"
                                                        className="mt-3"
                                                        onClick={() => console.log(`Generando CSV de la estadística ${item.id}`)}
                                                    >
                                                        Generar CSV
                                                    </Button>
                                                </Card.Body>
                                            </Card>
                                        )}
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