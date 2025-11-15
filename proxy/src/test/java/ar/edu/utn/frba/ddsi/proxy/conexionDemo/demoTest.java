package ar.edu.utn.frba.ddsi.proxy.conexionDemo;

import static org.mockito.Mockito.*;

import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Categoria;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Origen_Fuente;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.proxy.service.HechosServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class demoTest {
//    @Test
//    public void obtenerHechosDevuelveHechos() {
//        HechosRepository hechosRepository = new HechosRepository();
//        String nombreConexion = "mockConexion";
//        List<Hecho> hechosEsperados = Arrays.asList(new Hecho(
//                        "Incidente de tráfico",
//                        "Colisión leve en Av. Siempre Viva",
//                        new Categoria("Tránsito"),
//                        new Ubicacion(-34.6037, -58.3816),
//                        LocalDate.now().minusDays(2),
//                        LocalDateTime.now().minusHours(5),
//                        Origen_Fuente.ESTATICA
//                ),
//                new Hecho(
//                        "Corte de luz",
//                        "Interrupción del servicio eléctrico en barrio Norte",
//                        new Categoria("Servicios"),
//                        new Ubicacion(-34.5950, -58.3700),
//                        LocalDate.now().minusDays(1),
//                        LocalDateTime.now().minusHours(3),
//                        Origen_Fuente.DINAMICA
//                ),
//                new Hecho(
//                        "Evento deportivo",
//                        "Maratón anual de la ciudad",
//                        new Categoria("Deportes"),
//                        new Ubicacion(-34.6090, -58.3920),
//                        LocalDate.now().minusDays(7),
//                        LocalDateTime.now().minusHours(10),
//                        Origen_Fuente.INTERMEDIARIA
//                ),
//                new Hecho(
//                        "Manifestación",
//                        "Concentración en Plaza de Mayo",
//                        new Categoria("Social"),
//                        new Ubicacion(-34.6083, -58.3712),
//                        LocalDate.now().minusDays(3),
//                        LocalDateTime.now().minusHours(8),
//                        Origen_Fuente.DINAMICA
//                ),
//                new Hecho(
//                        "Obra pública",
//                        "Reparación de calzada en Av. Corrientes",
//                        new Categoria("Obras"),
//                        new Ubicacion(-34.6030, -58.3840),
//                        LocalDate.now().minusDays(5),
//                        LocalDateTime.now().minusHours(12),
//                        Origen_Fuente.ESTATICA
//                ));
//        hechosRepository.registrarHechos(nombreConexion, hechosEsperados);
//        HechosServices hechosService = new HechosServices(hechosRepository);
//
//        List<Hecho> hechosObtenidos = hechosService.obtenerHechos(nombreConexion);
//
//        Assertions.assertEquals(hechosEsperados, hechosObtenidos);
//    }
//
//    @Test
//    void actualizarHechosPeriodicamente() {
//        HechosRepository mockRepo = mock(HechosRepository.class);
//        HechosServices hechosService = new HechosServices(mockRepo);
//
//        hechosService.actualizarHechosPeriodicamente();
//
//        verify(mockRepo, atLeastOnce()).obtenerHechos();
//    }
}
