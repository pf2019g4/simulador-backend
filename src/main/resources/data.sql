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
delete from ponderacion_mercado;
delete from mercado_periodo;
delete from restriccion_precio;
delete from empresa_competidora;
delete from ponderacion_puntaje;
delete from puntaje_proyecto;
delete from escenario;

INSERT INTO escenario (id, titulo, descripcion, impuesto_porcentaje, maximos_periodos, nombre_periodos, costo_fijo, costo_variable, produccion_mensual, stock, calidad, cantidad_vendedores, publicidad, balance_id) VALUES
(1, 'Anteojos de Sol', 'El mercado de gafas para sol es un mercado de mucho potencial. \nCABA concentra mas del 20% de la venta total del país, y dónde será el territorio de competencia de las distintas marcas que competirán.', '0.35', 5, 'Mes', '200000.00', '3.50', 1500, 200, 5, 1, 1, 1),
(2, 'escenario 2', 'En este escenario vamos ...', '0.00', 2, 'Período', '100.00', '1.00', 100, 400, 2, 1, 1, 2);


INSERT INTO balance (id, caja, cuentas_por_cobrar, cuentas_por_cobrar_periodos, inventario, maquinaria, amortizacion_acumulada, proveedores, proveedores_periodos, deudas_bancarias, deudas_bancarias_periodos, capital_social, resultado_del_ejercicio) VALUES
(1, '80000.00', '120000.00', 4, '180000.00', '0.00', '0.00', '230000.00', 5, '50000.00', 3, '100000.00', '0.00'),
(2, '50000.00', '0.00', 0, '1500.00', '0.00', '0.00', '0.00', 0, '0.00', 0, '1000.00', '0.00'),
(3, '50000.00', '2000.00', 2, '1500.00', '0.00', '0.00', '0.00', 0, '0.00', 0, '1500.00', '0.00'),
(4, '50000.00', '0.00', 0, '1500.00', '0.00', '0.00', '0.00', 0, '0.00', 0, '1000.00', '0.00');

INSERT INTO decision (id, escenario_id, descripcion) VALUES
(1, 1, '¿Cuánto quiere invertir en publicidad?'),
(2, 1, '¿Cuánto quiere invertir en actualizar la maquinaria?'),
(3, 1, '¿Cuánto quiere invertir en otras cosas?');


INSERT INTO opcion (id, decision_id, descripcion, variacion_costo_fijo, variacion_costo_variable, variacion_produccion, variacion_calidad, variacion_publicidad, variacion_cantidad_vendedores) VALUES
(1, 1, 'No invertir en publicidad', '0.00', '0.00', 0, 0, '0.00', 0),
(2, 1, 'Inversión de $5.000', '0.00', '0.00', 0, 1, '0.00', 0),
(3, 1, 'Inversión de $10.000', '0.00', '0.00', 0, 1, '0.00', 0),
(4, 2, 'Sin inversión', '0.00', '0.00', 0, 1, '0.00', 0),
(5, 2, 'Inversión de $20.000 -> Reduce CF -50, CV +0, Aumenta la Producción en  +5 unidades', '-50.00', '0.00', 5, 0, '0.00', 0),
(6, 2, 'Inversión de $50.000 -> Reduce CF en $-40, Reduce CV en $-0.1, Aumenta la producción en +10 unidades', '-40.00', '-0.10', 10, 0, '0.00', 0),
(7, 3, '$10000 -> CF 20, CV -0.1, P +0', '20.00', '-0.10', 0, 0, '0.00', 0),
(8, 3, '$20000 -> CF 10, CV -0.2, P +10', '10.00', '-0.20', 10, 0, '0.00', 0),
(9, 3, '$30000 -> CF 10, CV -0.3, P +0', '10.00', '-0.30', 0, 0, '0.00', 0),
(10, 3, 'Sin inversión', '0.00', '0.00', 0, 0, '0.00', 0);

INSERT INTO consecuencia (id, opcion_id, monto, descripcion, periodo_inicio, cantidad_periodos, tipo_cuenta, tipo_flujo_fondo, tipo_balance) VALUES
(1, 2, '5000.00', 'publicidad', 0, 1, 'FINANCIERO', 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', NULL),
(2, 2, '-5000.00', 'publicidad', 1, 1, 'ECONOMICO', NULL, NULL),
(3, 3, '10000.00', 'publicidad', 0, 1, 'FINANCIERO', 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', NULL),
(4, 3, '-10000.00', 'publicidad', 1, 1, 'ECONOMICO', NULL, NULL),
(5, 5, '20000.00', 'maquinaria', 0, 1, 'FINANCIERO', 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', 'MAQUINARIAS'),
(6, 5, '2000.00', 'depreciación maquinaria', 1, 5, 'FINANCIERO', 'GASTOS_NO_DESEMBOLSABLES', 'AMORTIZACION_MAQUINARIAS'),
(7, 5, '-20000.00', 'maquinaria', 1, 1, 'ECONOMICO', NULL, 'MAQUINARIAS'),
(8, 6, '50000.00', 'maquinaria', 0, 1, 'FINANCIERO', 'EGRESOS_NO_AFECTOS_A_IMPUESTOS', 'MAQUINARIAS'),
(9, 6, '5000.00', 'depreciación maquinaria', 1, 5, 'FINANCIERO', 'GASTOS_NO_DESEMBOLSABLES', 'AMORTIZACION_MAQUINARIAS'),
(10, 6, '-50000.00', 'maquinaria', 1, 1, 'ECONOMICO', NULL, 'MAQUINARIAS'),
(11, 7, '10000.00', 'cosas', 0, 1, 'FINANCIERO', 'EGRESOS_AFECTOS_A_IMPUESTOS', NULL),
(12, 7, '-10000.00', 'cosas', 1, 1, 'ECONOMICO', NULL, NULL),
(13, 8, '20000.00', 'cosas', 0, 1, 'FINANCIERO', 'EGRESOS_AFECTOS_A_IMPUESTOS', NULL),
(14, 8, '-20000.00', 'cosas', 1, 1, 'ECONOMICO', NULL, NULL),
(15, 9, '30000.00', 'cosas', 0, 1, 'FINANCIERO', 'EGRESOS_AFECTOS_A_IMPUESTOS', NULL),
(16, 9, '-30000.00', 'cosas', 1, 1, 'ECONOMICO', NULL, NULL);


INSERT INTO curso (id, nombre, clave) VALUES
(1, 'K5152', 'Y2xhdmU='),
(2, 'K5252', 'Q2xhdmU=');


INSERT INTO curso_escenario (id, curso_id, escenario_id, abierto) VALUES
(1, 1, 1, 1),
(2, 1, 2, 1),
(3, 2, 1, 1);


INSERT INTO empresa_competidora (id, escenario_id, nombre, bajo, medio, alto) VALUES
(1, 1, 'RayBan', 0, 30, 70),
(2, 1, 'Adidas', 20, 60, 20),
(3, 1, 'La sureña', 100, 0, 0);



INSERT INTO financiacion (id, descripcion, escenario_id, tea, cantidad_cuotas) VALUES
(1, 'Nación', 1, '30.00', 5),
(2, 'Santander', 1, '35.00', 10),
(3, 'Macro', 1, '40.00', 12),
(4, 'Macro', 2, '38.00', 12);


INSERT INTO mercado_periodo (id, escenario_id, periodo, bajo, medio, alto) VALUES
(1, 1, 1, 1000, 1000, 80),
(2, 1, 2, 1000, 50, 80),
(3, 1, 3, 1000, 50, 60),
(4, 1, 4, 1000, 1000, 80),
(5, 1, 5, 1000, 1000, 30);


INSERT INTO proveedor (id, nombre, escenario_id, variacion_costo_variable, variacion_calidad) VALUES
(1, 'Default', NULL, '0.00', 0),
(2, 'RayBan', 1, '3.00', 5),
(3, 'Importador China', 1, '1.50', 2),
(4, 'Gafas SRL', 1, '1.50', 1),
(5, 'Único', 2, '0.00', 0);

INSERT INTO modalidad_pago (id, proveedor_id, porcentaje, offset_periodo) VALUES
(1, 1, '100.00', 0),
(7, 5, '100.00', 0),
(16, 4, '0.00', 0),
(17, 4, '30.00', 1),
(18, 4, '30.00', 2),
(19, 4, '40.00', 3),
(20, 2, '30.00', 0),
(21, 2, '30.00', 1),
(22, 2, '40.00', 2),
(23, 2, '0.00', 3),
(24, 3, '80.00', 0),
(25, 3, '20.00', 1),
(26, 3, '0.00', 2),
(27, 3, '0.00', 3);




INSERT INTO ponderacion_mercado (id, escenario_id, concepto, valor, bajo, medio, alto) VALUES
(1, 1, 'PRECIO_DESDE', '100.00', 100, 0, 0),
(2, 1, 'PRECIO_DESDE', '700.00', 0, 30, 70),
(3, 1, 'PRECIO_DESDE', '1200.00', 0, 0, 100),
(4, 1, 'MODALIDAD_DE_COBRO', '0.00', 0, 0, 0),
(5, 1, 'MODALIDAD_DE_COBRO', '1.00', 0, 0, 0),
(6, 1, 'MODALIDAD_DE_COBRO', '2.00', 0, 0, 0),
(7, 1, 'MODALIDAD_DE_COBRO', '3.00', 0, 0, 0),
(8, 1, 'PUBLICIDAD_DESDE', '0.00', 0, 0, 0),
(9, 1, 'PUBLICIDAD_DESDE', '0.00', 0, 0, 0),
(10, 1, 'PUBLICIDAD_DESDE', '0.00', 0, 0, 0),
(11, 1, 'CALIDAD_DESDE', '0.00', 0, 0, 0),
(12, 1, 'CALIDAD_DESDE', '0.00', 0, 0, 0),
(13, 1, 'CALIDAD_DESDE', '0.00', 0, 0, 0),
(14, 1, 'VENDEDORES_DESDE', '0.00', 0, 0, 0),
(15, 1, 'VENDEDORES_DESDE', '0.00', 0, 0, 0),
(16, 1, 'VENDEDORES_DESDE', '0.00', 0, 0, 0);


INSERT INTO ponderacion_puntaje (id, escenario_id, porcentaje_caja, porcentaje_ventas, porcentaje_renta, porcentaje_escenario) VALUES
(2, 1, '30.00', '50.00', '20.00', '10.00');

INSERT INTO restriccion_precio (id, escenario_id, precio_min, precio_max) VALUES
(1, 1, '100.00', '1300.00');

INSERT INTO usuario (id, mail, foto_url, nombre_completo, rol, curso_id) VALUES
(1, 'william.herrera89@gmail.com', 'https://lh3.googleusercontent.com/a-/AAuE7mCm6flrrzylkheBAHC3wkcq4xEI-55TUO-WzDAwMg', 'William Herrera', 'JUGADOR', 1),
(2, 'fperezfagonde@gmail.com', 'https://lh6.googleusercontent.com/-kq5wLY03j0U/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3reYQ4qEpytZoooiWqF8axxq7EbxRQ/photo.jpg', 'Federico Pérez Fagonde', 'JUGADOR', 1),
(3, 'pepe@gmail.com', NULL, 'pepe@gmail.com', 'JUGADOR', 1),
(4, 'laureano.clausi@gmail.com', 'https://lh3.googleusercontent.com/a-/AAuE7mBKqgkjOPjWbTk6KYllZ7lHYJzAfo3boZ5CsU5T_g', 'Laureano Gabriel Clausi', 'JUGADOR', 1),
(5, 'santiago.hgarcia94@gmail.com', 'https://lh3.googleusercontent.com/a-/AAuE7mCgvBK688UVhuKjHhCa0PNYodLAeEgRfb8VrSy3cA', 'Santiago Garcia', 'JUGADOR', 1);
