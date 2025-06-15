package com.perfulandia.inventario_services.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Entity
@Table(name = "productos")
@Data
public class Producto extends RepresentationModel<Producto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @NotBlank(message = "El código es obligatorio")
    @Column(nullable = false, unique = true)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Double precio;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El stock mínimo no puede ser nulo")
    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Integer stockMinimo;

    @NotBlank(message = "La sucursal es obligatoria")
    @Column(nullable = false)
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