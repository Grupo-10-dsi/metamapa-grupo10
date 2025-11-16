package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Contribuyente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Categoria;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Ubicacion;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Etiqueta;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Origen_Fuente;
import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.Anonimo;

@Getter
@Setter

public class HechoMultimedia extends Hecho {
    private List<String> contenidoMultimedia;

    public HechoMultimedia(String titulo,
                           String descripcion,
                           Categoria categoria,
                           Ubicacion ubicacion,
                           LocalDateTime fechaAcontecimiento,
                           List<Etiqueta> etiquetas,
                           Contribuyente contribuyente,
                           List<String> contenidoMultimedia) {
        super(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, etiquetas, contribuyente);
        this.contenidoMultimedia = contenidoMultimedia;
    }
}
