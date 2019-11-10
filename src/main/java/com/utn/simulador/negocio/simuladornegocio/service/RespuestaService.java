package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.repository.RespuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;

    public void guardar(Opcion respuesta) {
        respuestaRepository.save(respuesta);
    }

}
