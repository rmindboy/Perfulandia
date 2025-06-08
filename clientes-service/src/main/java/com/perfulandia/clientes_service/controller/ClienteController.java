package com.perfulandia.clientes_service.controller;

import com.perfulandia.clientes_service.DTO.ActualizarContrasenaRequest;
import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.repository.ClienteRepository;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);

        EntityModel<Cliente> resource = EntityModel.of(nuevoCliente);
        resource.add(linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).withSelfRel());

        URI location = linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    public EntityModel<Cliente> obtenerCliente(@PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Cliente>> listarClientes() {
        List<EntityModel<Cliente>> clientes = clienteRepository.findAll().stream()
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerCliente(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    // Validación del email duplicado
                    if (!cliente.getEmail().equals(clienteActualizado.getEmail()) &&
                            clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
                        throw new IllegalArgumentException("El email ya está registrado");
                    }

                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setEmail(clienteActualizado.getEmail());
                    cliente.setPassword(clienteActualizado.getPassword());
                    cliente.setDireccionesEnvio(clienteActualizado.getDireccionesEnvio());
                    cliente.setTelefono(clienteActualizado.getTelefono());

                    Cliente actualizado = clienteRepository.save(cliente);

                    return EntityModel.of(actualizado,
                            linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/contrasena")
    public ResponseEntity<EntityModel<Cliente>> actualizarContrasena(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarContrasenaRequest request) {

        return clienteRepository.findById(id)
                .map(cliente -> {
                    // Verificar que la contraseña actual coincide
                    if (!cliente.getPassword().equals(request.getContrasenaActual())) {
                        return null; // Más abajo se maneja como 403
                    }

                    // Actualizar la contraseña
                    cliente.setPassword(request.getNuevaContrasena());
                    Cliente actualizado = clienteRepository.save(cliente);

                    EntityModel<Cliente> resource = EntityModel.of(actualizado,
                            linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));

                    return resource;
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).build()); // Contraseña incorrecta o cliente no encontrado
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}