package ar.edu.utn.frba.ddsi.dinamica.controllers;

import ar.edu.utn.frba.ddsi.dinamica.services.DinamicaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DinamicaController.class)
class HechoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DinamicaService service;


    @Test
    void testObtenerHechos() throws Exception {
        mockMvc.perform(get("/api/dinamica/hechos"))
                .andExpect(status().isOk());
    }
}