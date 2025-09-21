package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import javax.persistence.*;


@Entity
@Table(name="origen_fuente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class OrigenFuente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public OrigenFuente() {
    }

    public static OrigenFuente getOrigenFuente(String tipo) {

        return switch (tipo) {
            case "DINAMICA" -> new Dinamica();
            case "PROXY" -> new Proxy();
            case "ESTATICA" -> new Estatica(null);

            default -> throw new IllegalArgumentException();
        };


    }

}
