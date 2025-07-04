package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import lombok.Getter;

@Getter
public class CriterioDTO {

    private String tipo;
    private String valor;

    public CriterioDTO(String tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}

// IDEA IMPLEMENTACION EN REPOSITORIO (SIMIL STRATEGY):
// "criterios": [
//  {
//      "tipo": "titulo",
//      "valor": "Titulo del Hecho"
//  },
//  ]
//   switch (criterio.getTipo()) {
//       case "titulo":
//           return new CriterioTitulo(criterio.getValor());