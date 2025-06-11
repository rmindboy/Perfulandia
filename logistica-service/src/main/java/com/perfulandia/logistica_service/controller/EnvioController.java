package com.perfulandia.logistica_service.controller;

import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;
import com.perfulandia.logistica_service.service.EnvioService;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public CollectionModel<EntityModel<Envio>> listarTodos() {
        List<EntityModel<Envio>> envios = envioService.listarTodos().stream()
            .map(envio -> EntityModel.of(envio,
            
            linkTo(methodOn(EnvioController.class).obtenerPorId(envio.getId())).withSelfRel(),            
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("Todos-los-productos")
                    ))
                    .collect(Collectors.toList());
                
                return CollectionModel.of(envios,
                
            linkTo(methodOn(EnvioController.class).listarTodos()).withSelfRel()
                    );
            }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> obtenerPorId(@PathVariable Long id) {
        Envio envio = envioService.obtenerPorId(id);
        EntityModel<Envio> envioModel = EntityModel.of(envio,
            linkTo(methodOn(EnvioController.class).obtenerPorId(id)).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-los-envios")
        );
        return ResponseEntity.ok(envioModel);
    }

    
    // Crear un nuevo envío
    @PostMapping
    public ResponseEntity<EntityModel<Envio>> crearEnvio(@RequestBody Envio envio) {
        Envio nuevoEnvio = envioService.crear(envio);
        EntityModel<Envio> envioModel = EntityModel.of(nuevoEnvio,
            linkTo(methodOn(EnvioController.class).obtenerPorId(nuevoEnvio.getId())).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-los-envios")
        );
        return ResponseEntity.created(
            linkTo(methodOn(EnvioController.class).obtenerPorId(nuevoEnvio.getId())).toUri()
        ).body(envioModel);
    }

    // Actualizar un envío existente
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> actualizarEnvio(@PathVariable Long id, @RequestBody Envio datosEnvio) {
        Envio envioActualizado = envioService.actualizar(id, datosEnvio);
        EntityModel<Envio> envioModel = EntityModel.of(envioActualizado,
            linkTo(methodOn(EnvioController.class).obtenerPorId(envioActualizado.getId())).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-los-envios")
        );
        return ResponseEntity.ok(envioModel);
    }

    // Eliminar un envío
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //se agrega este enpoint para dar mas claridad, pero lo correcto sería fusionar este con listar todos
    @GetMapping("/buscar")
    public CollectionModel<EntityModel<Envio>> buscarPorEstado(@RequestParam(required = false) String estado) {
        List<Envio> resultados;

        if (estado != null && !estado.isBlank()) {
            resultados = envioService.buscarPorEstado(estado);
        } else {
            resultados = envioService.listarTodos(); // si no se pasa parámetro, retorna todo
        }

        List<EntityModel<Envio>> envios = resultados.stream()
            .map(envio -> EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).obtenerPorId(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-los-envios")
            ))
            .collect(Collectors.toList());

        return CollectionModel.of(envios,
            linkTo(methodOn(EnvioController.class).listarTodos()).withSelfRel()
        );
    }

}