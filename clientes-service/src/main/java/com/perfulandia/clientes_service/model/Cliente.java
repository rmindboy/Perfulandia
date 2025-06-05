package com.perfulandia.clientes_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Entity
@Table(name = "clientes")
public class Cliente extends RepresentationModel<Cliente> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection
    @CollectionTable(name = "direcciones_envio", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "direccion")
    private List<String> direccionesEnvio;

    @Column(nullable = false)
    private String telefono;
}