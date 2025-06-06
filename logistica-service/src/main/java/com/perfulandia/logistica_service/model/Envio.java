package com.perfulandia.logistica_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotBlank(message = "El estado es obligatorio")
    private String estado; 

    private LocalDate fechaEnvio = LocalDate.now(); 
}