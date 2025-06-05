package com.perfulandia.cliente_service.model; 
 
import jakarta.persistence.*; 
import lombok.*; 
 
@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class Cliente { 
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 


 
    @Column(nullable = false) 
    private String nombre; 
 
    @Column(nullable = false, unique = true) 
    private String correo; 
 
    private String direccion; 
    private String telefono; 
}