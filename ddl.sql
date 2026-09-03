-- fund_tracker_project.activo definition

CREATE TABLE `activo` (
  `id_activo` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) DEFAULT NULL,
  `gestora` varchar(50) DEFAULT NULL,
  `isin` varchar(30) DEFAULT NULL,
  `tipo_activo` enum('FONDO','ETF','ACCION') DEFAULT NULL,
  `ticker` varchar(20) NOT NULL,
  PRIMARY KEY (`id_activo`),
  UNIQUE KEY `ticker` (`ticker`),
  UNIQUE KEY `isin` (`isin`)
) ENGINE=InnoDB AUTO_INCREMENT=16736 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--he anhadido la columna ticker yahoo, exchange y mic_code para poder usar la libreria de yahoo finance y obtener los precios de los activos de forma automatica
ALTER TABLE activo ADD COLUMN ticker_yahoo varchar(10) DEFAULT NULL,
ADD COLUMN exchange varchar(50) DEFAULT NULL,
ADD COLUMN mic_code varchar(10) DEFAULT NULL;

--quito la fila isin de los fondos ya que se guardan como ticker en los fondos y no se usan
ALTER TABLE activo DROP COLUMN isin;

-- fund_tracker_project.usuarios definition

CREATE TABLE `usuarios` (
  `nombre` varchar(30) NOT NULL,
  `apellidos` varchar(80) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `contrasenha` varchar(20) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `usuarios_dni` (`dni`),
  UNIQUE KEY `usuarios_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- fund_tracker_project.linea_cartera definition

CREATE TABLE `linea_cartera` (
  `id_usuario` bigint(20) NOT NULL,
  `id_activo` bigint(20) NOT NULL,
  `participaciones` decimal(15,4) NOT NULL DEFAULT 0.0000,
  `importe` decimal(15,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_usuario`,`id_activo`),
  KEY `fk_linea_cartera_activo` (`id_activo`),
  CONSTRAINT `fk_linea_cartera_activo` FOREIGN KEY (`id_activo`) REFERENCES `activo` (`id_activo`),
  CONSTRAINT `fk_linea_cartera_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- fund_tracker_project.operacion definition

CREATE TABLE `operacion` (
  `id_operacion` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_usuario` bigint(20) NOT NULL,
  `id_activo` bigint(20) NOT NULL,
  `tipo_operacion` enum('ALTA_INICIAL','COMPRA','VENTA') NOT NULL,
  `fecha` date NOT NULL,
  `participaciones` decimal(15,4) NOT NULL,
  `precio_unitario` decimal(15,4) NOT NULL,
  `cantidad` decimal(15,4) NOT NULL,
  `rentabilidad` decimal(15,4) NOT NULL,
  PRIMARY KEY (`id_operacion`),
  KEY `fk_operacion_linea_cartera` (`id_usuario`,`id_activo`),
  CONSTRAINT `fk_operacion_linea_cartera` FOREIGN KEY (`id_usuario`, `id_activo`) REFERENCES `linea_cartera` (`id_usuario`, `id_activo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--incluimos estadoOperacion posteriormente
ALTER TABLE operacion
ADD COLUMN estado ENUM('CANCELADA', 'EN_PROCESO', 'EJECUTADA') NOT NULL DEFAULT 'EJECUTADA';

-- fund_tracker_project.precio_activo definition

CREATE TABLE `precio_activo` (
  `id_activo` bigint(20) NOT NULL,
  `fecha` date NOT NULL,
  `precio` decimal(15,4) NOT NULL,
  PRIMARY KEY (`id_activo`,`fecha`),
  CONSTRAINT `fk_precio_activo_activo` FOREIGN KEY (`id_activo`) REFERENCES `activo` (`id_activo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;