package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.*;
import com.utn.simulador.negocio.simuladornegocio.domain.*;
import com.utn.simulador.negocio.simuladornegocio.repository.CuentaRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.EstadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.vo.DecisionVo;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DecisionServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private DecisionService decisionService;

    @Autowired
    private OpcionProyectoRepository opcionProyectoRepository;

    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void obtenerPorProyecto_porProyectoValido_devuelveDecisionesConSusRespuestasYConsecuencias() {

        Escenario escenario = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        OpcionBuilder.deDecisionMaquinaria(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Proyecto proyecto = ProyectoBuilder.proyectoConEscenario(escenario).build(em);

        List<DecisionVo> decisiones = decisionService.obtenerPorProyecto(proyecto.getId());
        assertThat(decisiones).hasSize(1);
        assertThat(decisiones.get(0).getOpciones()).hasSize(1);
        assertThat(decisiones.get(0).getOpciones().get(0).getConsecuencias()).hasSize(2);

    }

    @Test
    public void tomarDecision_dosConsecuenciasConDosIngresosCadaUna() {

        Escenario escenario = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        Opcion opcion = OpcionBuilder.deDecisionMaquinaria(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        Estado estadoAntes = estadoRepository.findByProyectoIdAndActivoTrue(proyecto.getId());
        em.detach(estadoAntes);
        long cantidadDecisionesTomadasAntes = opcionProyectoRepository.count();
        long cuentasAntes = cuentaRepository.count();

        decisionService.tomaDecision(proyecto.getId(), opcion.getId());

        Estado estadoDespues = estadoRepository.findByProyectoIdAndActivoTrue(proyecto.getId());
        long cuentasDespues = cuentaRepository.count();
        long cantidadDecisionesTomadasDespues = opcionProyectoRepository.count();

        assertThat(cuentasDespues).isEqualTo(cuentasAntes + (opcion.getConsecuencias().size()));
        assertThat(cantidadDecisionesTomadasDespues).isEqualTo(cantidadDecisionesTomadasAntes + 1);
        assertThat(estadoDespues.getCostoFijo()).isEqualTo(estadoAntes.getCostoFijo().subtract(BigDecimal.TEN));
        assertThat(estadoDespues.getCostoVariable()).isEqualTo(estadoAntes.getCostoVariable().subtract(BigDecimal.TEN));
        assertThat(estadoDespues.getProduccionMensual()).isEqualTo(estadoAntes.getProduccionMensual() + 10L);
    }

    @Test
    public void tomarDecision_opcionNoPerteneceAlEscenario() {

        Escenario escenarioDeLaDecision = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenarioDeLaDecision).build(em);

        Opcion opcion = OpcionBuilder.deDecisionMaquinaria(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Escenario escenarioDelProyecto = EscenarioBuilder.base().build(em);
        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenarioDelProyecto).build(em);

        long cantidadDecisionesTomadasAntes = opcionProyectoRepository.count();
        long cuentasAntes = cuentaRepository.count();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> decisionService.tomaDecision(proyecto.getId(), opcion.getId()));

        long cuentasDespues = cuentaRepository.count();
        long cantidadDecisionesTomadasDespues = opcionProyectoRepository.count();

        assertThat(cuentasDespues).isEqualTo(cuentasAntes);
        assertThat(cantidadDecisionesTomadasDespues).isEqualTo(cantidadDecisionesTomadasAntes);

    }

    @Test
    public void tomarDecision_decisionYaTomada() {

        Escenario escenario = EscenarioBuilder.base().build(em);

        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        Opcion opcion = OpcionBuilder.deDecisionMaquinaria(decision)
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .conConsecuencia(ConsecuenciaBuilder.financieraIngresoNoAfectoAImpuesto(BigDecimal.ONE).build())
                .build(em);

        Proyecto proyecto = ProyectoBuilder.proyectoConProductoYEstadoInicial(escenario).build(em);

        OpcionProyectoBuilder.deOpcionYProyecto(opcion, proyecto).build(em);

        long cantidadDecisionesTomadasAntes = opcionProyectoRepository.count();
        long cuentasAntes = cuentaRepository.count();

//       TODO agregar nuevo chequeo que valide que se borro toda la info asociada a una vieja decision!
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> decisionService.tomaDecision(proyecto.getId(), opcion.getId()));

        long cuentasDespues = cuentaRepository.count();
        long cantidadDecisionesTomadasDespues = opcionProyectoRepository.count();

        assertThat(cuentasDespues).isEqualTo(cuentasAntes);
        assertThat(cantidadDecisionesTomadasDespues).isEqualTo(cantidadDecisionesTomadasAntes);

    }

}
