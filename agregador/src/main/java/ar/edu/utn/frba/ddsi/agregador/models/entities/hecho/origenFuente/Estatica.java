package ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente;

import ar.edu.utn.frba.ddsi.agregador.models.entities.archivoProcesado.ArchivoProcesado;
import lombok.Getter;

import jakarta.persistence.*;


@Getter
@Entity
@DiscriminatorValue("estatica")
public class Estatica extends OrigenFuente {
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch=FetchType.EAGER)
    @JoinColumn(name = "archivo_id", referencedColumnName = "id")
    private ArchivoProcesado archivoProcesado;

    public Estatica(ArchivoProcesado archivoProcesado) {
        super();
        this.nombre = "ESTATICA";
        this.archivoProcesado = archivoProcesado;
    }

    public Estatica() {
        super();
        this.nombre = "ESTATICA";
    }

    @Override
    public void setArchivoProcesado(ArchivoProcesado archivoProcesado) {
        this.archivoProcesado = archivoProcesado;
    }
}
