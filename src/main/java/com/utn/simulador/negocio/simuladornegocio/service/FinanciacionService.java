package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CreditoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.FinanciacionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FinanciacionService {

    private final FinanciacionRepository financiacionRepository;
    private final CreditoRepository creditoRepository;

    private final CuentaService cuentaService;

    public List<Financiacion> obtenerPorEscenario(Long escenarioId) {
        return financiacionRepository.findByEscenarioId(escenarioId);
    }

    public Financiacion crear(Financiacion financiacion) {
        return financiacionRepository.save(financiacion);
    }

    public void editar(Financiacion financiacion) {
        financiacionRepository.save(financiacion);
    }

    public void borrar(Long financiacionId) {
        financiacionRepository.deleteById(financiacionId);
    }

    public Credito tomar(Credito credito) {
        return creditoRepository.save(credito);
    }

    public Credito obtenerCreditoPorProyecto(Long proyectoId) {
        return creditoRepository.findByProyectoId(proyectoId);
    }

    public void acreditar(Long proyectoId) {
        Credito credito = creditoRepository.findByProyectoId(proyectoId);

        if (credito.getMonto().compareTo(BigDecimal.ZERO) > 0) {
            Financiacion financionTomada = financiacionRepository.findById(credito.getFinanciacionId()).orElseThrow();
            BigDecimal intereses = calcularIntereses(credito, financionTomada);
            BigDecimal montoADevolver = credito.getMonto().add(intereses);
            BigDecimal montoADevolverPorPeriodo = montoADevolver.divide(new BigDecimal(financionTomada.getCantidadCuotas()), RoundingMode.DOWN);

            crearCuentasFinancieras(proyectoId, credito, montoADevolverPorPeriodo, financionTomada);
            crearCuentaEconómica(proyectoId, intereses, TipoTransaccion.OTROS);
        }
    }

    private void crearCuentaEconómica(Long proyectoId, BigDecimal intereses, TipoTransaccion tipoTransaccion) {
        //CREAR CUENTA ECONOMICA por interes
        cuentaService.crearCuentaEconomica(proyectoId, 1, "intereses credito", intereses.negate(), tipoTransaccion);
    }

    private void crearCuentasFinancieras(Long proyectoId, Credito credito, BigDecimal montoADevolverPorPeriodo, Financiacion financionTomada) {
        //CREAR CUENTA FINANCIERA DE INGRESO
        Cuenta cuentaObtencionCredito = cuentaService.crearCuentaFinanciera(proyectoId, "credito", TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS, null);
        cuentaService.crearCuentaFinancieraPeriodo(0, credito.getMonto(), cuentaObtencionCredito);

        //CREAR CUENTAS FINANCIERAS por pago cuotas
        Cuenta cuentaFinancieraPagoCredito = cuentaService.crearCuentaFinanciera(proyectoId, "credito", TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS,TipoBalance.DEUDA_BANCARIA);

        for (int i = 1; (i <= financionTomada.getCantidadCuotas()); i++) {

            cuentaService.crearCuentaFinancieraPeriodo(i + 1, montoADevolverPorPeriodo, cuentaFinancieraPagoCredito);
        }
    }

    private BigDecimal calcularIntereses(Credito credito, Financiacion financionTomada) {
        final BigDecimal tasaMensual
                = financionTomada.getTna().setScale(5).divide(new BigDecimal(100), RoundingMode.UP)
                        .divide(new BigDecimal(12), RoundingMode.UP);
        final BigDecimal tasaCorrespondientePorDuracionCredito = tasaMensual.multiply(new BigDecimal(financionTomada.getCantidadCuotas()));

        return credito.getMonto()
                .multiply(tasaCorrespondientePorDuracionCredito);
    }

}
