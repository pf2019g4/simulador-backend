CREATE TABLE IF NOT EXISTS escenario (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(300) NOT NULL,
  descripcion VARCHAR(1024) NOT NULL,
  impuesto_porcentaje decimal(19,2) NOT NULL,
  maximos_periodos integer NOT NULL,
  nombre_periodos VARCHAR(45),
  costo_fijo decimal(19,2),
  costo_variable decimal(19,2),
  produccion_mensual bigint,
  stock bigint,
  calidad integer,
  balance_id bigint NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS balance (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  caja decimal(19,2) not null DEFAULT 0,
  cuentas_por_cobrar decimal(19,2) not null DEFAULT 0,
  cuentas_por_cobrar_periodos integer not null DEFAULT 0,
  inventario decimal(19,2) not null DEFAULT 0,
  maquinaria decimal(19,2) not null DEFAULT 0,
  amortizacion_acumulada decimal(19,2) not null DEFAULT 0,
  proveedores decimal(19,2) not null DEFAULT 0,
  proveedores_periodos integer not null DEFAULT 0,
  deudas_bancarias decimal(19,2)  not null DEFAULT 0,
  deudas_bancarias_periodos integer  not null DEFAULT 0,
  capital_social decimal(19,2) not null DEFAULT 0,
  resultado_del_ejercicio decimal(19,2) not null DEFAULT 0,
  PRIMARY KEY (id),
);

CREATE TABLE IF NOT EXISTS proveedor (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(45) NOT NULL,
  escenario_id bigint UNSIGNED,
  variacion_costo_variable decimal(19,2) NOT NULL,
  variacion_calidad integer NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);

CREATE TABLE IF NOT EXISTS financiacion (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(45) NOT NULL,
  escenario_id bigint UNSIGNED,
  tea decimal(5,2) NOT NULL,
  cantidad_cuotas integer NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);

CREATE TABLE IF NOT EXISTS curso (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(20) NOT NULL,
  clave VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS usuario (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  mail VARCHAR(60) NOT NULL,
  rol VARCHAR(20) NOT NULL,
  curso_id bigint,
  FOREIGN KEY (curso_id) REFERENCES curso(id)

);

CREATE TABLE IF NOT EXISTS curso_escenario (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  curso_id bigint NOT NULL,
  escenario_id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (curso_id) REFERENCES curso(id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);

CREATE TABLE IF NOT EXISTS proyecto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  escenario_id bigint,
  usuario_id bigint,
  proveedor_id bigint NULL,
  nombre VARCHAR(45) NOT NULL,
  entregado boolean default false,
  PRIMARY KEY (id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id),
  FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);

CREATE TABLE IF NOT EXISTS credito (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id  bigint UNSIGNED,
  financiacion_id  bigint UNSIGNED,
  monto decimal(19,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id),
  FOREIGN KEY (financiacion_id) REFERENCES financiacion(id)
);


CREATE TABLE IF NOT EXISTS estado (
  id bigint UNSIGNED AUTO_INCREMENT,
  proyecto_id bigint NULL,
  costo_fijo decimal(19,2),
  costo_variable decimal(19,2),
  caja decimal(19,2),
  ventas decimal(19,2),
  capital_social decimal(19,2),
  demanda_potencial decimal(19,2),
  stock bigint,
  produccion_mensual bigint,
  calidad integer,
  es_forecast boolean NOT NULL DEFAULT FALSE,
  activo boolean NOT NULL DEFAULT FALSE,
  periodo integer,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS decision (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  escenario_id bigint,
  descripcion VARCHAR(450) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);

CREATE TABLE IF NOT EXISTS opcion (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  decision_id bigint not null,
  descripcion VARCHAR(200) NOT NULL,
  variacion_costo_fijo decimal(19,2) default 0,
  variacion_costo_variable decimal(19,2) default 0,
  variacion_produccion bigint default 0,
  variacion_calidad integer default 0,
  PRIMARY KEY (id),
  FOREIGN KEY (decision_id) REFERENCES decision(id)
);

CREATE TABLE IF NOT EXISTS opcion_proyecto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  opcion_id bigint,
  proyecto_id bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (opcion_id) REFERENCES opcion(id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS consecuencia (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  opcion_id bigint,
  monto decimal(19,2) NOT NULL,
  descripcion VARCHAR(45) NOT NULL,
  periodo_inicio int not null,
  cantidad_periodos int not null,
  tipo_cuenta varchar(20) NOT NULL,
  tipo_flujo_fondo varchar(40),
  tipo_balance varchar(40),
  PRIMARY KEY (id),
  FOREIGN KEY (opcion_id) REFERENCES opcion(id)
);

CREATE TABLE IF NOT EXISTS cuenta (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  opcion_id bigint NULL,
  descripcion VARCHAR(45) NOT NULL,
  tipo_cuenta varchar(20) NOT NULL,
  tipo_flujo_fondo varchar(40),
  tipo_transaccion varchar(40),
  tipo_balance varchar(40),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id),
  FOREIGN KEY (opcion_id) REFERENCES opcion(id)
);

CREATE TABLE IF NOT EXISTS cuenta_periodo (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cuenta_id bigint UNSIGNED not null,
  monto decimal(19,2) not null,
  periodo int not null,
  PRIMARY KEY (id),
  FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

CREATE TABLE IF NOT EXISTS forecast (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  periodo integer not null,
  cantidad_unidades bigint not null,
  precio decimal(19,2) not null,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS modalidad_cobro (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  porcentaje decimal(19,2) not null,
  offset_periodo int not null,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS modalidad_pago (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proveedor_id bigint,
  porcentaje decimal(19,2) not null,
  offset_periodo int not null,
  PRIMARY KEY (id),
  FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);

--ALTER TABLE escenario ADD FOREIGN KEY (estado_id) REFERENCES estado(id);
