package com.perfulandia.ventas_service.controller;

import com.perfulandia.ventas_service.model.DetalleVenta;
import com.perfulandia.ventas_service.model.Venta;
import com.perfulandia.ventas_service.repository.VentaRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaRepository ventaRepository;

    public VentaController(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venta>> crearVenta(@RequestBody Venta venta) {
        venta.setFecha(LocalDateTime.now());
        venta.setEstado("PROCESANDO");
        
        // Calcular total
        double total = venta.getDetalles().stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();
        venta.setTotal(total);
        
        Venta nuevaVenta = ventaRepository.save(venta);

        EntityModel<Venta> resource = EntityModel.of(nuevaVenta);
        resource.add(linkTo(methodOn(VentaController.class).obtenerVenta(nuevaVenta.getId())).withSelfRel());

        URI location = linkTo(methodOn(VentaController.class).obtenerVenta(nuevaVenta.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    public EntityModel<Venta> obtenerVenta(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        return EntityModel.of(venta,
                linkTo(methodOn(VentaController.class).obtenerVenta(id)).withSelfRel(),
                linkTo(methodOn(VentaController.class).listarVentas()).withRel("todas-las-ventas"));
    }

    @GetMapping
    public CollectionModel<EntityModel<Venta>> listarVentas() {
        List<EntityModel<Venta>> ventas = ventaRepository.findAll().stream()
                .map(venta -> EntityModel.of(venta,
                        linkTo(methodOn(VentaController.class).obtenerVenta(venta.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).listarVentas()).withSelfRel());
    }

    @GetMapping("/cliente/{clienteId}")
    public CollectionModel<EntityModel<Venta>> ventasPorCliente(@PathVariable Long clienteId) {
        List<EntityModel<Venta>> ventas = ventaRepository.findByClienteId(clienteId).stream()
                .map(venta -> EntityModel.of(venta,
                        linkTo(methodOn(VentaController.class).obtenerVenta(venta.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).ventasPorCliente(clienteId)).withSelfRel());
    }

    @PutMapping("/{id}/completar")
    public EntityModel<Venta> completarVenta(@PathVariable Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        venta.setEstado("COMPLETADA");
        Venta actualizada = ventaRepository.save(venta);
        
        return EntityModel.of(actualizada,
                linkTo(methodOn(VentaController.class).obtenerVenta(id)).withSelfRel());
    }
}