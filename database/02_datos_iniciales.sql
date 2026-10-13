USE bd_cssp_sismos;

INSERT INTO departamento (id, nombre, estado) VALUES
(1, 'Arequipa', 'ACTIVO'),
(2, 'Lima', 'ACTIVO'),
(3, 'Cusco', 'ACTIVO'),
(4, 'Tacna', 'ACTIVO'),
(5, 'Ica', 'ACTIVO');

INSERT INTO provincia (id, nombre, departamento_id, estado) VALUES
(1, 'Arequipa', 1, 'ACTIVO'),
(2, 'Lima', 2, 'ACTIVO'),
(3, 'Cusco', 3, 'ACTIVO'),
(4, 'Tacna', 4, 'ACTIVO'),
(5, 'Ica', 5, 'ACTIVO');

INSERT INTO distrito (id, nombre, provincia_id, estado) VALUES
(1, 'Arequipa', 1, 'ACTIVO'),
(2, 'Cayma', 1, 'ACTIVO'),
(3, 'Lima', 2, 'ACTIVO'),
(4, 'Miraflores', 2, 'ACTIVO'),
(5, 'Cusco', 3, 'ACTIVO'),
(6, 'Tacna', 4, 'ACTIVO'),
(7, 'Ica', 5, 'ACTIVO');

INSERT INTO estacion (id, codigo, nombre, provincia_id, latitud, longitud, fecha_instalacion, estado) VALUES
(1, 'EST001', 'Estacion Arequipa Centro', 1, -16.398800, -71.536900, '2020-03-15', 'ACTIVO'),
(2, 'EST002', 'Estacion Lima Norte', 2, -11.990000, -77.060000, '2019-07-01', 'ACTIVO'),
(3, 'EST003', 'Estacion Cusco Sur', 3, -13.550000, -71.970000, '2021-11-20', 'ACTIVO'),
(4, 'EST004', 'Estacion Tacna Antigua', 4, -18.010000, -70.250000, '2018-05-10', 'INACTIVO');

INSERT INTO sismo (id, codigo, fecha_hora, magnitud, profundidad, latitud, longitud, referencia, estado, estado_registro, distrito_id, estacion_id) VALUES
(1, 'SIS001', '2026-08-18 14:32:00', 5.6, 34, -16.409000, -71.537500, '20 km al sur de la ciudad de Arequipa', 'REGISTRADO', 'ACTIVO', 1, 1),
(2, 'SIS002', '2026-09-02 03:10:00', 4.5, 45, -12.046400, -77.042800, 'Lima Metropolitana', 'EN_EVALUACION', 'ACTIVO', 3, 2),
(3, 'SIS003', '2026-09-10 21:47:00', 4.8, 20, -13.531900, -71.967500, 'Cusco ciudad', 'CERRADO', 'ACTIVO', 5, 3),
(4, 'SIS004', '2026-09-14 08:05:00', 5.1, 60, -18.014600, -70.253600, 'Tacna ciudad', 'REGISTRADO', 'ACTIVO', 6, 1),
(5, 'SIS005', '2026-09-17 16:20:00', 4.2, 12, -14.067800, -75.728600, 'Ica ciudad', 'EN_SEGUIMIENTO', 'ACTIVO', 7, 2);
