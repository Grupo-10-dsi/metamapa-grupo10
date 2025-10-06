package ar.edu.utn.frba.ddsi.proxy.models.entities.personas;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Registrado.class, name = "registrado"),
        @JsonSubTypes.Type(value = Anonimo.class, name = "anonimo")
})

public abstract class Contribuyente {
    public Integer id;

    private List<Hecho> hechos;

    // Por ahora la defino como abstracta y sin metodos. TODO ver que agregar
    public Contribuyente() {}

    public String getNombre() {
        return "";
    }
}

