package com.perfulandia.inventario_services.service;

import com.perfulandia.inventario_services.model.Producto;
import com.perfulandia.inventario_services.repository.ProductoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServicio {
    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> obtenerTodos() {
        return productoRepositorio.findAll();
    }

    public List<Producto> obtenerPorSucursal(String sucursal) {
        return productoRepositorio.findBySucursal(sucursal);
    }

    public Producto obtenerPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public Producto obtenerPorCodigo(String codigo) {
        return productoRepositorio.findByCodigo(codigo);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto detallesProducto) {
        Producto producto = obtenerPorId(id);
        if (producto != null) {
            producto.setNombre(detallesProducto.getNombre());
            producto.setDescripcion(detallesProducto.getDescripcion());
            producto.setPrecio(detallesProducto.getPrecio());
            producto.setStock(detallesProducto.getStock());
            producto.setStockMinimo(detallesProducto.getStockMinimo());
            producto.setSucursal(detallesProducto.getSucursal());
            return productoRepositorio.save(producto);
        }
        return null;
    }

    @Transactional
    public Producto actualizarStock(String codigo, Integer cantidad) {
        Producto producto = obtenerPorCodigo(codigo);
        if (producto != null) {
            producto.setStock(producto.getStock() + cantidad);
            return productoRepositorio.save(producto);
        }
        return null;
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepositorio.deleteById(id);
    }
}