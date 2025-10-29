// visualizador/src/features/home-page/components/mapa.jsx
import {Card} from "react-bootstrap";
import React from "react";
import { useEffect, useState } from 'react'
import {MapContainer, TileLayer, Marker, Popup} from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import './mapa.css'
import L from 'leaflet'

/* fix para que cargue el icono, dsp poner personalizado */
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

function Mapa ({hechosMapa}) {
    const center = hechosMapa[0]
        ? { lat: hechosMapa[0].latitud, lng: hechosMapa[0].longitud }
        : { lat: -34.37049232747865, lng: -58.90407374255551 };

    return(<Card className="shadow-lg" style={{marginTop: '50px', marginBottom: '50px'}}>
        <Card.Body style={{padding: '0px'}}>
            <h4 className="text-center pt-3 pb-3 mb-0">Mapa de hechos registrados</h4>
            <div style={{height: '450px', width: '100%'}}>
                <MapContainer
                    center={center}
                    zoom={10}>
                    <TileLayer
                        url='https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    />
                    {hechosMapa.map(unHecho =>
                        <Marker position={{
                            lat: unHecho.latitud,
                            lng: unHecho.longitud
                        }}> {/* icono no carga, ver esto */}
                            <Popup>{unHecho.descripcion}</Popup>
                        </Marker>
                    )}
                </MapContainer>
            </div>
        </Card.Body>
    </Card>)
};

export default Mapa;