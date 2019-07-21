package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaPeriodoRepository cuentaPeriodoRepository;

    public List<Cuenta> obtenerPorProyectoYTipo(Long idProyecto, TipoCuenta tipoCuenta) {
        return cuentaRepository.findByProyectoIdAndTipoCuenta(idProyecto, tipoCuenta);
    }

    public void crearVentas(Estado estado) {
        crearCuetnaFinanciera(estado);
        crearCuentaEconomica(estado);
    }

    public void crearCuentaEconomica(Estado estado) {
        Cuenta cuentaEconomica = Cuenta.builder().descripcion("venta periodo " + estado.getMes())
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyectoId())
                .build();
        cuentaEconomica = cuentaRepository.save(cuentaEconomica);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuentaEconomica)
                .monto(estado.getVentas())
                .periodo(estado.getMes()).build());
    }

    public void crearCuetnaFinanciera(Estado estado) {
        Cuenta cuentaFinanciera = Cuenta.builder().descripcion("venta periodo " + estado.getMes())
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyectoId())
                .build();
        cuentaFinanciera = cuentaRepository.save(cuentaFinanciera);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(estado.getVentas())
                .periodo(estado.getMes()).build());

    }

    public void guardar(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }
}
