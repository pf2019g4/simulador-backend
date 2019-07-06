package com.utn.simulador.negocio.simuladornegocio.service;


import com.utn.simulador.negocio.simuladornegocio.domain.Consecuencia;
import com.utn.simulador.negocio.simuladornegocio.repository.ConsecuenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsecuenciaService {

    private final ConsecuenciaRepository consecuenciaRepository;

    public void guardar(Consecuencia consecuencia){
        consecuenciaRepository.save(consecuencia);
    }
}
