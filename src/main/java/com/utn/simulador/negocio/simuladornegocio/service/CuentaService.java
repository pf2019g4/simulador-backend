package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.CuentaPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaPeriodoRepository cuentaPeriodoRepository;
    private final EstadoRepository estadoRepository;

    public List<Cuenta> obtenerPorProyectoYTipoFlujoFondo(Long idProyecto, TipoFlujoFondo tipoFlujoFondo) {
        return cuentaRepository.findByProyectoIdAndTipoFlujoFondo(idProyecto, tipoFlujoFondo);
    }

    public void crearProduccion(Estado estado, BigDecimal costoPeriodo) {
        crearCuentaFinanacieraProduccion(estado, costoPeriodo);
        crearCuentaEconomicaProduccion(estado, costoPeriodo);

    }

    private void crearCuentaFinanacieraProduccion(Estado estado, BigDecimal costoPeriodo) {
        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getPeriodo()).build());
    }

    private void crearCuentaEconomicaProduccion(Estado estado, BigDecimal costoPeriodo) {

        Cuenta cuenta = Cuenta.builder().descripcion("costo produccion periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuenta = cuentaRepository.save(cuenta);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuenta)
                .monto(costoPeriodo.negate())
                .periodo(estado.getPeriodo()).build());
    }

    public void crearVentas(Estado estado) {
        crearCuetnaFinancieraVenta(estado);
        crearCuentaEconomicaVenta(estado);
    }

    private void crearCuentaEconomicaVenta(Estado estado) {
        Cuenta cuentaEconomica = Cuenta.builder().descripcion("venta periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.ECONOMICO)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuentaEconomica = cuentaRepository.save(cuentaEconomica);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuentaEconomica)
                .monto(estado.getVentas())
                .periodo(estado.getPeriodo()).build());
    }

    private void crearCuetnaFinancieraVenta(Estado estado) {
        Cuenta cuentaFinanciera = Cuenta.builder().descripcion("venta periodo " + estado.getPeriodo())
                .tipoCuenta(TipoCuenta.FINANCIERO)
                .tipoFlujoFondo(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                .cuentasPeriodo(new ArrayList<>())
                .proyectoId(estado.getProyecto().getId())
                .build();
        cuentaFinanciera = cuentaRepository.save(cuentaFinanciera);

        cuentaPeriodoRepository.save(CuentaPeriodo.builder()
                .cuenta(cuentaFinanciera)
                .monto(estado.getVentas())
                .periodo(estado.getPeriodo()).build());

    }

    public void guardar(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }

    public void imputar(List<Cuenta> cuentasAImputar, Estado estado) {

        for (Cuenta cuenta : cuentasAImputar) {
            for (CuentaPeriodo cuentaPeriodo : cuenta.getCuentasPeriodo()) {
                if (cuenta.getTipoCuenta().equals(TipoCuenta.FINANCIERO)
                        && cuentaPeriodo.getPeriodo().equals(estado.getPeriodo())) {
                    if ((cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_AFECTOS_A_IMPUESTOS)
                            || cuenta.getTipoFlujoFondo().equals(TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS))) {
                        estado.setCaja(estado.getCaja().add(cuentaPeriodo.getMonto()));
                    } else {
                        estado.setCaja(estado.getCaja().subtract(cuentaPeriodo.getMonto()));
                    }
                }
            }

            cuentaRepository.save(cuenta);
            estadoRepository.save(estado);

        }
    }
}
