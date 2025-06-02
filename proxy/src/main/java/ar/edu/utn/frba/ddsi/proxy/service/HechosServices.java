package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;

import java.util.List;

public class HechosServices {
    private HechosRepository HechosRepository;

    public List<Hecho> obtenerHechos() {
        return this.HechosRepository.findAll();
    }
}
