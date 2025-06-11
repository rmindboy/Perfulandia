package com.perfulandia.logistica_service.service;

import com.perfulandia.logistica_service.model.Envio;
import com.perfulandia.logistica_service.repository.EnvioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;

    public EnvioService(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    public Envio obtenerPorId(Long id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
    }

    @Transactional
    public Envio crear(Envio envio) {
        return envioRepository.save(envio);
    }

    @Transactional
    public Envio actualizar(Long id, Envio datosEnvio) {
        Envio envio = obtenerPorId(id);
        envio.setOrigen(datosEnvio.getOrigen());
        envio.setDestino(datosEnvio.getDestino());
        envio.setEstado(datosEnvio.getEstado());
        return envioRepository.save(envio);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new RuntimeException("Envío no encontrado");
        }
        envioRepository.deleteById(id);
    }

    public List<Envio> buscarPorEstado(String estado) {
        return envioRepository.findByEstado(estado);
    }
}