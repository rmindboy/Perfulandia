package com.perfulandia.inventario_services.repository;

import com.perfulandia.inventario_services.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findBySucursal(String sucursal);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    Producto findByCodigo(String codigo);
}