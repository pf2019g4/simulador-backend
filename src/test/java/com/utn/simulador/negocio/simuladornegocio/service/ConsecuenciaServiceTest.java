package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.CuentaPeriodoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.DecisionBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.RespuestaBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.ConsecuenciaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsecuenciaServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private ConsecuenciaService consecuenciaService;

    @Autowired
    private ConsecuenciaRepository consecuenciaRepository;

    @Test
    public void guardar_conConsecuenciaNueva_guardaEnBD() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Decision decision = DecisionBuilder.deProyecto(proyecto).build(em);
        Respuesta respuesta = RespuestaBuilder.deDecision(decision).build(em);
        Cuenta cuenta = CuentaBuilder.deProyecto(proyecto, TipoCuenta.ECONOMICO).build(em);
        CuentaPeriodo cuentaPeriodo = CuentaPeriodoBuilder.deCuenta(cuenta, 4).build(em);
        cuenta.getCuentasPeriodo().add(cuentaPeriodo);

        Consecuencia consecuencia = new Consecuencia();
        consecuencia.setMonto(new BigDecimal("456"));
        consecuencia.setCuentaId(cuenta.getId());
        consecuencia.setRespuestaId(respuesta.getId());
        consecuencia.setOperacion(Operacion.SUMA);

        consecuenciaService.guardar(consecuencia);

        assertThat(consecuenciaRepository.count()).isEqualTo(1);
    }

}
