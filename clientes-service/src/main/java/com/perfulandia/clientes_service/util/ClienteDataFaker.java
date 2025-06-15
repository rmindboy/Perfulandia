package com.perfulandia.clientes_service.util;

import com.perfulandia.clientes_service.model.Cliente;
import com.perfulandia.clientes_service.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.Profile; para activar profile dev

import java.util.List;
import java.util.stream.IntStream;

//@Profile("dev") // Solo se ejecuta si el perfil activo es "dev"
@Component
public class ClienteDataFaker implements CommandLineRunner {

        private final ClienteRepository clienteRepository;

    public ClienteDataFaker(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        Faker faker = new Faker();

        if (clienteRepository.count() == 0) {
            IntStream.range(0, 20).forEach(i -> {
                Cliente cliente = new Cliente();
                cliente.setNombre(faker.name().fullName());
                cliente.setEmail(faker.internet().emailAddress());
                cliente.setTelefono("+569" + faker.number().digits(8));
                cliente.setDireccionesEnvio(
                    List.of(faker.address().fullAddress())
                );
                cliente.setPassword(faker.internet().password()); // puedes también usar faker.internet().password()

                clienteRepository.save(cliente);
            });
            System.out.println("Clientes de prueba generados.");
        }
    }


}
