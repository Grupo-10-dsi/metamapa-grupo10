
import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'

class ApiAgregador {

    constructor() {
        this.tokenAuth = null
        this.axiosInstance = axios.create({
            baseURL: `${process.env.REACT_APP_API_GATEWAY_URL_BASE}/api/agregador`,
        })
    }

    setToken(token){
        this.tokenAuth = token
    }


    async obtenerHecho(id) {
        try {
            const response = await this.axiosInstance.get(`/hechos/${id}`)
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

    async obtenerCategorias() {
        try {
            const response = await this.axiosInstance.get('/categorias')
            console.log("Categorias obtenidas:", response.data)
            return response.data
        } catch(error) {
            console.error('Error al buscar categorias:', error)
            throw error
        }
    }

    async obtenerHechos(filtros, page = 0, size = 10) {
        try {

            const filtrosLimpios = Object.fromEntries(
                Object.entries(filtros).filter(([key, value]) => value != null && value !== '')
            );

            // Agregar parámetros de paginación
            filtrosLimpios.page = page;
            filtrosLimpios.size = size;

            const response = await this.axiosInstance.get('/hechos', {
                    params: filtrosLimpios,
                    paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
                })
            return response.data
        } catch(error) {
            console.error('Error al buscar hechos:', error)
            throw error
        }
    }

    async obtenerColecciones({ page = 0, size = 10 } = {}) {
        try {
            const response = await this.axiosInstance.get('/colecciones', {
                params: {
                    page: page,
                    size: size
                }
            })
            return response.data
        } catch(error) {
            console.error('Error al buscar colecciones:', error)
            throw error
        }
    }

    async obtenerUbicaciones() {
        try {
            const response = await this.axiosInstance.get('/hechos/ubicaciones')
            return response.data
        } catch(error) {
            console.error('Error al buscar ubicaciones de hechos:', error)
            throw error
        }
    }

    async eliminarColeccion(id){
        try {
            const response = await this.axiosInstance.delete(`/colecciones/${id}`,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${this.tokenAuth}`
                    },})
            return response.data
        } catch(error) {
            console.error('Error al eliminar coleccion:', error)
            throw error
        }
    }

    async obtenerSolicitudes(){
        try {
            const response = await this.axiosInstance.get('/solicitudes', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.tokenAuth}`
                },
            })
            return response.data
        } catch(error) {
            console.error('Error al trar solicitudes:', error)
            throw error
        }
    }


    async eliminarSolicitud(id) {
        try {
            const body = "RECHAZADA"
            const response = await this.axiosInstance.put(`/solicitudes/${id}`, body, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.tokenAuth}`
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al eliminar solicitud:', error)
            throw error
        }
    }

    async confirmarSolicitud(id) {
        try {
            const body = "ACEPTADA"
            const response = await this.axiosInstance.put(`/solicitudes/${id}`, body, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.tokenAuth}`
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al eliminar solicitud:', error)
            throw error
        }
    }

    // Nuevo: actualizar solo fuentes (lista de URLs)
    async actualizarColeccion(id, urls, algoritmo){
        try {
            const body = { urls_fuente: urls, algoritmo_consenso: algoritmo}
            console.log(urls)
            const response = await this.axiosInstance.patch(`/colecciones/${id}`, body, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.tokenAuth}`
                }
            })
            console.log(response.data)
            return response.data

        } catch (error) {
            console.error('Error al actualizar fuentes de colección:', error)
            throw error
        }
    }

    async crearColeccion(data) {
        try {
            const response = await this.axiosInstance.post('/colecciones', data, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.tokenAuth}`,
                }
            })
            return response.data
        } catch (error) {
            if (error.response) {
                console.error('Error al crear la colección:', error)
            }
        }
    }

    async enviarSolicitudEliminacion(data) {
        try {
            const response = await this.axiosInstance.post(`/solicitudes`, data, {
            })
            return response.data
        } catch (error) {
            if (error.response) {
                console.error('Error al enviar la solicitud de eliminación:', error)
            }
        }
    }

    async getHechosPorColeccion(id, filtros, consenso, page = 0, size = 10) {


        const cleanFiltros = {};


        for (const key in filtros) {
            const value = filtros[key];
            if (value !== null && value !== undefined && value !== '') {
                cleanFiltros[key] = value;
            }
        }

        const params = {
            ...cleanFiltros, // <-- Usamos el objeto limpio
            tipoNavegacion: consenso ? 'curada' : 'irrestricta',
            page: page,
            size: size
        };

        console.log("Enviando parámetros a la API:", params);

        try {
            // Usamos this.axiosInstance y paramsSerializer por consistencia
            const res = await this.axiosInstance.get(`/colecciones/${id}/hechos`, {
                params,
                paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' })
            });
            return res.data;
        } catch (error) {
            console.error('Error al obtener hechos por colección:', error);
            throw new Error('Error al obtener hechos');
        }
    }

    async obtenerColeccion(id) {
        try {
            const response = await this.axiosInstance.get(`/colecciones/${id}`)
            return response.data
        } catch(error) {
            console.error('Error al obtener la colección:', error)
            throw error
        }
    }

    async buscarPorTextoLibre(texto) {
        try {
            const response = await this.axiosInstance.get(`/search`, {
                params: {texto: texto}
            })
            return response.data
        } catch (error) {
            if (error.response) {
                console.error('Error al buscar los hechos:', error)
            }
        }
    }

}
const api = new ApiAgregador()
export default api
