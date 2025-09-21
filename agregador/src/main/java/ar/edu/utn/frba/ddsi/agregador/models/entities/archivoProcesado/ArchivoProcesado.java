package ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter

@Entity
public class ArchivoProcesado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private LocalDateTime fechaCarga;

    public ArchivoProcesado(String nombre, LocalDateTime fechaCarga) {
        this.nombre = nombre;
        this.fechaCarga = fechaCarga;
    }

    public ArchivoProcesado() {}
}
