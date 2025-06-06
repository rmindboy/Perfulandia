package com.perfulandia.inventario_services.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Producto extends RepresentationModel<Producto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @Column(nullable = false)
    private Double precio;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false)
    private Integer stockMinimo;
    
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