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
    Spinner
} from 'react-bootstrap';
import MapaInteractivo from "../detail-page/components/mapa-interactivo/mapa-interactivo";
import {useKeycloak} from "@react-keycloak/web";
import apiDinamica  from "../../api/api-dinamica";
import api from "../../api/api-agregador";

function RegistrarHecho() {

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

    // ... (handleChange, handleFileChange, handleSubmit, mockCategorias sin cambios) ...
    // ...
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
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

        // 1. Solo esperar a que Keycloak esté inicializado
        if (isSubmitting || !initialized) {
            console.error("Keycloak no está listo.");
            // Opcional: mostrar un error al usuario
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

        // 2. Determinar el 'detalle' (string) de la categoría
        const categoriaDetalle = formData.categoria === 'OTRA'
            ? formData.categoria_nueva
            : (selectedCategory ? selectedCategory.detalle : ''); // <-- Usamos el 'detalle' encontrado

        // 2. Construir el objeto base del hecho
        const hechoData = {
            tipo: tipo,
            titulo: formData.titulo,
            descripcion: formData.descripcion,
            categoria: {
                // Asumiendo que 'categoria_nueva' es 'detalle'
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

            // 3. Asignar 'contribuyente' por defecto como null (anónimo)
            contribuyente: null
        };

        // 4. Si el usuario SÍ está autenticado, poblar los datos
        //    (Tu Axios debe estar configurado para enviar el token también)
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
            // --- PASO 1: Enviar el hecho ---
            // Asumimos que 'crearHecho' devuelve la respuesta de axios
            // y que la respuesta contiene el hecho creado con su ID
            const response = await apiDinamica.crearHecho(hechoData);

            const nuevoIdHecho = response; // <-- AJUSTA ESTO según la respuesta de tu API

            console.log("Hecho registrado con éxito. ID:", nuevoIdHecho);

            // --- PASO 2: Subir las imágenes (si existen) ---
            if (formData.contenidoMultimedia.length > 0) {
                console.log(`Paso 2: Subiendo ${formData.contenidoMultimedia.length} archivos...`);

                // Creamos un array de promesas para todas las subidas
                const promesasDeSubida = [];

                for (let i = 0; i < formData.contenidoMultimedia.length; i++) {
                    const file = formData.contenidoMultimedia[i];

                    // Añadimos la promesa de subida al array
                    promesasDeSubida.push(
                        apiDinamica.cargarImagen(nuevoIdHecho, file)
                    );
                }

                // Esperamos a que TODAS las imágenes se suban en paralelo
                await Promise.all(promesasDeSubida);

                console.log("Todas las imágenes se subieron correctamente.");
            }

            // Aquí podrías redirigir al usuario o limpiar el formulario
            // Renderizar una Toast que diga hecho creado con exito



        } catch (error) {
            console.error("Error en el proceso de registro:", error);
            // ... (tu manejo de errores de Axios) ...
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

                                    {/* --- 1. Información Principal --- */}
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

                                    {/* Campo condicional de Descripción */}
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

                                    {/* --- 2. ¿Cuándo y Dónde? --- */}
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
                                                            value={formData.latitud} // Controlado por el estado
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
                                                            value={formData.longitud} // Controlado por el estado
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

                                    {/* --- 3. Contenido del Reporte (Tabs) --- */}
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

                                    {/* --- 4. Detalles Adicionales --- */}
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

                                    {/* --- 5. Botón de Envío --- */}
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
        </div>
    );
}

export default RegistrarHecho;