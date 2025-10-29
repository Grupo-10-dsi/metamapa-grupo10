import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

export default function ContadorElementos({ onChange }) {
    const [cantidad, setCantidad] = useState(1);

    const incrementar = () => {
        setCantidad(prev => {
            const next = prev + 1;
            onChange && onChange(next);
            return next;
        });
    };

    const decrementar = () => {
        setCantidad(prev => {
            const next = prev > 1 ? prev - 1 : 1;
            onChange && onChange(next);
            return next;
        });
    };

    return (
        <Form.Group className="mt-3 text-center">
            <Form.Label>Elementos a traer</Form.Label>
            <div className="d-flex justify-content-center align-items-center">
                <Button variant="outline-secondary" onClick={decrementar}>-</Button>
                <Form.Control
                    type="text"
                    value={cantidad}
                    readOnly
                    className="text-center mx-2"
                    style={{ width: "60px" }}
                />
                <Button variant="outline-secondary" onClick={incrementar}>+</Button>
            </div>
        </Form.Group>
    );
}