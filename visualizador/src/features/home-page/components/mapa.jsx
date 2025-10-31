// visualizador/src/features/home-page/components/mapa.jsx
import {Button, Card} from "react-bootstrap";
import React from "react";
import { useEffect, useState } from 'react'
import {MapContainer, TileLayer, Marker, Popup, CircleMarker} from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import './mapa.css'
import L from 'leaflet'
import {useNavigate} from "react-router-dom";
import MarkerClusterGroup from 'react-leaflet-cluster';
import ApiAgregador from "../../../api/api-agregador";

/* fix para que cargue el icono, dsp poner personalizado */
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

const myRenderer = L.canvas({ padding: 0.5 });


function Mapa ({hechosMapa}) {
    const navigate = useNavigate()

    const navigateToHecho = (hechoId) => {
        navigate(`/hecho/${hechoId}`);
    }

    const center = hechosMapa[0]
        ? { lat: hechosMapa[0].latitud, lng: hechosMapa[0].longitud }
        : { lat: -34.37049232747865, lng: -58.90407374255551 };

    return(<Card className="shadow-lg" style={{marginTop: '50px', marginBottom: '50px'}}>
        <Card.Body style={{padding: '0px'}}>
            <h4 className="text-center pt-3 pb-3 mb-0">Mapa de hechos registrados</h4>
            <div style={{height: '450px', width: '100%'}}>
                <MapContainer
                    preferCanvas={true}
                    renderer={myRenderer}
                    center={center}
                    zoom={10}>
                    <TileLayer
                        url='https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    />
                    <MarkerClusterGroup chunkedLoading>
                    {hechosMapa.map(unHecho =>
                        <CircleMarker center={{
                            lat: unHecho.latitud,
                            lng: unHecho.longitud
                        }}> {/* icono no carga, ver esto */}
                            <Popup>
                                <p></p>
                                <Button onClick={() => navigateToHecho(unHecho.id)}> Ver mas </Button>
                            </Popup>
                        </CircleMarker>
                    )}
                        </MarkerClusterGroup>
                </MapContainer>
            </div>
        </Card.Body>
    </Card>)
};

export default Mapa;