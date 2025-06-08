package com.perfulandia.ventas_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "ventas")
public class Venta extends RepresentationModel<Venta> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "el ID del cliente es obligatorio")
    @Column(nullable = false)
    private Long clienteId;
    
    @NotNull(message = "el ID de la sucursal es obligatorio")
    @Column(nullable = false)
    private Long sucursalId;
    
    @NotNull(message = "La fecha de ventas es obligatoria")
    @PastOrPresent(message = "La fecha de la venta no puede estar en el futuro")
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(nullable = false)
    private Double total;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "venta_id")
    private List<DetalleVenta> detalles;
    
    @Column(nullable = false)
    private String estado; // PROCESANDO, COMPLETADA, CANCELADA
}
