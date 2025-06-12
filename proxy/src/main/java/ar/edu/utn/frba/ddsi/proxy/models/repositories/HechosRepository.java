package ar.edu.utn.frba.ddsi.proxy.models.repositories;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.demo.conexion.Conexion;
import ar.edu.utn.frba.ddsi.proxy.demo.conexion.conexionHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class HechosRepository {

    private Map<String, Conexion> conexiones = Map.of();
    private Map<String, List<Hecho>> hechos = new HashMap<>(); // la conexion es la clave,


    @PostConstruct
    public void obtenerHechos() {

        for (String nombre : conexiones.keySet()) {
            Conexion conexion = conexiones.get(nombre);
            List<Hecho> hechosDeConexion = conexionHelper.getInstance().obtenerHechos(conexion);
            if (hechosDeConexion != null && !hechosDeConexion.isEmpty()) {
                this.hechos.put(nombre, hechosDeConexion);
            }
        }
    }

    public List<Hecho> findByName(String nombre) {
        return this.hechos.get(nombre);
    }
}
