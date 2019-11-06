package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import com.utn.simulador.negocio.simuladornegocio.domain.Forecast;
import com.utn.simulador.negocio.simuladornegocio.domain.ModalidadCobro;
import com.utn.simulador.negocio.simuladornegocio.domain.PonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import com.utn.simulador.negocio.simuladornegocio.domain.TipoPonderacionMercado;
import com.utn.simulador.negocio.simuladornegocio.repository.PonderacionMercadoRepository;
import com.utn.simulador.negocio.simuladornegocio.repository.ProyectoRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercadoService {

    private final EstadoService estadoService;
    private final PonderacionMercadoRepository ponderacionMercadoRepository;
    private final ForecastService forecastService;
    private final ProyectoRepository proyectoRepository;

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

        List<PonderacionMercado> ponderacionesMercadoPorModalidadCobro
                = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrdeByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.MODALIDAD_DE_COBRO);

        for (PonderacionMercado ponderacionMercado : ponderacionesMercadoPorModalidadCobro) {
            for (ModalidadCobro modalidadCobro : proyecto.getModalidadCobro()) {
                if (ponderacionMercado.getValor().intValue() == modalidadCobro.getOffsetPeriodo()) {
                    ponderacionMercadoAUtilizar = ponderacionMercado;
                    break;
                }
            }

            if (ponderacionMercadoAUtilizar != null) {
                break;
            }
        }

        if (ponderacionMercadoAUtilizar == null) {
            ponderacionMercadoAUtilizar = new PonderacionMercado();
            ponderacionMercadoAUtilizar.setAlto(0);
            ponderacionMercadoAUtilizar.setMedio(0);
            ponderacionMercadoAUtilizar.setBajo(0);
        }

        proyecto.aumentarPonderacionMercado(ponderacionMercadoAUtilizar);

    }

    private void definirPonderacionMercadoPorPublicidad(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorPublicidad = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrdeByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.PUBLICIDAD_DESDE);

        for (PonderacionMercado ponderacionMercadoPublicidad : ponderacionesMercadoPorPublicidad) {
            if (ponderacionMercadoPublicidad.getValor().compareTo(estado.getPublicidad()) <= 0) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoPublicidad);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorCalidad(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorCalidad = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrdeByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.CALIDAD_DESDE);

        for (PonderacionMercado ponderacionMercadoCalidad : ponderacionesMercadoPorCalidad) {
            if (ponderacionMercadoCalidad.getValor().intValue() < estado.getCalidad()) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoCalidad);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorVendedores(Proyecto proyecto, Estado estado) {
        List<PonderacionMercado> ponderacionesMercadoPorVendedores = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrdeByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.VENDEDORES_DESDE);

        for (PonderacionMercado ponderacionMercadoVendedores : ponderacionesMercadoPorVendedores) {
            if (ponderacionMercadoVendedores.getValor().intValue() < estado.getCantidadVendedores()) {
                proyecto.aumentarPonderacionMercado(ponderacionMercadoVendedores);
                break;
            }
        }
    }

    private void definirPonderacionMercadoPorPrecio(Proyecto proyecto) {
        List<PonderacionMercado> ponderacionesMercadoPorPrecio = ponderacionMercadoRepository.findByEscenarioIdAndConceptoOrdeByValorDesc(proyecto.getEscenario().getId(), TipoPonderacionMercado.PRECIO_DESDE);

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

    long obtenerCuotaMercado(Estado estado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
