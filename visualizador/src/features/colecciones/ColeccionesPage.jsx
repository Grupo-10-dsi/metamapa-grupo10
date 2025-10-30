import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
// Importamos un ícono para "colección" y el hook de navegación
import { FaLayerGroup } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

// 1. Datos de ejemplo. Eventualmente vendrán de tu API.
const mockColecciones = [
    {
        id: 1,
        titulo: "Incendios Forestales 2024",
        descripcion: "Seguimiento de todos los incendios reportados en la región patagónica durante el verano de 2024."
    },
    {
        id: 2,
        titulo: "Reportes de Vandalismo CABA",
        descripcion: "Colección de reportes de vandalismo y daños a la propiedad pública en la Ciudad de Buenos Aires."
    },
    {
        id: 3,
        titulo: "Problemas de Infraestructura",
        descripcion: "Seguimiento de cortes de luz, agua y problemas de infraestructura vial reportados por la comunidad."
    },
    {
        id: 4,
        titulo: "Eventos Comunitarios",
        descripcion: "Todos los hechos relacionados con festivales, ferias y actividades comunitarias positivas."
    },
    {
        id: 1,
        titulo: "Incendios Forestales 2024",
        descripcion: "Seguimiento de todos los incendios reportados en la región patagónica durante el verano de 2024."
    },
    {
        id: 2,
        titulo: "Reportes de Vandalismo CABA",
        descripcion: "Colección de reportes de vandalismo y daños a la propiedad pública en la Ciudad de Buenos Aires."
    },
    {
        id: 3,
        titulo: "Problemas de Infraestructura",
        descripcion: "Seguimiento de cortes de luz, agua y problemas de infraestructura vial reportados por la comunidad."
    },
    {
        id: 4,
        titulo: "Eventos Comunitarios",
        descripcion: "Todos los hechos relacionados con festivales, ferias y actividades comunitarias positivas."
    },
    {
        id: 1,
        titulo: "Incendios Forestales 2024",
        descripcion: "Seguimiento de todos los incendios reportados en la región patagónica durante el verano de 2024."
    },
    {
        id: 2,
        titulo: "Reportes de Vandalismo CABA",
        descripcion: "Colección de reportes de vandalismo y daños a la propiedad pública en la Ciudad de Buenos Aires."
    },
    {
        id: 3,
        titulo: "Problemas de Infraestructura",
        descripcion: "Seguimiento de cortes de luz, agua y problemas de infraestructura vial reportados por la comunidad."
    },
    {
        id: 4,
        titulo: "Eventos Comunitarios",
        descripcion: "Todos los hechos relacionados con festivales, ferias y actividades comunitarias positivas."
    }
];

// Estilo para el ícono (reemplaza tu "Image cap")
const cardIconStyle = {
    textAlign: 'center', // Centra el ícono
    paddingTop: '1.5rem', // Espacio superior
    paddingBottom: '1rem', // Espacio inferior
    backgroundColor: '#f8f9fa', // Un fondo gris claro
    color: '#6e2a34', // Color del ícono (tu marrón/rojo)
    fontSize: '3rem' // Tamaño del ícono
};

function ColeccionesPage() {
    // 2. Hook para manejar la navegación
    const navigate = useNavigate();

    // 3. Función que se llama al hacer clic en el botón
    const handleVerHechos = (coleccionId) => {
        // Navega a la ruta de los hechos de esa colección
        // (Asegúrate de tener esta ruta definida en tu Router)
        navigate(`/colecciones/${coleccionId}/hechos`);
    };

    return (
        // Usamos el fondo verde pálido de tu imagen
        <div style={{ backgroundColor: '#f0fdf4', minHeight: '100vh', padding: '2rem 0' }}>
            <Container>
                <h1 className="text-center mb-5">Colecciones de MetaMapa</h1>

                {/* 4. Grilla responsiva. Se ajustará a 2 columnas en pantallas medianas */}
                <Row xs={1} md={2} className="g-4">
                    {mockColecciones.map((coleccion) => (
                        <Col key={coleccion.id}>
                            <Card className="shadow-sm h-100">
                                {/* 5. El ícono reemplaza a la imagen */}
                                <div style={cardIconStyle}>
                                    <FaLayerGroup />
                                </div>

                                <Card.Body className="d-flex flex-column">
                                    <Card.Title className="fw-bold">{coleccion.titulo}</Card.Title>
                                    <Card.Text>
                                        {coleccion.descripcion}
                                    </Card.Text>

                                    {/* 6. El botón llama a la función de navegación */}
                                    <Button
                                        variant="warning"
                                        className="mt-auto" // Empuja el botón al final de la card
                                        onClick={() => handleVerHechos(coleccion.id)}
                                    >
                                        Ver Hechos
                                    </Button>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>

                {/* Aquí iría tu componente de paginación (1, 2, 3) */}

            </Container>
        </div>
    );
}

export default ColeccionesPage;