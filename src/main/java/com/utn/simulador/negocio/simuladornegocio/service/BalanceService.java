package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final EstadoService estadoService;
    private final CuentaService cuentaService;

    public Balance obtenerPorProyecto(Long proyectoId, Boolean esForecast) {
        Estado estado = estadoService.obtenerActual(proyectoId, esForecast);
        PatrimonioNeto patrimonioNeto = new PatrimonioNeto(estado.getCapitalSocial(), null);
        patrimonioNeto.setResultadoDelEjercicio(calcularResultadoDelEjercicio(patrimonioNeto, proyectoId, esForecast));
        Activo activo = new Activo(
                estado.getCaja(),
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.CREDITO_CLIENTES, esForecast), estado.getPeriodo()),
                null,
                calcularInventario(estado),
                sumaPeriodos(cuentaService.obtenerPorProyectoYTipoFlujoFondoYTipoBalance(
                        proyectoId, TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS, TipoBalance.MAQUINARIAS, esForecast)),
                sumaPeriodos(cuentaService.obtenerPorProyectoYTipoFlujoFondoYTipoBalance(
                        proyectoId, TipoFlujoFondo.GASTOS_NO_DESEMBOLSABLES, TipoBalance.AMORTIZACION_MAQUINARIAS, esForecast)).negate(),
                BigDecimal.ZERO
        );
        Pasivo pasivo = new Pasivo(
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.DEUDA_PROVEEDORES, esForecast), estado.getPeriodo()),
                null,
                sumaProximosPeriodos(cuentaService.obtenerPorProyectoYTipoBalance(proyectoId, TipoBalance.DEUDA_BANCARIA, esForecast), estado.getPeriodo()),
                null,
                BigDecimal.ZERO
        );

        BigDecimal sumaActivo = sumaActivo(activo);
        BigDecimal sumaPasivoYPatrimonio = sumaPasivo(pasivo).add(sumaPatrimonioNeto(patrimonioNeto));
        Integer comparacion = sumaActivo.compareTo(sumaPasivoYPatrimonio);
        if (comparacion > 0) {
            pasivo.setOtros(sumaActivo.subtract(sumaPasivoYPatrimonio));
        } else if (comparacion < 0) {
            activo.setOtros(sumaPasivoYPatrimonio.subtract(sumaActivo));
        }

        return new Balance(null, activo, pasivo, patrimonioNeto);
    }

    private BigDecimal sumaPeriodos(List<Cuenta> cuentas) {
        return cuentas
                .stream()
                .map(cuenta -> cuenta.getCuentasPeriodo()
                .stream()
                .map(CuentaPeriodo::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumaProximosPeriodos(List<Cuenta> cuentas, Integer periodo) {
        return cuentas
                .stream()
                .map(cuenta -> montoProximosPeriodo(cuenta.getCuentasPeriodo(), periodo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal montoProximosPeriodo(List<CuentaPeriodo> cuentaPeriodos, Integer periodo) {
        return cuentaPeriodos
                .stream()
                .filter(cuentaPeriodo -> cuentaPeriodo.getPeriodo() > periodo)
                .map(CuentaPeriodo::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularInventario(Estado estado) {
        return estado.getInventario();
    }

    private BigDecimal calcularResultadoDelEjercicio(PatrimonioNeto pn, Long idProyecto, Boolean esForecast) {
        if (pn.getResultadoDelEjercicio() != null) {
            return pn.getResultadoDelEjercicio();
        }
        List<Cuenta> cuentas = cuentaService.obtenerPorProyectoYTipoCuenta(idProyecto, TipoCuenta.ECONOMICO, esForecast);
        return cuentas.stream()
                .map(cuenta -> cuenta.getCuentasPeriodo().stream()
                .map(cuentaPeriodo -> cuentaPeriodo.getMonto() != null ? cuentaPeriodo.getMonto() : BigDecimal.ZERO)
                .reduce(BigDecimal::add).get())
                .reduce(BigDecimal::add).get();
    }

    private BigDecimal sumaActivo(Activo activo) {
        BigDecimal sum = new BigDecimal(0);
        if (activo.getCaja() != null) {
            sum = sum.add(activo.getCaja());
        }
        if (activo.getCuentasPorCobrar() != null) {
            sum = sum.add(activo.getCuentasPorCobrar());
        }
        if (activo.getInventario() != null) {
            sum = sum.add(activo.getInventario());
        }
        if (activo.getMaquinaria() != null) {
            sum = sum.add(activo.getMaquinaria());
        }
        if (activo.getAmortizacionAcumulada() != null) {
            sum = sum.add(activo.getAmortizacionAcumulada());
        }
        return sum;
    }

    private BigDecimal sumaPasivo(Pasivo pasivo) {
        BigDecimal sum = new BigDecimal(0);
        if (pasivo.getDeudasBancarias() != null) {
            sum = sum.add(pasivo.getDeudasBancarias());
        }
        if (pasivo.getProveedores() != null) {
            sum = sum.add(pasivo.getProveedores());
        }
        return sum;
    }

    private BigDecimal sumaPatrimonioNeto(PatrimonioNeto pn) {
        BigDecimal sum = new BigDecimal(0);
        if (pn.getCapitalSocial() != null) {
            sum = sum.add(pn.getCapitalSocial());
        }
        if (pn.getResultadoDelEjercicio() != null) {
            sum = sum.add(pn.getResultadoDelEjercicio());
        }
        return sum;
    }

}
