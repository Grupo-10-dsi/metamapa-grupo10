package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Registrado.class, name = "registrado"),
        @JsonSubTypes.Type(value = Anonimo.class, name = "anonimo")
})
public class Contribuyente {
    // Por ahora la defino como abstracta y sin metodos. TODO ver que agregar
    private Integer id;

    public Contribuyente() {
        // Constructor por defecto necesario para Jackson
    }


}
