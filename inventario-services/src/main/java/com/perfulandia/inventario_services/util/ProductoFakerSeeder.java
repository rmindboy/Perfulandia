package com.perfulandia.inventario_services.util;

import com.perfulandia.inventario_services.model.Producto;
import com.perfulandia.inventario_services.repository.ProductoRepositorio;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.Random;
import java.util.stream.IntStream;

@Component
public class ProductoFakerSeeder implements CommandLineRunner {

    private final ProductoRepositorio productoRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public ProductoFakerSeeder(ProductoRepositorio productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        if (productoRepository.count() == 0) {
            IntStream.range(0, 20).forEach(i -> {
                Producto producto = new Producto();
                producto.setCodigo("PERF" + faker.number().digits(5));
                producto.setNombre(faker.commerce().productName());
                producto.setDescripcion(faker.lorem().sentence());
                producto.setPrecio(faker.number().randomDouble(2, 1000, 10000));
                producto.setStock(random.nextInt(50) + 10);
                producto.setStockMinimo(10);
                producto.setSucursal(faker.address().cityName());

                productoRepository.save(producto);
            });
            System.out.println("Productos de prueba generados con Datafaker");
        }
    }
}
