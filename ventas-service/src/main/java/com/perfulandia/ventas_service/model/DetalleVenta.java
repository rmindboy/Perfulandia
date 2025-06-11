package com.perfulandia.ventas_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long productoId;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private Double precioUnitario;
    
    @Column(nullable = false)
    private Double subtotal;
}
