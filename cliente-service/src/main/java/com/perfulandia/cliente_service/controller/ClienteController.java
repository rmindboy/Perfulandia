package com.perfulandia.cliente_service.controller;

import com.perfulandia.cliente_service.model.Cliente; 
import com.perfulandia.cliente_service.repository.ClienteRepository; 
import lombok.RequiredArgsConstructor; 
import org.springframework.http.*; 
import org.springframework.web.bind.annotation.*; 
import jakarta.validation.Valid; 
import java.util.*; 
 


@RestController 
@RequestMapping("/api/clientes") 
@RequiredArgsConstructor 
public class ClienteController { 
 
    private final ClienteRepository clienteRepository; 
 
    @GetMapping 
    public List<Cliente> listar() { 
        return clienteRepository.findAll(); 
    } 
 
    @GetMapping("/{id}") 
    public ResponseEntity<Cliente> obtener(@PathVariable Long id) { 
        return clienteRepository.findById(id) 
            .map(ResponseEntity::ok) 
            .orElse(ResponseEntity.notFound().build()); 
    } 
 
    @PostMapping 
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente 
cliente) { 
        return new ResponseEntity<>(clienteRepository.save(cliente), 
HttpStatus.CREATED); 
    } 
 
    @PutMapping("/{id}") 
    public ResponseEntity<Void> actualizar(@PathVariable Long id, 
@RequestBody Cliente cliente) { 
        if (!clienteRepository.existsById(id)) return 
ResponseEntity.notFound().build(); 
        cliente.setId(id); 
        clienteRepository.save(cliente); 
        return ResponseEntity.noContent().build(); 
    } 
 
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { 
        if (!clienteRepository.existsById(id)) return 
ResponseEntity.notFound().build(); 
        clienteRepository.deleteById(id); 


        return ResponseEntity.noContent().build(); 
    } 
}