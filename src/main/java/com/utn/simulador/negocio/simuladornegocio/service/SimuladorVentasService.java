package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class SimuladorVentasService {

    Estado simular(Estado estado) {

        long unidadesVendidas = calcularVentas(estado);

        estado.setStock(estado.getStock() - unidadesVendidas);
        estado.setCaja(estado.getCaja()
                .add(estado.getProducto().getPrecio()
                        .multiply(new BigDecimal(unidadesVendidas))));

        return estado;

    }

    private long calcularVentas(Estado estado) {
        return estado.getParametrosVentas().getMedia();
    }

}
