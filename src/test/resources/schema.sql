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
