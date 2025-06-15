package com.logistica_service.repository;

import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EnvioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnvioRepository envioRepository;

    @Test
    void cuandoBuscarPorEstado_entoncesRetornarEnvios() {
        Envio envio1 = new Envio(null, "Origen1", "Destino1", "Pendiente", LocalDate.now());
        Envio envio2 = new Envio(null, "Origen2", "Destino2", "Entregado", LocalDate.now());
        
        entityManager.persist(envio1);
        entityManager.persist(envio2);
        entityManager.flush();

        List<Envio> encontrados = envioRepository.findByEstado("Pendiente");
        
        assertEquals(1, encontrados.size());
        assertEquals("Pendiente", encontrados.get(0).getEstado());
    }

    @Test
    void cuandoGuardarEnvio_entoncesPersistir() {
        Envio envio = new Envio(null, "Origen", "Destino", "Estado", LocalDate.now());
        
        Envio guardado = envioRepository.save(envio);
        
        assertNotNull(guardado.getId());
        assertEquals("Origen", guardado.getOrigen());
    }
}