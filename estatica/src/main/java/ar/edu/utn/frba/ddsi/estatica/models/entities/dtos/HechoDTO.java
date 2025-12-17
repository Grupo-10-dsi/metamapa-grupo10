package ar.edu.utn.frba.ddsi.estatica.models.entities.dtos;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class HechoDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private List<String> contenidoMultimedia;
    private String cuerpo;

    public HechoDTO(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, List<String> contenidoMultimedia, String cuerpo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.contenidoMultimedia = contenidoMultimedia;
        this.cuerpo = cuerpo;
    }
}

