package ar.edu.utn.frba.ddsi.agregador.models.repositories;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.FuenteEstatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.importador.Importador;

import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository {

//    @PersistenceContext(unitName = "Agregador_BD")
//    private EntityManager em;

    /**
     * Por ahora las fuentes se hardcodean aca. Eventualmente se
     * vera si se pueden agregar/sacar usando requests.
     */


    @Setter
    private List<Fuente> fuentes = List.of(
            //new Fuente("http://localhost:8082/api/dinamica/hechos", "DINAMICA"),
            //new Fuente("http://localhost:8083/api/proxy/hechos", "PROXY"),
            //new FuenteEstatica( "ESTATICA", "http://localhost:8081/api/estatica/hechos", new ArrayList<>())
    );

    private LocalDateTime ultimaConsulta;
    private final Importador importador = new Importador();


    public Hecho findById(Integer id) {
        // Implementacion sin base de datos
        return fuentes.stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> hecho.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

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
    }

    public List<Hecho> findAll() {
        return fuentes.stream().flatMap(fuente -> fuente.getHechos().stream())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public void importarHechosDesdeFuentes() {
        fuentes.forEach(fuente -> importador.importarHechos(fuente, this.ultimaConsulta));
        System.out.print("Ultima consulta: ");
        System.out.println(ultimaConsulta);
        this.ultimaConsulta = LocalDateTime.now();
    }


    public List<Fuente> findAllFuentes() {
        return fuentes;
    }

    public void update(List<Fuente> fuentesNuevas) {
        this.setFuentes(fuentesNuevas);

    }

    public List<Fuente> findFuentes(List<String> urls) {
        return fuentes.stream()
                .filter(fuente -> urls.contains(fuente.getUrl()))
                .toList();
    }

    public Long countFuentes() {
        return fuentes.stream().count();
    }
}
