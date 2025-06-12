package ar.edu.utn.frba.ddsi.estatica;

import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.*;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Anonimo;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Registrado;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.HechosRepository;*/

import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import java.time.LocalDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("removal")
@SpringBootTest
@AutoConfigureMockMvc


class EstaticaApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HechosRepository hechosRepository;

    Hecho hecho1 = new Hecho (
            "Titulo 1",
            "Descripcion 1",
            new Categoria("Accidente"),
            new Ubicacion(34.6037, -58.3816),
            LocalDateTime.now().toLocalDate());

    Hecho hecho2 = new Hecho (
            "Titulo 2",
            "Descripcion 2",
            new Categoria("accidenteRaro"),
            new Ubicacion(34.60357, -58.5516),
            LocalDateTime.now().toLocalDate());

    @Test
    void devolverHechosMockeados() throws Exception {

        Hecho hecho1 = new Hecho (
                "all boys campeon",
                "Descripcion 1",
                new Categoria("Accidente"),
                new Ubicacion(34.6037, -58.3816),
                LocalDateTime.now().toLocalDate());

        Hecho hecho2 = new Hecho (
                "descendio atlanta",
                "Descripcion 2",
                new Categoria("accidenteRaro"),
                new Ubicacion(34.60357, -58.5516),
                LocalDateTime.now().toLocalDate());

        Mockito.when(hechosRepository.findAll()).thenReturn(List.of(hecho1, hecho2));

        mockMvc.perform(get("/api/estatica/hechos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("all boys campeon"))
                .andExpect(jsonPath("$[1].titulo").value("descendio atlanta"));
    }

}
