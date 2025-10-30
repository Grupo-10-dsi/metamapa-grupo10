import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'

class ApiEstadistica {
    constructor() {
        this.tokenAuth = null
        this.axiosInstance = axios.create({
            baseURL: process.env.REACT_APP_IP_BACK || 'http://localhost:8088/api/estadisticas',
        })
    }


    async obtenerProvinciaPorColeccion({ id = null, formato = null, cantidadProvincias = 5}) {
        try {
            const response = await this.axiosInstance.get(`/colecciones/provincia-max-hechos`, {
                params: {
                    Id: id,
                    cantidadProvincias: cantidadProvincias
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

    async obtenerCategoriaMaxHechos({ formato = null, cantidadCategorias = 5}) {
        try {
            const response = await this.axiosInstance.get(`/hechos/max-categoria`, {
                params: {
                    formato: formato,
                    cantidadCategorias: cantidadCategorias,
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

    async obtenerProvinciaPorCategoria({ id = null, formato = null, cantidadProvincias = 5}) {
        try {
            const response = await this.axiosInstance.get(`/categoria/provincia-max-hechos`, {
                params: {
                    Id: id,
                    formato: formato,
                    cantidadProvincias: cantidadProvincias,
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

    async obtenerHoraMaxHechos({ id = null, cantidadHoras = 5, formato = null }) {
        try {
            const response = await this.axiosInstance.get(`/categoria/hora`, {
                params: {
                    Id: id,
                    cantidadHoras,
                    formato
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

    async obtenerSolicitudesSpam({ mostrar, formato = null }) {
        try {
            const response = await this.axiosInstance.get(`/solicitudes/spam`, {
                params: {
                    mostrar,
                    formato
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al obtener el hecho:', error)
            throw error
        }
    }

}
const api = new ApiEstadistica()
export default api