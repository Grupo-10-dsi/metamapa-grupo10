import React, { useState, useEffect } from 'react'
import Modal from 'react-bootstrap/Modal'
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form'
// import InputGroup from 'react-bootstrap/InputGroup' // No se usaba
import './ventana-configuracion.css'

const ALGORITMOS = [
    { key: 'MULTIPLES_MENCIONES', label: 'Múltiples menciones' },
    { key: 'MAYORIA_SIMPLE', label: 'Mayoría simple' },
    { key: 'ABSOLUTA', label: 'Absoluta' },
    { key: 'NINGUNO', label: 'Ninguno' },
]

export default function VentanaConfiguracion({ show, onClose, onConfirm, fuentes = [], algoritmoActual = null }) {

    const [algoritmoSeleccionado, setAlgoritmoSeleccionado] = useState(null)
    // 1. 'fuentesSeleccionadas' ahora solo tiene 'urls'
    const [fuentesSeleccionadas, setFuentesSeleccionadas] = useState({ urls: [] })

    // 2. Eliminamos los 'useState' de 'listaFuentes' y 'nuevaFuente'

    useEffect(() => {
        if (!show) return
        setAlgoritmoSeleccionado(algoritmoActual || null)

        // 3. Simplificamos la normalización de 'fuentes'
        const urls = Array.isArray(fuentes)
            ? fuentes.map(f => {
                if (!f) return null
                if (typeof f === 'string') return f
                return f.url || f.url_fuente || f.nombre || f.nombreFuente || null
            }).filter(Boolean)
            : []

        // 4. Precargamos el estado con las URLs existentes
        setFuentesSeleccionadas({ urls: urls })

    }, [show, fuentes, algoritmoActual])

    const handleCheckboxListChange = (listName, value) => {
        setFuentesSeleccionadas(prev => {
            // Esta función ya estaba bien, pero ahora 'listName' siempre será 'urls'
            const current = Array.isArray(prev[listName]) ? [...prev[listName]] : []
            const idx = current.indexOf(value)
            if (idx === -1) {
                current.push(value)
            } else {
                current.splice(idx, 1)
            }
            return { ...prev, [listName]: current }
        })
    }

    const isSelected = (value, listName = 'urls') => {
        return Array.isArray(fuentesSeleccionadas[listName]) && fuentesSeleccionadas[listName].includes(value)
    }

    // 5. Eliminamos la función 'handleAgregarFuente' que no se usaba

    const handleAceptar = () => {
        // Esta función ya estaba bien
        const urls = Object.values(fuentesSeleccionadas).flat().filter(Boolean)
        const uniqueUrls = Array.from(new Set(urls))
        const algoritmoToSend = algoritmoSeleccionado ? algoritmoSeleccionado : null
        onConfirm({ algoritmo: algoritmoToSend, fuentesSeleccionadas: uniqueUrls })
    }


    const urlsFuentes = {
        "ESTATICA": "http://localhost:8081/api/estatica/hechos",
        "DINAMICA": "http://localhost:8082/api/dinamica/hechos",
        "PROXY": "http://localhost:8083/api/proxy/hechos"
    }

    const tiposDeFuente = ["ESTATICA", "DINAMICA", "PROXY"];



    return (
        <Modal show={show} onHide={onClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Configurar colección</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>Algoritmo de consenso</Form.Label>
                        <Form.Select value={algoritmoSeleccionado || ''} onChange={e => setAlgoritmoSeleccionado(e.target.value || null)}>
                            <option value=''>-- Sin cambios --</option>
                            {ALGORITMOS.map(a => (
                                <option key={a.key} value={a.key}>{a.label}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Fuentes (URLs)</Form.Label>
                        <div style={{ maxHeight: 200, overflowY: 'auto', marginBottom: 8 }}>
                            {/* 6. Eliminamos la comprobación de 'listaFuentes.length' */}

                            { tiposDeFuente.map(fuente => (
                                <Form.Check
                                    key={fuente}
                                    type="checkbox"
                                    id={`check-fuente-${fuente}`}
                                    label={fuente}

                                    // 7. 'checked' lee de 'urls' (esto estaba bien)
                                    checked={isSelected(urlsFuentes[fuente], 'urls')}

                                    // 8. 'onChange' AHORA ESCRIBE EN 'urls' (ESTE ES EL ARREGLO)
                                    onChange={() => handleCheckboxListChange('urls', urlsFuentes[fuente])}
                                />
                            ))}
                        </div>
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>Cancelar</Button>
                <Button variant="primary" onClick={handleAceptar}>Guardar</Button>
            </Modal.Footer>
        </Modal>
    )
}