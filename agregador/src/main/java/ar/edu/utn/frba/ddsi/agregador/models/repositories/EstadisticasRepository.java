package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EstadisticasRepository extends JpaRepository<Hecho, Integer> { // Pongo <Hecho, Integer> para poder usar JPA, pero el repo devuelve distintos tipos de dato.

    @Query( value = """
            SELECT h.latitud, h.longitud
            FROM hecho h
            JOIN fuente f ON h.url_fuente = f.url
            JOIN coleccion_fuente cf ON cf.fuente_url = f.url
            JOIN coleccion c ON cf.coleccion_id = c.id
            WHERE c.id = ?1
        """,
            nativeQuery = true)
    List<Ubicacion> obtenerUbicacionesColeccion(Integer id); // el WHERE c.coleccion_id = ?1 compara el id de la coleccion con el primer parametro que recibe el metodo.

    // La instanciara directo a la categoria?
    @Query( value = """ 
            SELECT h.detalle
            FROM Hecho h
            GROUP BY h.detalle
            ORDER BY count(h.detalle) DESC LIMIT 1
        """,
            nativeQuery = true)
    Categoria obtenerCategoriaConMasHechos();

//    @Query( value = """
//            SELECT h.latitud, h.longitud
//            FROM hecho h
//            JOIN fuente f ON h.url_fuente = f.url
//            JOIN coleccion_fuente cf ON cf.fuente_url = f.url
//            JOIN coleccion c ON cf.coleccion_id = c.id
//            WHERE c.id = ?1
//            GROUP BY h.latitud, h.longitud\s
//            ORDER BY count(*) desc limit 1
//        """,
//            nativeQuery = true)
//    Ubicacion obtenerUbicacionMasFrecuenteDeColeccion(Integer id);

//    @Query( value = """
//           select h.latitud, h.longitud
//           from hecho h
//           where h.detalle = ?1
//           group by h.latitud, h.longitud
//           order by count(*) desc
//           limit 1
//        """,
//            nativeQuery = true)
//    Ubicacion obtenerUbicacionMasFrecuenteDeCategoria(Integer id);

    @Query( value = """
            SELECT h.latitud, h.longitud
            FROM hecho h
            WHERE h.detalle = ?1
        """,
            nativeQuery = true)
    List<Ubicacion> obtenerUbicacionesCategoria(Integer id);

    @Query( value = """
           select HOUR(h.fecha_acontecimiento)
           from hecho h
           group by hour(h.fecha_acontecimiento)
           order by count(*) desc
           limit 1
        """,
            nativeQuery = true)
    Integer obtenerHoraMasFrecuente(Integer id);

}
