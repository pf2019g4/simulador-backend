CREATE TABLE proyecto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE producto (
  id bigint UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  proyecto_id bigint,
  nombre VARCHAR(45) NOT NULL,
  precio decimal(19,2),
  PRIMARY KEY (id),
  FOREIGN KEY (proyecto_id) REFERENCES proyecto(id)
);
