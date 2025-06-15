package com.perfulandia.clientes_service.controller;

import com.perfulandia.clientes_service.DTO.ActualizarContrasenaRequest;
import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Clientes", description = "Operaciones relacionadas con la gestión de clientes en Perfulandia")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
        summary = "Crear nuevo cliente",
        description = "Registra un nuevo cliente en el sistema de Perfulandia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Cliente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Cliente.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o incompletos"
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del cliente a crear",
        required = true,
        content = @Content(
            schema = @Schema(implementation = Cliente.class),
            examples = @ExampleObject(
                name = "EjemploCliente",
                value = """
                {
                "nombre": "Juan Pérez",
                "email": "juan@example.com",
                "password": "123456789",
                "telefono": "+56912345678",
                "direccionesEnvio": ["Calle Falsa 123"]
                }
                """
            )
        )
    )
    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crearCliente(cliente);

        EntityModel<Cliente> resource = EntityModel.of(nuevoCliente);
        resource.add(linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).withSelfRel());

        URI location = linkTo(methodOn(ClienteController.class).obtenerCliente(nuevoCliente.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @Operation(
    summary = "Obtener cliente por ID",
    description = "Busca un cliente específico en Perfulandia según su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Cliente.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente no encontrado con el ID proporcionado"
        )
    })
    @Parameter(
        name = "id",
        description = "ID del cliente que se desea consultar",
        required = true,
        example = "1"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
    summary = "Listar todos los clientes",
    description = "Recupera una lista con todos los clientes registrados en el sistema de Perfulandia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado de clientes obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Cliente.class))
            )
        )
    })
    @GetMapping
    public CollectionModel<EntityModel<Cliente>> listarClientes() {
        List<EntityModel<Cliente>> clientes = clienteService.listarTodos().stream()
                .map(cliente -> EntityModel.of(cliente,
                        linkTo(methodOn(ClienteController.class).obtenerCliente(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel());
    }

    @Operation(
    summary = "Actualizar cliente existente",
    description = "Modifica los datos de un cliente identificado por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente actualizado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Cliente.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @Parameter(
        name = "id",
        description = "ID del cliente a actualizar",
        required = true,
        example = "1"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos actualizados del cliente",
        required = true,
        content = @Content(
            schema = @Schema(implementation = Cliente.class),
            examples = @ExampleObject(
                name = "EjemploClienteActualizado",
                value = """
                {
                "nombre": "Ana Gómez",
                "email": "ana.gomez@example.com",
                "password": "654321",
                "telefono": "+56998765432",
                "direccionesEnvio": ["Nueva Dirección 456"]
                }
                """
            )
        )
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        Cliente actualizado = clienteService.actualizarCliente(id, clienteActualizado);

        EntityModel<Cliente> resource = EntityModel.of(actualizado,
                linkTo(methodOn(ClienteController.class).obtenerCliente(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listarClientes()).withRel("todos-los-clientes"));

        return ResponseEntity.ok(resource);
    }

    @Operation(
    summary = "Actualizar contraseña de un cliente",
    description = "Permite modificar la contraseña de un cliente si se proporciona la contraseña actual correctamente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Contraseña actualizada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Cliente.class)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Contraseña actual incorrecta"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @Parameter(
        name = "id",
        description = "ID del cliente cuya contraseña se desea actualizar",
        required = true,
        example = "1"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Cuerpo con la contraseña actual y la nueva",
        required = true,
        content = @Content(
            schema = @Schema(implementation = ActualizarContrasenaRequest.class),
            examples = @ExampleObject(
                name = "EjemploActualizarContrasena",
                value = """
                {
                "contrasenaActual": "123456",
                "nuevaContrasena": "nuevaSegura2024"
                }
                """
            )
        )
    )
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

    @Operation(
    summary = "Eliminar cliente por ID",
    description = "Elimina un cliente existente en el sistema de Perfulandia usando su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Cliente eliminado exitosamente (sin contenido)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente no encontrado con el ID proporcionado"
        )
    })
    @Parameter(
        name = "id",
        description = "ID del cliente que se desea eliminar",
        required = true,
        example = "1"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
