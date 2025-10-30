import React from 'react';
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

function LocationMarker({ latitud, longitud, setFiltros }) {
  useMapEvents({
    click(e) {
      setFiltros(f => ({ ...f, latitud: e.latlng.lat, longitud: e.latlng.lng }));
    }
  });

  return latitud && longitud ? (
    <Marker position={[latitud, longitud]} />
  ) : null;
}

export default function MapaSelector({ latitud, longitud, setFiltros }) {
  const position = latitud && longitud ? [latitud, longitud] : [-34.61, -58.38]; // Default: Buenos Aires
  return (
    <div style={{ width: '100%', height: 220, margin: '18px 0', borderRadius: 12, overflow: 'hidden', boxShadow: '0 2px 8px #0001' }}>
      <MapContainer center={position} zoom={5} style={{ width: '100%', height: '100%' }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <LocationMarker latitud={latitud} longitud={longitud} setFiltros={setFiltros} />
      </MapContainer>
    </div>
  );
}
