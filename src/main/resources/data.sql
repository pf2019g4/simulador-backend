delete from cuenta_periodo;
delete from opcion_proyecto;
delete from consecuencia;
delete from opcion;
delete from decision;
delete from cuenta;
delete from estado;
delete from producto;
delete from modalidad_cobro;
delete from proyecto;
delete from escenario;

insert into escenario
(id , descripcion, impuesto_porcentaje) values
(1  , 'escenario 1', 0.0);

insert into proyecto
(id , nombre       , escenario_id) values
(1  , 'Proyecto 1' , 1);

insert into producto 
(id, nombre  , precio, proyecto_id) values 
(1 , 'Lentes', 500.0 , 1          );


insert into estado
(proyecto_id, activo, caja    ,ventas , costo_fijo, costo_variable, periodo, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media ) values
(1          , true  , 11500.0 ,9000.0 ,200.0      , 3.5           , 1  , 150               , 1          , 200  , 0.10                     , 180);

insert into cuenta
(id, descripcion  , tipo_cuenta   ,TIPO_FLUJO_FONDO, proyecto_id) values
(1 , 'Caja'       , 'FINANCIERO'  , null           ,1);

insert into cuenta_periodo
(id,cuenta_id, monto, periodo) values
(1,1         , 500.0,1       ),
(2,1         , 400.0,2       );

insert into modalidad_cobro
(id, proyecto_id, porcentaje, des_periodo) values
(1 , 1          , 60.0      , 0          ),
(2 , 1          ,  0.0      , 1          ),
(3 , 1          , 40.0      , 2          );
