package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SolicitudesRepository extends JpaRepository<SolicitudEliminacion, Integer> {
    SolicitudEliminacion findSolicitudEliminacionById(Integer id);

    SolicitudEliminacion findSolicitudEliminacionByHecho_Id(Integer hechoId);

    List<SolicitudEliminacion> findAllByEstado(Estado_Solicitud estado);

    // Nuevo: chequear si existe al menos una solicitud con estado dado para un hecho
    boolean existsByHecho_IdAndEstado(Integer hechoId, Estado_Solicitud estado);
    /*
    private final List<SolicitudEliminacion> solicitudes = new ArrayList<>();

    public List<SolicitudEliminacion> findAll() {
        return solicitudes;
    }

    public SolicitudEliminacion findById(Integer id) {
        return solicitudes.stream()
                .filter(solicitud -> solicitud.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public SolicitudEliminacion findByIdAndUpdate(Integer id, SolicitudEliminacion updatedSolicitud) {
        SolicitudEliminacion existingSolicitud = findById(id);
        if (existingSolicitud != null) {
            // Logica sin base de datos
            int index = solicitudes.indexOf(existingSolicitud);
            solicitudes.set(index, updatedSolicitud);
            return updatedSolicitud;
        }
        return null;
    }

    public void save(SolicitudEliminacion solicitud){
        // Logica sin base de datos
        solicitudes.add(solicitud);
    }*/

}
