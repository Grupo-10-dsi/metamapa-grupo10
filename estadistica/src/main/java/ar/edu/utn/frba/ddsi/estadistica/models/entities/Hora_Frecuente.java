package ar.edu.utn.frba.ddsi.estadistica.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity

public class Hora_Frecuente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalTime hora;

    private LocalDateTime fecha;

    private Integer categoria_id;

    public Hora_Frecuente() {
    }

    public Hora_Frecuente(LocalTime hora, Integer categoria_id) {
        this.hora = hora;
        this.fecha = LocalDateTime.now();
        this.categoria_id = categoria_id;
    }
}
