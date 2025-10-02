package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import lombok.Getter;

import jakarta.persistence.*;


@Getter
@Entity
@DiscriminatorValue("estatica")
public class Estatica extends OrigenFuente {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Origen_Fuente_id", referencedColumnName = "id")
    private ArchivoProcesado archivoProcesado;

    public Estatica(ArchivoProcesado archivoProcesado) {
        super();
        this.archivoProcesado = archivoProcesado;
    }

    public Estatica() {

    }
}
