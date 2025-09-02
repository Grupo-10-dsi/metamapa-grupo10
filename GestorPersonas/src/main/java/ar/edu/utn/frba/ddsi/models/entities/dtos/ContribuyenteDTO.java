package ar.edu.utn.frba.ddsi.models.entities.dtos;

import java.util.UUID;

public class ContribuyenteDTO {

    private UUID id;
    private String nombre;

    public ContribuyenteDTO(UUID id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
