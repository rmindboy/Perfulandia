package com.perfulandia.logistica_service.controller;

import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnvioControllerTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioController envioController;

    private Envio envio;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        envio = new Envio(1L, "Origen", "Destino", "Estado", LocalDate.now());
    }

    @Test
    void cuandoListarTodos_entoncesRetornarLista() {
        when(envioRepository.findAll()).thenReturn(Arrays.asList(envio));
        
        CollectionModel<EntityModel<Envio>> resultado = envioController.listarTodos();
        
        assertEquals(1, resultado.getContent().size());
        verify(envioRepository, times(1)).findAll();
    }

    @Test
    void cuandoObtenerPorIdExistente_entoncesRetornarEnvio() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        
        EntityModel<Envio> resultado = envioController.obtenerPorId(1L);
        
        assertEquals(envio, resultado.getContent());
    }

    @Test
    void cuandoCrearEnvio_entoncesRetornarCreated() {
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        
        ResponseEntity<EntityModel<Envio>> respuesta = envioController.crear(envio);
        
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
    }
}