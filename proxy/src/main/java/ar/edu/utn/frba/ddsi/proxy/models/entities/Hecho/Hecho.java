package ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter

public class Hecho {

    private final UUID id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaAcontecimiento;
    private LocalDateTime fechaImportacion;
    private Origen_Fuente origenFuente;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaAcontecimiento, LocalDateTime fechaImportacion, Origen_Fuente origenFuente) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaImportacion = fechaImportacion;
        this.origenFuente = origenFuente;
    }

}
