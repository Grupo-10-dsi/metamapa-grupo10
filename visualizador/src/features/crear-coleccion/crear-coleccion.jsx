import React, {useEffect, useState} from 'react';
import {
    Container,
    Card,
    Form,
    Row,
    Col,
    Button,
    Spinner
} from 'react-bootstrap';
import ApiAgregador from "../../api/api-agregador";

const algoritmosConsenso = [
    "MULTIPLES_MENCIONES",
    "MAYORIA_SIMPLE",
    "ABSOLUTA",
    "NINGUNO"
];

const tiposDeFuente = ["ESTATICA", "DINAMICA", "PROXY"];

const urlsFuentes = {
    "ESTATICA": "http://localhost:8081/api/estatica/hechos",
    "DINAMICA": "http://localhost:8082/api/dinamica/hechos",
    "PROXY": "http://localhost:8083/api/proxy/hechos"
}

const criteriosDisponibles = [
    "Categoria",
    "Descripcion",
    "Fecha Desde",
    "Fecha Hasta",
    "Titulo",
];

//const categorias = ApiAgregador.obtenerCategorias();

function CrearColeccion() {
    const [formData, setFormData] = useState({
        titulo: '',
        descripcion: '',
        algoritmo_consenso: 'NINGUNO',
        urls_fuente: [],
        criterios: [],
        criterio_categoria: '',
        criterio_fecha_desde: '',
        criterio_fecha_hasta: '',
        criterio_titulo: '',
        criterio_descripcion: '',
        criterio_categoria_nueva: ''
    });

    const [isSubmitting, setIsSubmitting] = useState(false);


    const [categorias, setCategorias] = useState([]);

    useEffect(() => {
        const cargarCategorias = async () => {
            try {

                const data = await ApiAgregador.obtenerCategorias();
                setCategorias(data);
            } catch (error) {
                console.error("Error al cargar categorías:", error);
            }
        };
        cargarCategorias();
    }, []);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleCheckboxListChange = (listName, value) => {
        setFormData(prevData => {
            const currentList = prevData[listName];
            if (currentList.includes(value)) {
                return {
                    ...prevData,
                    [listName]: currentList.filter(item => item !== value)
                };
            } else {
                return {
                    ...prevData,
                    [listName]: [...currentList, value]
                };
            }
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        console.log("Enviando Colección:", formData);

        const criterioMap = {
            "Categoria":   { tipo: "categoria",   valorKey: "criterio_categoria" },
            "Descripcion": { tipo: "descripcion", valorKey: "criterio_descripcion" },
            "Fecha Desde": { tipo: "fecha_desde", valorKey: "criterio_fecha_desde" },
            "Fecha Hasta": { tipo: "fecha_hasta", valorKey: "criterio_fecha_hasta" },
            "Titulo":      { tipo: "titulo",      valorKey: "criterio_titulo" }
        };

        // 2. Construye el array de criterios en el formato {tipo, valor}
        //    Itera sobre los criterios que el usuario SELECCIONÓ (ej: ["Categoria", "Titulo"])
        const criteriosParaRequest = formData.criterios
            .map(criterioNombre => {
                // Busca la configuración para ese criterio (ej: {tipo: "titulo", valorKey: "criterio_titulo"})
                const config = criterioMap[criterioNombre];
                if (!config) return null;

                // Obtiene el valor correspondiente del estado (ej: formData["criterio_titulo"])
                const valor = formData[config.valorKey];

                // Si el valor no está vacío, crea el objeto
                if (valor) {
                    return {
                        tipo: config.tipo,
                        valor: valor
                    };
                }
                return null;
            })
            .filter(Boolean); // Limpia cualquier 'null' (criterios seleccionados pero vacíos)


        // 3. Crea el 'payload' final para la API
        //    Esto evita enviar los campos 'criterio_...' sueltos
        const payload = {
            titulo: formData.titulo,
            descripcion: formData.descripcion,
            algoritmo_consenso: formData.algoritmo_consenso,
            urls_fuente: formData.urls_fuente,
            criterios: criteriosParaRequest // Aquí va tu lista formateada
        };

        try {


            const response = await ApiAgregador.crearColeccion(payload)

            console.log("Colección creada con éxito:", response);


        } catch (error) {
            console.error("Error al crear la colección:", error.message);

        } finally {
            setIsSubmitting(false);
        }

    };

    return (
        <div style={{ backgroundColor: '#f8f9fa', padding: '3rem 0' }}>
            <Container>
                <Row className="justify-content-center">
                    <Col md={10} lg={8}>
                        <Card className="shadow-sm border-0" style={{ borderRadius: '1rem' }}>
                            <Card.Body className="p-4 p-md-5">
                                <h2 className="text-center fw-bold mb-4">
                                    Crear Nueva Colección
                                </h2>

                                <Form onSubmit={handleSubmit}>

                                    <h5 className="mb-3">Información Principal</h5>
                                    <Form.Group className="mb-3" controlId="formTitulo">
                                        <Form.Label>Título *</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="titulo"
                                            placeholder="Ej: Colección de Incendios en Palermo"
                                            value={formData.titulo}
                                            onChange={handleChange}
                                            required
                                        />
                                    </Form.Group>
                                    <Form.Group className="mb-3" controlId="formDescripcion">
                                        <Form.Label>Descripción</Form.Label>
                                        <Form.Control
                                            as="textarea"
                                            rows={3}
                                            name="descripcion"
                                            placeholder="Una breve descripción de qué agrupa esta colección."
                                            value={formData.descripcion}
                                            onChange={handleChange}
                                        />
                                    </Form.Group>

                                    <hr className="my-4" />

                                    <h5 className="mb-3">Configuración</h5>
                                    <Row>
                                        <Col md={6}>
                                            <Form.Group className="mb-3" controlId="formAlgoritmo">
                                                <Form.Label>Algoritmo de Consenso *</Form.Label>
                                                <Form.Select
                                                    name="algoritmo_consenso"
                                                    value={formData.algoritmo_consenso}
                                                    onChange={handleChange}
                                                >
                                                    {algoritmosConsenso.map(alg => (
                                                        <option key={alg} value={alg}>{alg}</option>
                                                    ))}
                                                </Form.Select>
                                            </Form.Group>
                                        </Col>
                                        <Col md={6}>
                                            <Form.Group className="mb-3" controlId="formFuentes">
                                                <Form.Label>Tipos de Fuente *</Form.Label>
                                                {tiposDeFuente.map(fuente => (
                                                    <Form.Check
                                                        key={fuente}
                                                        type="checkbox"
                                                        id={`check-fuente-${fuente}`}
                                                        label={fuente}
                                                        onChange={() => handleCheckboxListChange('urls_fuente', urlsFuentes[fuente])}
                                                    />
                                                ))}
                                            </Form.Group>
                                        </Col>
                                    </Row>

                                    <hr className="my-4" />

                                    <h5 className="mb-3">Criterios de Agrupación *</h5>
                                    <Form.Text className="text-muted d-block mb-3">
                                        Seleccione los criterios que se usarán para agrupar los hechos.
                                    </Form.Text>

                                    {criteriosDisponibles.map(criterio => (
                                        <div key={criterio} className="mb-3 p-3 border rounded" style={{backgroundColor: '#f8f9fa'}}>
                                            <Form.Check
                                                type="checkbox"
                                                id={`check-criterio-${criterio}`}
                                                label={<strong>{criterio}</strong>}
                                                onChange={() => handleCheckboxListChange('criterios', criterio)}
                                                checked={formData.criterios.includes(criterio)}
                                            />

                                            {criterio === 'Categoria' && formData.criterios.includes('Categoria') && (
                                                <>
                                                    <Form.Group className="mt-2 ms-4" controlId="formCriterioCategoria">
                                                        <Form.Label className="small">Seleccionar Categoría</Form.Label>
                                                        <Form.Select
                                                            name="criterio_categoria"
                                                            value={formData.criterio_categoria}
                                                            onChange={handleChange}
                                                        >
                                                            {categorias.map(cat => (
                                                                <option key={cat.id} value={cat.id}>{cat.detalle}</option>
                                                            ))}
                                                        </Form.Select>
                                                    </Form.Group>

                                                </>
                                            )}

                                            {criterio === 'Descripcion' && formData.criterios.includes('Descripcion') && (
                                                <Form.Group className="mt-2 ms-4" controlId="formCriterioDescripcion">
                                                    <Form.Label className="small">Texto a buscar en Descripción</Form.Label>
                                                    <Form.Control
                                                        type="text"
                                                        name="criterio_descripcion"
                                                        value={formData.criterio_descripcion}
                                                        onChange={handleChange}
                                                        placeholder="Ej: 'bomberos', 'policía'"
                                                    />
                                                </Form.Group>
                                            )}

                                            {criterio === 'Fecha Desde' && formData.criterios.includes('Fecha Desde') && (
                                                <Form.Group className="mt-2 ms-4" controlId="formCriterioFechaDesde">
                                                    <Form.Label className="small">Fecha Desde</Form.Label>
                                                    <Form.Control
                                                        type="date"
                                                        name="criterio_fecha_desde"
                                                        value={formData.criterio_fecha_desde}
                                                        onChange={handleChange}
                                                    />
                                                </Form.Group>
                                            )}

                                            {criterio === 'Fecha Hasta' && formData.criterios.includes('Fecha Hasta') && (
                                                <Form.Group className="mt-2 ms-4" controlId="formCriterioFechaHasta">
                                                    <Form.Label className="small">Fecha Hasta</Form.Label>
                                                    <Form.Control
                                                        type="date"
                                                        name="criterio_fecha_hasta"
                                                        value={formData.criterio_fecha_hasta}
                                                        onChange={handleChange}
                                                    />
                                                </Form.Group>
                                            )}

                                            {criterio === 'Titulo' && formData.criterios.includes('Titulo') && (
                                                <Form.Group className="mt-2 ms-4" controlId="formCriterioTitulo">
                                                    <Form.Label className="small">Texto a buscar en Título</Form.Label>
                                                    <Form.Control
                                                        type="text"
                                                        name="criterio_titulo"
                                                        value={formData.criterio_titulo}
                                                        onChange={handleChange}
                                                        placeholder="Ej: 'incendio', 'corte'"
                                                    />
                                                </Form.Group>
                                            )}
                                        </div>
                                    ))}

                                    <div className="d-grid mt-5">
                                        <Button
                                            variant="warning"
                                            type="submit"
                                            size="lg"
                                            disabled={isSubmitting}
                                        >
                                            {isSubmitting ? (
                                                <>
                                                    <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true"/>
                                                    <span className="ms-2">Creando...</span>
                                                </>
                                            ) : (
                                                "Crear Colección"
                                            )}
                                        </Button>
                                    </div>

                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default CrearColeccion;