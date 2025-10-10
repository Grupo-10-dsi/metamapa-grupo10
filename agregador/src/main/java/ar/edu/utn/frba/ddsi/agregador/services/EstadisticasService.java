package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.UbicacionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.EstadisticasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class EstadisticasService {
    private final EstadisticasRepository estadisticasRepository;

    public EstadisticasService(EstadisticasRepository estadisticasRepository) {
        this.estadisticasRepository = estadisticasRepository;
    }

    // SERVICIOS PARA EL MÓDULO DE ESTADÍSTICAS
    public List<Ubicacion> obtenerUbicacionesColeccion(Integer idColeccion) {
        return this.estadisticasRepository.obtenerUbicacionesColeccion(idColeccion);
    }

    public Categoria obtenerCategoriaConMasHechos() {
        return this.estadisticasRepository.obtenerCategoriaConMasHechos();
    }

//    public Ubicacion obtenerUbicacionMasFrecuenteDeColeccion(Integer idColeccion) {
//        return this.estadisticasRepository.obtenerUbicacionMasFrecuenteDeColeccion(idColeccion);
//    }
//
    public List<Ubicacion> obtenerUbicacionesCategoria(Integer idCategoria) {
        return this.estadisticasRepository.obtenerUbicacionesCategoria(idCategoria);
    }

    public LocalTime obtenerHoraMasFrecuente(Integer Id) {
        Integer hora =  this.estadisticasRepository.obtenerHoraMasFrecuente(Id);
        return LocalTime.of(hora,0);
    }
}
