package com.perfulandia.logistica_service;

import com.perfulandia.logistica_service.controller.EnvioController;
import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;
import com.perfulandia.logistica_service.service.EnvioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository; // crea el mock

    @InjectMocks
    private EnvioService envioService;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio();
        envio.setId(1L);
        envio.setOrigen("Santiago");
        envio.setDestino("Viña del Mar");
        envio.setEstado("en camino");
        envio.setFechaEnvio(java.time.LocalDate.parse("2025-06-05"));
    }

    @Test
    void listarTodos_conEnvios_devuelveTodosLosEnvios(){

        Envio otroEnvio = new Envio();
        otroEnvio.setId(2L);
        otroEnvio.setOrigen("Valparaíso");
        otroEnvio.setDestino("La Serena");
        otroEnvio.setEstado("pendiente");
        otroEnvio.setFechaEnvio(java.time.LocalDate.parse("2025-06-10"));

        List<Envio> enviosSimulados = List.of(envio,otroEnvio);

        when(envioRepository.findAll()).thenReturn(enviosSimulados);

        List<Envio> resultado = envioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(envio));
        assertEquals(envio.getEstado(), resultado.get(0).getEstado());


    }

    @Test
    void obtenerPorId_conId_devuelveEnvioencontrado(){

        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));

        Envio resultado = envioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getOrigen());

        verify(envioRepository, times(1)).findById(1L);

    } 

    @Test
    void crear_conEnvioBueno_devuelveEnvioCreado(){

        when(envioRepository.save(envio)).thenReturn(envio);

        Envio resultado = envioService.crear(envio);

        assertNotNull(envio);
        assertEquals("en camino", resultado.getEstado());
        verify(envioRepository, times(1)).save(envio);
        
    }

    




}
