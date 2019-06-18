CREATE TABLE proyecto (
  id bigint UNSIGNED AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE producto (
  id bigint UNSIGNED AUTO_INCREMENT,
  proyecto_id bigint,
  nombre VARCHAR(45) NOT NULL,
  precio decimal(19,2),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);

CREATE TABLE estado (
  id bigint UNSIGNED AUTO_INCREMENT,
  producto_id bigint NOT NULL,
  costo_fijo decimal(19,2),
  costo_variable decimal(19,2),
  caja decimal(19,2),
  stock bigint,
  produccion_mensual bigint,
  mes integer,
  PRIMARY KEY (id),
  FOREIGN KEY (producto_id) REFERENCES producto(id)
);