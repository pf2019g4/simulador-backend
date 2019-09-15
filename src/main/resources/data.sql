delete from consecuencia;
delete from opcion;
delete from decision;
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
delete from forecast;

insert into producto
(id, nombre  , precio) values
(1 , 'Lentes', 500.0 );

insert into estado
(id, proyecto_id, activo, caja   , ventas, costo_fijo, costo_variable, periodo, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media, es_forecast) values
(1 , NULL       , false , 11500.0, 9000.0, 200.0     , 3.5           , 0      , 150               , 1          , 200  , 0.10                    , 180                    , false      );

insert into escenario
(id, titulo       , maximos_periodos, descripcion                  , impuesto_porcentaje, estado_id) values
(1 , 'escenario 1', 5               , 'El Grupo Macri es uno de...', 0.0                , 1        );

insert into escenario
(id, titulo       , maximos_periodos, descripcion                  , impuesto_porcentaje, estado_id) values
(2 , 'escenario 2', 5               , 'El Grupo Macri es uno de...', 0.0                , 1        );
        
insert into escenario
(id, titulo       , maximos_periodos, descripcion                  , impuesto_porcentaje, estado_id) values
(3 , 'escenario 3', 5               , 'El Grupo Macri es uno de...', 0.0                , 1        );

insert into proyecto
(id, nombre      , escenario_id) values
(1 , 'Proyecto 1', 1           );

insert into estado
(proyecto_id, activo, caja    , ventas , costo_fijo, costo_variable, periodo, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media, es_forecast) values
(1          , true  , 11500.0 , 9000.0 , 200.0     , 3.5           , 0      , 150               , 1          , 200  , 0.10                    , 180                    , false      );

insert into estado
(proyecto_id, activo, caja    , ventas , costo_fijo, costo_variable, periodo, produccion_mensual, producto_id, stock, parametros_ventas_desvio, parametros_ventas_media, es_forecast) values
(1          , true  , 11500.0 , 9000.0 , 200.0     , 3.5           , 0      , 150               , 1          , 200  , 0.10                    , 180                    , true      );

insert into cuenta
(id, descripcion, tipo_cuenta, tipo_flujo_fondo, proyecto_id) values
(1 , 'Caja'     , 'ECONOMICO', null            , 1          );

insert into cuenta_periodo
(id, cuenta_id , monto, periodo, es_forecast) values
(1 , 1         , 500.0, 1      , false      ),
(2 , 1         , 400.0, 2      , false      );

insert into modalidad_cobro
(id, proyecto_id, porcentaje, offset_periodo) values
(1 , 1          , 60.0      , 0             ),
(2 , 1          , 10.0      , 1             ),
(3 , 1          , 30.0      , 2             );

insert into decision 
(id, escenario_id, descripcion                                          ) values
(1 , 1           , 'Cuanto quiere invertir en publicidad?'              ),
(2 , 1           , 'Cuanto quiere invertir en actualizar la maquinaria?'),
(3 , 1           , 'Cuanto quiere invertir en otras cosas?'             );


insert into opcion
(id, decision_id, descripcion                                                , variacion_costo_fijo, variacion_costo_variable, variacion_produccion) values
(1 , 1          , '$0'                                                       , 0                   , 0                       , 0                   ),
(2 , 1          , '$5000'                                                    , 0                   , 0                       , 0                   ),
(3 , 1          , '$10000'                                                   , 0                   , 0                       , 0                   ),
(4 , 2          , '$0'                                                       , 0                   , 0                       , 0                   ),
(5 , 2          , '$20000 -> CF -50, CV +0, P +5'                            , -50                 , 0                       , 5                   ),
(6 , 2          , '$50000 -> CF -40, CV -0.1, P +10'                         , -40                 , -0.1                    , 10                  ),
(7 , 3          , '$10000 -> CF 20, CV -0.1, P +0'                           , 20                  , -0.1                    , 0                   ),
(8 , 3          , '$20000 -> CF 10, CV -0.2, P +10'                          , 10                  , -0.2                    , 0                   ),
(9 , 3          , '$30000 -> CF 10, CV -0.3, P +0'                           , 10                  , -0.3                    , 0                   );


insert into consecuencia
(opcion_id, monto , descripcion , tipo_cuenta , periodo_inicio  , cantidad_periodos, tipo_flujo_fondo                ) values
(2        , 5000  , 'publicidad', 'FINANCIERO', 0               , 2                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS'),
(2        , -5000 , 'publicidad', 'ECONOMICO' , 0               , 2                , null                            ),
(3        , 10000 , 'publicidad', 'FINANCIERO', 0               , 2                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS'),
(3        , -10000, 'publicidad', 'ECONOMICO' , 0               , 2                , null                            ),
(5        , 20000 , 'maquinaria', 'FINANCIERO', 0               , 2                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS'),
(5        , -20000, 'maquinaria', 'ECONOMICO' , 0               , 2                , null                            ),
(6        , 50000 , 'maquinaria', 'FINANCIERO', 0               , 2                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS'),
(6        , -50000, 'maquinaria', 'ECONOMICO' , 0               , 2                , null                            ),
(7        , 10000 , 'cosas'     , 'FINANCIERO', 0               , 2                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   ),
(7        , -10000, 'cosas'     , 'ECONOMICO' , 0               , 2                , null                            ),
(8        , 20000 , 'cosas'     , 'FINANCIERO', 0               , 2                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   ),
(8        , -20000, 'cosas'     , 'ECONOMICO' , 0               , 2                , null                            ),
(9        , 30000 , 'cosas'     , 'FINANCIERO', 0               , 2                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   ),
(9        , -30000, 'cosas'     , 'ECONOMICO' , 0               , 2                , null                            );
