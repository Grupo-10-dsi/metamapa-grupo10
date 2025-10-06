package ar.edu.utn.frba.ddsi.proxy.models.entities.personas;

import ar.edu.utn.frba.ddsi.proxy.models.entities.personas.Contribuyente;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeName("registrado")
public class Registrado extends Contribuyente {

    // El id debe matchear con el id de la persona en el sistema de usuarios
    private String nombre;

    public Registrado(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }
}
