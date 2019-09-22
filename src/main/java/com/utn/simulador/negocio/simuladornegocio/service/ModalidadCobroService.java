package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.repository.ModalidadCobroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModalidadCobroService {

    private final ModalidadCobroRepository modalidadCobroRepository;

    public void guardar(List<ModalidadCobro> listaModalidadesCobro){
        modalidadCobroRepository.saveAll(listaModalidadesCobro);
    }

    public void eliminarViejasModalidadesCobro(Long idProyecto){
        modalidadCobroRepository.deleteByProyectoId(idProyecto);
    }

    public List<ModalidadCobro> obtenerModalidadesCobro(Long idProyecto){
        return modalidadCobroRepository.findByProyectoId(idProyecto);
    }

}
