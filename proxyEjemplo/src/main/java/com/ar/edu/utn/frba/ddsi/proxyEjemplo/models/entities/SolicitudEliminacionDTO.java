package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudEliminacionDTO {

    private UUID idHecho;
    private String justificacion;

}
