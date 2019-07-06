package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.DecisionBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Respuesta;
import com.utn.simulador.negocio.simuladornegocio.repository.RespuestaRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class RespuestaServiceTest extends SimuladorNegocioApplicationTests {

    @Autowired
    private RespuestaService respuestaService;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Test
    public void guardar_conRespuestaNueva_guardaEnBD() {

        Proyecto proyecto = ProyectoBuilder.proyectoAbierto().build(em);
        Decision decision = DecisionBuilder.deProyecto(proyecto).build(em);

        Respuesta respuesta = new Respuesta();
        respuesta.setDescripcion("Desc1");
        respuesta.setDecisionId(decision.getId());

        respuestaService.guardar(respuesta);

        assertThat(respuestaRepository.count()).isEqualTo(1);
    }

}
