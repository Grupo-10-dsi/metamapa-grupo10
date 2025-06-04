package ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho;


import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public abstract class Hecho {
    private UUID id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen_Fuente origen;
    private boolean estaOculto;
    private List<Etiqueta> etiquetas;
    private Contribuyente contribuyente;
    private boolean esAnonimo;

    public Hecho(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, List<Etiqueta> etiquetas, Contribuyente contribuyente, boolean esAnonimo) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.origen = Origen_Fuente.DINAMICA;
        this.estaOculto = false;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.esAnonimo = esAnonimo;
    }
}





