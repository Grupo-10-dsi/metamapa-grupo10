package ar.edu.utn.frba.ddsi.estatica.models.repositories;

import ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.estatica.models.entities.importador.Importador;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;

import java.util.List;


@Repository
public class HechosRepository {
    private static Logger log = LoggerFactory.getLogger(HechosRepository.class);

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
        importador.setArchivosProcesados(nombresArchivos);
        this.importarHechos();
    }

    public void importarHechos() {
        this.hechos.clear();
        log.debug("Importando hechos...");
        List<ArchivoProcesado> hechosImportados = importador.importarHechos();

        this.addHechos(hechosImportados);

        log.info("Se importaron {} hechos de {} archivos procesados.", hechosImportados.stream().mapToInt(a -> a.getHechos().size()).sum(), hechosImportados.size());
    }

}
