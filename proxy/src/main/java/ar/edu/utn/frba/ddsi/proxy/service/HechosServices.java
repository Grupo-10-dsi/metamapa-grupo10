package ar.edu.utn.frba.ddsi.proxy.service;


import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosServices {
    private HechosRepository HechosRepository;
    private MetaMapaClient instanciaMetaMapa;

    public List<Hecho> obtenerHechos(String nombre) {
        return this.HechosRepository.findByName(nombre);
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // cada 1 hora
    public void actualizarHechosPeriodicamente() {
        HechosRepository.obtenerHechos();
    }

    public List<Hecho> obtenerHechosMetaMapa(FiltroRequest query) {
        return this.instanciaMetaMapa.obtenerHechos(query);
    }
}
