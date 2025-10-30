package ar.edu.utn.frba.ddsi.dinamica;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.*;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Anonimo;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Registrado;
import ar.edu.utn.frba.ddsi.dinamica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.dinamica.models.repositories.SolicitudesRepository;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Random;

@SuppressWarnings("removal")
@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HechosRepository hechosRepository;

    @MockBean
    private SolicitudesRepository solicitudesRepository;



//    @Test
//    void debeRetornarHechos() throws Exception {
//        HechoTextual hecho1 = new HechoTextual(
//                "Noticia importante",
//                "Descripción de la noticia",
//                new Categoria("Accidente"),
//                new Ubicacion(34.6037, -58.3816),
//                LocalDateTime.now().minusDays(1),
//                List.of(new Etiqueta("urgente"), new Etiqueta("tránsito")),
//                new Registrado(1, "Juan Pérez"),
//                "Contenido completo de la noticia con todos los detalles del accidente..."
//
//        );
//
//        HechoMultimedia hecho2 = new HechoMultimedia(
//                "Evento cultural",
//                "Descripción del evento",
//                new Categoria("Cultura"),
//                new Ubicacion(34.6037, -58.3816),
//                LocalDateTime.now().minusDays(2),
//                List.of(new Etiqueta("arte"), new Etiqueta("música")),
//                Anonimo.getInstance(),
//                List.of("https://example.com/image1.jpg", "https://example.com/video1.mp4")
//        );
//
//        Mockito.when(hechosRepository.findAll())
//                .thenReturn(List.of(hecho1, hecho2));
//
//        mockMvc.perform(get("/api/dinamica/hechos"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].titulo").value("Noticia importante"))
//                .andExpect(jsonPath("$[1].titulo").value("Evento cultural"));
//
//    }
//
//    @Test
//    void debeCrearHecho() throws Exception {
//
//        HechoDTO hechoDTO = new HechoDTO();
//        hechoDTO.setTitulo("Nuevo hecho");
//        hechoDTO.setDescripcion("Descripción del nuevo hecho");
//        hechoDTO.setCategoria(new Categoria("Accidente"));
//        hechoDTO.setUbicacion(new Ubicacion(34.6037, -58.3816));
//        hechoDTO.setFechaAcontecimiento(LocalDateTime.now());
//        hechoDTO.setEtiquetas(List.of(new Etiqueta("urgente"), new Etiqueta("tránsito")));
//        hechoDTO.setTipo("textual");
//        hechoDTO.setCuerpo("Cuerpo del hecho de prueba");
//
//
//
//        mockMvc.perform(post("/api/dinamica/hechos")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(new ObjectMapper()
//                    .registerModule(new JavaTimeModule())
//                    .writeValueAsString(hechoDTO)))
//                    .andExpect(status().isCreated())
//                    .andReturn().getResponse().getContentAsString();
//
//        Mockito.verify(hechosRepository, Mockito.times(1)).save(Mockito.any(Hecho.class));
//
//
//    }
//
//    @Test
//    void debeActualizarHecho() throws Exception {
//
//        HechoDTO nuevoHechoDTO = new HechoDTO();
//        nuevoHechoDTO.setTitulo("Hecho actualizado");
//        nuevoHechoDTO.setDescripcion("Descripción del hecho actualizado");
//        nuevoHechoDTO.setCategoria(new Categoria("Actualización"));
//        nuevoHechoDTO.setUbicacion(new Ubicacion(34.6037, -58.3816));
//        nuevoHechoDTO.setFechaAcontecimiento(LocalDateTime.now());
//        nuevoHechoDTO.setEtiquetas(List.of(new Etiqueta("actualizado"), new Etiqueta("nuevo")));
//        nuevoHechoDTO.setTipo("textual");
//        nuevoHechoDTO.setCuerpo("Cuerpo del hecho actualizado");
//
//        HechoTextual hechoOriginal = new HechoTextual(
//                "Noticia importante",
//                "Descripción de la noticia",
//                new Categoria("Accidente"),
//                new Ubicacion(34.6037, -58.3816),
//                LocalDateTime.now().minusDays(1),
//                List.of(new Etiqueta("urgente"), new Etiqueta("tránsito")),
//                new Registrado(1, "Juan Pérez"),
//                "Contenido completo de la noticia con todos los detalles del accidente..."
//        );
//
//        Integer idHechoAModificar = hechoOriginal.getId();
//
//        HechoTextual hechoActualizado = new HechoTextual(
//                nuevoHechoDTO.getTitulo(),
//                nuevoHechoDTO.getDescripcion(),
//                nuevoHechoDTO.getCategoria(),
//                nuevoHechoDTO.getUbicacion(),
//                nuevoHechoDTO.getFechaAcontecimiento(),
//                nuevoHechoDTO.getEtiquetas(),
//                hechoOriginal.getContribuyente(), // Mantener el contribuyente original
//                nuevoHechoDTO.getCuerpo()
//        );
//        hechoActualizado.setId(idHechoAModificar);
//
//
//        Mockito.when(hechosRepository.findById(idHechoAModificar))
//                .thenReturn(hechoOriginal);
//
//        Mockito.when(hechosRepository.findByIdAndUpdate(Mockito.eq(idHechoAModificar), Mockito.any()))
//                .thenReturn(hechoActualizado);
//
//        mockMvc.perform(put("/api/dinamica/hechos/{id}", idHechoAModificar)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .writeValueAsString(nuevoHechoDTO)))
//                .andExpect(status().isOk())
//                // Verificar que el ID es el mismo
//                .andExpect(jsonPath("$.id").value(idHechoAModificar.toString()))
//                // Verificar que los campos se actualizaron correctamente
//                .andExpect(jsonPath("$.titulo").value(nuevoHechoDTO.getTitulo()))
//                .andExpect(jsonPath("$.descripcion").value(nuevoHechoDTO.getDescripcion()))
//                .andExpect(jsonPath("$.categoria.detalle").value(nuevoHechoDTO.getCategoria().getDetalle()))
//                .andExpect(jsonPath("$.etiquetas[0].descripcion").value(nuevoHechoDTO.getEtiquetas().get(0).getDescripcion()))
//                .andExpect(jsonPath("$.etiquetas[1].descripcion").value(nuevoHechoDTO.getEtiquetas().get(1).getDescripcion()));
//
//        // Verificar que se llamaron los métodos del repositorio
//        Mockito.verify(hechosRepository).findById(idHechoAModificar);
//        Mockito.verify(hechosRepository).findByIdAndUpdate(Mockito.eq(idHechoAModificar), Mockito.any());
//
//    }
//
//    @Test
//    void debeRetornarSolicitudes() throws Exception {
//        int idHechoRandom = 1;
//
//        SolicitudEliminacion solicitud1 = new SolicitudEliminacion(
//                idHechoRandom,
//                "Solicitud de eliminación 1"
//        );
//
//        SolicitudEliminacion solicitud2 = new SolicitudEliminacion(
//                idHechoRandom,
//                "Solicitud de eliminación 2"
//        );
//
//        Mockito.when(solicitudesRepository.findAll())
//                .thenReturn(List.of(solicitud1, solicitud2));
//
//        mockMvc.perform(get("/api/dinamica/solicitudes"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].justificacion").value("Solicitud de eliminación 1"))
//                .andExpect(jsonPath("$[1].justificacion").value("Solicitud de eliminación 2"));
//    }
//
//    @Test
//    void debeRechazarSolicitudPorFaltaCaracteres() throws Exception {
//        Integer idHechoRandom = new Random().nextInt(1,10000);
//
//        SolicitudDTO solicitudDTO = new SolicitudDTO();
//        solicitudDTO.setIdHecho(idHechoRandom);
//        solicitudDTO.setJustificacion("Justificación de la solicitud");
//
//
//        mockMvc.perform(post("/api/dinamica/solicitudes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .writeValueAsString(solicitudDTO)))
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    void debeCrearSolicitud() throws Exception {
//        Integer idHechoRandom = new Random().nextInt(1,10000);
//
//        SolicitudDTO solicitudDTO = new SolicitudDTO();
//        solicitudDTO.setIdHecho(idHechoRandom);
//        solicitudDTO.setJustificacion("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//
//        Mockito.when(hechosRepository.findById(idHechoRandom))
//                .thenReturn(new HechoTextual(
//                        "Hecho de prueba",
//                        "Descripción del hecho de prueba",
//                        new Categoria("Prueba"),
//                        new Ubicacion(34.6037, -58.3816),
//                        LocalDateTime.now(),
//                        List.of(new Etiqueta("prueba")),
//                        Anonimo.getInstance(),
//                        "Cuerpo del hecho de prueba")
//
//                );
//
//        mockMvc.perform(post("/api/dinamica/solicitudes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .writeValueAsString(solicitudDTO)))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//
//        Mockito.verify(solicitudesRepository, Mockito.times(1)).save(Mockito.any(SolicitudEliminacion.class));
//
//    }
//
//    @Test
//    void debeActualizarSolicitud() throws Exception {
//        Integer idSolicitud = new Random().nextInt(1,10000);
//        Estado_Solicitud nuevoEstado = Estado_Solicitud.RECHAZADA;
//
//        SolicitudEliminacion solicitudOriginal = new SolicitudEliminacion(
//                idSolicitud,
//                "Justificación original"
//        );
//
//        Mockito.when(solicitudesRepository.findById(idSolicitud))
//                .thenReturn(solicitudOriginal);
//
//        solicitudOriginal.setEstado(nuevoEstado);
//
//
//        Mockito.when(solicitudesRepository.findByIdAndUpdate(Mockito.eq(idSolicitud), Mockito.any()))
//                .thenReturn(solicitudOriginal);
//
//        mockMvc.perform(put("/api/dinamica/solicitudes/{id}", idSolicitud)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper()
//                                .registerModule(new JavaTimeModule())
//                                .writeValueAsString(nuevoEstado)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.estado").value(nuevoEstado.toString()));
//
//        Mockito.verify(solicitudesRepository).findById(idSolicitud);
//        Mockito.verify(solicitudesRepository).findByIdAndUpdate(Mockito.eq(idSolicitud), Mockito.any());
//    }


}