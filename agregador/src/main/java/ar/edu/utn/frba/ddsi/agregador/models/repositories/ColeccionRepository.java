package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion,Integer> {

    Coleccion findColeccionById(Integer id);

    @Query("SELECT ")
    String obtenerProvinciaConMasHechos(Integer id);

/*
    private final List<Coleccion> colecciones = new ArrayList<>();

//    public void save(Hecho hecho) {
//        colecciones.add(hecho);
//    }

    public void save(Coleccion coleccion) {
        colecciones.add(coleccion);
    }

    public Coleccion findById(Integer id) {
        return colecciones.stream().filter(coleccion -> coleccion.getId().equals(id)).findFirst().orElse(null);
    }



    public Coleccion findByIdAndUpdate(Integer id, Coleccion updatedColeccion) {
        Coleccion existingColeccion = findById(id);
        if (existingColeccion != null) {
            // Logica sin base de datos
            int index = colecciones.indexOf(existingColeccion);
            colecciones.set(index, updatedColeccion);
            return updatedColeccion;
        }
        return null;
    }

    public List<Coleccion> findAll() {
        return colecciones;
    }

    public Coleccion findAndDelete(Integer id) {
        Coleccion coleccionEncontrada = findById(id);

        if (coleccionEncontrada != null) {
            colecciones.remove(coleccionEncontrada);
            return coleccionEncontrada;
        }

        return null; //TODO: IMPLEMENTAR
    }*/


}


