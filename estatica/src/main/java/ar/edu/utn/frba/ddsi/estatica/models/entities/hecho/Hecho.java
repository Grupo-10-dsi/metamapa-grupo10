package ar.edu.utn.frba.ddsi.estatica.models.entities.hecho;

import java.time.LocalDateTime; //Fecha con hora
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Hecho {

    private final UUID id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente origenFuente;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.origenFuente = Origen_Fuente.ESTATICA;
    }

}

