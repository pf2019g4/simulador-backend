package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CreditoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.FinanciacionRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.IntStream;

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
            BigDecimal cuotaAnual = calcularCuotaAmortizacionFrances(credito, financionTomada);
            BigDecimal montoDeuda = credito.getMonto();

            Cuenta cuentaFinancieraIngresoCredito = cuentaService.crearCuentaFinanciera(proyectoId, "Crédito", TipoFlujoFondo.INGRESOS_NO_AFECTOS_A_IMPUESTOS, null, TipoTransaccion.OTROS);
            cuentaService.crearCuentaFinancieraPeriodo(0, credito.getMonto(), cuentaFinancieraIngresoCredito);

            Cuenta cuentaFinancieraInteresCredito = cuentaService.crearCuentaFinanciera(proyectoId, "Interés deuda", TipoFlujoFondo.EGRESOS_AFECTOS_A_IMPUESTOS,TipoBalance.DEUDA_BANCARIA, TipoTransaccion.OTROS);
            Cuenta cuentaFinancieraAmortCuotaCredito = cuentaService.crearCuentaFinanciera(proyectoId, "Amortización cuota", TipoFlujoFondo.EGRESOS_NO_AFECTOS_A_IMPUESTOS,TipoBalance.DEUDA_BANCARIA, TipoTransaccion.OTROS);

            for( int i = 1 ; i <= financionTomada.getCantidadCuotas(); i++){
                BigDecimal intereses = calcularInteresesAmortizacionFrances(montoDeuda, financionTomada).setScale(2,RoundingMode.HALF_UP);
                BigDecimal amortizacionCuota = cuotaAnual.subtract(intereses).setScale(2,RoundingMode.HALF_UP);
                cuentaService.crearCuentaFinancieraPeriodo(i, intereses, cuentaFinancieraInteresCredito);
                cuentaService.crearCuentaFinancieraPeriodo(i, amortizacionCuota, cuentaFinancieraAmortCuotaCredito);
                cuentaService.crearCuentaEconomica(proyectoId, i, "Interés deuda", intereses.negate(), TipoTransaccion.OTROS);

                montoDeuda = montoDeuda.subtract(amortizacionCuota);
            }
        }
    }

    private BigDecimal calcularInteresesAmortizacionFrances(BigDecimal saldoCapital, Financiacion financionTomada) {
        return saldoCapital.multiply(financionTomada.getTea().divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP));
    }

    private BigDecimal calcularCuotaAmortizacionFrances(Credito credito, Financiacion financiacion){
        BigDecimal dividendo = BigDecimal.ONE.subtract(BigDecimal.ONE.add(financiacion.getTea().divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).pow(-1 * financiacion.getCantidadCuotas(), MathContext.DECIMAL32));
        return credito.getMonto().multiply(financiacion.getTea().divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP)).divide(dividendo, 2, RoundingMode.HALF_UP);
    }
}
