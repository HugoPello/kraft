create database LoginRegistro;
use LoginRegistro;

CREATE TABLE USERS (
    USR VARCHAR(100) NOT NULL PRIMARY KEY,
	PWD VARCHAR(255) NOT NULL,
    ROL BOOLEAN,
    correo VARCHAR(150), -- Opcional en entorno local
    ruta_imagen_perfil VARCHAR(255) DEFAULT '/src/main/resources/img/default.png',
    nivel INT DEFAULT 1,
    puntos INT DEFAULT 0,          -- Puntos totales para el Ranking Mundial
    monedas_tienda INT DEFAULT 0,  -- Monedas para gastar en Cosméticos
    fechaNacimiento DATE,
    fecha_registro DATE DEFAULT (CURRENT_DATE)
);

CREATE TABLE PREGUNTA_SEGURIDAD (
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    enunciado VARCHAR(500) NOT NULL
);

CREATE TABLE RESPONDE_PREGUNTA (
	nombreUsuario VARCHAR(100) NOT NULL,
    id_pregunta INT NOT NULL,
    respuesta_texto VARCHAR(255) NOT NULL, -- Aquí se guarda la respuesta original
    PRIMARY KEY (nombreUsuario, id_pregunta),
    FOREIGN KEY (nombreUsuario) REFERENCES USERS(USR) ON DELETE CASCADE,
    FOREIGN KEY (id_pregunta) REFERENCES PREGUNTA_SEGURIDAD(id_pregunta) ON DELETE CASCADE
);

INSERT INTO `PREGUNTA_SEGURIDAD` (`enunciado`) VALUES
('¿Cuál es el nombre de tu madre?'),
('¿Cuál es el nombre de tu padre?'),
('¿Cuál es el nombre de tu primera mascota?'),
('¿Cuál fue tu primer colegio?');


-- SENTENCIAS LOGIN

-- String query = "SELECT * FROM users WHERE usr=? AND pwd=?"; // Para logear al usuario de forma segura
-- String query = "SELECT USR FROM users WHERE USR = ?"; // Para saber si el usuario ya existe en la base de datos

INSERT INTO 'NOTICIAS'