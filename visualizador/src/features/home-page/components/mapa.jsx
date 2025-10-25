// visualizador/src/features/home-page/components/mapa.jsx
import {Card} from "react-bootstrap";
import React from "react";
import {MapContainer, TileLayer, Marker, Popup} from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import './mapa.css'


// -34.37049232747865, -58.90407374255551
//TODO: Reemplazar el mapa de google con uno que permita agregar marcadores
const Mapa = () => (
    <Card className="shadow-lg" style={{ marginTop: '50px', marginBottom: '50px' }}>
        <Card.Body style={{ padding: '0px' }}>
            <h4 className="text-center pt-3 pb-3 mb-0">Mapa de hechos registrados</h4>
            <div style={{ height: '450px', width: '100%' }}>
                <MapContainer
                    center={{lat: '-34.37049232747865', lng: '-58.90407374255551'}}
                    zoom={10} >
                    <TileLayer
                        url='https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    />
                    <Marker position={{lat: '-34.37049232747865', lng: '-58.90407374255551'}}> {/* icono no carga, ver esto */}
                        <Popup>Aca van los hechos</Popup>
                    </Marker>
                </MapContainer>
            </div>
        </Card.Body>
    </Card>
);

export default Mapa;