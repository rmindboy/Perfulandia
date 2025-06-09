package com.perfulandia.inventario_services;

import com.perfulandia.inventario_services.model.Producto;
import com.perfulandia.inventario_services.repository.ProductoRepositorio;
import com.perfulandia.inventario_services.service.ProductoServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServicioTest {

    @Mock
    private ProductoRepositorio productoRepositorio; //crea un mock del repositorio

    @InjectMocks 
    private ProductoServicio productoServicio;

    private Producto producto;

    @BeforeEach
    void setup(){
        producto = new Producto();
        producto.setId(1L);
        producto.setCodigo("XXX-000");
        producto.setDescripcion("Descripcion Perfume prueba");
        producto.setNombre("Perfume prueba");
        producto.setPrecio(9.99);
        producto.setStock(100);
        producto.setStockMinimo(10);
        producto.setSucursal("Viña del Mar");

    }

    @Test
    void obtenerTodos_conProductos_devuelveListaProductos(){

        Producto otroProducto = new Producto();
        otroProducto.setId(2L);
        otroProducto.setCodigo("YYY-111");
        otroProducto.setNombre("Otro Perfume");
        otroProducto.setDescripcion("Otro perfume de prueba");
        otroProducto.setPrecio(19.99);
        otroProducto.setStock(50);
        otroProducto.setStockMinimo(5);
        otroProducto.setSucursal("Meiggs");

        List<Producto> productosSimulados = List.of(producto, otroProducto);

        // Simulamos el comportamiento del repositorio
        when(productoRepositorio.findAll()).thenReturn(productosSimulados);

        List<Producto> resultado = productoServicio.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto));
        assertTrue(resultado.contains(otroProducto));

        // Verificamos que el repositorio fue llamado exactamente una vez
        verify(productoRepositorio, times(1)).findAll();
    }

    @Test
    void obtenerPorSucursal_devuelveLosProductosDeUnaSucursal(){

        List<Producto> productosSucursal = List.of(producto);

        when(productoRepositorio.findBySucursal("Viña del Mar")).thenReturn(productosSucursal);
        
        List<Producto> resultado = productoServicio.obtenerPorSucursal("Viña del Mar");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getSucursal().equals("Viña del Mar")));//Todos los productos devueltos pertenezcan a la sucursal esperada

        verify(productoRepositorio, times(1)).findBySucursal("Viña del Mar"); 
        
    }

    @Test
    void obtenerPorId_conId_devuelveProdctoEncontrado(){

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoServicio.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Perfume prueba", resultado.getNombre());

        verify(productoRepositorio, times(1)).findById(1L);
    }






}
