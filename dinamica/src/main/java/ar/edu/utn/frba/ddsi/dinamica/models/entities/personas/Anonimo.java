package ar.edu.utn.frba.ddsi.dinamica.models.entities.personas;

public class Anonimo extends Contribuyente {

    private static Anonimo instance;

    public static Anonimo getInstance() {
        if (instance == null) {
            instance = new Anonimo();
        }
        return instance;
    }

}
