CREATE DATABASE IF NOT EXISTS bd_cssp_sismos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bd_cssp_sismos;

CREATE TABLE IF NOT EXISTS departamento (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT chk_departamento_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS provincia (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    departamento_id BIGINT NOT NULL,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_provincia_departamento FOREIGN KEY (departamento_id) REFERENCES departamento(id),
    CONSTRAINT uq_provincia_departamento_nombre UNIQUE (departamento_id, nombre),
    CONSTRAINT chk_provincia_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS distrito (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    provincia_id BIGINT NOT NULL,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_distrito_provincia FOREIGN KEY (provincia_id) REFERENCES provincia(id),
    CONSTRAINT uq_distrito_provincia_nombre UNIQUE (provincia_id, nombre),
    CONSTRAINT chk_distrito_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS estacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    provincia_id BIGINT NOT NULL,
    latitud DECIMAL(9,6) NOT NULL,
    longitud DECIMAL(9,6) NOT NULL,
    fecha_instalacion DATE NOT NULL,
    estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_estacion_provincia FOREIGN KEY (provincia_id) REFERENCES provincia(id),
    CONSTRAINT chk_estacion_latitud CHECK (latitud BETWEEN -90 AND 90),
    CONSTRAINT chk_estacion_longitud CHECK (longitud BETWEEN -180 AND 180),
    CONSTRAINT chk_estacion_estado CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS sismo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    fecha_hora DATETIME NOT NULL,
    magnitud DECIMAL(3,1) NOT NULL,
    profundidad DECIMAL(6,2) NOT NULL,
    latitud DECIMAL(9,6) NOT NULL,
    longitud DECIMAL(9,6) NOT NULL,
    referencia VARCHAR(200) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'REGISTRADO',
    estado_registro VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
    distrito_id BIGINT NOT NULL,
    estacion_id BIGINT NOT NULL,
    CONSTRAINT fk_sismo_distrito FOREIGN KEY (distrito_id) REFERENCES distrito(id),
    CONSTRAINT fk_sismo_estacion FOREIGN KEY (estacion_id) REFERENCES estacion(id),
    CONSTRAINT chk_sismo_latitud CHECK (latitud BETWEEN -90 AND 90),
    CONSTRAINT chk_sismo_longitud CHECK (longitud BETWEEN -180 AND 180),
    CONSTRAINT chk_sismo_magnitud CHECK (magnitud > 0),
    CONSTRAINT chk_sismo_profundidad CHECK (profundidad >= 0),
    CONSTRAINT chk_sismo_estado CHECK (estado IN ('REGISTRADO', 'EN_EVALUACION', 'EN_SEGUIMIENTO', 'CERRADO')),
    CONSTRAINT chk_sismo_estado_registro CHECK (estado_registro IN ('ACTIVO', 'INACTIVO'))
);
