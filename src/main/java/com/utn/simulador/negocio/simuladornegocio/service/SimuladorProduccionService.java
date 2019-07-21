package com.utn.simulador.negocio.simuladornegocio.service;

import com.utn.simulador.negocio.simuladornegocio.domain.Estado;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimuladorProduccionService {

    private final CuentaService cuentaService;

    Estado simular(Estado estado) {
        aumentarStock(estado);
        imputarGastosProduccion(estado);
        return estado;
    }

    private void imputarGastosProduccion(Estado estado) {
        BigDecimal costoProduccionPeriodo
                = estado.getCostoVariable().multiply(new BigDecimal(estado.getProduccionMensual()))
                        .add(estado.getCostoFijo());

        estado.setCaja(estado.getCaja()
                .subtract(costoProduccionPeriodo));

        cuentaService.crearProduccion(estado, costoProduccionPeriodo);
    }

    private void aumentarStock(Estado estado) {
        estado.setStock(estado.getStock() + estado.getProduccionMensual());
    }
}
