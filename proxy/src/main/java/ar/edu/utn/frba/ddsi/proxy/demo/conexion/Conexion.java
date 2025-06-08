package ar.edu.utn.frba.ddsi.proxy.demo.conexion;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;

public interface Conexion {
    public Map<String, Object> siguienteHecho(URL url, LocalDate fechaUltimaConsulta);
    public URL url();
}