package ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Etiqueta;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
public abstract class HechoDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Contribuyente contribuyente;
    private List<Etiqueta> etiquetas;
}

