CREATE TABLE IF NOT EXISTS proyecto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
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
  producto_id bigint NOT NULL,
  costo_fijo decimal(19,2),
  costo_variable decimal(19,2),
  caja decimal(19,2),
  stock bigint,
  produccion_mensual bigint,
  activo boolean NOT NULL DEFAULT FALSE,
  mes integer,
  parametros_ventas_media bigint,
  parametros_ventas_desvio decimal(10,5),
  PRIMARY KEY (id),
  FOREIGN KEY (producto_id) REFERENCES producto(id),

);

CREATE TABLE IF NOT EXISTS cuenta (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  descripcion VARCHAR(45) NOT NULL,
  tipo_cuenta varchar(20) NOT NULL,
  tipo_flujo_fondo varchar(40),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS cuenta_periodo (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cuenta_id bigint UNSIGNED not null,
  monto decimal not null,
  periodo int not null,
  PRIMARY KEY (id),
  FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);

CREATE TABLE IF NOT EXISTS decision (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(250) NOT NULL,
    proyecto_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE IF NOT EXISTS respuesta (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(45) NOT NULL,
    decision_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (decision_id) REFERENCES decision(id)
);

CREATE TABLE IF NOT EXISTS consecuencia (
    id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    monto decimal(19,2) NOT NULL,
    respuesta_id bigint,
    cuenta_id bigint,
    operacion varchar(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (respuesta_id) REFERENCES respuesta(id),
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);