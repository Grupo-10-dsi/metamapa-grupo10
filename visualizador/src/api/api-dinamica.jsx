import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'

class ApiDinamica {
    constructor() {
        this.tokenAuth = null
        this.axiosInstance = axios.create({
            baseURL: process.env.REACT_APP_IP_BACK || 'http://localhost:8089/api/dinamica',
        })
    }

    async crearHecho(hechoData) {
        try {
            const response = await this.axiosInstance.post('/hechos', hechoData)
            return response.data
        } catch (error) {
            console.error('Error al crear el hecho:', error)
            throw error
        }
    }
}

const apiDinamica = new ApiDinamica()
export default apiDinamica