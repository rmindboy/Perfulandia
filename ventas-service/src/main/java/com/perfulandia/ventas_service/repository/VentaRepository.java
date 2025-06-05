package com.perfulandia.ventas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfulandia.ventas_service.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}