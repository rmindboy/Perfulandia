package com.perfulandia.inventario_services.controller;

import com.perfulandia.inventario_services.model.Producto;
import com.perfulandia.inventario_services.service.ProductoServicio;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/inventario")
public class ProductoControlador {
    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> obtenerTodosLosProductos() {
        List<EntityModel<Producto>> productos = productoServicio.obtenerTodos().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Producto> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoServicio.obtenerPorId(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }

        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos"),
                linkTo(methodOn(ProductoControlador.class).obtenerProductosPorSucursal(producto.getSucursal())).withRel("productos-sucursal"));
    }

    @GetMapping("/sucursal/{sucursal}")
    public CollectionModel<EntityModel<Producto>> obtenerProductosPorSucursal(@PathVariable String sucursal) {
        List<EntityModel<Producto>> productos = productoServicio.obtenerPorSucursal(sucursal).stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoControlador.class).obtenerProductosPorSucursal(sucursal)).withRel("productos-sucursal")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControlador.class).obtenerProductosPorSucursal(sucursal)).withSelfRel(),
                linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("todos-los-productos"));
    }

    @GetMapping("/buscar")
    public CollectionModel<EntityModel<Producto>> buscarProductos(@RequestParam String nombre) {
        List<EntityModel<Producto>> productos = productoServicio.buscarPorNombre(nombre).stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControlador.class).buscarProductos(nombre)).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoServicio.guardar(producto);
        
        EntityModel<Producto> recursoProducto = EntityModel.of(productoGuardado,
                linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(productoGuardado.getId())).withSelfRel());
        
        return ResponseEntity
                .created(recursoProducto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(recursoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoActualizado = productoServicio.actualizar(id, producto);
        if (productoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        
        EntityModel<Producto> recursoProducto = EntityModel.of(productoActualizado,
                linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos"));
        
        return ResponseEntity.ok(recursoProducto);
    }

    @PatchMapping("/{codigo}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable String codigo, @RequestParam Integer cantidad) {
        Producto productoActualizado = productoServicio.actualizarStock(codigo, cantidad);
        if (productoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        
        EntityModel<Producto> recursoProducto = EntityModel.of(productoActualizado,
                linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(productoActualizado.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos"));
        
        return ResponseEntity.ok(recursoProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}