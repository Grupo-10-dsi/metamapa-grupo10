package ar.edu.utn.frba.ddsi.proxy.demo.conexion;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;


public class ConexionEjemplo implements Conexion {

    private URL url;
    @Override
    public Map<String, Object> siguienteHecho(URL url, LocalDate fechaUltimaConsulta) {
        // Implementación de ejemplo que retorna un mapa vacío
        return Map.of();
    }

    @Override
    public URL url() {
        return url;
    }
}