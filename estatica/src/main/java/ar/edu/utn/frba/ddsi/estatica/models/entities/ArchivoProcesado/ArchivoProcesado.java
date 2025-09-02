package ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class ArchivoProcesado {

    private String nombre;
    private LocalDateTime fechaCarga;
    private List<Hecho> hechos;

    public ArchivoProcesado(String nombre, LocalDateTime fechaCarga, List<Hecho> hechos) {
        this.nombre = nombre;
        this.fechaCarga = fechaCarga;
        this.hechos = hechos;
    }
}
