package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.importador.Importador;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;

@Repository
public interface HechosRepository extends JpaRepository<Hecho, Integer> {
    List<Hecho> findByOrigenFuente(OrigenFuente origenFuente);



    //List<Hecho> findHechosByFuente(Fuente fuente);


    /**
     * Por ahora las fuentes se hardcodean aca. Eventualmente se
     * vera si se pueden agregar/sacar usando requests.
     */



/*
    public Hecho findByIdAndUpdate(Integer id, Hecho updatedHecho) {
        Hecho existingHecho = findById(id);
        if (existingHecho != null) {
            // Logica sin base de datos
            fuentes.forEach(fuente -> {
                List<Hecho> hechos = fuente.getHechos();
                int index = hechos.indexOf(existingHecho);
                if (index != -1) {
                    hechos.set(index, updatedHecho);
                    fuente.setHechos(hechos);
                }
            });

        }
        return updatedHecho;
    }*/

//    public List<Hecho> findAll() {
//        EntityManager em = BdUtils.getEntityManager();
//
//
//        return fuentes.stream().flatMap(fuente -> fuente.getHechos().stream())
//                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//    }
//
    /* DESCOMPONER EN EL SERVICE */
//    public void importarHechosDesdeFuentes() {
//        fuentes.forEach(fuente -> importador.importarHechos(fuente, this.ultimaConsulta));
//        System.out.print("Ultima consulta: ");
//        System.out.println(ultimaConsulta);
//        this.ultimaConsulta = LocalDateTime.now();
//    }
//
//
    /* A FUENTESREPOSITORY */
//    public List<Fuente> findAllFuentes() {
//        return fuentes;
//    }
//
    /* A FUENTESREPOSITORY */
//    public void update(List<Fuente> fuentesNuevas) {
//        this.setFuentes(fuentesNuevas);
//
//    }
//
     /* A FUENTESREPOSITORY */
//    public List<Fuente> findFuentes(List<String> urls) {
//        return fuentes.stream()
//                .filter(fuente -> urls.contains(fuente.getUrl()))
//                .toList();
//    }
//
    /* A FUENTESREPOSITORY */
//    public Long countFuentes() {
//        return fuentes.stream().count();
//    }
}
