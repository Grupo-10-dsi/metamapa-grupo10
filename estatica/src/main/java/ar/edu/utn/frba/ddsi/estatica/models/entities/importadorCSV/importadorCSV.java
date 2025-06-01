package ar.edu.utn.frba.ddsi.estatica.models.entities.importadorCSV;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class importadorCSV {
    public List<Hecho> importarHechosDeCSV(String nombreArchivo){
        List<String[]> filas = this.obtenerFilasDesdeCSV(nombreArchivo);
        List<Hecho> hechosImportados = new ArrayList<>();
        for (String[] fila : filas) {
            Hecho hecho = this.obtenerHechoPorFila(fila);
            hechosImportados.add(hecho);
        }
    }

    public Hecho obtenerHechoPorFila(String[] fila) {
        String titulo = fila[0];
        String descripcion = fila[1];
        String categoria = fila[2];
        Double latitud = Double.parseDouble(fila[3]);
        Double longitud = Double.parseDouble(fila[4]);
        LocalDate fechaAcontecimiento = LocalDate.parse(fila[5]);

        Hecho hecho = new Hecho(titulo, descripcion, new Categoria(categoria), new Ubicacion(latitud, longitud), fechaAcontecimiento);
        return hecho;
    }
}
