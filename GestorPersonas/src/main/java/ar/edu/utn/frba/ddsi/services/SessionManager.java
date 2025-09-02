package ar.edu.utn.frba.ddsi.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static SessionManager instancia;

    private Map<String, Map<String, Object>> sesiones;

    private SessionManager() {
        this.sesiones = new HashMap<>();
    }

    public static SessionManager get() {
        if (instancia == null) {
            instancia = new SessionManager();
        }
        return instancia;
    }

    public String crearSesion(String clave, Object valor) {
        HashMap<String, Object> atributo = new HashMap<>();
        atributo.put(clave, valor);
        return this.crearSesion(atributo);
    }

    public String crearSesion(Map<String, Object> atributos) {
        String id = UUID.randomUUID().toString();
        this.sesiones.put(id, atributos);
        return id;
    }

    public Object getPerfil(String token) {
        Map<String, Object> sesion = this.sesiones.get(token);
        if (sesion != null) {
            return sesion.get("perfil");
        }
        return null;
    }

}
