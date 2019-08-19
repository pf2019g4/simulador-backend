package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class DecisionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private DecisionService decisionService;

    @Autowired
    private OpcionProyectoRepository opcionProyectoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void obtenerPorProyecto_porProyectoValido_devuelveDecisionesConSusRespuestasYConsecuencias() {

        Escenario escenario = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        OpcionBuilder.deDecision(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Proyecto proyecto = ProyectoBuilder.proyectoDeEscenario(escenario).build(em);

        List<DecisionVo> decisiones = decisionService.obtenerPorProyecto(proyecto.getId());
        assertThat(decisiones).hasSize(1);
        assertThat(decisiones.get(0).getOpciones()).hasSize(1);
        assertThat(decisiones.get(0).getOpciones().get(0).getConsecuencias()).hasSize(2);

    }

    @Test
    public void tomarDecision_escenarioValido() {

        Escenario escenario = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        Opcion opcion = OpcionBuilder.deDecision(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        
        
        long cantidadDecisionesTomadasAntes = opcionProyectoRepository.count();
        long cuentasAntes = cuentaRepository.count();

        decisionService.tomaDecision(proyecto.getId(), opcion.getId());

        long cuentasDespues = cuentaRepository.count();
        long cantidadDecisionesTomadasDespues = opcionProyectoRepository.count();

        assertThat(cuentasDespues).isEqualTo(cuentasAntes + opcion.getConsecuencias().size());
        assertThat(cantidadDecisionesTomadasDespues).isEqualTo(cantidadDecisionesTomadasAntes + 1);

    }

}
