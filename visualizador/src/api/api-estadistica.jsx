import axios from 'axios'
import { data } from 'react-router'
import qs from 'qs'
import kc from "../App.js"

class ApiEstadistica {
    constructor() {
        // 1. Inicializa tu token como null
        this.tokenAuth = null;

        this.axiosInstance = axios.create({
            baseURL: `${process.env.REACT_APP_API_GATEWAY_URL_BASE}/api/estadisticas`,
        });

        this.axiosInstance.interceptors.request.use(
            (config) => {
                // Si el token existe, lo adjunta a la cabecera
                if (this.tokenAuth) {
                    config.headers['Authorization'] = `Bearer ${this.tokenAuth}`;
                }
                return config;
            },
            (error) => {
                // Maneja errores de la petición
                return Promise.reject(error);
            }
        );
    }

    setToken(token) {
        this.tokenAuth = token;
    }


    async obtenerProvinciaPorColeccion({ id = null, formato = null, cantidadProvincias = 5}) {
        try {
            const response = await this.axiosInstance.get(`/colecciones/provincia-max-hechos`, {
                params: {
                    Id: id,
                    cantidadProvincias: cantidadProvincias
                },
                responseType: formato === "csv" ? "blob" : "json",
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
                responseType: formato === "csv" ? "blob" : "json",
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
                responseType: formato === "csv" ? "blob" : "json",
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
                responseType: formato === "csv" ? "blob" : "json",
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
                responseType: formato === "csv" ? "blob" : "json",
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