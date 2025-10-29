import React, { useState } from 'react';
import {
    Container,
    Card,
    Form,
    Row,
    Col,
    Button,
    Spinner
} from 'react-bootstrap';

const algoritmosConsenso = [
    "MULTIPLES_MENCIONES",
    "MAYORIA_SIMPLE",
    "ABSOLUTA",
    "NINGUNO"
];

const tiposDeFuente = ["ESTATICA", "DINAMICA", "PROXY"];

const criteriosDisponibles = [
    "Categoria",
    "Descripcion",
    "Fecha Desde",
    "Fecha Hasta",
    "Titulo",
];

const mockCategorias = [
    { id: 1, nombre: 'Incendio' },
    { id: 2, nombre: 'Accidente Vial' },
    { id: 3, nombre: 'Robo' },
    { id: 4, nombre: 'Corte de Luz' }
];

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
        await new Promise(resolve => setTimeout(resolve, 1500));
        setIsSubmitting(false);
        console.log("Colección creada");
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
                                                        onChange={() => handleCheckboxListChange('urls_fuente', fuente)}
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
                                                            {mockCategorias.map(cat => (
                                                                <option key={cat.id} value={cat.id}>{cat.nombre}</option>
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