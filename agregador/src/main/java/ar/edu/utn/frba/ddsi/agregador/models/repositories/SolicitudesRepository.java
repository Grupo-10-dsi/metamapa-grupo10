package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitudesRepository {
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
    }

}
