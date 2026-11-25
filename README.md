# 🚀 KRAFT | Plataforma Integral de Gamificación Saludable

<div align="center">

  [![Java SE 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
  [![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
  [![Jakarta Mail](https://img.shields.io/badge/Jakarta_Mail-2.0.1-007396?style=for-the-badge)](https://eclipse-ee4j.github.io/mail/)
  [![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
</div>

---

## 🌑 Concepto del Proyecto
**KRAFT** es una ecosistema de escritorio desarrollado en **Java Swing** que transforma la gestión de hábitos y el rendimiento deportivo en una experiencia gamificada de tipo RPG. El sistema permite a los usuarios registrar entrenamientos reales para progresar en niveles, competir en clanes y personalizar su identidad digital mediante un sistema de economía interna.

## 🏗️ Arquitectura Técnica (MVC Estricto)
El proyecto implementa el patrón **Modelo-Vista-Controlador** mediante una interfaz común (`Vista.java`) que estandariza la comunicación entre las 19 ventanas funcionales:

* **Modelo (`Modelo.java`):** Gestiona la persistencia JDBC, la lógica de negocio y la lectura dinámica de credenciales mediante `config.ini`.
* **Vista (`Vista/`):** 19 interfaces gráficas optimizadas con estética *Dark Mode*, incluyendo validación de campos en tiempo real y componentes personalizados.
* **Controlador (`Controlador.java`):** Orquestador central encargado de la navegación, manipulación de ficheros y el despacho de servicios externos.

---

## 🔒 Seguridad y Verificación de Identidad

### 📧 Autenticación de Doble Factor (2FA)
KRAFT protege el acceso mediante un flujo de seguridad OTP (One-Time Password):
* **Generación:** Un trigger en la base de datos genera el código tras un login exitoso.
* **Despacho:** Envío síncrono al email del usuario mediante **Jakarta Mail 2.0.1**.
* **Validación:** Interfaz dedicada para la verificación del token antes de acceder al sistema.

### 🛡️ Protección Avanzada
* **Captcha Dinámico:** Generación de tokens alfanuméricos aleatorios para prevenir registros automatizados.
* **Recuperación Criptográfica:** Restablecimiento de contraseña basado en preguntas de seguridad vinculadas a la identidad del usuario.
* **Control de Roles:** Verificación jerárquica (Admin/User) para restringir el acceso a paneles sensibles y bases de datos.

---

## 🏃 Funcionalidades Core (User Experience)

### 📊 Gestión de Actividades y Misiones
* **Seguimiento Multideporte:** Módulos para Running, Montaña, Agua, Cancha y Dieta con barras de progreso en tiempo real.
* **Misiones Dinámicas:** Sistema de retos diarios y semanales con recompensas automáticas en XP y monedas.
* **Creador de Rutinas:** Herramienta para que los usuarios propongan y guarden nuevas actividades personalizadas.

### 👥 Sede de Clanes y Social
* **Gestión de Grupos:** Creación y unión a clanes (públicos o privados con contraseña).
* **Hub Grupal:** Visualización de estadísticas colectivas, ranking interno de miembros y feed de actividades recientes.
* **Rankings Mundiales:** Clasificaciones globales y semanales actualizadas mediante consultas SQL optimizadas.

### 🛒 Personalización y Perfil
* **Tienda de Cosméticos:** Adquisición de marcos, banners y efectos visuales.
* **Perfil Dinámico:** Resumen de nivel, historial de logros y gestión de avatar mediante almacenamiento local gestionado.

---

## 🛠️ Panel de Administración (Backoffice)
Herramienta exclusiva para administradores que permite un control total del entorno:
* **Economía:** Modificación manual de puntos y saldos de monedas.
* **Contenido:** Gestión de noticias, eventos globales y misiones especiales.
* **Moderación:** Alta, baja y modificación de perfiles de usuario.

---

## 🚀 Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/HugoPello/kraft.git
   cd kraft
   ```

2. **Crear la base de datos** (MySQL 8) ejecutando los scripts `Tablas UD13.sql` y `LoginRegistro.sql`.

3. **Configurar la conexión:** copia `config.example.ini` como `config.ini` y pon tu usuario, contraseña y URL JDBC. `config.ini` está en `.gitignore` y nunca se sube al repositorio. También se puede cambiar desde la ventana de *Configuración* de la app.

4. **(Opcional) Activar el envío del código 2FA por email** con una cuenta de Gmail y una [App Password](https://support.google.com/accounts/answer/185833):
   ```powershell
   $env:KRAFT_MAIL_USER = "tu_cuenta@gmail.com"
   $env:KRAFT_MAIL_PASSWORD = "tu_app_password"
   ```
   Sin estas variables, la app funciona igual pero no envía el correo.

5. **Compilar y ejecutar** (Java 21 + Maven), con el conector `lib/mysql-connector-j-9.6.0.jar` en el classpath. Punto de entrada: `Controlador.Main`.

---

## 👥 Equipo

Proyecto final de **1.º de DAM en U-Tad (curso 2025-26)**, desarrollado en equipo por **404 Team Not Found**:

* **Hugo Pello**
* **Carlos**
* **Diego Marquez**
* **Ivan Mena**

> Este repositorio es una copia pública del repositorio original de clase, al que se le han eliminado credenciales y archivos compilados.
