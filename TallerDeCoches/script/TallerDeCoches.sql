
CREATE TABLE Cliente (
  DNI varchar(12),
  Apellidos varchar(255) DEFAULT NULL,
  Nombre varchar(255) DEFAULT NULL,
  PRIMARY KEY(DNI)
);

CREATE TABLE Coche(
  Matricula varchar(10) ,
  Marca varchar(25) NOT NULL,
  Modelo varchar(25) NOT NULL,
  ClientePertenece varchar(15) NOT NULL,
  PRIMARY KEY(Matricula),
  FOREIGN KEY (ClientePertenece) REFERENCES Cliente(DNI) ON DELETE CASCADE
);

CREATE TABLE Revision(
  CodigoRevision int,
  Descripcion varchar(255) NOT NULL,
  Fecha varchar(255) NOT NULL,
  PrecioRevision FLOAT,
  TipoRevision varchar(255) NOT NULL,
  Matricula_Revisiones varchar(10) NOT NULL,
  PRIMARY KEY(CodigoRevision),
  FOREIGN KEY (Matricula_Revisiones) REFERENCES Coche(Matricula) ON DELETE CASCADE
);
