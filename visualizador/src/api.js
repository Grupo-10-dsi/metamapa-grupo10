import axios from 'axios';

// visualizador/src/api.js
// deberia estar la url de la api en un .env

const API_URL = process.env.REACT_APP_API_URL;

export async function getHechosPorColeccion({ id, filtros, consenso }) {
    const params = {
        ...filtros,
        tipoNavegacion: consenso ? 'curada' : 'irrestricta'
    };
    try {
        const res = await axios.get(`${API_URL}/colecciones/${id}/hechos`, { params });
        return res.data;
    } catch (error) {
        throw new Error('Error al obtener hechos');
    }
}
