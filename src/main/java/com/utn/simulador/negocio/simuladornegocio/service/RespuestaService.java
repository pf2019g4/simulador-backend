package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Respuesta;
import com.utn.simulador.negocio.simuladornegocio.repository.RespuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;

    public void guardar(Respuesta respuesta) {
        respuestaRepository.save(respuesta);
    }

}
