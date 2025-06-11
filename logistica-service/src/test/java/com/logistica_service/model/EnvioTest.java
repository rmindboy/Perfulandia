package com.logistica_service.model;

import jakarta.persistence.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.junit.jupiter.api.*;

import com.perfulandia.logistica_service.model.Envio;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnvioTest {
    
    private static Validator validator;
    
    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void cuandoCamposValidos_entoncesSinViolaciones() {
        Envio envio = new Envio(1L, "Origen", "Destino", "Estado", LocalDate.now());
        Set<ConstraintViolation<Envio>> violations = validator.validate(envio);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void cuandoOrigenVacio_entoncesViolacion() {
        Envio envio = new Envio(1L, "", "Destino", "Estado", LocalDate.now());
        Set<ConstraintViolation<Envio>> violations = validator.validate(envio);
        assertEquals(1, violations.size());
    }
    
    @Test
    void cuandoDestinoVacio_entoncesViolacion() {
        Envio envio = new Envio(1L, "Origen", "", "Estado", LocalDate.now());
        Set<ConstraintViolation<Envio>> violations = validator.validate(envio);
        assertEquals(1, violations.size());
    }
    
    @Test
    void cuandoEstadoVacio_entoncesViolacion() {
        Envio envio = new Envio(1L, "Origen", "Destino", "", LocalDate.now());
        Set<ConstraintViolation<Envio>> violations = validator.validate(envio);
        assertEquals(1, violations.size());
    }
}