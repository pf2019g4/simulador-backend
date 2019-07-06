package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Consecuencia;
import com.utn.simulador.negocio.simuladornegocio.domain.Cuenta;
import com.utn.simulador.negocio.simuladornegocio.domain.Operacion;
import com.utn.simulador.negocio.simuladornegocio.domain.Respuesta;

import java.math.BigDecimal;

public class ConsecuenciaBuilder extends AbstractPersistenceBuilder<Consecuencia> {

    private ConsecuenciaBuilder() {
        instance = new Consecuencia();
    }

    public static ConsecuenciaBuilder deRespuestaYCuenta(Respuesta respuesta, Cuenta cuenta) {
        ConsecuenciaBuilder consecuenciaBuilder = new ConsecuenciaBuilder();
        consecuenciaBuilder.instance.setOperacion(Operacion.SUMA);
        consecuenciaBuilder.instance.setMonto(new BigDecimal("300"));
        consecuenciaBuilder.instance.setRespuestaId(respuesta.getId());
        consecuenciaBuilder.instance.setCuentaId(cuenta.getId());

        return consecuenciaBuilder;
    }

}
