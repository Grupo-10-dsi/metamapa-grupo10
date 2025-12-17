package ar.edu.utn.frba.ddsi.normalizador.services;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador.Normalizador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalizadorService {

    @Autowired
    private Normalizador normalizador;

    public NormalizadorService() {

    }

    public HechoDTO normalizar(HechoDTO hechoCrudo) {
        return normalizador.normalizar(hechoCrudo);
    }

}
