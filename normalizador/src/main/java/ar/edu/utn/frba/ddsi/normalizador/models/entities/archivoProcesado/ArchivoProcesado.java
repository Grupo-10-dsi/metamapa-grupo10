package ar.edu.utn.frba.ddsi.normalizador.models.entities.archivoProcesado;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArchivoProcesado {

    private Integer id;
    private String nombre;
    private LocalDateTime fechaCarga;

    public ArchivoProcesado(Integer id, String nombre, LocalDateTime fechaCarga) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCarga = fechaCarga;
    }
}
