insert into proyecto
(id , nombre        ) values 
(1  , 'Proyecto 1'  );

insert into producto 
(id, nombre  , precio, proyecto_id) values 
(1 , 'Lentes', 500.0 , 1          );


insert into estado
(activo, caja  , costo_fijo, costo_variable, mes, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media ) values
(true  , 11500.0,200.0     , 3.5           , 1  , 150               , 1          , 200  , 180                     , 0.10);