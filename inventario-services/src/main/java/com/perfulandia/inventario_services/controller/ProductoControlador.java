package com.perfulandia.inventario_services.controller;

import com.perfulandia.inventario_services.model.Producto;
import com.perfulandia.inventario_services.service.ProductoServicio;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Productos", description = "Operaciones relacionadas con los productos en inventario")
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoControlador {
    
    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @Operation(
    summary = "Obtener todos los productos",
    description = "Recupera una lista de todos los productos disponibles en el sistema"
        )
        @ApiResponses(value = {
        @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida correctamente",
        content = @Content(
        mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = Producto.class))
        )
        )
        })


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

        @Operation(
        summary = "Obtener producto por ID",
        description = "Recupera un producto específico utilizando su identificador único"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado exitosamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
        })
        @Parameter(
        name = "id",
        description = "ID del producto que se desea buscar",
        required = true,
        example = "10"
        )
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


        @Operation(
        summary = "Obtener productos por sucursal",
        description = "Devuelve todos los productos disponibles en una sucursal específica"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de productos obtenida correctamente",
                content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class))
                )
        )
        })
        @Parameter(
        name = "sucursal",
        description = "Nombre de la sucursal",
        required = true,
        example = "Santiago"
        )
    @GetMapping("/porSucursal/{sucursal}")
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


        @Operation(
        summary = "Buscar productos por nombre",
        description = "Busca productos cuyo nombre coincida con el texto proporcionado (parcial o completo)"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de productos encontrados",
                content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Producto.class))
                )
        )
        })
        @Parameter(
        name = "nombre",
        description = "Texto a buscar dentro del nombre de los productos",
        required = true,
        example = "Perfume"
        )
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


        @Operation(
        summary = "Crear un nuevo producto",
        description = "Registra un nuevo producto en el inventario de Perfulandia"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Producto creado exitosamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos")
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del nuevo producto a registrar",
        required = true,
        content = @Content(
                schema = @Schema(implementation = Producto.class),
                examples = @ExampleObject(
                name = "EjemploProducto",
                value = """
                {
                "codigo": "PERF12345",
                "nombre": "Perfume Floral",
                "descripcion": "Aroma suave con notas de jazmín",
                "precio": 15990.0,
                "stock": 50,
                "stockMinimo": 10,
                "sucursal": "Viña del Mar"
                }
                """
                )
        )
        )
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoServicio.guardar(producto);
        
        EntityModel<Producto> recursoProducto = EntityModel.of(productoGuardado,
                linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(productoGuardado.getId())).withSelfRel());
        
        return ResponseEntity
                .created(recursoProducto.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(recursoProducto);
    }

        @Operation(
        summary = "Actualizar producto por ID",
        description = "Actualiza completamente un producto existente identificado por su ID"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Producto actualizado exitosamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
        })
        @Parameter(
        name = "id",
        description = "ID del producto que se desea actualizar",
        required = true,
        example = "3"
        )
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos completos del producto actualizados",
        required = true,
        content = @Content(
                schema = @Schema(implementation = Producto.class),
                examples = @ExampleObject(
                name = "EjemploProductoActualizado",
                value = """
                {
                "codigo": "PERF98765",
                "nombre": "Perfume Intenso",
                "descripcion": "Aroma amaderado y duradero",
                "precio": 18990.0,
                "stock": 30,
                "stockMinimo": 8,
                "sucursal": "Santiago"
                }
                """
                )
        )
        )
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

        @Operation(
        summary = "Actualizar stock de un producto por código",
        description = "Modifica el stock actual de un producto sumando la cantidad proporcionada"
        )
        @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Stock actualizado correctamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Producto.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado con el código especificado"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
        })
        @Parameter(
        name = "codigo",
        description = "Código único del producto (por ejemplo, PERF12345)",
        required = true,
        example = "PERF12345"
        )
        @Parameter(
        name = "cantidad",
        description = "Cantidad a sumar o restar del stock actual. Puede ser negativa.",
        required = true,
        example = "10"
        )
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


        @Operation(
        summary = "Eliminar un producto por ID",
        description = "Elimina un producto del inventario de Perfulandia utilizando su identificador único"
        )
        @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado con el ID proporcionado")
        })
        @Parameter(
        name = "id",
        description = "ID del producto que se desea eliminar",
        required = true,
        example = "5"
        )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        productoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}