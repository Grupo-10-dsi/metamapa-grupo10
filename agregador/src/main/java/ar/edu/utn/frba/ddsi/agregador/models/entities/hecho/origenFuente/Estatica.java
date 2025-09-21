package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import lombok.Getter;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Entity  @DiscriminatorValue("estatica")
public class Estatica extends OrigenFuente {
    @OneToOne
    @JoinColumn(name = "Origen_Fuente_id", referencedColumnName = "id")
    private ArchivoProcesado archivoProcesado;

    public Estatica(ArchivoProcesado archivoProcesado) {
        super();
        this.archivoProcesado = archivoProcesado;
    }
}
