package ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class SolicitudesRepository {
    private final List<SolicitudEliminacion> solicitudes = new ArrayList<>();

    public void save(SolicitudEliminacion nuevaSolicitud){
        solicitudes.add(nuevaSolicitud);
    }


    public List<SolicitudEliminacion> findAll(){
        return solicitudes;
    }

    public SolicitudEliminacion findById(UUID id) {
        return solicitudes.stream()
                .filter(solicitud -> solicitud.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public SolicitudEliminacion findByIdAndUpdate(UUID id, SolicitudEliminacion updatedSolicitud) {
        SolicitudEliminacion existingSolicitud = findById(id);
        if (existingSolicitud != null) {
            int index = solicitudes.indexOf(existingSolicitud);
            solicitudes.set(index, updatedSolicitud);
            return updatedSolicitud;
        }
        return null;
    }


















}
