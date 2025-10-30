import React, { useState } from 'react';
import { Modal, Button, Form, Spinner } from 'react-bootstrap';

// Este modal recibe 4 props:
// 1. show: (true/false) para mostrarse u ocultarse.
// 2. handleClose: La función del padre para cerrarse.
// 3. hecho: El objeto "hecho" del cual queremos solicitar la eliminación.
// 4. onSubmit: La función que se ejecutará al enviar el formulario.
function VentanaFlotante({ show, handleClose, hecho, onSubmit }) {
    const [motivo, setMotivo] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async () => {
        if (!motivo) {
            alert("Por favor, ingrese un motivo.");
            return;
        }

        setIsSubmitting(true);

        // Llamamos a la función onSubmit que nos pasó el padre
        // y le pasamos el id del hecho y el motivo.
        await onSubmit({ idHecho: hecho?.id, justificacion: motivo });

        setIsSubmitting(false);
        setMotivo(""); // Limpiamos el campo
        handleClose(); // Cerramos el modal
    };

    const handleModalClose = () => {
        setMotivo(""); // Limpiamos el campo al cerrar
        handleClose();
    };

    return (
        // Usamos onHide para que se cierre al hacer clic fuera
        <Modal show={show} onHide={handleModalClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Solicitar Eliminación</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>
                    Estás solicitando la eliminación del hecho:
                    <strong> {hecho?.titulo || 'Hecho seleccionado'}</strong>
                </p>
                <Form>
                    <Form.Group controlId="formMotivo">
                        <Form.Label>Motivo de la solicitud:</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            placeholder="Ej: Hecho con información incorrecta, es spam..."
                            value={motivo}
                            onChange={(e) => setMotivo(e.target.value)}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleModalClose} disabled={isSubmitting}>
                    Cancelar
                </Button>
                <Button variant="danger" onClick={handleSubmit} disabled={isSubmitting}>
                    {isSubmitting ? (
                        <>
                            <Spinner
                                as="span"
                                animation="border"
                                size="sm"
                                role="status"
                                aria-hidden="true"
                            />
                            {' '}Enviando...
                        </>
                    ) : (
                        "Enviar Solicitud"
                    )}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default VentanaFlotante;