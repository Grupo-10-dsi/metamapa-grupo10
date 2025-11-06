package ar.edu.utn.frba.ddsi.estadistica.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Solicitud_Cantidad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer cantidad;

    private LocalDateTime fecha;

    public Solicitud_Cantidad() {
    }

    public Solicitud_Cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }
}
