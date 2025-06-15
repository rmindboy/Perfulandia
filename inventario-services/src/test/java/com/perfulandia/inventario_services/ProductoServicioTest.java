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

    @Test
    void obtenerPorCodigo_conCodigo_devuelveProductoEncontrado(){

        when(productoRepositorio.findByCodigo("XXX-000")).thenReturn(producto);

        Producto resultado = productoServicio.obtenerPorCodigo("XXX-000");

        assertNotNull(resultado);
        assertEquals("XXX-000", resultado.getCodigo());
        assertEquals("Perfume prueba", resultado.getNombre());

        verify(productoRepositorio, times(1)).findByCodigo("XXX-000");

    }

    @Test
    void buscarPorNombre_conNombre_devuelveProductoEncontrado(){

        when(productoRepositorio.findByNombreContainingIgnoreCase("Perfume prueba")).thenReturn(List.of(producto));

        List<Producto> resultado = productoServicio.buscarPorNombre("Perfume prueba");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Perfume prueba", resultado.get(0).getNombre());

        verify(productoRepositorio, times(1)).findByNombreContainingIgnoreCase("Perfume prueba");
    }

    @Test
    void guardar_conProductoValido_devuelveProductoGuardado() {
        // Arrange
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoServicio.guardar(producto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Perfume prueba", resultado.getNombre());
        verify(productoRepositorio, times(1)).save(producto);
    }

    @Test
    void actualizar_conIdExistente_actualizaYDevuelveProducto() {
        // Arrange
        Producto detalles = new Producto();
        detalles.setNombre("Nuevo Nombre");
        detalles.setDescripcion("Nueva descripción");
        detalles.setPrecio(19.99);
        detalles.setStock(200);
        detalles.setStockMinimo(20);
        detalles.setSucursal("Santiago");

        when(productoRepositorio.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoServicio.actualizar(1L, detalles);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals("Santiago", resultado.getSucursal());
        verify(productoRepositorio, times(1)).save(producto);
    }

    @Test
    void actualizar_conIdInexistente_devuelveNull() {
        // Arrange
        Producto detalles = new Producto();
        detalles.setNombre("Otro Nombre");

        when(productoRepositorio.findById(99L)).thenReturn(Optional.empty());

        // Act
        Producto resultado = productoServicio.actualizar(99L, detalles);

        // Assert
        assertNull(resultado);
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    void actualizarStock_conCodigoExistente_actualizaYDevuelveProducto() {
        // Arrange
        when(productoRepositorio.findByCodigo("XXX-000")).thenReturn(producto);
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoServicio.actualizarStock("XXX-000", 50);

        // Assert
        assertNotNull(resultado);
        assertEquals(150, resultado.getStock());
        verify(productoRepositorio, times(1)).save(producto);
    }

    @Test
    void eliminar_conIdValido_invocaDeleteById() {
        // Act
        productoServicio.eliminar(1L);

        // Assert
        verify(productoRepositorio, times(1)).deleteById(1L);
    }






}
