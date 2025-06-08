package com.perfulandia.clientes_service.service;

import com.perfulandia.clientes_service.DTO.ActualizarContrasenaRequest;
import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.repository.ClienteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            if (!cliente.getEmail().equals(clienteActualizado.getEmail()) &&
                clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
                throw new IllegalArgumentException("El email ya está registrado por otro cliente");
            }

            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setEmail(clienteActualizado.getEmail());
            cliente.setPassword(clienteActualizado.getPassword());
            cliente.setDireccionesEnvio(clienteActualizado.getDireccionesEnvio());
            cliente.setTelefono(clienteActualizado.getTelefono());

            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente actualizarContrasena(Long id, ActualizarContrasenaRequest request) {
        return clienteRepository.findById(id).map(cliente -> {
            if (!cliente.getPassword().equals(request.getContrasenaActual())) {
                throw new SecurityException("Contraseña actual incorrecta");
            }

            cliente.setPassword(request.getNuevaContrasena());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}
