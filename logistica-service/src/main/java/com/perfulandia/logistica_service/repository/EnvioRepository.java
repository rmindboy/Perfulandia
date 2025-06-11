package com.perfulandia.logistica_service.repository;

import com.perfulandia.logistica_service.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByEstado(String estado);
}