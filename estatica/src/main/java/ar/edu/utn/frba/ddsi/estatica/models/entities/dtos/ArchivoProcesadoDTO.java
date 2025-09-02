package ar.edu.utn.frba.ddsi.estatica.models.entities.dtos;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArchivoProcesadoDTO {

    private String nombre;
    private LocalDateTime ultimaConsulta;
    private List<HechoDTO> hechos;

    public ArchivoProcesadoDTO(String nombre, LocalDateTime ultimaConsulta, List<HechoDTO> hechos) {
        this.nombre = nombre;
        this.ultimaConsulta = ultimaConsulta;
        this.hechos = hechos;
    }

}
