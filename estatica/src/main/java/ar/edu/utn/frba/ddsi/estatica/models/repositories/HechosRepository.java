package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.Importador;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Repository;


import java.util.ArrayList;

import java.util.List;
import java.util.UUID;


@Repository
public class HechosRepository {

    private final List<ArchivoProcesado> hechos = new ArrayList<>();
    private final Importador importador;

    public HechosRepository(Importador importador) {
        this.importador = importador;
    }

    public List<ArchivoProcesado> findAllArchivosProcesados() {
        return this.hechos;
    }


    public void addHechos(List<ArchivoProcesado> hechos) {
        this.hechos.addAll(hechos);
    }

    public void importarHechosSin(List<String> nombresArchivos) {
        System.out.println("Importando hechos sin: " + nombresArchivos);
        importador.setArchivosProcesados(nombresArchivos);
        this.importarHechos();
    }

    public void importarHechos() {
        System.out.println("Importando hechos...");
        List<ArchivoProcesado> hechosImportados = importador.importarHechos();

        this.addHechos(hechosImportados);
    }

}
