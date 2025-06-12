package ar.edu.utn.frba.ddsi.proxy.demo.conexion;

import Categoria;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Origen_Fuente;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class conexionHelper {
    // Instancia única (singleton)
    private static final conexionHelper instancia = new conexionHelper();

    // Constructor privado
    private conexionHelper() {
    }

    public static conexionHelper getInstance() {
        return instancia;
    }

    public List<Hecho> obtenerHechos(Conexion conexion) {
        ArrayList<Hecho> hechos = new ArrayList<>();
        LocalDate fechaHaceUnaHora = LocalDateTime.now().minusHours(1).toLocalDate();
        Map<String, Object> hechoDescompuesto = conexion.siguienteHecho(conexion.url(), fechaHaceUnaHora);
        while (hechoDescompuesto != null) {
            hechos.add(convertirHecho(hechoDescompuesto));
            hechoDescompuesto = conexion.siguienteHecho(conexion.url(), LocalDate.now());
        }
        return hechos;
    }

    public Hecho convertirHecho(Map<String, Object> hechoDescompuesto) {
        String titulo = (String) hechoDescompuesto.get("titulo");
        String descripcion = (String) hechoDescompuesto.get("descripcion");
        Categoria categoria = (Categoria) hechoDescompuesto.get("categoria");
        Ubicacion ubicacion = (Ubicacion) hechoDescompuesto.get("ubicacion");
        LocalDate fechaAcontecimiento = (LocalDate) hechoDescompuesto.get("fechaAcontecimiento");
        LocalDateTime fechaImportacion = LocalDateTime.now();
        Origen_Fuente origenFuente = Origen_Fuente.INTERMEDIARIA;
        return new Hecho(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, fechaImportacion, origenFuente);
    }
}
