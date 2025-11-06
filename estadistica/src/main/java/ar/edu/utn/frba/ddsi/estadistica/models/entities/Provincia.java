package ar.edu.utn.frba.ddsi.estadistica.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
public class Provincia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String detalle;

    private LocalDateTime fecha;

    private Integer categoria_id;

    public  Provincia() {
    }

    public Provincia(String detalle, Integer categoria_id) {
        this.categoria_id = categoria_id;
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }
}
