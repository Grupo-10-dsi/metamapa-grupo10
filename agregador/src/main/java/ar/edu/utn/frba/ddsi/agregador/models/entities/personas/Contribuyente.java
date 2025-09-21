package ar.edu.utn.frba.ddsi.agregador.models.entities.personas;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
    // Por ahora la defino como abstracta y sin metodos. TODO ver que agregar
    public Contribuyente() {}
}
