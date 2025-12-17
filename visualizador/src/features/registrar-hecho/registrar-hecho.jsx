import React, {useEffect, useState} from 'react';
import {
    Container,
    Card,
    Form,
    Row,
    Col,
    Button,
    Tabs,
    Tab,
    Spinner,
    Modal
} from 'react-bootstrap';
import MapaInteractivo from "../detail-page/components/mapa-interactivo/mapa-interactivo";
import {useKeycloak} from "@react-keycloak/web";
import { useNavigate } from 'react-router-dom';
import apiDinamica  from "../../api/api-dinamica";
import api from "../../api/api-agregador";

function RegistrarHecho() {
    const navigate = useNavigate();
    const {keycloak, initialized} = useKeycloak();
    const [anonimo, setAnonimo] = useState(false)

    const [showModal, setShowModal] = useState(false);
    const [nuevoIdHecho, setNuevoIdHecho] = useState(null);

    const estadoInicialForm = {
        titulo: '',
        descripcion: '',
        categoria: '',
        categoria_nueva: '',
        latitud: '',
        longitud: '',
        fechaAcontecimiento: '',
        etiquetas: '',
        cuerpo: '',
        contenidoMultimedia: []
    };

    const [formData, setFormData] = useState(estadoInicialForm);

    const [activeTab, setActiveTab] = useState('multimedia');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleVolverAHome = () => {
        setShowModal(false);
        if (nuevoIdHecho) {
            navigate(`/home`);
        }
    }

    const handleCrearOtro = () => {
        setShowModal(false);
        setFormData(estadoInicialForm);
        setActiveTab('multimedia');
    };

    const handleFileChange = (e) => {
        setFormData(prevData => ({
            ...prevData,
            contenidoMultimedia: e.target.files
        }));
    };

    const handleMapChange = (coordenadas) => {
        setFormData(prevData => ({
            ...prevData,
            latitud: coordenadas.latitud,
            longitud: coordenadas.longitud
        }));
    }

    // Busco las categorias:
    const [categorias, setCategorias] = useState([]);
    useEffect(() => {
        const fetchCategorias = async () => {
            try {
                const data = await api.obtenerCategorias();
                setCategorias(data);
            } catch (error) {
                console.error("Error al obtener las categorías:", error);
            }
        };
        fetchCategorias();
    }, []);

    // ...

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (isSubmitting || !initialized) {
            console.error("Keycloak no está listo.");

            return;
        }

        setIsSubmitting(true);
        let tipo = "multimedia";

        if (formData.cuerpo !== '') {
            tipo = "textual";
        }

        const selectedCategory = categorias.find(
            cat => cat.id.toString() === formData.categoria
        );

        const categoriaDetalle = formData.categoria === 'OTRA'
            ? formData.categoria_nueva
            : (selectedCategory ? selectedCategory.detalle : '');


        const hechoData = {
            tipo: tipo,
            titulo: formData.titulo,
            descripcion: formData.descripcion,
            categoria: {

                detalle: categoriaDetalle
            },
            ubicacion: {
                latitud: parseFloat(formData.latitud),
                longitud: parseFloat(formData.longitud)
            },
            fechaAcontecimiento: formData.fechaAcontecimiento,
            etiquetas: formData.etiquetas.split(',').map(tag => tag.trim()),
            cuerpo: formData.cuerpo,
            contenidoMultimedia: [],

            contribuyente: null
        };

        if (keycloak.authenticated && keycloak.tokenParsed) {
            if(!anonimo) {
                hechoData.contribuyente = {
                    sub: keycloak.tokenParsed.sub,
                    nombre: keycloak.tokenParsed.name,
                    email: keycloak.tokenParsed.email
                };
            }

        }

        console.log("Datos del hecho a enviar:", hechoData);

        try {
            const response = await apiDinamica.crearHecho(hechoData);

            console.log(response);
            const idHechoCreado  = response;
            setNuevoIdHecho(idHechoCreado);

            console.log("Hecho registrado con éxito. ID:", idHechoCreado);
            setShowModal(true);

            if (formData.contenidoMultimedia.length > 0) {
                console.log(`Paso 2: Subiendo ${formData.contenidoMultimedia.length} archivos...`);


                const promesasDeSubida = [];

                for (let i = 0; i < formData.contenidoMultimedia.length; i++) {
                    const file = formData.contenidoMultimedia[i];

                    // Añadimos la promesa de subida al array
                    promesasDeSubida.push(
                        apiDinamica.cargarImagen(idHechoCreado, file)
                    );
                }

                await Promise.all(promesasDeSubida);

                console.log("Todas las imágenes se subieron correctamente.");
            }




        } catch (error) {
            console.error("Error en el proceso de registro:", error);
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
                                    Registrar un Hecho
                                </h2>

                                <Form onSubmit={handleSubmit}>

                                    <h5 className="mb-3">Información Principal</h5>

                                    <Row>
                                        <Col md={6}>
                                            <Form.Group className="mb-3" controlId="formTitulo">
                                                <Form.Label>Título *</Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    name="titulo"
                                                    placeholder="Ej: Incendio en Palermo Hollywood"
                                                    value={formData.titulo}
                                                    onChange={handleChange}
                                                    required
                                                />
                                            </Form.Group>
                                        </Col>
                                    </Row>

                                    {activeTab === 'cuerpo' && (
                                        <Form.Group className="mb-3" controlId="formDescripcionBreve">
                                            <Form.Label>Descripción Breve *</Form.Label>
                                            <Form.Control
                                                as="textarea"
                                                rows={2}
                                                name="descripcion"
                                                placeholder="Describa brevemente lo que sucedió..."
                                                value={formData.descripcion}
                                                onChange={handleChange}
                                                required={activeTab === 'cuerpo'}
                                            />
                                        </Form.Group>
                                    )}

                                    <Form.Group className="mb-3" controlId="formCategoria">
                                        <Form.Label>Categoría *</Form.Label>
                                        <Form.Select
                                            name="categoria"
                                            value={formData.categoria}
                                            onChange={handleChange}
                                            required
                                        >
                                            <option value="">Seleccione una categoría...</option>
                                            {categorias.map(cat => (
                                                <option key={cat.id} value={cat.id}>{cat.detalle}</option>
                                            ))}
                                            <option value="OTRA">-- Agregar nueva categoría --</option>
                                        </Form.Select>
                                    </Form.Group>

                                    {formData.categoria === 'OTRA' && (
                                        <Form.Group className="mb-3" controlId="formCategoriaNueva">
                                            <Form.Label>Nombre de la nueva categoría *</Form.Label>
                                            <Form.Control
                                                type="text"
                                                name="categoria_nueva"
                                                value={formData.categoria_nueva}
                                                onChange={handleChange}
                                                placeholder="Ej: Vandalismo"
                                                required
                                            />
                                        </Form.Group>
                                    )}

                                    <hr className="my-4" />

                                    <h5 className="mb-3">¿Cuándo y Dónde?</h5>
                                    <Form.Group className="mb-3" controlId="formFecha">
                                        <Form.Label>Fecha y Hora del Acontecimiento *</Form.Label>
                                        <Form.Control
                                            type="datetime-local"
                                            name="fechaAcontecimiento"
                                            value={formData.fechaAcontecimiento}
                                            onChange={handleChange}
                                            required
                                        />
                                    </Form.Group>
                                    <Tabs
                                        defaultActiveKey="manual"
                                        id="location-tabs"
                                        className="mb-3"
                                    >
                                        <Tab eventKey="manual" title="Ingreso Manual">
                                            <Row>
                                                <Col md={6}>
                                                    <Form.Group className="mb-3" controlId="formLatitud">
                                                        <Form.Label>Latitud *</Form.Label>
                                                        <Form.Control
                                                            type="number"
                                                            name="latitud"
                                                            placeholder="-34.5811"
                                                            value={formData.latitud}
                                                            onChange={handleChange}
                                                            required
                                                        />
                                                    </Form.Group>
                                                </Col>
                                                <Col md={6}>
                                                    <Form.Group className="mb-3" controlId="formLongitud">
                                                        <Form.Label>Longitud *</Form.Label>
                                                        <Form.Control
                                                            type="number"
                                                            name="longitud"
                                                            placeholder="-58.4377"
                                                            value={formData.longitud}
                                                            onChange={handleChange}
                                                            required
                                                        />
                                                    </Form.Group>
                                                </Col>
                                            </Row>
                                        </Tab>

                                        <Tab eventKey="mapa" title="Seleccionar en Mapa" mountOnEnter>
                                            <MapaInteractivo
                                                value={{ latitud: formData.latitud, longitud: formData.longitud }}
                                                onChange={handleMapChange}
                                            />
                                            <Form.Text className="text-muted">
                                                Haz clic en el mapa para establecer la latitud y longitud.
                                            </Form.Text>
                                        </Tab>
                                    </Tabs>

                                    <hr className="my-4" />

                                    <h5 className="mb-3">Contenido del Reporte</h5>

                                    <Tabs
                                        activeKey={activeTab}
                                        onSelect={(k) => setActiveTab(k)}
                                        id="reporte-tabs"
                                        className="mb-3"
                                    >
                                        <Tab eventKey="multimedia" title="Reporte con Multimedia">
                                            <Form.Group className="mb-3" controlId="formDescripcion">
                                                <Form.Label>Descripción *</Form.Label>
                                                <Form.Control
                                                    as="textarea"
                                                    rows={3}
                                                    name="descripcion"
                                                    placeholder="Describa brevemente lo que sucedió..."
                                                    value={formData.descripcion}
                                                    onChange={handleChange}
                                                    required={activeTab === 'multimedia'}
                                                />
                                            </Form.Group>
                                            <Form.Group controlId="formFile" className="mb-3">
                                                <Form.Label>Imágenes o Videos</Form.Label>
                                                <Form.Control
                                                    type="file"
                                                    name="contenidoMultimedia"
                                                    onChange={handleFileChange}
                                                    multiple
                                                />
                                            </Form.Group>
                                        </Tab>

                                        <Tab eventKey="cuerpo" title="Reporte de solo Texto (Cuerpo)">
                                            <Form.Group className="mb-3" controlId="formCuerpo">
                                                <Form.Label>Cuerpo del Reporte *</Form.Label>
                                                <Form.Control
                                                    as="textarea"
                                                    rows={8}
                                                    name="cuerpo"
                                                    placeholder="Escriba aquí el reporte detallado..."
                                                    value={formData.cuerpo}
                                                    onChange={handleChange}
                                                    required={activeTab === 'cuerpo'}
                                                />
                                            </Form.Group>
                                        </Tab>

                                    </Tabs>

                                    <hr className="my-4" />

                                    <h5 className="mb-3">Detalles Adicionales</h5>
                                    <Form.Group className="mb-3" controlId="formEtiquetas">
                                        <Form.Label>Etiquetas</Form.Label>
                                        <Form.Control
                                            type="text"
                                            name="etiquetas"
                                            placeholder="incendio, bomberos, palermo"
                                            value={formData.etiquetas}
                                            onChange={handleChange}
                                        />
                                        <Form.Text className="text-muted">
                                            Separar etiquetas con comas.
                                        </Form.Text>
                                    </Form.Group>

                                    {keycloak.authenticated && (
                                        <Form.Group className="mb-3" controlId="formAnonimo">
                                            <Form.Check
                                                type="checkbox"
                                                label="Registrar como anónimo"
                                                checked={anonimo}
                                                onChange={(e) => setAnonimo(e.target.checked)}
                                            />
                                            <Form.Text className="text-muted">
                                                Si marcas esto, tu nombre de usuario no quedará asociado a este reporte.
                                            </Form.Text>
                                        </Form.Group>
                                    )}

                                    <div className="d-grid mt-5">
                                        <Button
                                            variant="warning"
                                            type="submit"
                                            size="lg"
                                            disabled={isSubmitting}
                                        >
                                            {isSubmitting ? (
                                                <>
                                                    <Spinner
                                                        as="span"
                                                        animation="border"
                                                        size="sm"
                                                        role="status"
                                                        aria-hidden="true"
                                                    />
                                                    <span className="ms-2">Registrando...</span>
                                                </>
                                            ) : (
                                                "Registrar Hecho"
                                            )}
                                        </Button>
                                    </div>

                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>Hecho registrado con éxito</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    El hecho se registró correctamente.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleCrearOtro}>
                        Crear otro hecho
                    </Button>
                    <Button variant="primary" onClick={handleVolverAHome}>
                        Volver a Home
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default RegistrarHecho;
