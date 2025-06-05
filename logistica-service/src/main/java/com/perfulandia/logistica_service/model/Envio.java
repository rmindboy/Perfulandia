package com.perfulandia.logistica_service.model; 
 
import jakarta.persistence.*; 
import lombok.*; 
import java.time.LocalDate; 
 
@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Envio { 
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
 
    private String origen; 
    private String destino; 
    private String estado; // Ej: Pendiente, En ruta, Entregado 
    private LocalDate fechaEnvio; 
}