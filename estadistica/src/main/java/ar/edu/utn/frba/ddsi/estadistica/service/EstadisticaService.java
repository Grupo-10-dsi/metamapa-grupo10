package ar.edu.utn.frba.ddsi.estadistica.service;

import ar.edu.utn.frba.ddsi.estadistica.models.entities.AgregadorClient;
import ar.edu.utn.frba.ddsi.estadistica.models.entities.Categoria;
import org.springframework.stereotype.Service;

@Service
public class EstadisticaService {

    private final AgregadorClient agregadorClient = new AgregadorClient("http://localhost:8080/api/agregador");

    public String obtenerProvinciaDeColeccion(Integer Id) {
        return agregadorClient.obtenerProvinciaDeColeccion(Id);
    }

    public Categoria obtenerCategoriaConMasHechos() {
        return this.agregadorClient.obtenerCategoriaConMasHechos();
    }
}
