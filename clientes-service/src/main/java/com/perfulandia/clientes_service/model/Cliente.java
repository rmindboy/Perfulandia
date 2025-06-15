package com.perfulandia.clientes_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Entity
@Table(name = "clientes")
public class Cliente extends RepresentationModel<Cliente> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre completo del cliente", example = "María López")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Correo electrónico del cliente", example = "maria@example.com")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "Contraseña del cliente. debe tener al menos 6 caracteres", example = "123456")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @Schema(description = "Lista de direcciones donde el cliente puede recibir envíos", example = "[\"Calle Falsa 123\", \"Av. Siempre Viva 742\"]")
    @Size(min = 1, message = "Debe proporcionar al menos una dirección de envío")
    @ElementCollection
    @CollectionTable(name = "direcciones_envio", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "direccion")
    private List<@NotBlank(message = "La dirección no puede estar vacía") String> direccionesEnvio;

    @Schema(description = "Teléfono de contacto del cliente, el numero debe tener entre 9 y 15 digitos", example = "+56987654321")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(
        regexp = "\\+?\\d{9,15}",
        message = "El número de teléfono debe contener entre 9 y 15 dígitos, y puede comenzar con '+'"
    )
    @Column(nullable = false)
    private String telefono;

}