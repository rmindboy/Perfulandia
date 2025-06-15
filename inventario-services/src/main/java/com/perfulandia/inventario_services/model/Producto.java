package com.perfulandia.inventario_services.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Entity
@Table(name = "productos")
@Data
@Schema(description = "Entidad que representa un producto en el inventario de Perfulandia")
public class Producto extends RepresentationModel<Producto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1")
    private Long id;
    

    @NotBlank(message = "El código es obligatorio")
    @Column(nullable = false, unique = true)
    @Schema(description = "Código único del producto", example = "PERF12345")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Perfume Floral")
    private String nombre;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Descripción del producto", example = "Aroma floral con notas de jazmín")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false)
    @Schema(description = "Precio de venta del producto", example = "15990.0")
    private Double precio;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    @Column(nullable = false)
    @Schema(description = "Cantidad actual en stock", example = "35")
    private Integer stock;

    @NotNull(message = "El stock mínimo no puede ser nulo")
    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    @Column(nullable = false)
    @Schema(description = "Cantidad mínima antes de considerar reposición", example = "10")
    private Integer stockMinimo;

    @NotBlank(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Sucursal donde está disponible el producto", example = "Viña del Mar")
    private String sucursal; // "Meiggs", "Concepción", "Viña del Mar"
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id) && 
               Objects.equals(codigo, producto.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}