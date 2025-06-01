package ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.util.List;

public class importadorCSV {
    public List<Hecho> importarHechosDeCSV(String nombreArchivo){
        List<String[]> filas = this.obtenerFilasDesdeCSV(nombreArchivo);
        List<Hecho> hechosImportados = new ArrayList<>();


    }

    public Hecho obtenerHechoPorFila(String[] fila) {
        String titulo = fila[0];
        String descripcion = fila[1];
        String categoria = fila[2];
        Float latitud = Float.parseFloat(fila[3]);
        Float longitud = Float.parseFloat(fila[4]);
        LocalDate fechaAcontecimiento = LocalDate.parse(fila[5]);

        Hecho hecho = new Hecho(new Categoria(categoria), descripcion, new Ubicacion(latitud, longitud), fechaAcontecimiento);
        return hecho;
    }
}
