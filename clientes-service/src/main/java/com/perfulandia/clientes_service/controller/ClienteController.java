package com.perfulandia.clientes_service.controller;

import com.perfulandia.clientes_service.DTO.ActualizarContrasenaRequest;
import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.service.ClienteService;

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

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);

        EntityModel<Cliente> resource = EntityModel.of(nuevoCliente);
        resource.add(linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).withSelfRel());

        URI location = linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public CollectionModel<EntityModel<Cliente>> listarClientes() {
        List<EntityModel<Cliente>> clientes = clienteService.listarTodos().stream()
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerCliente(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        Cliente actualizado = clienteService.actualizarCliente(id, clienteActualizado);

        EntityModel<Cliente> resource = EntityModel.of(actualizado,
                linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));

        return ResponseEntity.ok(resource);
    }

    @PatchMapping("/{id}/contrasena")
    public ResponseEntity<EntityModel<Cliente>> actualizarContrasena(
            @PathVariable Long id,
            @RequestBody @Valid ActualizarContrasenaRequest request) {
        try {
            Cliente actualizado = clienteService.actualizarContrasena(id, request);

            EntityModel<Cliente> resource = EntityModel.of(actualizado,
                    linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                    linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));

            return ResponseEntity.ok(resource);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // contraseña incorrecta
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // cliente no encontrado
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
