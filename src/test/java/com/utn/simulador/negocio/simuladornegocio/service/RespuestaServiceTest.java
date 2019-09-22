package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.SimuladorNegocioApplicationTests;
import com.utn.simulador.negocio.simuladornegocio.builder.DecisionBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.EscenarioBuilder;
import com.utn.simulador.negocio.simuladornegocio.builder.ProyectoBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Decision;
import com.utn.simulador.negocio.simuladornegocio.domain.Escenario;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
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

        Escenario escenario = EscenarioBuilder.base().build(em);
        Decision decision = DecisionBuilder.deEscenario(escenario).build(em);

        Opcion respuesta = new Opcion();
        respuesta.setDescripcion("Desc1");
        respuesta.setDecision(decision);

        respuestaService.guardar(respuesta);

        assertThat(respuestaRepository.count()).isEqualTo(1);
    }

}
