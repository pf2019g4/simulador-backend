package com.utn.simulador.negocio.simuladornegocio.builder;

import com.somospnt.test.builder.AbstractPersistenceBuilder;
import com.utn.simulador.negocio.simuladornegocio.domain.Producto;
import com.utn.simulador.negocio.simuladornegocio.domain.Proyecto;
import java.math.BigDecimal;

public class ProductoBuilder extends AbstractPersistenceBuilder<Producto> {

    private ProductoBuilder() {
        instance = new Producto();
    }

    public static ProductoBuilder base() {
        ProductoBuilder productoBuilder = new ProductoBuilder();
        
        productoBuilder.instance.setNombre("Nombre producto");
        productoBuilder.instance.setPrecio(new BigDecimal(1000));
        return productoBuilder;
    }
    
    

}
