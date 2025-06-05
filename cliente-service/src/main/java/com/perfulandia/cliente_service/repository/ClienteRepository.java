package com.perfulandia.cliente_service.repository;

import com.perfulandia.cliente_service.model.Cliente; 
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.List; 
 
public interface ClienteRepository extends JpaRepository<Cliente, Long> { 
    List<Cliente> findByNombreContainingIgnoreCase(String nombre); 
}