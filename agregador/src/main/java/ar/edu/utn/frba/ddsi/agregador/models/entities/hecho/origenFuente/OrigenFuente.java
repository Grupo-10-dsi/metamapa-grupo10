package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name="origen_fuente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class OrigenFuente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String nombre;

    public OrigenFuente() {
    }

    public static OrigenFuente getOrigenFuente(String tipo) {

        return switch (tipo) {
            case "DINAMICA" -> new Dinamica();
            case "PROXY" -> new Proxy();
            case "ESTATICA" -> new Estatica();

            default -> throw new IllegalArgumentException();
        };


    }

    public void setArchivoProcesado(ArchivoProcesado archivoProcesado) {
    }

}
