import { useState, useEffect } from 'react';

export default function useUbicacionFromCoords(lat, lng) {
    const [ubicacion, setUbicacion] = useState({ provincia: null, ciudad: null });

    useEffect(() => {
        if (!lat || !lng) return;

        const fetchUbicacion = async () => {
            try {
                const response = await fetch(
                    `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json&accept-language=es`
                );

                if (!response.ok) {
                    throw new Error(`Error en la consulta: ${response.status}`);
                }

                const data = await response.json();

                const ciudad = data.address.city
                    || data.address.town
                    || data.address.village
                    || data.address.hamlet
                    || null;

                const provincia = data.address.state || null;

                setUbicacion({ ciudad, provincia });
            } catch (error) {
                console.error('Error al obtener ubicación:', error);
            }
        };

        fetchUbicacion();
    }, [lat, lng]);

    return ubicacion;
}