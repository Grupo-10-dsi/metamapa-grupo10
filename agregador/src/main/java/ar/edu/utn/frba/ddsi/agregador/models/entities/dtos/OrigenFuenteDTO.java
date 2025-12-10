package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrigenFuenteDTO {
    private String tipo; // "ESTATICA", "DINAMICA", "PROXY"
    private String nombre;
    private String nombreArchivo; // Solo para ESTATICA
}