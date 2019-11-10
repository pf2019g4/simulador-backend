package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.EmpresaCompetidora;
import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import com.utn.simulador.negocio.simuladornegocio.domain.MercadoPeriodo;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.RestriccionPrecio;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoPonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.repository.EmpresasCompetidorasRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.MercadoPeriodoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionMercadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.RestriccionPrecioRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MercadoService {

    private final EstadoService estadoService;
    private final PonderacionMercadoRepository ponderacionMercadoRepository;
    private final ForecastService forecastService;
    private final ProyectoRepository proyectoRepository;
    private final MercadoPeriodoRepository mercadoPeriodoRepository;
    private final EmpresasCompetidorasRepository empresasCompetidorasRepository;
    private final RestriccionPrecioRepository restriccionPrecioRepository;

    public void establecerPonderaciones(Proyecto proyecto) {

        Estado estado = estadoService.obtenerActual(proyecto.getId(), true);

        definirPonderacionMercadoPorPrecio(proyecto);

        definirPonderacionMercadoPorCalidad(proyecto, estado);

        definirPonderacionMercadoPorVendedores(proyecto, estado);

        definirPonderacionMercadoPorPublicidad(proyecto, estado);

        definirPonderacionMercadoPorModalidadCobro(proyecto);

        proyectoRepository.save(proyecto);

    }

    private void definirPonderacionMercadoPorModalidadCobro(Proyecto proyecto) {
        PonderacionMercado ponderacionMercadoAUtilizar = null;

        ponderacionMercadoAUtilizar = tomarModalidadCobroMasDistanteMayorAl25(proyecto, ponderacionMercadoAUtilizar);

        if (ponderacionMercadoAUtilizar == null) {
            ponderacionMercadoAUtilizar = new PonderacionMercado();
            ponderacionMercadoAUtilizar.setAlto(0);
            ponderacionMercadoAUtilizar.setMedio(0);
            ponderacionMercadoAUtilizar.setBajo(0);
        }

        proyecto.aumentarPonderacionMercado(ponderacionMercadoAUtilizar);

    }

    private PonderacionMercado tomarModalidadCobroMasDistanteMayorAl25(Proyecto proyecto, PonderacionMercado ponderacionMercadoAUtilizar) {
        List<PonderacionMercado> ponderacionesMercadoPorModalidadCobro
                = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrderByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.MODALIDAD_DE_COBRO);

        for (PonderacionMercado ponderacionMercado : ponderacionesMercadoPorModalidadCobro) {
            for (ModalidadCobro modalidadCobro : proyecto.getModalidadCobro()) {
                if (ponderacionMercado.getValor().intValue() == modalidadCobro.getOffsetPeriodo()) {
                    if (modalidadCobro.getPorcentaje().compareTo(new BigDecimal(25)) > 0) {
                        ponderacionMercadoAUtilizar = ponderacionMercado;
                        break;
                    }
                }
            }

            if (ponderacionMercadoAUtilizar != null) {
                break;
            }
        }
        return ponderacionMercadoAUtilizar;
    }

    private void definirPonderacionMercadoPorPublicidad(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorPublicidad = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrderByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.PUBLICIDAD_DESDE);

        for (PonderacionMercado ponderacionMercadoPublicidad : ponderacionesMercadoPorPublicidad) {
            if (ponderacionMercadoPublicidad.getValor().compareTo(estado.getPublicidad()) <= 0) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoPublicidad);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorCalidad(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorCalidad = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrderByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.CALIDAD_DESDE);

        for (PonderacionMercado ponderacionMercadoCalidad : ponderacionesMercadoPorCalidad) {
            if (ponderacionMercadoCalidad.getValor().intValue() <= estado.getCalidad()) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoCalidad);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorVendedores(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorVendedores = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrderByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.VENDEDORES_DESDE);

        for (PonderacionMercado ponderacionMercadoVendedores : ponderacionesMercadoPorVendedores) {
            if (ponderacionMercadoVendedores.getValor().intValue() <= estado.getCantidadVendedores()) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoVendedores);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorPrecio(Proyecto proyecto) {
        List<PonderacionMercado> ponderacionesMercadoPorPrecio = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrderByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.PRECIO_DESDE);

        List<Forecast> forecasts = forecastService.obtenerPorProyecto(proyecto.getId());

        BigDecimal sumatoriaDePrecios = BigDecimal.ZERO;
        for (Forecast forecast : forecasts) {
            sumatoriaDePrecios = sumatoriaDePrecios.add(forecast.getPrecio());
        }

        BigDecimal precioPromedio = sumatoriaDePrecios.divide(new BigDecimal(proyecto.getEscenario().getMaximosPeriodos()));

        for (PonderacionMercado ponderacionMercadoPrecio : ponderacionesMercadoPorPrecio) {
            if (ponderacionMercadoPrecio.getValor().compareTo(precioPromedio) <= 0) {

                proyecto.aumentarPonderacionMercado(ponderacionMercadoPrecio);
                break;
            }
        }
    }

    public long obtenerCuotaMercado(Estado estado) {

        Proyecto proyecto = estado.getProyecto();
        long cuotaMercado = 0;
        MercadoPeriodo mercadoPeriodo = mercadoPeriodoRepository.findByEscenarioIdAndPeriodo(proyecto.getEscenario().getId(), estado.getPeriodo());
        RestriccionPrecio restriccionPrecio = restriccionPrecioRepository.findByEscenarioId(proyecto.getEscenario().getId()).get(0);

        BigDecimal precioPeriodoActual = forecastService.obtenerPorProyectoYPeriodo(proyecto.getId(), estado.getPeriodo()).getPrecio();

        if (precioPeriodoActual.compareTo(restriccionPrecio.getPrecioMin()) < 0) {
            return 0;
        } else if (precioPeriodoActual.compareTo(restriccionPrecio.getPrecioMax()) > 0) {
            return 0;
        }

        List<EmpresaCompetidora> empresasCopetidoras = empresasCompetidorasRepository.findByEscenarioId(estado.getProyecto().getEscenario().getId());

        Integer ponderacionTotalmercadoBajo = proyecto.getPonderacionMercadoBajo();
        Integer ponderacionTotalmercadoMedio = proyecto.getPonderacionMercadoMedio();
        Integer ponderacionTotalmercadoAlto = proyecto.getPonderacionMercadoAlto();

        for (EmpresaCompetidora empresaCopetidora : empresasCopetidoras) {
            ponderacionTotalmercadoAlto += empresaCopetidora.getAlto();
            ponderacionTotalmercadoMedio += empresaCopetidora.getMedio();
            ponderacionTotalmercadoBajo += empresaCopetidora.getBajo();
        }

        cuotaMercado += calcularCuotaMercado(proyecto.getPonderacionMercadoAlto(), ponderacionTotalmercadoAlto, mercadoPeriodo.getAlto());
        cuotaMercado += calcularCuotaMercado(proyecto.getPonderacionMercadoMedio(), ponderacionTotalmercadoMedio, mercadoPeriodo.getMedio());
        cuotaMercado += calcularCuotaMercado(proyecto.getPonderacionMercadoBajo(), ponderacionTotalmercadoBajo, mercadoPeriodo.getBajo());

        return cuotaMercado;

    }

    private long calcularCuotaMercado(Integer ponderacionMercadoParaProyecto, Integer ponderacionTotalmercadoAlto, Integer tamanioMercado) {
        BigDecimal porcentajePonderacionMercadoProyecto = (new BigDecimal(ponderacionMercadoParaProyecto.toString())
                .divide(new BigDecimal(ponderacionTotalmercadoAlto.toString()), 6, RoundingMode.HALF_UP));
        long cuotaMercado = porcentajePonderacionMercadoProyecto.multiply(new BigDecimal(tamanioMercado.toString())).longValue();
        return cuotaMercado;
    }

}
