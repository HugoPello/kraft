Create
database Tablas;
use
Tablas;

-- ========================================================
-- 1. TABLAS PRINCIPALES (Entidades Fuertes)
-- ========================================================

CREATE TABLE USERS
(
    USR                VARCHAR(100) NOT NULL PRIMARY KEY,
    PWD                VARCHAR(255) NOT NULL,
    ROL                BOOLEAN,
    correo             VARCHAR(150),           -- Opcional en entorno local
    ruta_imagen_perfil VARCHAR(255) DEFAULT '/src/main/resources/img/default.png',
    nivel              INT          DEFAULT 1,
    puntos             INT          DEFAULT 0, -- Puntos totales para el Ranking Mundial
    monedas_tienda     INT          DEFAULT 0, -- Monedas para gastar en Cosméticos
    fechaNacimiento    DATE,
    fecha_registro     DATE         DEFAULT (CURRENT_DATE)
);

CREATE TABLE ACTIVIDADES
(
    id_actividad  INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    tipo          VARCHAR(50),           -- Ej: 'Montaña', 'Agua', 'Dieta'
    dificultad    VARCHAR(50),
    puntos        INT          NOT NULL, -- Puntos/Monedas que otorga al completarse
    frecuencia    VARCHAR(50),
    unidad_medida VARCHAR(50)            -- Ej: 'Km', 'Minutos', 'Repeticiones'
);

INSERT INTO ACTIVIDADES (nombre, tipo, dificultad, puntos, frecuencia, unidad_medida) VALUES 
('Senderismo por la Sierra', 'Montaña', 'Baja', 50, 'Semanal', 'km'),
('Escalada en Rocódromo', 'Montaña', 'Media', 100, 'Mensual', 'sesiones'),
('Ruta BTT (Ciclismo)', 'Montaña', 'Alta', 150, 'Semanal', 'km'),
('Natación Libre', 'Agua', 'Baja', 40, 'Diaria', 'metros'),
('Sesión de Surf', 'Agua', 'Media', 120, 'Ocasional', 'horas'),
('Waterpolo en equipo', 'Agua', 'Alta', 130, 'Semanal', 'partidos'),
('Partido de Tenis', 'Cancha', 'Media', 80, 'Semanal', 'partidos'),
('Entrenamiento Baloncesto', 'Cancha', 'Baja', 60, 'Diaria', 'minutos'),
('Fútbol 7', 'Cancha', 'Alta', 110, 'Semanal', 'goles'),
('Beber 2L de agua', 'Dieta', 'Baja', 20, 'Diaria', 'litros'),
('Comer 5 piezas de fruta', 'Dieta', 'Baja', 30, 'Diaria', 'unidades'),
('Cero procesados (semanal)', 'Dieta', 'Media', 90, 'Semanal', 'días'),
('Trote suave 5km', 'Running', 'Baja', 45, 'Diaria', 'km'),
('Series de velocidad', 'Running', 'Alta', 100, 'Semanal', 'series'),
('Media Maratón preparada', 'Running', 'Muy Alta', 500, 'Anual', 'completado');

CREATE TABLE LOGROS
(
    id_logro      INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   TEXT,
    puntos_logros INT          NOT NULL
);

CREATE TABLE MISIONES
(
    id_mision   INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    recompensa  INT          NOT NULL,
    tipo ENUM('Diaria','Semanal') NOT NULL
);

/* MISIONES DIARIAS */
INSERT INTO MISIONES (descripcion, recompensa, tipo) VALUES 
('Completar 10.000 pasos hoy', 50, 'Diaria'),
('Entrenar al menos 45 minutos', 100, 'Diaria'),
('Beber 2 litros de agua', 30, 'Diaria'),
('Realizar 10 minutos de estiramientos', 25, 'Diaria'),
('Registrar todas las comidas del día', 40, 'Diaria'),
('No consumir azúcar procesado hoy', 60, 'Diaria');

/* MISIONES SEMANALES */
INSERT INTO MISIONES (descripcion, recompensa, tipo) VALUES 
('Entrenar fuerza 4 días a la semana', 500, 'Semanal'),
('Realizar 150 minutos de cardio total', 450, 'Semanal'),
('Probar una nueva receta saludable', 200, 'Semanal'),
('Dormir al menos 7 horas durante 5 días', 350, 'Semanal'),
('Completar 3 sesiones de movilidad/yoga', 300, 'Semanal'),
('Perder o mantener peso según objetivo', 600, 'Semanal');



CREATE TABLE COSMETICOS
(
    id_cosmetico         INT AUTO_INCREMENT PRIMARY KEY,
    nombre               VARCHAR(100) NOT NULL,
    tipo_personalizacion VARCHAR(50)  NOT NULL, -- Ej: 'Banner', 'Marco', 'Efecto'
    coste_monedas        INT          NOT NULL,
    ruta_imagen          VARCHAR(255)
);

CREATE TABLE PREGUNTA_SEGURIDAD
(
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    enunciado   VARCHAR(255) NOT NULL
);

CREATE TABLE EVENTOS
(
    id_evento         INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(150) NOT NULL,
    tipo_evento       VARCHAR(50), -- Ej: 'Actividad' o 'Hábito'
    puntos_recompensa INT          NOT NULL,
    fecha_evento      DATETIME
);

INSERT INTO EVENTOS (nombre, tipo_evento, puntos_recompensa, fecha_evento) VALUES 
('Correr 10km', 'Actividad', 100, '2026-05-10 09:00:00'),
('Estiramientos diarios', 'Hábito', 20, '2026-05-04 08:30:00'),
('Partido de fútbol', 'Actividad', 150, '2026-05-15 18:00:00'),
('Hidratación constante', 'Hábito', 15, '2026-05-05 10:00:00'),
('Natación 30 min', 'Actividad', 80, '2026-05-12 11:15:00'),
('Subir escaleras', 'Hábito', 10, '2026-05-06 09:00:00'),
('Clase de Yoga', 'Actividad', 60, '2026-05-18 17:30:00'),
('Caminar 10,000 pasos', 'Hábito', 50, '2026-05-07 20:00:00'),
('Ciclismo de montaña', 'Actividad', 200, '2026-05-24 10:00:00'),
('Dormir 8 horas', 'Hábito', 30, '2026-05-08 23:00:00'),
('Crossfit 1h', 'Actividad', 120, '2026-05-14 19:00:00'),
('Meditación 15 min', 'Hábito', 25, '2026-05-09 07:45:00'),
('Senderismo', 'Actividad', 180, '2026-05-30 08:00:00'),
('Lectura técnica 30 min', 'Hábito', 40, '2026-05-11 21:00:00'),
('Tenis (Partido)', 'Actividad', 140, '2026-05-21 16:30:00');


CREATE TABLE GRUPO (
    id_grupo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    es_privado BOOLEAN DEFAULT FALSE,
    fecha_creacion DATE DEFAULT (CURRENT_DATE)
);
ALTER TABLE GRUPO ADD COLUMN contrasena VARCHAR(50) DEFAULT NULL;

-- ========================================================
-- 2. TABLAS INTERMEDIAS (Relaciones N:M con Atributos)
-- ========================================================

-- Relacion "Pertenece" (USUARIOS - Grupos)
CREATE TABLE PERTENECE_GRUPO (
    nombreUsuario VARCHAR(100) NOT NULL,
    id_grupo INT NOT NULL,
    fecha_union DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (nombreUsuario, id_grupo),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS(USR) ON DELETE CASCADE,
    FOREIGN KEY (id_grupo) REFERENCES GRUPO(id_grupo) ON DELETE CASCADE
);

-- Relación "Completar" (Usuarios - Actividades)
CREATE TABLE COMPLETAR_ACTIVIDAD
(
    nombreUsuario     VARCHAR(100) NOT NULL,
    id_actividad      INT          NOT NULL,
    fecha_realizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- La PK es triple para permitir repetir la misma actividad en diferentes momentos
    PRIMARY KEY (nombreUsuario, id_actividad, fecha_realizacion),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_actividad) REFERENCES ACTIVIDADES (id_actividad) ON DELETE CASCADE
);

-- Relación "Hacer" (Usuarios - Logros)
CREATE TABLE HACER_LOGRO
(
    nombreUsuario    VARCHAR(100) NOT NULL,
    id_logro         INT          NOT NULL,
    fecha_desbloqueo DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (nombreUsuario, id_logro),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_logro) REFERENCES LOGROS (id_logro) ON DELETE CASCADE
);

-- Relación "Tiene" (Usuarios - Cosméticos / El Inventario)
CREATE TABLE TIENE_COSMETICO
(
    nombreUsuario VARCHAR(100) NOT NULL,
    id_cosmetico  INT          NOT NULL,
    esta_equipado BOOLEAN  DEFAULT FALSE, -- Crucial para saber qué lleva puesto
    fecha_compra  DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (nombreUsuario, id_cosmetico),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_cosmetico) REFERENCES COSMETICOS (id_cosmetico) ON DELETE CASCADE
);

-- Relación "Se generan" (Actividades - Misiones)
CREATE TABLE MISION_ACTIVIDAD
(
    id_mision          INT NOT NULL,
    id_actividad       INT NOT NULL,
    cantidad_requerida INT DEFAULT 1, -- Cuántas veces hay que hacer la actividad
    PRIMARY KEY (id_mision, id_actividad),
    FOREIGN KEY (id_mision) REFERENCES MISIONES (id_mision) ON DELETE CASCADE,
    FOREIGN KEY (id_actividad) REFERENCES ACTIVIDADES (id_actividad) ON DELETE CASCADE
);
-- Relación "Responde" (Usuarios - Pregunta de Seguridad)
CREATE TABLE RESPONDE_PREGUNTA
(
    nombreUsuario   VARCHAR(100) NOT NULL,
    id_pregunta     INT          NOT NULL,
    respuesta_texto VARCHAR(255) NOT NULL, -- Aquí se guarda la respuesta original
    PRIMARY KEY (nombreUsuario, id_pregunta),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_pregunta) REFERENCES PREGUNTA_SEGURIDAD (id_pregunta) ON DELETE CASCADE
);

-- Relación "Participa" (Usuarios - Eventos)
CREATE TABLE PARTICIPA_EVENTO
(
    nombreUsuario   VARCHAR(100) NOT NULL,
    id_evento       INT          NOT NULL,
    progreso_evento INT DEFAULT 0, -- Para tu JProgressBar de la Vista Eventos
    PRIMARY KEY (nombreUsuario, id_evento),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_evento) REFERENCES EVENTOS (id_evento) ON DELETE CASCADE
);

-- Relación "Realiza" (Usuarios - Misiones)
CREATE TABLE REALIZA_MISION
(
    nombreUsuario VARCHAR(100) NOT NULL,
    id_mision     INT          NOT NULL,
    progreso      INT DEFAULT 0,
    PRIMARY KEY (nombreUsuario, id_mision),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS (USR) ON DELETE CASCADE,
    FOREIGN KEY (id_mision) REFERENCES MISIONES (id_mision) ON DELETE CASCADE
);

-- ========================================================
-- 3. DATOS INICIALES (Admins)
-- ========================================================


-- Ejemplo de inserción para tu catálogo de preguntas (Para tu JComboBox):
INSERT INTO PREGUNTA_SEGURIDAD (enunciado)
VALUES ('¿Cuál es el nombre de tu madre?'),
       ('¿Cuál es el nombre de tu padre?'),
       ('¿Cuál es el nombre de tu primera mascota?'),
       ('¿Cuál fue tu primer colegio?');
       
-- ========================================================
-- 1. CREAR USUARIOS DE PRUEBA Y SUS PUNTOS GLOBALES
-- ========================================================
-- Insertamos nuevos usuarios y pre-calculamos sus puntos globales
INSERT IGNORE INTO USERS (USR, PWD, ROL, puntos) VALUES
('Laura', '1234', 0, 1200),
('Marcos', '1234', 0, 600),
('Ana', '1234', 0, 450),
('Pablo', '1234', 0, 200),
('Elena', '1234', 0, 1000);


-- ========================================================
-- 2. CREAR LOS GRUPOS
-- ========================================================
INSERT IGNORE INTO GRUPO (id_grupo, nombre, descripcion) VALUES
(1, 'Los Espartanos', 'Solo tryhards'),
(2, 'Rangers del Norte', 'Exploradores natos'),
(3, 'Cardio Lovers', 'Si no sudas, no cuenta');


-- ========================================================
-- 3. ASIGNAR USUARIOS A GRUPOS
-- ========================================================
INSERT IGNORE INTO PERTENECE_GRUPO (nombreUsuario, id_grupo) VALUES
('Laura', 1),   -- Laura es rival directa de Diego en el mismo grupo
('Marcos', 2),
('Ana', 2),     
('Pablo', 3);   -- Pablo está solo en su grupo. (Elena es un lobo solitario, sin grupo)


-- ========================================================
-- 4. HISTORIAL DE ACTIVIDADES (PARA EL RANKING SEMANAL)
-- ========================================================
-- ID 1 = Senderismo (100 pts) | ID 2 = Escalada (150 pts) 
-- ID 4 = Natación (100 pts)   | ID 11 = Maratón (500 pts)

-- LAURA (Semana actual: 1000 puntos | Antiguas: 200 puntos)
INSERT IGNORE INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad, fecha_realizacion) VALUES
('Laura', 4,  '2026-01-10 08:00:00'), -- Antigua 
('Laura', 4,  '2026-01-11 08:00:00'), -- Antigua 
('Laura', 11, '2026-04-29 10:00:00'), -- ESTA SEMANA 
('Laura', 11, '2026-05-01 10:00:00'); -- ESTA SEMANA 

-- MARCOS (Semana actual: 0 puntos | Antiguas: 600 puntos)
-- Él solo aparecerá en el Mundial y en el Grupal, NO en el Semanal
INSERT IGNORE INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad, fecha_realizacion) VALUES
('Marcos', 11, '2026-03-01 09:00:00'), -- Antigua 
('Marcos', 1,  '2026-03-05 09:00:00'); -- Antigua 

-- ANA (Semana actual: 150 puntos | Antiguas: 300 puntos)
INSERT IGNORE INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad, fecha_realizacion) VALUES
('Ana', 2, '2026-02-20 10:00:00'), -- Antigua 
('Ana', 2, '2026-02-21 10:00:00'), -- Antigua 
('Ana', 2, '2026-04-30 16:00:00'); -- ESTA SEMANA 

-- PABLO (Semana actual: 200 puntos | Antiguas: 0 puntos)
INSERT IGNORE INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad, fecha_realizacion) VALUES
('Pablo', 4, '2026-04-27 07:00:00'), -- ESTA SEMANA 
('Pablo', 4, '2026-05-01 07:00:00'); -- ESTA SEMANA