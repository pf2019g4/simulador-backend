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

        estado.setStock(estado.getStock() - unidadesVendidas);
        estado.setCaja(estado.getCaja().add(montoVendido));
        estado.setVentas(montoVendido);

        cuentaService.crearVentas(estado);

        return estado;
    }

    private long calcularUnidadesVendidas(Estado estado) {
        return estado.getParametrosVentas().getMedia();
    }

}
