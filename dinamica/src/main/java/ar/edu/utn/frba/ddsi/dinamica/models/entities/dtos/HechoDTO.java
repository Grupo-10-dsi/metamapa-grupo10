package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Ubicacion;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Registrado;
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
    private Registrado registrado;
    private List<Etiqueta> etiquetas;
    private List<String> contenidoMultimedia;
    private String cuerpo;

    public HechoDTO(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento, Registrado registrado, List<Etiqueta> etiquetas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.registrado = registrado;
        this.etiquetas = etiquetas;

    }


}

