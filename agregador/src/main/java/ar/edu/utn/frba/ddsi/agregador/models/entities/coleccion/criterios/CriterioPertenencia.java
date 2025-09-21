package ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;

import javax.persistence.*;

@Entity
@Table(name="criterios_pertenencia")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CriterioPertenencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    boolean cumpleConCriterio(Hecho hecho) {
        return false;
    }
}


/*
* hechosParaColeccion = hechos.stream().filter(coleccion.getCriterioPertenencia()::cumpleConCriterio).toList();
*
* */