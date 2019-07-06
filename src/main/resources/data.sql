insert into proyecto
(id , nombre        ) values 
(1  , 'Proyecto 1'  );

insert into producto 
(id, nombre  , precio, proyecto_id) values 
(1 , 'Lentes', 500.0 , 1          );


insert into estado
(activo, caja  , costo_fijo, costo_variable, mes, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media ) values
(true  , 11500.0,200.0     , 3.5           , 1  , 150               , 1          , 200  , 0.10                     , 180);

insert into cuenta
(id, descripcion  , monto , tipo_cuenta   ,  periodo, proyecto_id) values
(1 , 'Caja'       , 500.0 , 'FINANCIERO'  , 1       , 1),
(2 , 'Caja'       , 400.0 , 'FINANCIERO'  , 2       , 1);