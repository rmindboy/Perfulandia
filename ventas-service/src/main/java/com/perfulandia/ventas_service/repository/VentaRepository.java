package com.perfulandia.ventas_service.repository;

import com.perfulandia.ventas_service.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteId(Long clienteId);
    List<Venta> findBySucursalId(Long sucursalId);
}