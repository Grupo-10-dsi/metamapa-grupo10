package ar.edu.utn.frba.ddsi.estatica.models.entities.dtos;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArchivoProcesadoDTO {

    private String nombre;
    private LocalDateTime fechaCarga;
    private List<HechoDTO> hechos;

    public ArchivoProcesadoDTO(String nombre, LocalDateTime fechaCarga, List<HechoDTO> hechos) {
        this.nombre = nombre;
        this.fechaCarga = fechaCarga;
        this.hechos = hechos;
    }

}
