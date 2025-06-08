package com.perfulandia.clientes_service.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarContrasenaRequest {

    @NotBlank(message = "Debe ingresar la contraseña actual")
    private String contrasenaActual;

    @NotBlank(message = "Debe ingresar la nueva contraseña")
    @Size(min = 6, message = "La nueva contraseña debe tener al menos 6 caracteres")
    private String nuevaContrasena;
}
