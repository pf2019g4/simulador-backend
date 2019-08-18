package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorVentasService {

    private final CuentaService cuentaService;

    Estado simular(Estado estado) {
        long unidadesVendidas = calcularUnidadesVendidas(estado);

        BigDecimal montoVendido = estado.getProducto().getPrecio()
                .multiply(new BigDecimal(unidadesVendidas));
//        TODO: tato mira esto, corre los test cuando agregues funcionalidad para ver que no rompiste otra cosa.
//        BigDecimal montoVendido = calcularMontoVendido(estado, unidadesVendidas);

        estado.setStock(estado.getStock() - unidadesVendidas);
        estado.setCaja(estado.getCaja().add(montoVendido));
        estado.setVentas(montoVendido);

        cuentaService.crearVentas(estado);

        return estado;
    }

    private long calcularUnidadesVendidas(Estado estado) {
        return estado.getParametrosVentas().getMedia();
    }

    private BigDecimal calcularMontoVendido(Estado estado, Long unidadesVendidas) {
        Integer periodo = estado.getPeriodo();
        BigDecimal montoVendido = BigDecimal.ZERO;

        while (periodo >= 1) { //TODO: tiene que cambiar también las unidadesVendidas

            BigDecimal porcentajeVentas = BigDecimal.ZERO;
            if (estado.getProyecto().getModalidadCobro().size() > estado.getPeriodo() - periodo) {
                porcentajeVentas = estado.getProyecto().getModalidadCobro().get(estado.getPeriodo() - periodo).getPorcentaje().divide(new BigDecimal(100));
            }
            BigDecimal precio = estado.getProducto().getPrecio();
            montoVendido = montoVendido.add(precio.multiply(new BigDecimal(unidadesVendidas)).multiply(porcentajeVentas));

            periodo = periodo - 1;
        }

        return montoVendido;
    }

}
