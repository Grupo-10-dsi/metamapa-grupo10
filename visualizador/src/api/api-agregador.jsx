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


}
const api = new ApiAgregador()
export default api