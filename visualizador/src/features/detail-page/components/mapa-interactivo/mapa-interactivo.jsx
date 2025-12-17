import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

function MapEvents({ onMapClick, position }) {
    const map = useMap();

    useEffect(() => {
        setTimeout(() => {
            map.invalidateSize();
        }, 100);
    }, [map]);

    // 1. Evento de clic en el mapa
    useMapEvents({
        click(e) {
            onMapClick(e.latlng);
        },
    });

    useEffect(() => {
        if (position.lat && position.lng) {
            map.setView(position, 15);
        }
    }, [position, map]);

    return null;
}

function MapaInteractivo({ value, onChange }) {


    const position = {
        lat: value.latitud || '',
        lng: value.longitud || ''
    };

    // Centro por defecto (ej. Buenos Aires) si no hay nada
    const defaultCenter = [
        -34.6037, // Latitud de BA
        -58.3816  // Longitud de BA
    ];

    const handleMapClick = (latlng) => {
        onChange({
            latitud: latlng.lat.toString(),
            longitud: latlng.lng.toString()
        });
    };

    return (
        <div
            style={{
                height: '300px',
                width: '100%',
                borderRadius: 'var(--bs-border-radius)',
                overflow: 'hidden'
            }}
        >
            <MapContainer
                center={defaultCenter}
                zoom={10}
                style={{ height: '100%', width: '100%' }}
            >
                <TileLayer
                    url='https://tile.openstreetmap.org/{z}/{x}/{y}.png'
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                />
                <MapEvents onMapClick={handleMapClick} position={position} />

                {position.lat && position.lng && (
                    <Marker position={position}>
                        <Popup>Ubicación seleccionada</Popup>
                    </Marker>
                )}
            </MapContainer>
        </div>
    );
};

export default MapaInteractivo;