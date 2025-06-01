package ar.edu.utn.frba.ddsi.estatica.services;
import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosServices implements IHechosServices{
    private HechosRepository hechosRepository;

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }


    @Override
    public List<Hecho> obtenerHechos() {

        return this.hechosRepository.findAll();
    }
}
