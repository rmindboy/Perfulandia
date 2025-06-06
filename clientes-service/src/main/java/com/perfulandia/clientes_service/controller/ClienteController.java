package com.perfulandia.clientes_service.controller;

import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.repository.ClienteRepository;
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
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@RequestBody Cliente cliente) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}