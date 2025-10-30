import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'

class ApiAgregador {
    constructor() {
        this.tokenAuth = null
        this.axiosInstance = axios.create({
            baseURL: process.env.REACT_APP_IP_BACK || 'http://localhost:8080/agregador',
        })
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

    async obtenerHechos(filtros) {
        try {
            const response = await this.axiosInstance.get('/hechos', {
                    params: filtros,
                    paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
                })
            return response.data
        } catch(error) {
            console.error('Error al buscar hechos:', error)
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

    async obtenerColecciones() {
        try {
            const response = await this.axiosInstance.get('/colecciones')
            return response.data
        } catch(error) {
            console.error('Error al buscar colecciones:', error)
            throw error
        }
    }


    async eliminarColeccion(id){
        try {
            const response = await this.axiosInstance.delete(`/colecciones/${id}`)
            return response.data
        } catch(error) {
            console.error('Error al eliminar coleccion:', error)
            throw error
        }
    }



    async obtenerSolicitudes(){
        try {
            const response = await this.axiosInstance.get('/solicitudes/pendientes')
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
                },
            })
            return response.data
        } catch (error) {
            console.error('Error al eliminar solicitud:', error)
            throw error
        }
    }


    async actualizarAlgoritmoColeccion(id, algoritmo){
        try {
            const body = { algoritmo_consenso: algoritmo }
            const response = await this.axiosInstance.patch(`/colecciones/${id}`, body, { headers: { 'Content-Type': 'application/json' }})
            return response.data
        } catch (error) {
            console.error('Error al actualizar algoritmo de colección:', error)
            throw error
        }
    }


    // Nuevo: actualizar solo fuentes (lista de URLs)
    async actualizarColeccion(id, urls, algoritmo){
        try {
            const body = { urls_fuente: urls, algoritmo_consenso: algoritmo}
            console.log(urls)
            const response = await this.axiosInstance.patch(`/colecciones/${id}`, body, { headers: { 'Content-Type': 'application/json' }})
            console.log(response.data)
            return response.data

        } catch (error) {
            console.error('Error al actualizar fuentes de colección:', error)
            throw error
        }
    }


}
const api = new ApiAgregador()
export default api