package ar.edu.utn.frba.ddsi.estatica.services;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV.ImportadorCSV;
import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosServices {
    private final HechosRepository hechosRepository;

    public HechosServices(HechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;

    }

    public List<Hecho> obtenerHechos(String ultimaConsulta) {
        return this.hechosRepository.findAll()
                .stream()
                .filter(hecho -> ultimaConsulta == null ||
                                hecho.getFechaCarga().isAfter(LocalDateTime.parse(ultimaConsulta)))
                .collect(Collectors.toList());
    }
}
