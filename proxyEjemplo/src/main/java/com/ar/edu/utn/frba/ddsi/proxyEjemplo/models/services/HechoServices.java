package com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.services;

import com.ar.edu.utn.frba.ddsi.proxyEjemplo.models.entities.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HechoServices {

    private  List<Hecho> HECHOS;
    private  Coleccion COLECCION_A;
    private  Coleccion COLECCION_B;
    private  List<Coleccion> COLECCIONES;


    public HechoServices() {
        HECHOS = new ArrayList<>();
        COLECCIONES = new ArrayList<>();
    }

    @PostConstruct
    public void inicializarDatos() {
        Contribuyente contribuyente1 = new Registrado(2, "Juan Perez");
        Contribuyente contribuyente2 = new Registrado(3, "Maria Lopez");
        Contribuyente contribuyente3= new Registrado(4, "Carlos Gomez");

        // Descripciones largas reutilizables
        String descObelisco = "Un grupo de manifestantes pertenecientes a organizaciones sociales ha interrumpido el tránsito en la Avenida 9 de Julio, a la altura del Obelisco. La protesta genera importantes demoras en todo el microcentro porteño y afecta el servicio del Metrobús. Se recomienda a los conductores evitar la zona y buscar rutas alternativas.";
        String descCaminito = "Un colectivo de artistas ha finalizado un nuevo mural a gran escala en el icónico paseo de Caminito, en La Boca. La obra retrata figuras históricas del barrio y ha sido bien recibida por vecinos y turistas.";
        String descSanTelmo = "La histórica feria de antigüedades de Plaza Dorrego, en el corazón de San Telmo, ofreció este domingo una variedad excepcional de objetos. Turistas y locales pudieron encontrar desde muebles restaurados y vajilla de época hasta vinilos de colección y juguetes antiguos. El buen clima acompañó la jornada, que se extendió hasta el atardecer con espectáculos de tango callejero.";
        String descRecoleta = "Debido a las fuertes ráfagas de viento de esta madrugada, un árbol de gran porte cayó sobre uno de los pasillos internos del Cementerio de Recoleta. Afortunadamente, no se reportaron daños a mausoleos históricos ni heridos. Personal de Espacio Público ya trabaja en la remoción.";
        String descPalermo = "Vecinos del barrio de Palermo Soho denuncian ruidos molestos provenientes de un bar ubicado en la intersección de Serrano y Costa Rica. Según el relato, el volumen de la música excede los niveles permitidos, especialmente durante la madrugada, dificultando el descanso. Solicitan intervención de la agencia de control comunal.";
        String descPlazaMayo = "Se realizó un acto conmemorativo en Plaza de Mayo para recordar un nuevo aniversario de un evento histórico nacional. El acto contó con la presencia de diversas agrupaciones políticas y sociales, y se leyó un documento frente a la Casa Rosada.";
        String descMisiones = "La región de San Vicente en Misiones sufrió los efectos de una intensa ráfagas de más de 100 km/h. El incidente obligando a evacuar a residentes de la zona. Se ha convocado al comité de crisis para coordinar las acciones de respuesta.";
        String descMalba = "El Museo de Arte Latinoamericano de Buenos Aires (MALBA) se prepara para inaugurar su principal exhibición del año. La muestra, titulada 'Visiones Continentales', reunirá a más de 50 artistas modernos de toda la región y explorará las identidades compartidas. La inauguración oficial será el próximo jueves por la tarde.";
        String descMalbaFoto = "Registro fotográfico de la fachada principal del MALBA, ubicado en el barrio de Palermo, preparándose para la nueva exhibición 'Visiones Continentales'.";
        String descCorte9Julio = "Fuentes gremiales confirmaron que el corte en la 9 de Julio fue organizado por sindicatos del sector de transporte, en reclamo de mejoras salariales y condiciones laborales. La medida de fuerza se decidió en asamblea esta mañana y se mantendrá, según los voceros, 'por tiempo indeterminado' o hasta obtener una respuesta favorable del ministerio.";
        String descFotoCorte = "Imagen tomada desde un edificio aledaño que muestra la magnitud del corte sobre la Avenida 9 de Julio, con manifestantes y pancartas bloqueando ambos sentidos de la traza principal.";


        HECHOS = new ArrayList<>(List.of(
                new HechoTextual(
                        "Manifestación en el Obelisco",
                        descObelisco, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Tránsito"),
                        new Ubicacion(-34.6037, -58.3816), // Obelisco
                        LocalDateTime.now().minusDays(1),
                        List.of(new Etiqueta("Tránsito"), new Etiqueta("Centro")),
                        contribuyente2,
                        descObelisco // 8vo campo (contenido)
                ),
                new HechoMultimedia(
                        "Nuevo mural de arte urbano en Caminito",
                        descCaminito, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Arte"),
                        new Ubicacion(-34.6383, -58.3606), // Caminito, La Boca
                        LocalDateTime.now().minusHours(5),
                        List.of(new Etiqueta("La Boca"), new Etiqueta("Arte Urbano")),
                        contribuyente1,
                        List.of("https://i.pinimg.com/originals/25/0a/7d/250a7dba76142d939684273b4d32ff86.jpg", "https://i.pinimg.com/564x/20/51/dc/2051dc9a1cfec7d894251cd1787a58d8.jpg")
                ),
                new HechoTextual(
                        "Feria de antigüedades en Plaza Dorrego",
                        descSanTelmo, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Cultura"),
                        new Ubicacion(-34.6251, -58.3715), // Plaza Dorrego, San Telmo
                        LocalDateTime.now().minusDays(3),
                        List.of(new Etiqueta("San Telmo"), new Etiqueta("Feria")),
                        contribuyente3,
                        descSanTelmo // 8vo campo (contenido)
                ),
                new HechoMultimedia(
                        "Árbol caído en Cementerio de Recoleta",
                        descRecoleta, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Incidente"),
                        new Ubicacion(-34.5880, -58.3916), // Cementerio de Recoleta
                        LocalDateTime.now().minusHours(2),
                        List.of(new Etiqueta("Recoleta"), new Etiqueta("Clima")),
                        Anonimo.getInstance(),
                        List.of("https://images.adsttc.com/media/images/582b/4896/e58e/cef9/f300/01ea/newsletter/Andrew_Currie.jpg?1479231631")
                ),
                new HechoTextual(
                        "Ruido excesivo en bar de Palermo Soho",
                        descPalermo, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Testimonios"),
                        new Ubicacion(-34.5888, -58.4282), // Plaza Serrano, Palermo Soho
                        LocalDateTime.now().minusMinutes(30),
                        List.of(new Etiqueta("Palermo"), new Etiqueta("Queja")),
                        contribuyente1,
                        descPalermo // 8vo campo (contenido)
                ),
                new HechoMultimedia(
                        "Acto conmemorativo en Plaza de Mayo",
                        descPlazaMayo, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Eventos"),
                        new Ubicacion(-34.6083, -58.3712), // Plaza de Mayo
                        LocalDateTime.now().minusDays(1),
                        List.of(new Etiqueta("Plaza de Mayo"), new Etiqueta("Política")),
                        contribuyente2,
                        List.of("https://indiehoy.com/wp-content/uploads/2016/08/plaza-de-mayo.jpg")
                ),
                new HechoTextual(
                        "Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones",
                        descMisiones, // 2do campo (descripcion) ACTUALIZADO
                        new Categoria("Ráfagas de más de 100 km/h"),
                        new Ubicacion(-27.029465, -54.436559),
                        LocalDateTime.of(2007, 12, 21, 0, 0),
                        List.of(),
                        contribuyente3,
                        descMisiones // 8vo campo (contenido)
                )));

        COLECCION_A = new Coleccion(
                "A123", // Colección sobre Arte y Cultura
                List.of(
                        new HechoTextual(
                                "Nueva exhibición en el MALBA",
                                descMalba, // 2do campo (descripcion) ACTUALIZADO
                                new Categoria("Cultura"),
                                new Ubicacion(-34.5770, -58.4022), // MALBA
                                LocalDateTime.now().minusDays(7),
                                List.of(new Etiqueta("Museo"), new Etiqueta("Arte")),
                                Anonimo.getInstance(),
                                descMalba // 8vo campo (contenido)
                        ),
                        new HechoMultimedia(
                                "Foto de la obra principal del MALBA",
                                descMalbaFoto, // 2do campo (descripcion) ACTUALIZADO
                                new Categoria("Multimedia"),
                                new Ubicacion(-34.5770, -58.4022), // MALBA
                                LocalDateTime.now().minusDays(6),
                                List.of(new Etiqueta("MALBA"), new Etiqueta("Foto")),
                                Anonimo.getInstance(),
                                List.of("https://ba-h.com.ar/wp-content/uploads/2018/10/arti-museo-malba-sm-desta.jpg")
                        )
                )
        );
        COLECCION_B = new Coleccion(
                "B456", // Colección sobre Manifestación en Obelisco
                List.of(
                        new HechoTextual(
                                "Detalles del corte en 9 de Julio",
                                descCorte9Julio, // 2do campo (descripcion) ACTUALIZADO
                                new Categoria("Tránsito"),
                                new Ubicacion(-34.6037, -58.3816), // Obelisco
                                LocalDateTime.now().minusDays(1).plusHours(1),
                                List.of(new Etiqueta("Tránsito")),
                                contribuyente1,
                                descCorte9Julio // 8vo campo (contenido)
                        ),
                        new HechoMultimedia(
                                "Foto de la manifestación",
                                descFotoCorte, // 2do campo (descripcion) ACTUALIZADO
                                new Categoria("Pruebas"),
                                new Ubicacion(-34.6037, -58.3816), // Obelisco
                                LocalDateTime.now().minusDays(1).plusHours(2),
                                List.of(new Etiqueta("Foto")),
                                new Registrado(5, "Lucas Diaz"), // Otro contribuyente
                                List.of("https://imagenes.elpais.com/resizer/v2/GNWUKMSUQVHSTJBCE3PKUIJ7WI.jpg?auth=f8b615f23f92054da99651e16ee061149cad87d1c217080e9fb9ab0b714b0ea7&width=414")
                        )
                )

        );
        COLECCIONES = List.of(COLECCION_A, COLECCION_B);
    }

    public List<Hecho> getHechos(String fecha_acontecimiento_hasta,
                                 String latitud,
                                 String longitud,
                                 String fecha_acontecimiento_desde,
                                 String fecha_reporte_hasta,
                                 String fecha_reporte_desde,
                                 String categoria,
                                 String ultimaConsulta) {


        return HECHOS.stream()
                .filter(hecho -> categoria == null ||
                        (hecho.getCategoria() != null && categoria.equalsIgnoreCase(hecho.getCategoria().getDetalle())))
                .filter(hecho -> {
                    if (fecha_reporte_desde == null && fecha_reporte_hasta == null) return true;
                    if (hecho.getFechaCarga() == null) return false;
                    boolean desde = fecha_reporte_desde == null ||
                            !hecho.getFechaCarga().isBefore(LocalDateTime.parse(fecha_reporte_desde));
                    boolean hasta = fecha_reporte_hasta == null ||
                            !hecho.getFechaCarga().isAfter(LocalDateTime.parse(fecha_reporte_hasta));
                    return desde && hasta;
                })
                .filter(hecho -> {
                    if (fecha_acontecimiento_desde == null && fecha_acontecimiento_hasta == null) return true;
                    if (hecho.getFechaAcontecimiento() == null) return false;
                    boolean desde = fecha_acontecimiento_desde == null ||
                            !hecho.getFechaAcontecimiento().isBefore(LocalDateTime.parse(fecha_acontecimiento_desde));
                    boolean hasta = fecha_acontecimiento_hasta == null ||
                            !hecho.getFechaAcontecimiento().isAfter(LocalDateTime.parse(fecha_acontecimiento_hasta));
                    return desde && hasta;
                })
                .filter(hecho -> {
                    if (latitud == null && longitud == null) return true;
                    if (hecho.getUbicacion() == null) return false;
                    // Corrección: Convertir Double a String para comparar, o mejor, comparar Doubles.
                    // Asumiendo que latitud y longitud son Strings, la comparación debe ser con los valores de Ubicacion convertidos a String
                    // O, si latitud/longitud son parte de un query, deberían ser parseados a Double.
                    // Voy a asumir que la lógica original de comparar Strings es intencional, aunque es frágil.
                    // Una mejor lógica sería parsear latitud/longitud a Double y comparar con un rango.
                    // Por ahora, mantengo la lógica original de comparación de Strings.
                    boolean latOk = latitud == null || hecho.getUbicacion().getLatitud().toString().equals(latitud);
                    boolean lonOk = longitud == null || hecho.getUbicacion().getLongitud().toString().equals(longitud);
                    return latOk && lonOk;
                })
                .filter(hecho -> {
                    if (ultimaConsulta == null) return true;
                    return hecho.getFechaCarga().isAfter(LocalDateTime.parse(ultimaConsulta));
                })
                .collect(Collectors.toList());
    }

    public Hecho getHechoPorId(@PathVariable Integer id) {
        return HECHOS.stream()
                .filter(h -> id.equals(h.getId()))
                .findFirst()
                .orElse(null);
    }


    public List<Hecho> getHechosPorColeccion(@PathVariable String id) {
        return COLECCIONES.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(Coleccion::getHechos)
                .orElse(List.of());
    }

}