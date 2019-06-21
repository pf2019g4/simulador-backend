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