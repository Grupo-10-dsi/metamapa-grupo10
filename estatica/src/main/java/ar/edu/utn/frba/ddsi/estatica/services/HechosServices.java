package ar.edu.utn.frba.ddsi.estatica.services;
import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;

    }

    public List<Hecho> obtenerHechos(String ultimaConsulta) {
        this.hechosRepository.importarHechos();

        return this.hechosRepository.findAll()
                .stream()
                .filter(hecho -> ultimaConsulta == null ||
                                hecho.getFechaCarga().isAfter(LocalDateTime.parse(ultimaConsulta)))
                .collect(Collectors.toList());
    }
}
