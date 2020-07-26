delete from consecuencia;
delete from opcion;
delete from decision;
delete from credito;
delete from cuenta_periodo;
delete from opcion_proyecto;
delete from consecuencia;
delete from opcion;
delete from decision;
delete from cuenta;
delete from estado;
delete from forecast;
delete from modalidad_cobro;
delete from modalidad_pago;
delete from financiacion;
delete from proveedor;
delete from proyecto;
delete from usuario;
delete from curso_escenario;
delete from curso;
delete from escenario;
delete from ponderacion_mercado;
delete from mercado_periodo;
delete from restriccion_precio;
delete from empresa_competidora;
delete from ponderacion_puntaje;
delete from puntaje_proyecto;

insert into balance
(id, caja   , cuentas_por_cobrar, cuentas_por_cobrar_periodos, inventario, maquinaria, amortizacion_acumulada, proveedores, proveedores_periodos, deudas_bancarias, deudas_bancarias_periodos, capital_social, resultado_del_ejercicio) values
(1 , 50000.0, 2000              , 2                          , 1500.0    , 0         , 0                     , 0          , 0                   , 0               , 0                        , 1500           , 0                      ),
(2 , 50000.0, 0                 , 0                          , 1500.0    , 0         , 0                     , 0          , 0                   , 0               , 0                        , 1000           , 0                      );

insert into escenario
(id, titulo       , maximos_periodos, nombre_periodos, descripcion                                                                                                                                                                                                , impuesto_porcentaje, costo_fijo, costo_variable, produccion_mensual, stock,  calidad,  cantidad_vendedores,  publicidad, balance_id) values
(1 , 'escenario 1', 5               , 'Mes'          , 'El mercado de gafas para sol es un mercado de mucho potencial. \nCABA concentra mas del 20% de la venta total del país, y dónde será el territorio de competencia de las distintas marcas que competirán.', 0.35               , 200.0     , 3.5           , 150               , 200  ,  5      ,                    1,           1,          1),
(2 , 'escenario 2', 2               , 'Período'      , 'En este escenario vamos ...'                                                                                                                                                                              , 0.0                , 100.0     , 1.0           , 100               , 400  ,  2      ,                    1,           1,          2);

insert into curso
(id, nombre , clave     ) values
(1 , 'curso', 'Y2xhdmU='); --Base64 de la palabra 'clave'

insert into curso_escenario
(id, curso_id, escenario_id, abierto) values
(1 , 1       , 1           , true   ),
(2 , 1       , 2           , true   );

insert into proveedor
(id, escenario_id, nombre   , variacion_costo_variable, variacion_calidad) values
(1 , NULL        , 'Default', 0                       , 0                ),
(2 , 1           , 'Prov 1' , 2                       , 5                ),
(3 , 1           , 'Prov 1' , 3.5                     , 5                ),
(4 , 1           , 'Prov 2' , 1.5                     , 2                ),
(5 , 2           , 'Único'  , 0                       , 0                );

insert into financiacion
(id, escenario_id, descripcion, tea , cantidad_cuotas) values
(1 , 1           , 'Nación'   , 30  , 5              ),
(2 , 1           , 'Santander', 35  , 10             ),
(3 , 1           , 'Macro'    , 40  , 12              ),
(4 , 2           , 'Macro'    , 38  , 12             );

insert into modalidad_pago
(id, proveedor_id, porcentaje, offset_periodo) values
(1 , 1           , 100.00    , 0            ),
(2 , 2           , 100.00    , 0            ),
(3 , 3           , 20.00     , 0            ),
(4 , 3           , 30.00     , 1            ),
(5 , 3           , 50.00     , 2            ),
(6 , 4           , 100.00    , 1            ),
(7 , 5           , 100.00    , 0            );

insert into decision 
(id, escenario_id, descripcion                                          ) values
(1 , 1           , '¿Cuánto quiere invertir en publicidad?'              ),
(2 , 1           , '¿Cuánto quiere invertir en actualizar la maquinaria?'),
(3 , 1           , '¿Cuánto quiere invertir en otras cosas?'             );

insert into opcion
(id, decision_id, descripcion                                                , variacion_costo_fijo, variacion_costo_variable, variacion_produccion, variacion_calidad, variacion_publicidad, variacion_cantidad_vendedores) values
(1 , 1          , '$0'                                                       , 0                   , 0                       , 0                   , 0                , 0                   , 0                ),
(2 , 1          , '$5000'                                                    , 0                   , 0                       , 0                   , 1                , 0                   , 0                ),
(3 , 1          , '$10000'                                                   , 0                   , 0                       , 0                   , 1                , 0                   , 0                ),
(4 , 2          , '$0'                                                       , 0                   , 0                       , 0                   , 1                , 0                   , 0                ),
(5 , 2          , '$20000 -> CF -50, CV +0, P +5'                            , -50                 , 0                       , 5                   , 0                , 0                   , 0                ),
(6 , 2          , '$50000 -> CF -40, CV -0.1, P +10'                         , -40                 , -0.1                    , 10                  , 0                , 0                   , 0                ),
(7 , 3          , '$10000 -> CF 20, CV -0.1, P +0'                           , 20                  , -0.1                    , 0                   , 0                , 0                   , 0                ),
(8 , 3          , '$20000 -> CF 10, CV -0.2, P +10'                          , 10                  , -0.2                    , 10                  , 0                , 0                   , 0                ),
(9 , 3          , '$30000 -> CF 10, CV -0.3, P +0'                           , 10                  , -0.3                    , 0                   , 0                , 0                   , 0                );

insert into consecuencia
(id, opcion_id, monto , descripcion              , tipo_cuenta , periodo_inicio  , cantidad_periodos, tipo_flujo_fondo                , tipo_balance              ) values
(1 , 2        , 5000  , 'publicidad'             , 'FINANCIERO', 0               , 1                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', null                      ),
(2 , 2        , -5000 , 'publicidad'             , 'ECONOMICO' , 1               , 1                , null                            , null                      ),
(3 , 3        , 10000 , 'publicidad'             , 'FINANCIERO', 0               , 1                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', null                      ),
(4 , 3        , -10000, 'publicidad'             , 'ECONOMICO' , 1               , 1                , null                            , null                      ),
(5 , 5        , 20000 , 'maquinaria'             , 'FINANCIERO', 0               , 1                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', 'MAQUINARIAS'             ),
(6 , 5        , 2000  , 'depreciación maquinaria', 'FINANCIERO', 1               , 5                , 'GASTOS_NO_DESEMBOLSABLES'      , 'AMORTIZACION_MAQUINARIAS'),
(7 , 5        , -20000, 'maquinaria'             , 'ECONOMICO' , 1               , 1                , null                            , 'MAQUINARIAS'             ),
(8 , 6        , 50000 , 'maquinaria'             , 'FINANCIERO', 0               , 1                , 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', 'MAQUINARIAS'             ),
(9 , 6        , 5000  , 'depreciación maquinaria', 'FINANCIERO', 1               , 5                , 'GASTOS_NO_DESEMBOLSABLES'      , 'AMORTIZACION_MAQUINARIAS'),
(10, 6        , -50000, 'maquinaria'             , 'ECONOMICO' , 1               , 1                , null                            , 'MAQUINARIAS'             ),
(11, 7        , 10000 , 'cosas'                  , 'FINANCIERO', 0               , 1                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   , null                      ),
(12, 7        , -10000, 'cosas'                  , 'ECONOMICO' , 1               , 1                , null                            , null                      ),
(13, 8        , 20000 , 'cosas'                  , 'FINANCIERO', 0               , 1                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   , null                      ),
(14, 8        , -20000, 'cosas'                  , 'ECONOMICO' , 1               , 1                , null                            , null                      ),
(15, 9        , 30000 , 'cosas'                  , 'FINANCIERO', 0               , 1                , 'EGRESOS_AFECTOS_A_IMPUESTOS'   , null                      ),
(16, 9        , -30000, 'cosas'                  , 'ECONOMICO' , 1               , 1                , null                            , null                      );