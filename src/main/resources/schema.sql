CREATE TABLE IF NOT EXISTS escenario (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  descripcion VARCHAR(300) NOT NULL,
  impuesto_porcentaje DECIMAL NOT NULL,
  maximos_periodos integer NOT NULL,
  estado_id bigint NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS proyecto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  escenario_id bigint,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);
CREATE TABLE IF NOT EXISTS forecast (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  periodo integer not null,
  cantidad_unidades bigint not null,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS producto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  nombre VARCHAR(45) NOT NULL,
  precio decimal(19,2),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS estado (
  id bigint UNSIGNED AUTO_INCREMENT,
  proyecto_id bigint NULL,
  producto_id bigint NULL,
  costo_fijo decimal(19,2),
  costo_variable decimal(19,2),
  caja decimal(19,2),
  ventas decimal(19,2),
  stock bigint,
  produccion_mensual bigint,
  es_forecast boolean NOT NULL default false,
  activo boolean NOT NULL DEFAULT FALSE,
  periodo integer,
  parametros_ventas_media bigint,
  parametros_ventas_desvio decimal(10,5),
  PRIMARY KEY (id),
  FOREIGN KEY (producto_id) REFERENCES producto(id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id),

);

CREATE TABLE IF NOT EXISTS decision (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(250) NOT NULL,
    escenario_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (escenario_id) REFERENCES escenario(id)
);

CREATE TABLE IF NOT EXISTS opcion (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(45) NOT NULL,
    decision_id bigint,
    variacion_costo_fijo decimal(19,2) default 0,
    variacion_costo_variable decimal(19,2) default 0,
    variacion_produccion bigint default 0,
    PRIMARY KEY (id),
    FOREIGN KEY (decision_id) REFERENCES decision(id)
);

CREATE TABLE IF NOT EXISTS cuenta (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  opcion_id bigint NULL,
  descripcion VARCHAR(45) NOT NULL,
  tipo_cuenta varchar(20) NOT NULL,
  tipo_flujo_fondo varchar(40),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id),
  FOREIGN KEY (opcion_id) REFERENCES opcion(id)
);

CREATE TABLE IF NOT EXISTS cuenta_periodo (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cuenta_id bigint UNSIGNED not null,
  monto decimal not null,
  periodo int not null,
  es_forecast boolean default false,
  PRIMARY KEY (id),
  FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

CREATE TABLE IF NOT EXISTS consecuencia (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    monto decimal(19,2) NOT NULL,
    descripcion VARCHAR(45) NOT NULL,
    opcion_id bigint,
    periodo_inicio int not null,
    cantidad_periodos int not null,
    tipo_cuenta varchar(20) NOT NULL,
    tipo_flujo_fondo varchar(40),
    PRIMARY KEY (id),
    FOREIGN KEY (opcion_id) REFERENCES opcion(id)
);

CREATE TABLE IF NOT EXISTS opcion_proyecto (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    opcion_id bigint,
    proyecto_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(id),
    FOREIGN KEY (opcion_id) REFERENCES opcion(id)
);



CREATE TABLE IF NOT EXISTS modalidad_cobro (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  porcentaje decimal not null,
  offset_periodo int not null,
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

--ALTER TABLE escenario ADD FOREIGN KEY (estado_id) REFERENCES estado(id);
