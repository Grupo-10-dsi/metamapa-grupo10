package ar.edu.utn.frba.ddsi.proxy.models.repositories;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.conexion.Conexion;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;

public class HechosRepository {

    private final Map<String, Conexion> conexiones = Map.of();
    private Map<String, List<Hecho>> hechos = Map.of();


    @PostConstruct
    public void obtenerHechos() {

        for (String nombre : conexiones.keySet()) {
            Conexion conexion = conexiones.get(nombre);
            List<Hecho> hechosDeConexion = conexion.obtenerHechos();
            if (hechosDeConexion != null && !hechosDeConexion.isEmpty()) {
                this.hechos.put(nombre, hechosDeConexion);
            }
        };
    }

    public List<Hecho> findByName(String nombre) {
        return this.hechos.get(nombre);
    }
}
