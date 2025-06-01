package ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import java.util.List;

public interface Importador {
    public List<Hecho> importarHechosDeCSV(String nombreArchivo);
}
