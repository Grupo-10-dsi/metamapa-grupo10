package domain;

public class CriterioTitulo extends Criterio {
    private String titulo;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}

public class CriterioDescripcion extends Criterio {
    private String descripcion;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}

public class CriterioCategoria extends Criterio {
    private Categoria categoria;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}

public class CriterioUbicacion extends Criterio {
    private Ubicacion ubicacion;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}

public class CriterioFecha extends Criterio {
    private LocalDate fechaInicial;
    private LocalDate fechaFinal;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}

public class CriterioOrigen extends Criterio {
    private Origen_Fuente origen;

    @Override
    public Bool cumpleConCriterio(Hecho hecho) {
        // TODO: Implementar logica
    }
}
