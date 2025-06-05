package com.perfulandia.ventas_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ventas_service.model.Venta;
import com.perfulandia.ventas_service.repository.VentaRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaRepository ventaRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venta crearVenta(@Valid @RequestBody Venta venta) {
        return ventaRepository.save(venta);
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }
}