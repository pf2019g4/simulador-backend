package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondo(Long idProyecto, TipoFlujoFondo tipoFlujoFondo){
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondo(idProyecto, tipoFlujoFondo);
    }

    public void guardar(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }
}
