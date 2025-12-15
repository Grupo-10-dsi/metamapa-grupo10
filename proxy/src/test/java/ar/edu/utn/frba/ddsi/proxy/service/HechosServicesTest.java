package ar.edu.utn.frba.ddsi.proxy.service;

import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.metaMapa.MetaMapaClient;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.models.repositories.HechosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HechosServicesTest {

    @Mock
    private HechosRepository hechosRepository;

    @InjectMocks
    private HechosServices hechosServices;

    @BeforeEach
    void setup() {
        // Evita NPE por instanciasMetaMapa vacía
        // Simula PostConstruct inicializando una instancia dummy
        try {
            var field = HechosServices.class.getDeclaredField("instanciasMetaMapa");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<MetaMapaClient> lista = (List<MetaMapaClient>) field.get(null);
            if (lista != null) lista.clear();
        } catch (Exception ignored) { }
    }

    @Test
    void findAllHechos_SinUltimaConsulta_CombineLocalYMetaMapa() {
        // Arrange
        Hecho h1 = mock(Hecho.class);
        when(hechosRepository.findAll()).thenReturn(List.of(h1));

        // MetaMapa vacío por defecto
        // Act
        List<Hecho> res = hechosServices.findAllHechos(null);

        // Assert
        assertNotNull(res);
        assertEquals(1, res.size());
        verify(hechosRepository, times(1)).findAll();
    }

    @Test
    void obtenerHechos_CuandoNoHayHechos_DeberiaLanzar404() {
        // Arrange
        when(hechosRepository.findByName("demo")).thenReturn(Collections.emptyList());

        // Act & Assert
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> hechosServices.obtenerHechos("demo"));
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("No se encontraron hechos"));
    }

    @Test
    void obtenerHechos_CuandoExisteContenido_DeberiaRetornarLista() {
        // Arrange
        Hecho h = mock(Hecho.class);
        when(hechosRepository.findByName("demo")).thenReturn(List.of(h));

        // Act
        List<Hecho> res = hechosServices.obtenerHechos("demo");

        // Assert
        assertEquals(1, res.size());
        verify(hechosRepository, times(1)).findByName("demo");
    }

    @Test
    void obtenerHechosMetaMapa_ConInstanciaSimulada_DeberiaUnirResultados() throws Exception {
        // Arrange
        // Inyectar instancia simulada de MetaMapaClient en el campo estatico
        var field = HechosServices.class.getDeclaredField("instanciasMetaMapa");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<MetaMapaClient> lista = (List<MetaMapaClient>) field.get(null);
        MetaMapaClient cliente = mock(MetaMapaClient.class);
        when(cliente.obtenerHechos(any(FiltroRequest.class))).thenReturn(Collections.emptyList());
        lista.add(cliente);

        // Act
        List<Hecho> res = hechosServices.obtenerHechosMetaMapa(new FiltroRequest(null, null, null, null, null, null, null, null));

        // Assert
        assertNotNull(res);
        verify(cliente, times(1)).obtenerHechos(any(FiltroRequest.class));
    }

    @Test
    void crearSolicitudDeEliminacion_UsaPrimeraInstancia_DeberiaDelegar() throws Exception {
        // Arrange
        var field = HechosServices.class.getDeclaredField("instanciasMetaMapa");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<MetaMapaClient> lista = (List<MetaMapaClient>) field.get(null);
        MetaMapaClient cliente = mock(MetaMapaClient.class);
        lista.add(cliente);

        UUID id = UUID.randomUUID();
        SolicitudEliminacion se = new SolicitudEliminacion(id, "justificacion".repeat(50));
        when(cliente.crearSolicitudDeEliminacion(eq(id), anyString())).thenReturn(se);

        // Act
        SolicitudEliminacion res = hechosServices.crearSolicitudDeEliminacion(id, "justificacion".repeat(50));

        // Assert
        assertNotNull(res);
        assertEquals(id, res.getIdHecho());
        verify(cliente, times(1)).crearSolicitudDeEliminacion(eq(id), anyString());
    }
}
