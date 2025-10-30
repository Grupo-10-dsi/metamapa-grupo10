package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Ubicacion;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Contribuyente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor

public class HechoDTO {
    private String tipo;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private ContribuyenteDTO contribuyente;
    private List<Etiqueta> etiquetas;
    private List<String> contenidoMultimedia;
    private String cuerpo;

    public HechoDTO(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento, ContribuyenteDTO contribuyente, List<Etiqueta> etiquetas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.contribuyente = contribuyente;
        this.etiquetas = etiquetas;

    }


}

