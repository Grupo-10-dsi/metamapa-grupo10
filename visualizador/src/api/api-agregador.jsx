import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'

class ApiAgregador {
    constructor() {
        this.tokenAuth = null
        this.axiosInstance = axios.create({
            baseURL: process.env.REACT_APP_IP_BACK || 'http://localhost:8089/agregador',
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

    async obtenerColecciones() {
        try {
            const response = await this.axiosInstance.get('/colecciones')
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

}
const api = new ApiAgregador()
export default api