import { Button, Card } from "react-bootstrap";
import React from "react";
import { MapContainer, TileLayer, Popup, CircleMarker } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import { useNavigate } from "react-router-dom";
import './mapa.css'

// Importaciones para Marker Cluster
import MarkerClusterGroup from 'react-leaflet-markercluster';
import 'leaflet.markercluster/dist/MarkerCluster.css';
import 'leaflet.markercluster/dist/MarkerCluster.Default.css';

import { GeoAltFill, ArrowRightCircle } from "react-bootstrap-icons";

// --- INICIO: Fix para íconos de Leaflet ---
import iconRetinaUrl from 'leaflet/dist/images/marker-icon-2x.png';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import shadowUrl from 'leaflet/dist/images/marker-shadow.png';

/* fix para que cargue el icono, dsp poner personalizado */
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: iconRetinaUrl,
    iconUrl: iconUrl,
    shadowUrl: shadowUrl
});
// --- FIN: Fix para íconos de Leaflet ---

const myRenderer = L.canvas({ padding: 0.5 });

function Mapa({ hechosMapa }) {
    const navigate = useNavigate()

    const navigateToHecho = (hechoId) => {
        navigate(`/hecho/${hechoId}`);
    }

    const center = hechosMapa[0]
        ? { lat: hechosMapa[0].latitud, lng: hechosMapa[0].longitud }
        : { lat: -34.37049232747865, lng: -58.90407374255551 };

    return (<Card className="shadow-lg" style={{ marginTop: '50px', marginBottom: '50px' }}>
        <Card.Body style={{ padding: '0px' }}>
            <h4 className="text-center pt-3 pb-3 mb-0">Mapa de hechos registrados</h4>
            <div style={{ height: '450px', width: '100%' }}>
                <MapContainer
                    preferCanvas={true}
                    renderer={myRenderer}
                    center={center}
                    zoom={10}>
                    <TileLayer
                        url='https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    />

                    <MarkerClusterGroup>
                        {hechosMapa.map(unHecho =>
                            <CircleMarker
                                key={unHecho.id}
                                center={{
                                    lat: unHecho.latitud,
                                    lng: unHecho.longitud
                                }}
                            >
                                <Popup className="custom-popup">
                                    <div className="popup-card-content">
                                        {/* Encabezado con color e ícono */}
                                        <div className="popup-header">
                                            <GeoAltFill className="me-2" />
                                            <strong>Incidente #{unHecho.id}</strong>
                                        </div>

                                        {/* Cuerpo del popup */}
                                        <div className="popup-body">
                                            <h6 className="mb-2">{unHecho.titulo}</h6>
                                            {/* Puedes agregar fecha o categoría aquí si la tienes */}
                                            <p className="text-muted small mb-3">
                                                Haz click para ver el detalle completo.
                                            </p>

                                            <Button
                                                size="sm"
                                                variant="primary"
                                                className="w-100 d-flex align-items-center justify-content-center"
                                                onClick={() => navigateToHecho(unHecho.id)}
                                            >
                                                Ver más <ArrowRightCircle className="ms-2"/>
                                            </Button>
                                        </div>
                                    </div>
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

