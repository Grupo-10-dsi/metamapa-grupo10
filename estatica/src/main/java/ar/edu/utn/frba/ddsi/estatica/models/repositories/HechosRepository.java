package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.Importador;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV.ImportadorCSV;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.*;

@Repository
public class HechosRepository {

    private final List<Hecho> hechos = new ArrayList<Hecho>();
    private final Importador importador;



    public HechosRepository(Importador importador) {
        this.importador = importador;
    }

    public List<Hecho> findAll() {
        return this.hechos;
    }

    public Hecho findById(UUID id) {
        return this.hechos.stream()
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void save(Hecho hecho) {
        this.hechos.add(hecho);
    }

    public void delete(Hecho hecho) {
        this.hechos.removeIf(h -> h.getId().equals(hecho.getId()));
    }

    public void addHechos(List<Hecho> hechos) {
        this.hechos.addAll(hechos);
    }

    @PostConstruct
    public void importarHechos() {
        System.out.println("Importando hechos...");
        List<Hecho> hechosImportados = importador.importarHechos();

        this.addHechos(hechosImportados);

    }

}
