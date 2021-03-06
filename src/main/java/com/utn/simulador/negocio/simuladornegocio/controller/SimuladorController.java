package com.utn.simulador.negocio.simuladornegocio.controller;

import com.utn.simulador.negocio.simuladornegocio.domain.Opcion;
import com.utn.simulador.negocio.simuladornegocio.domain.OpcionProyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoCuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoFlujoFondo;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.repository.OpcionProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.service.CuentaService;
import com.utn.simulador.negocio.simuladornegocio.service.DecisionService;
import com.utn.simulador.negocio.simuladornegocio.service.FinanciacionService;
import com.utn.simulador.negocio.simuladornegocio.service.ProyectoService;
import com.utn.simulador.negocio.simuladornegocio.service.PuntajeService;
import com.utn.simulador.negocio.simuladornegocio.service.SimuladorService;
import com.utn.simulador.negocio.simuladornegocio.service.EscenarioService;
import com.utn.simulador.negocio.simuladornegocio.service.MercadoService;
import com.utn.simulador.negocio.simuladornegocio.service.ProveedorService;
import java.util.List;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@Transactional
public class SimuladorController {

    private final DecisionService decisionService;
    private final SimuladorService simuladorService;
    private final FinanciacionService financiacionService;
    private final CuentaService cuentaService;
    private final ProyectoService proyectoService;
    private final OpcionProyectoRepository opcionProyectoRepository;
    private final PuntajeService puntajeService;
    private final EscenarioService escenarioService;
    private final ProveedorService proveedorService;
    private final MercadoService mercadoService;

    @GetMapping("/tipoFlujoFondos")
    public List<TipoFlujoFondo> getTipoFlujoFondos() {
        return Arrays.asList(TipoFlujoFondo.values());
    }

    @GetMapping("/tipoCuentas")
    public List<TipoCuenta> getTipoCuentas() {
        return Arrays.asList(TipoCuenta.values());
    }

    @PostMapping("/proyecto/{proyectoId}/simular-forecast")
    public void simularForecast(@PathVariable("proyectoId") Long proyectoId,
            @RequestBody List<Opcion> opciones) {
        Proyecto proyecto = proyectoService.obtenerProyecto(proyectoId);

        boolean esForecast = true;

        if (!proyecto.getEntregado()) {
            simuladorService.deshacerSimulacionPrevia(proyectoId);
            simuladorService.crearPrimerEstadoSimulacion(proyectoId, esForecast);
            for (Opcion opcion : opciones) {
                decisionService.tomaDecision(proyectoId, opcion.getId(), esForecast);
            }
            proveedorService.aplicarCambiosAtributos(proyectoId, esForecast);
            cuentaService.crearPorBalanceInicial(proyectoId, esForecast);
            financiacionService.acreditar(proyectoId, esForecast);
            simuladorService.simularPeriodos(proyectoId, esForecast);
        }
    }

    @PostMapping("/escenario/{escenarioId}/curso/{cursoId}/simular-mercado")
    public void simularMercado(@PathVariable("escenarioId") Long escenarioId,
            @PathVariable("cursoId") Long cursoId) {

        proyectoService.cerrarProyectos(cursoId, escenarioId);
        correrSimulacionProyectos(cursoId, escenarioId);
        escenarioService.cerrarCursoEscenario(cursoId, escenarioId);
        puntajeService.calcularPuntajesTotales(cursoId, escenarioId);
    }

    private void correrSimulacionProyectos(Long cursoId, Long escenarioId) {

        List<Proyecto> proyectosASimular = proyectoService.obtenerPorCursoYEscenario(cursoId, escenarioId);

        boolean esForecast = false;
        prepararEscenarioParaSimularMercado(proyectosASimular, esForecast);
        simularMercadoYCalcularPuntaje(proyectosASimular, esForecast);

    }

    private void simularMercadoYCalcularPuntaje(List<Proyecto> proyectosASimular, boolean esForecast) {
        for (Proyecto proyecto : proyectosASimular) {
            simuladorService.simularPeriodos(proyecto.getId(), esForecast);
            puntajeService.calcularPuntajesProyecto(proyecto.getId());
        }
    }

    private void prepararEscenarioParaSimularMercado(List<Proyecto> proyectosASimular, boolean esForecast) {
        for (Proyecto proyecto : proyectosASimular) {
            
            if (proyecto.getProveedorSeleccionado() == null) {
                proyecto.setProveedorSeleccionado(proveedorService.obtenerProveedorPorDefecto());
            }
            
            List<OpcionProyecto> opcionesProyecto = opcionProyectoRepository.findByProyectoId(proyecto.getId());
          
            for (OpcionProyecto opcionProyecto : opcionesProyecto) {
                decisionService.tomaDecision(proyecto.getId(), opcionProyecto.getOpcion().getId(), esForecast);
            }
            proveedorService.aplicarCambiosAtributos(proyecto.getId(), esForecast);
            cuentaService.crearPorBalanceInicial(proyecto.getId(), esForecast);
            financiacionService.acreditar(proyecto.getId(), esForecast);
            mercadoService.establecerPonderaciones(proyecto);
            
        }
    }

}
