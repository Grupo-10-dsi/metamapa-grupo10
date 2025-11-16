package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

public class CategoriaDTO {
    private Integer id;
    private String detalle;

    public CategoriaDTO(Integer id, String detalle) {
        this.id = id;
        this.detalle = detalle;
    }

    public Integer getId() {
        return id;
    }

    public String getDetalle() {
        return detalle;
    }
}
