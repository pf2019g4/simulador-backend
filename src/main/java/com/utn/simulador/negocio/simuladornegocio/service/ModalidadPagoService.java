package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadPago;
import com.utn.simulador.negocio.simuladornegocio.repository.ModalidadPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModalidadPagoService {

    private final ModalidadPagoRepository modalidadPagoRepository;

    public void guardar(List<ModalidadPago> listaModalidadesPago){
        modalidadPagoRepository.saveAll(listaModalidadesPago);
    }

    public List<ModalidadPago> obtenerModalidadesPagoProveedor(Long idProveedor){
        return modalidadPagoRepository.findByProveedorId(idProveedor);
    }

}
