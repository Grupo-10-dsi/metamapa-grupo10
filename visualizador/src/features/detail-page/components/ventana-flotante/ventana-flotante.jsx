import React, { useState } from 'react';
import { Modal, Button, Form, Spinner } from 'react-bootstrap';

function VentanaFlotante({ show, handleClose, hecho, onSubmit }) {
    const [motivo, setMotivo] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async () => {
        if (!motivo) {
            alert("Por favor, ingrese un motivo.");
            return;
        }

        setIsSubmitting(true);

        await onSubmit({ idHecho: hecho?.id, justificacion: motivo });

        setIsSubmitting(false);
        setMotivo("");
        handleClose();
    };

    const handleModalClose = () => {
        setMotivo("");
        handleClose();
    };

    return (
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