package com.perfulandia.logistica_service.controller; 
 
import com.perfulandia.logistica_service.model.Envio; 
import com.perfulandia.logistica_service.repository.EnvioRepository; 
import lombok.RequiredArgsConstructor; 
import org.springframework.http.*; 
import org.springframework.web.bind.annotation.*; 
import java.util.*; 
 
@RestController 
@RequestMapping("/api/envios") 
@RequiredArgsConstructor 
public class EnvioController { 
 
    private final EnvioRepository envioRepository; 
 
    @GetMapping 
    public List<Envio> listarTodos() { 
        return envioRepository.findAll(); 
    } 
 
    @GetMapping("/{id}") 
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) 
{ 
        return envioRepository.findById(id) 
            .map(ResponseEntity::ok) 
            .orElse(ResponseEntity.notFound().build()); 
    } 
 
    @PostMapping 


    public ResponseEntity<Envio> crear(@RequestBody Envio envio) { 
        return new ResponseEntity<>(envioRepository.save(envio), 
HttpStatus.CREATED); 
    } 
 
    @PutMapping("/{id}") 
    public ResponseEntity<Void> actualizar(@PathVariable Long id, 
@RequestBody Envio envio) { 
        if (!envioRepository.existsById(id)) return 
ResponseEntity.notFound().build(); 
        envio.setId(id); 
        envioRepository.save(envio); 
        return ResponseEntity.noContent().build(); 
    } 
 
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { 
        if (!envioRepository.existsById(id)) return 
ResponseEntity.notFound().build(); 
        envioRepository.deleteById(id); 
        return ResponseEntity.noContent().build(); 
    } 
 
    @GetMapping("/estado") 
    public List<Envio> buscarPorEstado(@RequestParam String estado) 
{ 
        return envioRepository.findByEstado(estado); 
    } 
}