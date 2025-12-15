package ar.edu.utn.frba.ddsi.normalizador.services;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador.Normalizador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NormalizadorServiceTest {

    @Mock
    private Normalizador normalizador;

    @InjectMocks
    private NormalizadorService service;

    private HechoDTO hechoEntrada;
    private HechoDTO hechoSalida;

    @BeforeEach
    void setup() {
        hechoEntrada = new HechoDTO();
        hechoEntrada.setTitulo("crudo");
        hechoSalida = new HechoDTO();
        hechoSalida.setTitulo("normalizado");
    }

    @Test
    void normalizar_DeberiaDelegarEnNormalizador() {
        // Arrange
        when(normalizador.normalizar(any(HechoDTO.class))).thenReturn(hechoSalida);

        // Act
        HechoDTO resultado = service.normalizar(hechoEntrada);

        // Assert
        assertNotNull(resultado);
        assertEquals("normalizado", resultado.getTitulo());
        verify(normalizador, times(1)).normalizar(hechoEntrada);
    }
}
