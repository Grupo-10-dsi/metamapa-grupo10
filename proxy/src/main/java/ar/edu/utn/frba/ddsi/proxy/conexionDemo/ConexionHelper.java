package ar.edu.utn.frba.ddsi.proxy.conexionDemo;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.*;

import ar.edu.utn.frba.ddsi.proxy.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.proxy.models.entities.personas.Anonimo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConexionHelper {
    private static final ConexionHelper instancia = new ConexionHelper();

    private ConexionHelper() {
    }

    public static ConexionHelper getInstance() {
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
        LocalDateTime fechaAcontecimiento = (LocalDateTime) hechoDescompuesto.get("fechaAcontecimiento");
        List<Etiqueta> etiquetas = (List<Etiqueta>) hechoDescompuesto.get("etiquetas");
        Contribuyente contribuyente  = Anonimo.getInstance();
        LocalDateTime fechaCarga = LocalDateTime.now();
        Origen_Fuente origenFuente = Origen_Fuente.INTERMEDIARIA;
        String cuerpo = (String) hechoDescompuesto.get("cuerpo");
        List<String> contenidoMultimedia = (List<String>) hechoDescompuesto.get("contenidoMultimedia");
        if(cuerpo != null) {
            return new HechoTextual(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, etiquetas ,contribuyente, cuerpo);
        } else {
            return new HechoMultimedia(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento, etiquetas, contribuyente, contenidoMultimedia);
        }
    }
}
