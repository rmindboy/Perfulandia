package com.perfulandia.clientes_service;

import com.perfulandia.clientes_service.DTO.ActualizarContrasenaRequest;
import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.repository.ClienteRepository;
import com.perfulandia.clientes_service.service.ClienteService;

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

@ExtendWith(MockitoExtension.class) // Activa Mockito en esta clase
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository; // Simulamos el repositorio

    @InjectMocks
    private ClienteService clienteService; // Se inyecta el mock en el servicio real

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan@example.com");
        cliente.setPassword("123456");
        cliente.setDireccionesEnvio(List.of("Calle Falsa 123"));
        cliente.setTelefono("+56912345678");
    }

    @Test
    void crearCliente_deberiaGuardarClienteSiNoExisteEmail() {
        // Arrange
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        // Act
        Cliente creado = clienteService.crearCliente(cliente);

        // Assert
        assertNotNull(creado);
        assertEquals("Juan Pérez", creado.getNombre());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void crearCliente_deberiaLanzarExcepcionSiEmailYaExiste() {
        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> clienteService.crearCliente(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void buscarPorId_deberiaRetornarClienteSiExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
    }

    @Test
    void buscarPorId_deberiaRetornarVacioSiNoExiste() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(2L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void actualizarCliente_deberiaActualizarDatosCliente() {
        Cliente actualizado = new Cliente();
        actualizado.setNombre("Juan Actualizado");
        actualizado.setEmail("juan@example.com"); // mismo email
        actualizado.setPassword("nueva123");
        actualizado.setDireccionesEnvio(List.of("Calle Nueva 456"));
        actualizado.setTelefono("+56998765432");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.actualizarCliente(1L, actualizado);

        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarCliente_deberiaLanzarExcepcionSiNoExiste() {
        when(clienteRepository.findById(5L)).thenReturn(Optional.empty());

        Cliente nuevo = new Cliente();
        nuevo.setEmail("nuevo@example.com");

        assertThrows(RuntimeException.class, () -> clienteService.actualizarCliente(5L, nuevo));
    }

    @Test
    void eliminarCliente_deberiaEliminarClientePorId() {
        // Act
        clienteService.eliminarCliente(1L);

        // Assert
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void actualizarContrasena_deberiaActualizarCuandoLaActualEsCorrecta() {
        ActualizarContrasenaRequest request = new ActualizarContrasenaRequest();
        request.setContrasenaActual("123456");
        request.setNuevaContrasena("nueva123");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.actualizarContrasena(1L, request);

        assertEquals("nueva123", resultado.getPassword());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void actualizarContrasena_deberiaLanzarExcepcionSiContrasenaActualEsIncorrecta() {
        ActualizarContrasenaRequest request = new ActualizarContrasenaRequest();
        request.setContrasenaActual("incorrecta");
        request.setNuevaContrasena("nueva123");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        assertThrows(SecurityException.class, () -> {
            clienteService.actualizarContrasena(1L, request);
        });

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void actualizarContrasena_deberiaLanzarExcepcionSiClienteNoExiste() {
        ActualizarContrasenaRequest request = new ActualizarContrasenaRequest();
        request.setContrasenaActual("123456");
        request.setNuevaContrasena("nueva123");

        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clienteService.actualizarContrasena(2L, request);
        });

        verify(clienteRepository, never()).save(any());
    }


}