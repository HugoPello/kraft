/**
 * Clase que gestiona la lógica de negocio y la persistencia de datos.
 * Se comunica con la base de datos MySQL mediante JDBC y gestiona el estado
 * de la sesión del usuario.
 * 
 * @author Hugo
 * @version 1.0
 */

package Modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.jdbc.CallableStatement;

import Vista.Vista;

public class Modelo {

	// --- REFERENCIAS MVC ---
	private Vista[] misVistas;

	// --- CREDENCIALES DE BASE DE DATOS ---
	private String login;
	private String pwdDB;
	private String url;
	private Connection conexion;

	// --- ESTADO DE LA SESIÓN ---
	private String usuarioLogueado;
	private String tipoActividadSeleccionada;
	private String resultado;
	private boolean ROL = false;
	private int fallos;

	// =========================================================================
	// 1. CONEXIÓN Y CONFIGURACIÓN BÁSICA
	// =========================================================================

	public Modelo() {
		leerConfig();
	}

	// LECTURA FICHERO
	
	/**
	 * Carga los parámetros de conexión desde un archivo externo .ini.
	 * Permite que la aplicación sea portable sin necesidad de recompilar el código 
	 * ante cambios en el servidor de base de datos.
	 */
	public void leerConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader("config.ini"))) {
			// Aplicamos la limpieza a cada línea leída
			this.login = extraerValor(br.readLine());
			this.pwdDB = extraerValor(br.readLine());
			this.url = extraerValor(br.readLine());

		} catch (Exception ex) {
			System.err.println("Error al leer config.ini: " + ex.getMessage());
		}
	}

	// Método auxiliar para limpiar las etiquetas "usr:", "pwd:", etc.
	private String extraerValor(String linea) {
		if (linea != null && linea.contains(":")) {
			// Buscamos SOLO el primer ":" (el de la etiqueta del .ini)
			int primerDosPuntos = linea.indexOf(":");
			// Devolvemos todo lo que hay DESPUÉS del primer ":"
			return linea.substring(primerDosPuntos + 1).trim();
		}
		return (linea != null) ? linea.trim() : "";
	}

	// FORMULARIO DE CONEXIÓN (Escritura)
	public void guardarConfig(String nUsr, String nPwd, String nUrl) {
		// 1. Escritura física en el fichero config.ini
		try (PrintWriter pw = new PrintWriter(new FileWriter("config.ini"))) {
			pw.println("usr: " + nUsr);
			pw.println("pwd: " + nPwd);
			pw.println("url: " + nUrl);

			this.login = nUsr;
			this.pwdDB = nPwd;
			this.url = nUrl;

			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
			}

		} catch (Exception ex) {
			System.err.println("Error al actualizar y conectar: " + ex.getMessage());
		}
	}

	/**
	 * Establece la conexión física con el servidor de base de datos MySQL.
	 * Carga el driver JDBC dinámicamente y utiliza las credenciales obtenidas
	 * del archivo de configuración para abrir un socket de comunicación.
	 * 
	 * @return true si la conexión se establece o ya estaba abierta; false si hay error de red o credenciales.
	 */
	public boolean conectarBD() {
		try {
			System.out.println("Conectando a: " + url);
			
			// Verificación de estado: Solo intentamos conectar si la conexión es nula o se ha cerrado
			if (conexion == null || conexion.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				// Intento de autenticación mediante el DriverManager utilizando la URL y credenciales del .ini
				conexion = DriverManager.getConnection(url, login, pwdDB);
				System.out.println("-> Conexión establecida");
			}
			return true;
		} catch (Exception e) {
			System.err.println("Error de conexión: " + e.getMessage());
			return false;
		}
	}

	public void setVistas(Vista[] misVistas) {
		this.misVistas = misVistas;
	}

	public void terminar() {
		try {
			if (conexion != null) {
				conexion.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// =========================================================================
	// 2. GESTIÓN DEL ESTADO DE LA SESIÓN (GETTERS Y SETTERS)
	// =========================================================================

	public void setUsuarioLogueado(String usuario) {
		this.usuarioLogueado = usuario;
	}

	public String getUsuarioLogueado() {
		return this.usuarioLogueado;
	}

	public void setTipoActividadSeleccionada(String tipo) {
		this.tipoActividadSeleccionada = tipo;
	}

	public String getTipoActividadSeleccionada() {
		return this.tipoActividadSeleccionada;
	}

	public String getResultado() {
		return this.resultado;
	}

	public boolean isAdmin() {
		return ROL;
	}

	public boolean esUsuarioAdmin() {
		return this.ROL;
	}

	public String sacarEmail() {
		String email = "";
		String usr = getUsuarioLogueado();
		String query = "SELECT correo FROM USERS WHERE USR = ?";
		
		try {
			PreparedStatement pstmt = conexion.prepareStatement(query);
			pstmt.setString(1, usr);
			ResultSet rset = pstmt.executeQuery();
			
			if(rset.next()){
				email = rset.getString("correo"); 
			}
			pstmt.close();
		} catch(Exception e) {
			System.err.println("Error al sacar email: " + e.getMessage());
		}
		
		return email;
	}

	// =========================================================================
	// 3. MOTORES GENÉRICOS DE CONSULTA
	// =========================================================================

	
	/**
     * Motor de consulta genérico para obtener datos de una columna específica.
     * Utiliza Varargs para permitir un número dinámico de parámetros en el PreparedStatement.
     * 
     * @param query Sentencia SQL con marcadores '?'
     * @param nombreColumna Etiqueta de la columna a extraer
     * @param parametros Valores para inyectar en los marcadores de la query
     * @return Array de String con los resultados o mensaje de "No hay datos"
     */
	public String[] obtenerDatosColumna(String query, String nombreColumna, Object... parametros) {
		ArrayList<String> listaDatos = new ArrayList<>();

		if (conexion == null) {
			return new String[] { "Carga los datos en Login" };
		}

		try {
			PreparedStatement pstmt = conexion.prepareStatement(query);
			for (int i = 0; i < parametros.length; i++) {
				pstmt.setObject(i + 1, parametros[i]);
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				listaDatos.add(rs.getString(nombreColumna));
			}
			pstmt.close();

		} catch (SQLException e) {
			System.err.println("Error SQL: " + e.getMessage());
		}

		if (listaDatos.isEmpty()) {
			listaDatos.add("No hay datos");
		}

		return listaDatos.toArray(new String[0]);
	}
	
	/**
	 * Obtiene la lista de nombres de logros desbloqueados por un usuario.
d	 * @return ArrayList con los nombres de los logros.
	 */
	public ArrayList<String> obtenerLogrosUsuario(String usuario) {
	    ArrayList<String> logros = new ArrayList<>();
	    String sql = "SELECT L.nombre FROM LOGROS L " +
	                 "JOIN HACER_LOGRO HL ON L.id_logro = HL.id_logro " +
	                 "WHERE HL.nombreUsuario = ?";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setString(1, usuario);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            logros.add(rs.getString("nombre"));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al obtener logros: " + e.getMessage());
	    }
	    return logros;
	}
	
	public ArrayList<String> obtenerTodosLosLogros() {
	    ArrayList<String> lista = new ArrayList<>();
	    String sql = "SELECT nombre FROM LOGROS";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            lista.add(rs.getString("nombre"));
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al cargar logros globales: " + e.getMessage());
	    }
	    return lista;
	}

	public Object[][] obtenerDatosTabla(String query, int numColumnas, Object... parametros) {
		ArrayList<Object[]> listaDatos = new ArrayList<>();

		try {
			PreparedStatement pstmt = conexion.prepareStatement(query);
			for (int i = 0; i < parametros.length; i++) {
				pstmt.setObject(i + 1, parametros[i]);
			}

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Object[] fila = new Object[numColumnas];
				for (int j = 0; j < numColumnas; j++) {
					fila[j] = rs.getObject(j + 1);
				}
				listaDatos.add(fila);
			}
			pstmt.close();

		} catch (SQLException e) {
			System.err.println("Error en consulta de tabla: " + e.getMessage());
		}

		if (listaDatos.isEmpty()) {
			Object[] filaVacia = new Object[numColumnas];
			filaVacia[0] = "Sin registros";
			for (int i = 1; i < numColumnas; i++) {
				filaVacia[i] = "";
			}
			listaDatos.add(filaVacia);
		}

		return listaDatos.toArray(new Object[0][0]);
	}

	// =========================================================================
	// 4. AUTENTICACIÓN Y GESTIÓN DE USUARIOS
	// =========================================================================
	
	
	/**
	 * Realiza un login adaptativo. 
	 * Detecta si el usuario requiere 2FA (tiene email) o si es una cuenta antigua.
	 * @return "OTP_ENVIADO", "LOGIN_DIRECTO" o "INCORRECTO"
	 */
	public String loginSeguro(String usr, String pwd) {
	    // 1. Buscamos al usuario sin filtrar por is_verified aún para permitir acceso a cuentas antiguas
	    String query = "SELECT correo, is_verified, ROL FROM USERS WHERE USR=? AND PWD=?";
	    
	    try {
	        PreparedStatement pstmt = conexion.prepareStatement(query);
	        pstmt.setString(1, usr);
	        pstmt.setString(2, pwd);
	        ResultSet rset = pstmt.executeQuery();

	        if (rset.next()) {
	            this.usuarioLogueado = usr;
	            this.ROL = rset.getBoolean("ROL");
	            String email = rset.getString("correo");
	            int verificado = rset.getInt("is_verified");

	            // CASO A: Tiene email configurado (Cuenta nueva o actualizada)
	            if (email != null && !email.trim().isEmpty()) {
	                // Generamos el OTP mediante el procedimiento
	                try (java.sql.CallableStatement cst = conexion.prepareCall("{call create_otp(?)}")) {
	                    cst.setString(1, usr);
	                    cst.execute();
	                }
	                this.ultimoUsuarioRegistrado = usr;
	                return "OTP_ENVIADO";
	            } 
	            
	            // CASO B: No tiene email (Cuenta antigua)
	            // Entra directamente para no bloquear al usuario
	            return "LOGIN_DIRECTO";
	            
	        } else {
	            // LOGIN INCORRECTO
	            this.fallos++; 
	            if (this.fallos >= 3) {
	                return "CERRAR";
	            }
	            return "INCORRECTO";
	        }
	    } catch (SQLException e) {
	        System.err.println("Error en Login Adaptativo: " + e.getMessage());
	        return "ERROR_BD";
	    }
	}

	/**
     * Realiza un registro atómico del usuario utilizando transacciones SQL.
     * Garantiza que el usuario no se cree si falla la inserción en la tabla de seguridad.
     * 
     * @param usr Nombre de identificación único
     * @param pwd Contraseña (texto plano por requerimiento de proyecto)
     * @param correo Dirección de email para el sistema 2FA
     * @return true si el commit se realizó con éxito; false si hubo rollback
     */
	public boolean registroCompleto(String usr, String pwd, boolean rol, String fechaNac, int idPregunta,
            String respuesta, String correo) {
        try {
            conexion.setAutoCommit(false); // Iniciamos transacción

            // 1. Insertamos en USERS incluyendo el correo y el estado de verificación inicial
            String query1 = "INSERT INTO USERS (USR, PWD, ROL, correo, fechaNacimiento, is_verified) VALUES (?, ?, ?, ?, ?, 0)";
            PreparedStatement ps1 = conexion.prepareStatement(query1);
            ps1.setString(1, usr);
            ps1.setString(2, pwd);
            ps1.setBoolean(3, rol);
            ps1.setString(4, correo); // El correo del usuario para el 2FA

            if (fechaNac == null || fechaNac.trim().isEmpty()) {
                ps1.setNull(5, java.sql.Types.DATE);
            } else {
                ps1.setDate(5, java.sql.Date.valueOf(fechaNac));
            }
            ps1.executeUpdate();

            // 2. Insertamos la pregunta de seguridad en tu segunda tabla
            String query2 = "INSERT INTO RESPONDE_PREGUNTA (nombreUsuario, id_pregunta, respuesta_texto) VALUES (?, ?, ?)";
            PreparedStatement ps2 = conexion.prepareStatement(query2);
            ps2.setString(1, usr);
            ps2.setInt(2, idPregunta);
            ps2.setString(3, respuesta);
            ps2.executeUpdate();

            conexion.commit(); // Si todo va bien, guardamos cambios
            this.ultimoUsuarioRegistrado = usr;
            return true;

        } catch (Exception e) {
            try {
                conexion.rollback(); // Si algo falla, deshacemos todo
            } catch (Exception ex) {}
            System.err.println("Error en registro: " + e.getMessage());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (Exception ex) {}
        }
    }

	public ArrayList<Object[]> obtenerDatosUsuarios() {
		ArrayList<Object[]> listaUsuarios = new ArrayList<>();
		try (PreparedStatement pstmt = conexion.prepareStatement("SELECT USR, PWD FROM USERS");
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				listaUsuarios.add(new Object[] { rs.getString("USR"), rs.getString("PWD") });
			}
		} catch (SQLException e) {
		}
		return listaUsuarios;
	}

	/**
	 * Inserta un nuevo usuario en el sistema con permisos de usuario estándar (ROL = false).
	 * Este método es utilizado por el panel de administración para el alta manual de cuentas.
	 * 
	 * @param usr Nombre de usuario único para el nuevo registro.
	 * @param pwd Contraseña asociada a la cuenta.
	 * @return true si el usuario se insertó correctamente (filas afectadas > 0); false si hubo un error (ej. usuario duplicado).
	 */
	public boolean insertarUsuarioAdmin(String usr, String pwd) {
		// Definimos la consulta parametrizada para prevenir ataques de Inyección SQL
		String query = "INSERT INTO USERS (USR, PWD, ROL) VALUES (?, ?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(query)) {
			ps.setString(1, usr);
			ps.setString(2, pwd);
			// Por defecto, las altas manuales desde el panel se crean como usuarios estándar
			ps.setBoolean(3, false);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	// --- MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA Y PERFIL ---

	// Verifica si la respuesta secreta coincide con la de la BD
	public boolean verificarRespuestaSeguridad(String usr, int idPregunta, String respuesta) {
		// OJO con las mayúsculas/minúsculas de tus tablas en Aiven.
		String sql = "SELECT respuesta_texto FROM RESPONDE_PREGUNTA WHERE nombreUsuario = ? AND id_pregunta = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr);
			ps.setInt(2, idPregunta);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String respReal = rs.getString("respuesta_texto");
				// equalsIgnoreCase para que no importe si pone "Madrid" o "madrid"
				return respReal.equalsIgnoreCase(respuesta.trim());
			}
		} catch (SQLException e) {
			System.err.println("Error verificando seguridad: " + e.getMessage());
		}
		return false;
	}

	// Obtiene [puntos, total_actividades_completadas]
	public int[] obtenerEstadisticasUsuario(String usr) {
		int[] stats = new int[] { 0, 0 };
		String sqlPuntos = "SELECT puntos FROM USERS WHERE USR = ?";
		String sqlActividades = "SELECT COUNT(*) FROM COMPLETAR_ACTIVIDAD WHERE nombreUsuario = ?";

		try {
			// 1. Obtener Puntos
			PreparedStatement ps1 = conexion.prepareStatement(sqlPuntos);
			ps1.setString(1, usr);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next())
				stats[0] = rs1.getInt(1);
			ps1.close();

			// 2. Obtener Actividades Completadas
			PreparedStatement ps2 = conexion.prepareStatement(sqlActividades);
			ps2.setString(1, usr);
			ResultSet rs2 = ps2.executeQuery();
			if (rs2.next())
				stats[1] = rs2.getInt(1);
			ps2.close();
		} catch (SQLException e) {
			System.err.println("Error sacando estadisticas: " + e.getMessage());
		}
		return stats;
	}
	
	public boolean actualizarAjustes(String usrActual, String nuevoUsr, String nuevoCorreo, String nuevaPwd) {
		// 1. Empezamos a construir la query básica (Nombre y Correo)
		StringBuilder sql = new StringBuilder("UPDATE USERS SET USR = ?, correo = ?");
		
		// 2. Comprobamos si el usuario ha escrito una nueva contraseña
		boolean cambiarPwd = (nuevaPwd != null && !nuevaPwd.trim().isEmpty());
		
		if (cambiarPwd) {
			sql.append(", PWD = ?"); // Añadimos la columna de la contraseña
		}
		
		// 3. Añadimos el final de la query
		sql.append(" WHERE USR = ?");

		try {
			java.sql.PreparedStatement ps = conexion.prepareStatement(sql.toString());
			ps.setString(1, nuevoUsr);
			ps.setString(2, nuevoCorreo);
			
			// 4. Asignamos los parámetros dependiendo de si hay contraseña o no
			if (cambiarPwd) {
				ps.setString(3, nuevaPwd);
				ps.setString(4, usrActual);
			} else {
				// Si no hay contraseña, el parámetro 3 pasa a ser el usrActual del WHERE
				ps.setString(3, usrActual); 
			}
			
			return ps.executeUpdate() > 0;
			
		} catch (java.sql.SQLException e) {
			System.err.println("Error al actualizar perfil: " + e.getMessage());
			return false;
		}
	}

	// =========================================================================
	// 5. GESTIÓN DE ACTIVIDADES, MISIONES Y EVENTOS
	// =========================================================================

	public boolean crearActividad(String nombre, String tipo, String dificultad, int puntos, String frecuencia,
			String unidad) {
		String sql = "INSERT INTO ACTIVIDADES (nombre, tipo, dificultad, puntos, frecuencia, unidad_medida) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
			pstmt.setString(1, nombre);
			pstmt.setString(2, tipo);
			pstmt.setString(3, dificultad);
			pstmt.setInt(4, puntos);
			pstmt.setString(5, frecuencia);
			pstmt.setString(6, unidad);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Registra la finalización de una actividad por parte de un usuario específico.
	 * El método realiza una búsqueda previa del ID de la actividad basándose en su nombre
	 * para asegurar la integridad referencial antes de insertar el registro en el historial.
	 * 
	 * @param usr El nombre del usuario que completa la acción.
	 * @param nombreActividad El nombre legible de la actividad realizada.
	 * @return true si se encontró la actividad y se registró el progreso; false en caso contrario.
	 */
	public boolean registrarActividadCompletada(String usr, String nombreActividad) {
		try {
			int idActividad = -1;
			
			// Obtenemos el ID numérico asociado al nombre de la actividad.
			PreparedStatement ps1 = conexion.prepareStatement("SELECT id_actividad FROM ACTIVIDADES WHERE nombre = ?");
			ps1.setString(1, nombreActividad);
			ResultSet rs = ps1.executeQuery();

			if (rs.next()) {
				idActividad = rs.getInt("id_actividad");
			}
			
			// Si el ID existe, registramos la relación en la tabla intermedia.
			if (idActividad != -1) {
				// Se asocia el usuario logueado con la actividad mediante sus llaves foráneas.
				PreparedStatement ps2 = conexion.prepareStatement(
						"INSERT INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad) VALUES (?, ?)");
				ps2.setString(1, usr);
				ps2.setInt(2, idActividad);
				return ps2.executeUpdate() > 0;
			}
		} catch (SQLException e) {
		}
		return false;
	}

	public ArrayList<Object[]> obtenerMisiones(String tipoMision) {
		ArrayList<Object[]> lista = new ArrayList<>();
		String query = "SELECT descripcion, recompensa FROM MISIONES WHERE tipo = ?";
		try (PreparedStatement ps = conexion.prepareStatement(query)) {
			ps.setString(1, tipoMision);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				lista.add(new Object[] { rs.getString("descripcion"), rs.getInt("recompensa") + " EXP" });
			}
		} catch (SQLException e) {
		}
		return lista;
	}

	public ArrayList<Object[]> obtenerEventos() {
		ArrayList<Object[]> lista = new ArrayList<>();
		String sql = "SELECT nombre, tipo_evento, puntos_recompensa FROM EVENTOS ORDER BY fecha_evento ASC";
		try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				lista.add(new Object[] { rs.getString("nombre"), rs.getString("tipo_evento"),
						rs.getInt("puntos_recompensa") });
			}
		} catch (SQLException e) {
		}
		return lista;
	}

	public boolean insertarEvento(String nombre, String tipo, int puntos, String fecha) {
		String sql = "INSERT INTO EVENTOS (nombre, tipo_evento, puntos_recompensa, fecha_evento) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, nombre);
			ps.setString(2, tipo);
			ps.setInt(3, puntos);
			ps.setString(4, fecha);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean eliminarEvento(String nombre) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM EVENTOS WHERE nombre = ?")) {
			ps.setString(1, nombre);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Gestiona la creación de un nuevo grupo de usuarios y asigna automáticamente
	 * al creador como el primer miembro del mismo. 
	 * Implementa una transacción SQL para asegurar que el grupo no se cree si
	 * falla la vinculación del creador.
	 * 
	 * @param nombre Nombre único del grupo.
	 * @param descripcion Breve texto descriptivo.
	 * @param esPrivado Define si el grupo requiere contraseña para unirse.
	 * @param contrasena Clave de acceso (será null si el grupo es público).
	 * @param creador Nombre del usuario que funda el grupo.
	 * @return true si el grupo y la pertenencia se registraron con éxito; false en caso de error.
	 */
	public boolean crearGrupo(String nombre, String descripcion, boolean esPrivado, String contrasena, String creador) {
		try {
			conexion.setAutoCommit(false);
			String sqlGrupo = "INSERT INTO GRUPO (nombre, descripcion, es_privado, contrasena) VALUES (?, ?, ?, ?)";
			// Utilizamos RETURN_GENERATED_KEYS para obtener el ID autonumérico creado por MySQL
			PreparedStatement ps1 = conexion.prepareStatement(sqlGrupo, PreparedStatement.RETURN_GENERATED_KEYS);
			ps1.setString(1, nombre);
			ps1.setString(2, descripcion);
			ps1.setBoolean(3, esPrivado);
			ps1.setString(4, esPrivado ? contrasena : null); // Si no es privado, guardamos null
			ps1.executeUpdate();

			// Recuperamos el ID generado por la base de datos para usarlo en la siguiente inserción
			ResultSet rs = ps1.getGeneratedKeys();
			int idGrupo = -1;
			if (rs.next()) {
				idGrupo = rs.getInt(1);
			}
			ps1.close();

			// Si el grupo se creó, añadimos al usuario a la tabla PERTENECE_GRUPO
			if (idGrupo != -1) {
				String sqlPertenece = "INSERT INTO PERTENECE_GRUPO (nombreUsuario, id_grupo) VALUES (?, ?)";
				PreparedStatement ps2 = conexion.prepareStatement(sqlPertenece);
				ps2.setString(1, creador);
				ps2.setInt(2, idGrupo);
				ps2.executeUpdate();
				ps2.close();

				// Si ambas operaciones fueron exitosas, consolidamos los cambios en la BD
				conexion.commit();
				return true;
			}
		} catch (SQLException e) {
			try {
				conexion.rollback();
			} catch (SQLException ex) {
			}
		} finally {
			try {
				conexion.setAutoCommit(true);
			} catch (SQLException ex) {
			}
		}
		return false;
	}

	// Devuelve los grupos a los que el usuario AÚN NO pertenece
	public String[] obtenerGruposDisponibles(String usr) {
		String sql = "SELECT nombre FROM GRUPO WHERE id_grupo NOT IN (SELECT id_grupo FROM PERTENECE_GRUPO WHERE nombreUsuario = ?)";
		return obtenerDatosColumna(sql, "nombre", usr);
	}

	// Intenta unir a un usuario validando la contraseña si es privado
	public String unirseGrupo(String usr, String nombreGrupo, String passIntentada) {
		try {
			// 1. Buscamos los datos del grupo
			String sqlGrupo = "SELECT id_grupo, es_privado, contrasena FROM GRUPO WHERE nombre = ?";
			PreparedStatement ps = conexion.prepareStatement(sqlGrupo);
			ps.setString(1, nombreGrupo);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int idGrupo = rs.getInt("id_grupo");
				boolean privado = rs.getBoolean("es_privado");
				String passReal = rs.getString("contrasena");
				ps.close();

				// 2. Comprobamos contraseña
				if (privado && (passReal != null && !passReal.equals(passIntentada))) {
					return "Contraseña incorrecta.";
				}

				// 3. Le unimos al grupo
				String sqlUnir = "INSERT INTO PERTENECE_GRUPO (nombreUsuario, id_grupo) VALUES (?, ?)";
				PreparedStatement psUnir = conexion.prepareStatement(sqlUnir);
				psUnir.setString(1, usr);
				psUnir.setInt(2, idGrupo);
				psUnir.executeUpdate();
				psUnir.close();

				return "EXITO";
			}
		} catch (SQLException e) {
			return "Error de base de datos.";
		}
		return "El grupo no existe.";
	}
	
	public boolean existeUsuario(String usr) {
        String query = "SELECT USR FROM USERS WHERE USR = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
            pstmt.setString(1, usr);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public int modificar(String usr, String pwd) {
        try (PreparedStatement pstmt = conexion.prepareStatement("UPDATE USERS SET PWD = ? WHERE USR = ?")) {
            pstmt.setString(1, pwd);
            pstmt.setString(2, usr);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int borrar(String usr, String pwd) {
        try (PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM USERS WHERE usr = ? AND pwd=?")) {
            pstmt.setString(1, usr);
            pstmt.setString(2, pwd);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

	// Permite a un usuario abandonar un grupo
	public boolean abandonarGrupo(String usr, String nombreGrupo) {
		// Hacemos el DELETE usando una subconsulta para encontrar el id_grupo por su
		// nombre
		String sql = "DELETE FROM PERTENECE_GRUPO WHERE nombreUsuario = ? AND id_grupo = (SELECT id_grupo FROM GRUPO WHERE nombre = ?)";
		try (java.sql.PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr);
			ps.setString(2, nombreGrupo);
			return ps.executeUpdate() > 0;
		} catch (java.sql.SQLException e) {
			System.err.println("Error al abandonar grupo: " + e.getMessage());
			return false;
		}
	}
	// =========================================================================
	// 6. GESTIÓN DE NOTICIAS
	// =========================================================================

	public ArrayList<Object[]> obtenerNoticias() {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT tipo, titulo FROM NOTICIAS");
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String formato = "[" + rs.getString("tipo") + "] " + rs.getString("titulo");
				lista.add(new Object[] { formato });
			}
		} catch (SQLException e) {
		}
		return lista;
	}

	public ArrayList<Object[]> getNoticiasAdmin() {
		ArrayList<Object[]> listaNoticias = new ArrayList<>();
		try (PreparedStatement pstmt = conexion.prepareStatement("SELECT titulo, tipo FROM NOTICIAS");
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				listaNoticias.add(new Object[] { rs.getString("titulo"), rs.getString("tipo") });
			}
		} catch (SQLException e) {
		}
		return listaNoticias;
	}

	public boolean añadirNoticia(String titulo, String tipo) {
		try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO NOTICIAS (titulo, tipo) VALUES (?, ?)")) {
			ps.setString(1, titulo);
			ps.setString(2, tipo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean eliminarNoticia(String titulo) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM NOTICIAS WHERE titulo = ?")) {
			ps.setString(1, titulo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public void guardarConfiguracion(String nUsr, String nPwd, String nUrl) {
		try (PrintWriter pw = new PrintWriter(new FileWriter("config.ini"))) {
			pw.println("usr: " + nUsr);
			pw.println("pwd: " + nPwd);
			pw.println("url: " + nUrl);
		} catch (IOException ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

	public String getLoginDB() {
		return this.login;
	}

	public String getPwdDB() {
		return this.pwdDB;
	}

	public String getUrlDB() {
		return this.url;
	}
	
	/**
     * Recupera el código OTP generado automáticamente por el motor de base de datos.
     * Este método actúa como puente entre el Trigger de MySQL y el servicio de Email en Java.
     * 
     * @param usuario Nombre del usuario recién registrado
     * @return El código alfanumérico de 6 caracteres o null si no se encuentra
     */
	public String obtenerUltimoOTP(String usuario) {
	    String otp = null;
	    // Buscamos el código más reciente generado para este usuario
	    String sql = "SELECT otp_code FROM OTP_CODES WHERE USR = ? ORDER BY expires_at DESC LIMIT 1";
	    
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	        pst.setString(1, usuario);
	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            otp = rs.getString("otp_code");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al recuperar OTP: " + e.getMessage());
	    }
	    return otp;
	}
	
	public Connection getConexion() {
	    return this.conexion;
	}

	// Para saber a quién estamos verificando en la ventana OTP
	private String ultimoUsuarioRegistrado;

	public String getUltimoUsuarioRegistrado() {
	    return this.ultimoUsuarioRegistrado;
	}
	
	/**
	 * Establece el nombre del usuario que está intentando acceder o registrarse.
	 * Se utiliza para que la ventana de verificación OTP sepa a qué usuario validar.
	 * * @param usr Nombre de usuario
	 */
	public void setUltimoUsuarioRegistrado(String usr) {
	    this.ultimoUsuarioRegistrado = usr;
	}
	
	public boolean actualizarPuntos(String usuario, int puntos) {
	    // Usamos SUM para que acepte valores negativos (restar) y positivos (sumar)
	    String sql = "UPDATE USERS SET puntos = puntos + ? WHERE USR = ?";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, puntos);
	        ps.setString(2, usuario);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        return false;
	    }
	}

	public boolean asignarLogroAUsuario(String usuario, String nombreLogro) {
	    // 1. Buscamos el ID del logro por su nombre
	    String sqlId = "SELECT id_logro FROM LOGROS WHERE nombre = ?";
	    try {
	        PreparedStatement ps1 = conexion.prepareStatement(sqlId);
	        ps1.setString(1, nombreLogro);
	        ResultSet rs = ps1.executeQuery();
	        
	        if (rs.next()) {
	            int id = rs.getInt("id_logro");
	            // 2. Insertamos la relación (HACER_LOGRO es tu tabla intermedia)
	            String sqlIns = "INSERT INTO HACER_LOGRO (nombreUsuario, id_logro, fecha_desbloqueo) VALUES (?, ?, CURRENT_DATE)";
	            PreparedStatement ps2 = conexion.prepareStatement(sqlIns);
	            ps2.setString(1, usuario);
	            ps2.setInt(2, id);
	            return ps2.executeUpdate() > 0;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al dar logro: " + e.getMessage());
	    }
	    return false;
	}
	
	public int consultarPuntosUsuario(String usuario) {
	    String sql = "SELECT puntos FROM USERS WHERE USR = ?";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setString(1, usuario);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) return rs.getInt("puntos");
	    } catch (SQLException e) {
	        System.err.println("Error al consultar puntos: " + e.getMessage());
	    }
	    return 0;
	}
	
	public boolean eliminarLogroUsuario(String usuario, String nombreLogro) {
	    String sql = "DELETE HL FROM HACER_LOGRO HL " +
	                 "JOIN LOGROS L ON HL.id_logro = L.id_logro " +
	                 "WHERE HL.nombreUsuario = ? AND L.nombre = ?";
	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setString(1, usuario);
	        ps.setString(2, nombreLogro);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        return false;
	    }
	}
}