package com.perfulandia.logistica_service.controller;

import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioRepository envioRepository;

    public EnvioController(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Envio>> listarTodos() {
        List<EntityModel<Envio>> envios = envioRepository.findAll().stream()
            .map(envio -> EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).obtenerPorId(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).actualizar(envio.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(EnvioController.class).eliminar(envio.getId())).withRel("eliminar")
            ))
            .collect(Collectors.toList());

        return CollectionModel.of(envios,
            linkTo(methodOn(EnvioController.class).listarTodos()).withSelfRel(),
            linkTo(methodOn(EnvioController.class).crear(null)).withRel("crear"));
    }

    @GetMapping("/{id}")
    public EntityModel<Envio> obtenerPorId(@PathVariable Long id) {
        Envio envio = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        return EntityModel.of(envio,
            linkTo(methodOn(EnvioController.class).obtenerPorId(id)).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-envios"),
            linkTo(methodOn(EnvioController.class).buscarPorEstado(envio.getEstado())).withRel("envios-por-estado"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Envio>> crear(@RequestBody Envio envio) {
        Envio envioGuardado = envioRepository.save(envio);
        EntityModel<Envio> resource = EntityModel.of(envioGuardado,
            linkTo(methodOn(EnvioController.class).obtenerPorId(envioGuardado.getId())).withSelfRel());

        return ResponseEntity
            .created(resource.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> actualizar(@PathVariable Long id, @RequestBody Envio envio) {
        Envio envioActualizado = envioRepository.findById(id)
            .map(e -> {
                e.setOrigen(envio.getOrigen());
                e.setDestino(envio.getDestino());
                e.setEstado(envio.getEstado());
                return envioRepository.save(e);
            })
            .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        EntityModel<Envio> resource = EntityModel.of(envioActualizado,
            linkTo(methodOn(EnvioController.class).obtenerPorId(id)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        envioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado")
    public CollectionModel<EntityModel<Envio>> buscarPorEstado(@RequestParam String estado) {
        List<EntityModel<Envio>> envios = envioRepository.findByEstado(estado).stream()
            .map(envio -> EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).obtenerPorId(envio.getId())).withSelfRel())
            )
            .collect(Collectors.toList());

        return CollectionModel.of(envios,
            linkTo(methodOn(EnvioController.class).buscarPorEstado(estado)).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("todos-envios"));
    }
}