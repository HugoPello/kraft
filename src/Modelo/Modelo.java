package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Vista.Vista;

/**
 * Clase que gestiona la lógica de negocio y la persistencia de datos. 
 * Se comunica con la base de datos MySQL mediante JDBC. 
 * @author Hugo / Carlos / Diego
 * @version 2.0 (Código Completo, Refactorizado y Sin Conflictos)
 */
public class Modelo {

	private Vista[] misVistas;
	private String login, pwdDB, url;
	private Connection conexion;
	private String usuarioLogueado;
	private String tipoActividadSeleccionada;
	private boolean ROL = false;
	private int fallos;
	private String ultimoUsuarioRegistrado;

	public Modelo() { 
		leerConfig(); 
	}

	// =========================================================================
	// 1. CONEXIÓN Y CONFIGURACIÓN
	// =========================================================================

	public void leerConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader("config.ini"))) {
			this.login = extraerValor(br.readLine());
			this.pwdDB = extraerValor(br.readLine());
			this.url = extraerValor(br.readLine());
		} catch (Exception ex) { 
			System.err.println("Error al leer config.ini"); 
		}
	}

	private String extraerValor(String linea) {
		if (linea != null && linea.contains(":")) {
			return linea.substring(linea.indexOf(":") + 1).trim();
		}
		return (linea != null) ? linea.trim() : "";
	}

	public void guardarConfig(String nUsr, String nPwd, String nUrl) {
		try (PrintWriter pw = new PrintWriter(new FileWriter("config.ini"))) {
			pw.println("usr: " + nUsr); 
			pw.println("pwd: " + nPwd); 
			pw.println("url: " + nUrl);
			this.login = nUsr; 
			this.pwdDB = nPwd; 
			this.url = nUrl;
			if (conexion != null && !conexion.isClosed()) conexion.close();
		} catch (Exception ex) {}
	}

	public boolean conectarBD() {
		try {
			if (conexion == null || conexion.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conexion = DriverManager.getConnection(url, login, pwdDB);
				System.out.println("-> Conexión establecida.");
			}
			return true;
		} catch (Exception e) { return false; }
	}

	public Connection getConexion() { return this.conexion; }
	public void setVistas(Vista[] misVistas) { this.misVistas = misVistas; }
	public void terminar() {
		try { if (conexion != null) conexion.close(); } catch (SQLException e) { e.printStackTrace(); }
	}

	// =========================================================================
	// 2. GETTERS Y SETTERS DE SESIÓN
	// =========================================================================

	public void setUsuarioLogueado(String usuario) { this.usuarioLogueado = usuario; }
	public String getUsuarioLogueado() { return this.usuarioLogueado; }
	public String getUsuarioActual() { return this.usuarioLogueado; }

	public void setTipoActividadSeleccionada(String tipo) { this.tipoActividadSeleccionada = tipo; }
	public String getTipoActividadSeleccionada() { return this.tipoActividadSeleccionada; }

	public void setUltimoUsuarioRegistrado(String usr) { this.ultimoUsuarioRegistrado = usr; }
	public String getUltimoUsuarioRegistrado() { return this.ultimoUsuarioRegistrado; }

	public boolean isAdmin() { return ROL; }
	public boolean esUsuarioAdmin() { return this.ROL; }

	public String getLoginDB() { return this.login; }
	public String getPwdDB() { return this.pwdDB; }
	public String getUrlDB() { return this.url; }

	public String sacarEmail() {
		String email = "";
		String query = "SELECT correo FROM USERS WHERE USR = ?";
		try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
			pstmt.setString(1, this.usuarioLogueado);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) email = rset.getString("correo");
		} catch (Exception e) {}
		return email;
	}

	// =========================================================================
	// 3. MOTORES DE CONSULTA GENÉRICOS
	// =========================================================================

	public String[] obtenerDatosColumna(String query, String nombreColumna, Object... parametros) {
		ArrayList<String> listaDatos = new ArrayList<>();
		if (conexion == null) return new String[] { "Sin conexión a BD" };
		try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
			for (int i = 0; i < parametros.length; i++) pstmt.setObject(i + 1, parametros[i]);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) listaDatos.add(rs.getString(nombreColumna));
		} catch (SQLException e) { System.err.println("Error SQL: " + e.getMessage()); }
		if (listaDatos.isEmpty()) listaDatos.add("No hay datos");
		return listaDatos.toArray(new String[0]);
	}

	public Object[][] obtenerDatosTabla(String query, int numColumnas, Object... parametros) {
		ArrayList<Object[]> listaDatos = new ArrayList<>();
		try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
			for (int i = 0; i < parametros.length; i++) pstmt.setObject(i + 1, parametros[i]);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Object[] fila = new Object[numColumnas];
				for (int j = 0; j < numColumnas; j++) fila[j] = rs.getObject(j + 1);
				listaDatos.add(fila);
			}
		} catch (SQLException e) { System.err.println("Error SQL: " + e.getMessage()); }
		if (listaDatos.isEmpty()) {
			Object[] filaVacia = new Object[numColumnas];
			filaVacia[0] = "Sin registros";
			for (int i = 1; i < numColumnas; i++) filaVacia[i] = "";
			return new Object[][] { filaVacia };
		}
		return listaDatos.toArray(new Object[0][0]);
	}

	// =========================================================================
	// 4. AUTENTICACIÓN Y SEGURIDAD
	// =========================================================================

	public String loginSeguro(String usr, String pwd) {
		String query = "SELECT correo, is_verified, ROL FROM USERS WHERE USR=? AND PWD=?";
		try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
			pstmt.setString(1, usr);
			pstmt.setString(2, pwd);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				this.usuarioLogueado = usr;
				this.ROL = rset.getBoolean("ROL");
				String email = rset.getString("correo");
				
				if (email != null && !email.trim().isEmpty()) {
					try (java.sql.CallableStatement cst = conexion.prepareCall("{call create_otp(?)}")) {
						cst.setString(1, usr);
						cst.execute();
					}
					this.ultimoUsuarioRegistrado = usr;
					return "OTP_ENVIADO";
				}
				return "LOGIN_DIRECTO";
			} else {
				this.fallos++;
				if (this.fallos >= 3) return "CERRAR";
				return "INCORRECTO";
			}
		} catch (SQLException e) { return "ERROR_BD"; }
	}

	public boolean registroCompleto(String usr, String pwd, boolean rol, String fechaNac, int idPregunta, String respuesta, String correo) {
		try {
			conexion.setAutoCommit(false);
			String query1 = "INSERT INTO USERS (USR, PWD, ROL, correo, fechaNacimiento, is_verified) VALUES (?, ?, ?, ?, ?, 0)";
			PreparedStatement ps1 = conexion.prepareStatement(query1);
			ps1.setString(1, usr); ps1.setString(2, pwd); ps1.setBoolean(3, rol); ps1.setString(4, correo);
			if (fechaNac == null || fechaNac.trim().isEmpty()) ps1.setNull(5, java.sql.Types.DATE);
			else ps1.setDate(5, java.sql.Date.valueOf(fechaNac));
			ps1.executeUpdate();

			String query2 = "INSERT INTO RESPONDE_PREGUNTA (nombreUsuario, id_pregunta, respuesta_texto) VALUES (?, ?, ?)";
			PreparedStatement ps2 = conexion.prepareStatement(query2);
			ps2.setString(1, usr); ps2.setInt(2, idPregunta); ps2.setString(3, respuesta);
			ps2.executeUpdate();

			conexion.commit();
			this.ultimoUsuarioRegistrado = usr;
			return true;
		} catch (Exception e) {
			try { conexion.rollback(); } catch (Exception ex) {}
			return false;
		} finally {
			try { conexion.setAutoCommit(true); } catch (Exception ex) {}
		}
	}

	public String obtenerUltimoOTP(String usuario) {
		String otp = null;
		String sql = "SELECT otp_code FROM OTP_CODES WHERE USR = ? ORDER BY expires_at DESC LIMIT 1";
		try (PreparedStatement pst = conexion.prepareStatement(sql)) {
			pst.setString(1, usuario);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) otp = rs.getString("otp_code");
		} catch (SQLException e) {}
		return otp;
	}

	public boolean verificarRespuestaSeguridad(String usr, int idPregunta, String respuesta) {
		String sql = "SELECT respuesta_texto FROM RESPONDE_PREGUNTA WHERE nombreUsuario = ? AND id_pregunta = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr); ps.setInt(2, idPregunta);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getString("respuesta_texto").equalsIgnoreCase(respuesta.trim());
		} catch (SQLException e) {}
		return false;
	}

	public boolean verificarRespuestaSoloPorUsuario(String usr, String respuesta) {
		String sql = "SELECT respuesta_texto FROM RESPONDE_PREGUNTA WHERE nombreUsuario = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getString("respuesta_texto").equalsIgnoreCase(respuesta.trim());
		} catch (SQLException e) {}
		return false;
	}

	public String obtenerPreguntaDeUsuario(String usuario) {
		String sql = "SELECT P.enunciado FROM PREGUNTA_SEGURIDAD P JOIN RESPONDE_PREGUNTA R ON P.id_pregunta = R.id_pregunta WHERE R.nombreUsuario = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getString("enunciado");
		} catch (SQLException e) {}
		return null;
	}

	/**
	 * Actualiza los datos de identidad y seguridad del usuario en la base de datos.
	 * Implementa una construcción dinámica de la consulta SQL mediante StringBuilder 
	 * para permitir la actualización opcional de la contraseña (PWD) sin afectar 
	 * a la integridad del resto de los campos.
	 * * @param usrActual Identificador actual del usuario (necesario para la cláusula WHERE).
	 * @param nuevoUsr Nuevo nombre de usuario (puede ser el mismo si no se desea cambiar).
	 * @param nuevoCorreo Nueva dirección de correo electrónico vinculada.
	 * @param nuevaPwd Nueva contraseña cifrada (si es null o vacía, se omite en la actualización).
	 * @return true si los cambios se aplicaron correctamente; false en caso de error de persistencia.
	 */
	public boolean actualizarAjustes(String usrActual, String nuevoUsr, String nuevoCorreo, String nuevaPwd) {
		StringBuilder sql = new StringBuilder("UPDATE USERS SET USR = ?, correo = ?");
		boolean cambiarPwd = (nuevaPwd != null && !nuevaPwd.trim().isEmpty());
		if (cambiarPwd) sql.append(", PWD = ?");
		sql.append(" WHERE USR = ?");
		
		try (PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
			ps.setString(1, nuevoUsr); ps.setString(2, nuevoCorreo);
			if (cambiarPwd) { ps.setString(3, nuevaPwd); ps.setString(4, usrActual); } 
			else { ps.setString(3, usrActual); }
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public boolean existeUsuario(String usr) {
		try (PreparedStatement pstmt = conexion.prepareStatement("SELECT USR FROM USERS WHERE USR = ?")) {
			pstmt.setString(1, usr);
			return pstmt.executeQuery().next();
		} catch (SQLException e) { return false; }
	}

	public int modificar(String usr, String pwd) {
		try (PreparedStatement pstmt = conexion.prepareStatement("UPDATE USERS SET PWD = ? WHERE USR = ?")) {
			pstmt.setString(1, pwd); pstmt.setString(2, usr);
			return pstmt.executeUpdate();
		} catch (SQLException e) { return 0; }
	}

	public int borrar(String usr, String pwd) {
		try (PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM USERS WHERE usr = ? AND pwd=?")) {
			pstmt.setString(1, usr); pstmt.setString(2, pwd);
			return pstmt.executeUpdate();
		} catch (SQLException e) { return 0; }
	}

	public boolean insertarUsuarioAdmin(String usr, String pwd) {
		try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO USERS (USR, PWD, ROL) VALUES (?, ?, ?)")) {
			ps.setString(1, usr); ps.setString(2, pwd); ps.setBoolean(3, false);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public ArrayList<Object[]> obtenerDatosUsuarios() {
		ArrayList<Object[]> listaUsuarios = new ArrayList<>();
		try (PreparedStatement pstmt = conexion.prepareStatement("SELECT USR, PWD FROM USERS"); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) listaUsuarios.add(new Object[] { rs.getString("USR"), rs.getString("PWD") });
		} catch (SQLException e) {}
		return listaUsuarios;
	}

	// =========================================================================
	// 5. ESTADÍSTICAS Y ECONOMÍA
	// =========================================================================

	public int[] obtenerEstadisticasUsuario(String usr) {
		int[] stats = new int[] { 0, 0, 0 }; // XP, Actividades, Monedas
		try {
			PreparedStatement ps1 = conexion.prepareStatement("SELECT puntos, monedas_tienda FROM USERS WHERE USR = ?");
			ps1.setString(1, usr);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				stats[0] = rs1.getInt("puntos");
				stats[2] = rs1.getInt("monedas_tienda");
			}
			PreparedStatement ps2 = conexion.prepareStatement("SELECT COUNT(*) FROM COMPLETAR_ACTIVIDAD WHERE nombreUsuario = ?");
			ps2.setString(1, usr);
			ResultSet rs2 = ps2.executeQuery();
			if (rs2.next()) stats[1] = rs2.getInt(1);
		} catch (SQLException e) {}
		return stats;
	}

	public int[] obtenerProgresoGlobal(String usr) {
		int[] progreso = new int[]{0, 0, 0, 0}; // MisLog, TotLog, MisCos, TotCos
		try {
			ResultSet rs1 = conexion.prepareStatement("SELECT COUNT(*) FROM LOGROS").executeQuery();
			if(rs1.next()) progreso[1] = rs1.getInt(1);
			PreparedStatement ps2 = conexion.prepareStatement("SELECT COUNT(*) FROM HACER_LOGRO WHERE nombreUsuario = ? AND esta_hecho = 1");
			ps2.setString(1, usr);
			ResultSet rs2 = ps2.executeQuery();
			if(rs2.next()) progreso[0] = rs2.getInt(1);
			ResultSet rs3 = conexion.prepareStatement("SELECT COUNT(*) FROM COSMETICOS").executeQuery();
			if(rs3.next()) progreso[3] = rs3.getInt(1);
			PreparedStatement ps4 = conexion.prepareStatement("SELECT COUNT(*) FROM TIENE_COSMETICO WHERE nombreUsuario = ?");
			ps4.setString(1, usr);
			ResultSet rs4 = ps4.executeQuery();
			if(rs4.next()) progreso[2] = rs4.getInt(1);
		} catch (SQLException e) {}
		return progreso;
	}

	public boolean actualizarPuntos(String usuario, int puntos) {
		String sql = "UPDATE USERS SET puntos = puntos + ? WHERE USR = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setInt(1, puntos); ps.setString(2, usuario);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public int consultarPuntosUsuario(String usuario) {
		String sql = "SELECT puntos FROM USERS WHERE USR = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("puntos");
		} catch (SQLException e) {}
		return 0;
	}

	// =========================================================================
	// 6. GESTIÓN DE ACTIVIDADES
	// =========================================================================

	/**
	 * Realiza la creación atómica de una actividad, su misión vinculada y la relación entre ambas.
	 * Este método es un "workflow" completo que garantiza la consistencia del catálogo:
	 * 1. Inserta la ACTIVIDAD con sus métricas (meta, puntos, dificultad).
	 * 2. Genera automáticamente una MISIÓN lógica basada en los datos de la actividad 
	 * (ej: "Hacer 5.0 km de Running").
	 * 3. Crea el registro en la tabla puente MISION_ACTIVIDAD para establecer la meta técnica.
	 * * Implementa control de transacciones (Commit/Rollback) para asegurar que no se creen 
	 * actividades sin misiones o viceversa.
	 * * @param nombre Nombre de la actividad física.
	 * @param tipo Categoría (Fuerza, Cardio, etc.).
	 * @param dificultad Nivel de esfuerzo (Baja, Media, Alta).
	 * @param puntos Recompensa base por ejecución.
	 * @param frecuencia Diaria o Semanal.
	 * @param unidad Unidad de medida (km, repeticiones, minutos).
	 * @param meta Cantidad objetivo para completar la misión.
	 * @return true si el proceso completo tuvo éxito; false si hubo rollback.
	 */
	public boolean crearActividad(String nombre, String tipo, String dificultad, int puntos, String frecuencia, String unidad, double meta, String categoria) {
		try {
			conexion.setAutoCommit(false);
			String sqlActividad = "INSERT INTO ACTIVIDADES (nombre, tipo, dificultad, puntos, frecuencia, unidad_medida, meta_cantidad, categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps1 = conexion.prepareStatement(sqlActividad, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps1.setString(1, nombre); ps1.setString(2, tipo); ps1.setString(3, dificultad); ps1.setInt(4, puntos);
			ps1.setString(5, frecuencia); ps1.setString(6, unidad); ps1.setDouble(7, meta); ps1.setString(8, categoria);
			ps1.executeUpdate();

			ResultSet rsActividad = ps1.getGeneratedKeys();
			int idActividad = -1;
			if (rsActividad.next()) idActividad = rsActividad.getInt(1);
			ps1.close();

			String descMision = "Hacer " + meta + " " + unidad + " de " + nombre;
			int recompensaMision = puntos * 2;
			String tipoMision = (frecuencia.equalsIgnoreCase("Semanal")) ? "Semanal" : "Diaria";

			String sqlMision = "INSERT INTO MISIONES (descripcion, recompensa, tipo) VALUES (?, ?, ?)";
			PreparedStatement ps2 = conexion.prepareStatement(sqlMision, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps2.setString(1, descMision); ps2.setInt(2, recompensaMision); ps2.setString(3, tipoMision);
			ps2.executeUpdate();

			ResultSet rsMision = ps2.getGeneratedKeys();
			int idMision = -1;
			if (rsMision.next()) idMision = rsMision.getInt(1);
			ps2.close();

			if (idActividad != -1 && idMision != -1) {
				String sqlPuente = "INSERT INTO MISION_ACTIVIDAD (id_mision, id_actividad, cantidad_requerida) VALUES (?, ?, ?)";
				PreparedStatement ps3 = conexion.prepareStatement(sqlPuente);
				ps3.setInt(1, idMision); ps3.setInt(2, idActividad); ps3.setDouble(3, meta);
				ps3.executeUpdate(); ps3.close();
			}
			conexion.commit();
			return true;
		} catch (SQLException e) {
			try { conexion.rollback(); } catch (SQLException ex) {}
			return false;
		} finally {
			try { conexion.setAutoCommit(true); } catch (SQLException ex) {}
		}
	}

	public boolean registrarActividadCompletada(String usr, String nombreActividad, double cantidad) {
		try {
			String sqlInfo = "SELECT id_actividad, categoria, meta_cantidad, puntos FROM ACTIVIDADES WHERE nombre = ?";
			PreparedStatement psInfo = conexion.prepareStatement(sqlInfo);
			psInfo.setString(1, nombreActividad);
			ResultSet rsInfo = psInfo.executeQuery();

			if (rsInfo.next()) {
				int idAct = rsInfo.getInt("id_actividad");
				String cat = rsInfo.getString("categoria");
				double meta = rsInfo.getDouble("meta_cantidad");
				int ptsBase = rsInfo.getInt("puntos");

				// 1. Insertamos historial
				PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO COMPLETAR_ACTIVIDAD (nombreUsuario, id_actividad, cantidad) VALUES (?, ?, ?)");
				ps2.setString(1, usr); ps2.setInt(2, idAct); ps2.setDouble(3, cantidad);
				ps2.executeUpdate();

				// 2. Pagamos XP y Monedas
				int recompensa = (int) ((cantidad / meta) * ptsBase);
				if (recompensa > 0) {
					PreparedStatement psPago = conexion.prepareStatement("UPDATE USERS SET puntos = puntos + ?, monedas_tienda = monedas_tienda + ? WHERE USR = ?");
					psPago.setInt(1, recompensa); psPago.setInt(2, recompensa); psPago.setString(3, usr);
					psPago.executeUpdate();
				}

				// 3. Motor Dinámico de Logros
				String sqlUpsert = "INSERT INTO HACER_LOGRO (nombreUsuario, id_logro, progreso, esta_hecho) VALUES (?, (SELECT id_logro FROM LOGROS WHERE categoria = ? LIMIT 1), LEAST(100, ?), IF(? >= 100, 1, 0)) " +
								   "ON DUPLICATE KEY UPDATE progreso = LEAST(100, progreso + ?), esta_hecho = IF(progreso >= 100, 1, 0), fecha_desbloqueo = IF(progreso >= 100 AND fecha_desbloqueo IS NULL, CURRENT_DATE, fecha_desbloqueo)";
				PreparedStatement psLog = conexion.prepareStatement(sqlUpsert);
				double porc = (cantidad / meta) * 100.0;
				psLog.setString(1, usr); psLog.setString(2, cat); psLog.setDouble(3, porc); psLog.setDouble(4, porc); psLog.setDouble(5, porc);
				psLog.executeUpdate();

				return true;
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return false;
	}

	public int getPorcentajeCategoria(String usr, String tipo) {
		String sqlHechas = "SELECT COUNT(*) FROM (SELECT CA.id_actividad FROM COMPLETAR_ACTIVIDAD CA JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad "
				+ "WHERE CA.nombreUsuario = ? AND A.tipo = ? GROUP BY CA.id_actividad, A.meta_cantidad HAVING SUM(CA.cantidad) >= A.meta_cantidad) as completadas";
		String sqlTotal = "SELECT COUNT(*) FROM ACTIVIDADES WHERE tipo = ?";
		try {
			int hechas = 0, total = 0;
			PreparedStatement ps1 = conexion.prepareStatement(sqlHechas);
			ps1.setString(1, usr); ps1.setString(2, tipo);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) hechas = rs1.getInt(1);

			PreparedStatement ps2 = conexion.prepareStatement(sqlTotal);
			ps2.setString(1, tipo);
			ResultSet rs2 = ps2.executeQuery();
			if (rs2.next()) total = rs2.getInt(1);

			if (total == 0) return 0;
			return (int) ((hechas * 100.0) / total);
		} catch (SQLException e) { return 0; }
	}

	// =========================================================================
	// 7. TIENDA Y COSMÉTICOS
	// =========================================================================

	public Object[][] obtenerCatalogoTienda(String usuario) {
		String sql = "SELECT C.nombre, C.tipo_personalizacion, CONCAT(C.coste_monedas, ' 💰'), "
				+ "IF(TC.id_cosmetico IS NULL, 'DISPONIBLE', 'ADQUIRIDO') FROM COSMETICOS C "
				+ "LEFT JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico AND TC.nombreUsuario = ?";
		return obtenerDatosTabla(sql, 4, usuario);
	}

	public boolean comprarCosmetico(String usuario, String nombreCosmetico, int precio) {
		try {
			conexion.setAutoCommit(false);
			PreparedStatement psId = conexion.prepareStatement("SELECT id_cosmetico FROM COSMETICOS WHERE nombre = ?");
			psId.setString(1, nombreCosmetico);
			ResultSet rsId = psId.executeQuery();
			if (!rsId.next()) { conexion.rollback(); return false; }
			int idCosm = rsId.getInt("id_cosmetico");

			PreparedStatement psPago = conexion.prepareStatement("UPDATE USERS SET monedas_tienda = monedas_tienda - ? WHERE USR = ? AND monedas_tienda >= ?");
			psPago.setInt(1, precio); psPago.setString(2, usuario); psPago.setInt(3, precio);
			if (psPago.executeUpdate() == 0) { conexion.rollback(); return false; }

			PreparedStatement psInv = conexion.prepareStatement("INSERT INTO TIENE_COSMETICO (nombreUsuario, id_cosmetico, equipado) VALUES (?, ?, false)");
			psInv.setString(1, usuario); psInv.setInt(2, idCosm);
			psInv.executeUpdate();

			conexion.commit();
			return true;
		} catch (SQLException e) {
			try { conexion.rollback(); } catch (SQLException ex) {}
			return false;
		} finally { try { conexion.setAutoCommit(true); } catch (SQLException ex) {} }
	}

	public boolean equiparCosmetico(String usuario, String nombreCosmetico) {
		try {
			PreparedStatement psInfo = conexion.prepareStatement("SELECT id_cosmetico, tipo_personalizacion FROM COSMETICOS WHERE nombre = ?");
			psInfo.setString(1, nombreCosmetico);
			ResultSet rsInfo = psInfo.executeQuery();
			if (!rsInfo.next()) return false;

			int idCosmetico = rsInfo.getInt("id_cosmetico");
			String tipo = rsInfo.getString("tipo_personalizacion");

			PreparedStatement psDes = conexion.prepareStatement("UPDATE TIENE_COSMETICO TC JOIN COSMETICOS C ON TC.id_cosmetico = C.id_cosmetico SET TC.equipado = false WHERE TC.nombreUsuario = ? AND C.tipo_personalizacion = ?");
			psDes.setString(1, usuario); psDes.setString(2, tipo);
			psDes.executeUpdate();

			PreparedStatement psEq = conexion.prepareStatement("UPDATE TIENE_COSMETICO SET equipado = true WHERE nombreUsuario = ? AND id_cosmetico = ?");
			psEq.setString(1, usuario); psEq.setInt(2, idCosmetico);
			return psEq.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public boolean desequiparCosmetico(String usr, String nombre) {
		String sql = "UPDATE TIENE_COSMETICO TC JOIN COSMETICOS C ON TC.id_cosmetico = C.id_cosmetico SET TC.equipado = false WHERE TC.nombreUsuario = ? AND C.nombre = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr); ps.setString(2, nombre);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public byte[] obtenerImagenCosmetico(String categoria, String usuario) {
		String sql = "SELECT C.imagen_blob FROM COSMETICOS C JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico WHERE TC.nombreUsuario = ? AND TC.equipado = true AND C.tipo_personalizacion = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario); ps.setString(2, categoria);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getBytes("imagen_blob");
		} catch (SQLException e) {}
		return null;
	}

	public boolean inyectarCosmeticoDesdePC(String nombre, String tipo, int coste, String rutaLocal) {
		String sql = "INSERT INTO COSMETICOS (nombre, tipo_personalizacion, coste_monedas, imagen_blob) VALUES (?, ?, ?, ?)";
		try (java.sql.PreparedStatement ps = getConexion().prepareStatement(sql)) {
			ps.setString(1, nombre); ps.setString(2, tipo); ps.setInt(3, coste);
			
			File archivo = new File(rutaLocal);
			byte[] bytesImagen = Files.readAllBytes(archivo.toPath());
			ps.setBytes(4, bytesImagen);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.err.println("Error subiendo cosmético: " + e.getMessage());
			return false;
		}
	}

	// =========================================================================
	// 8. MISIONES Y EVENTOS
	// =========================================================================

	public boolean tieneMisionesHoy(String usr) {
		try (PreparedStatement ps = conexion.prepareStatement("SELECT COUNT(*) FROM REALIZA_MISION WHERE nombreUsuario = ? AND fecha_asignacion = CURRENT_DATE")) {
			ps.setString(1, usr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getInt(1) > 0;
		} catch (SQLException e) {}
		return false;
	}

	public void generarMisionesDiarias(String usr) {
		try {
			PreparedStatement ps1 = conexion.prepareStatement("INSERT INTO REALIZA_MISION (nombreUsuario, id_mision, fecha_asignacion) SELECT ?, id_mision, CURRENT_DATE FROM MISIONES WHERE tipo = 'Diaria' ORDER BY RAND() LIMIT 3");
			ps1.setString(1, usr); ps1.executeUpdate();
			PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO REALIZA_MISION (nombreUsuario, id_mision, fecha_asignacion) SELECT ?, id_mision, CURRENT_DATE FROM MISIONES WHERE tipo = 'Semanal' ORDER BY RAND() LIMIT 1");
			ps2.setString(1, usr); ps2.executeUpdate();
		} catch (SQLException e) {}
	}

	/**
	 * Motor de progresión y recompensas. Gestiona el avance de las misiones activas 
	 * del usuario y automatiza la entrega de premios.
	 * * El proceso se divide en:
	 * 1. Incremento de progreso: Suma la cantidad realizada a todas las misiones 
	 * que vinculen al usuario con la actividad ejecutada.
	 * 2. Evaluación de objetivos: Identifica misiones que han alcanzado la meta requerida.
	 * 3. Entrega de premios (Transaccional): Incrementa la experiencia (EXP) y 
	 * monedas del usuario, marcando la misión como finalizada para evitar cobros duplicados.
	 * * @param usr Nombre del usuario que realiza la actividad.
	 * @param nombreActividad Nombre técnico de la actividad completada.
	 * @param cantidad Valor numérico del desempeño realizado (km, reps, etc.).
	 */
	public void actualizarProgresoMisiones(String usr, String nombreActividad, double cantidad) {
		String sql = "UPDATE REALIZA_MISION RM JOIN MISION_ACTIVIDAD MA ON RM.id_mision = MA.id_mision JOIN ACTIVIDADES A ON MA.id_actividad = A.id_actividad SET RM.progreso = RM.progreso + ? WHERE RM.nombreUsuario = ? AND A.nombre = ? AND RM.fecha_asignacion = CURRENT_DATE";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setDouble(1, cantidad); ps.setString(2, usr); ps.setString(3, nombreActividad);
			ps.executeUpdate();

			String sqlComprobar = "SELECT RM.id_mision, M.recompensa FROM REALIZA_MISION RM JOIN MISION_ACTIVIDAD MA ON RM.id_mision = MA.id_mision JOIN MISIONES M ON RM.id_mision = M.id_mision WHERE RM.nombreUsuario = ? AND RM.fecha_asignacion = CURRENT_DATE AND RM.completada = FALSE AND RM.progreso >= MA.cantidad_requerida";
			PreparedStatement psComprobar = conexion.prepareStatement(sqlComprobar);
			psComprobar.setString(1, usr);
			ResultSet rsMisionesHechas = psComprobar.executeQuery();

			while (rsMisionesHechas.next()) {
				int idMis = rsMisionesHechas.getInt("id_mision");
				int xpGanada = rsMisionesHechas.getInt("recompensa");
				PreparedStatement psPremio = conexion.prepareStatement("UPDATE USERS SET puntos = puntos + ?, monedas_tienda = monedas_tienda + ? WHERE USR = ?");
				psPremio.setInt(1, xpGanada); psPremio.setInt(2, xpGanada); psPremio.setString(3, usr);
				psPremio.executeUpdate();

				PreparedStatement psSellar = conexion.prepareStatement("UPDATE REALIZA_MISION SET completada = TRUE WHERE nombreUsuario = ? AND id_mision = ? AND fecha_asignacion = CURRENT_DATE");
				psSellar.setString(1, usr); psSellar.setInt(2, idMis);
				psSellar.executeUpdate();
			}
		} catch (SQLException e) {}
	}

	/**
	 * Recupera el listado de misiones activas y el estado de progreso del usuario actual.
	 * Utiliza un **LEFT JOIN** para asegurar que se muestren todas las misiones del catálogo, 
	 * incluso si el usuario aún no ha interactuado con ellas (en cuyo caso el progreso será 0).
	 * * El método transforma los datos técnicos de la base de datos (booleanos y doubles) 
	 * en información legible para la interfaz de usuario ("✅ HECHA" o "Progreso: X").
	 * * @param usr Identificador del usuario que consulta sus misiones.
	 * @param tipo Filtro para diferenciar entre misiones "Diaria" o "Semanal".
	 * @return Una lista de arrays de objetos lista para ser inyectada en un DefaultTableModel.
	 */
	public ArrayList<Object[]> obtenerMisionesHoy(String usr, String tipo) {
		ArrayList<Object[]> lista = new ArrayList<>();
		String sql = "SELECT DISTINCT M.descripcion, IFNULL(RM.progreso, 0) AS progreso, M.recompensa, IFNULL(RM.completada, 0) AS completada FROM MISIONES M LEFT JOIN REALIZA_MISION RM ON M.id_mision = RM.id_mision AND RM.nombreUsuario = ? WHERE M.tipo = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usr); ps.setString(2, tipo);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String estado = rs.getBoolean("completada") ? "✅ HECHA" : "Progreso: " + rs.getDouble("progreso");
				lista.add(new Object[] { rs.getString("descripcion"), estado, rs.getInt("recompensa") + " EXP" });
			}
		} catch (SQLException e) {}
		return lista;
	}

	public ArrayList<Object[]> obtenerMisiones(String tipoMision) {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT descripcion, tipo, recompensa FROM MISIONES WHERE tipo = ?")) {
			ps.setString(1, tipoMision);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) lista.add(new Object[] { rs.getString("descripcion"), rs.getString("tipo"), rs.getInt("recompensa") + " EXP" });
		} catch (SQLException e) {}
		return lista;
	}

	public boolean insertarMision(String descripcion, String tipo, int recompensa) {
		try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO MISIONES (descripcion, recompensa, tipo) VALUES (?, ?, ?)")) {
			ps.setString(1, descripcion); ps.setInt(2, recompensa); ps.setString(3, tipo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public boolean eliminarMision(int idMision) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM MISIONES WHERE id_mision = ?")) {
			ps.setInt(1, idMision);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public ArrayList<Object[]> consultarMisionesAdmin() {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT id_mision, descripcion, tipo, recompensa FROM MISIONES"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(new Object[] { rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4) });
		} catch (SQLException e) {}
		return lista;
	}

	public ArrayList<Object[]> obtenerEventos() {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT nombre, tipo_evento, puntos_recompensa FROM EVENTOS ORDER BY fecha_evento ASC"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(new Object[] { rs.getString("nombre"), rs.getString("tipo_evento"), rs.getInt("puntos_recompensa") });
		} catch (SQLException e) {}
		return lista;
	}

	public boolean insertarEvento(String nombre, String tipo, int puntos, String fecha) {
		try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO EVENTOS (nombre, tipo_evento, puntos_recompensa, fecha_evento) VALUES (?, ?, ?, ?)")) {
			ps.setString(1, nombre); ps.setString(2, tipo); ps.setInt(3, puntos); ps.setString(4, fecha);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public boolean eliminarEvento(String nombre) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM EVENTOS WHERE nombre = ?")) {
			ps.setString(1, nombre);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	// =========================================================================
	// 9. LOGROS
	// =========================================================================

	public Object[][] obtenerLogrosPendientes(String usuario) {
		String sql = "SELECT L.nombre, L.descripcion, L.puntos_logros, COALESCE(HL.progreso, 0) FROM LOGROS L LEFT JOIN HACER_LOGRO HL ON L.id_logro = HL.id_logro AND HL.nombreUsuario = ? WHERE HL.esta_hecho IS NULL OR HL.esta_hecho = 0";
		return obtenerDatosTabla(sql, 4, usuario);
	}

	public ArrayList<String> obtenerLogrosUsuario(String usuario) {
		ArrayList<String> logros = new ArrayList<>();
		String sql = "SELECT L.nombre FROM LOGROS L JOIN HACER_LOGRO HL ON L.id_logro = HL.id_logro WHERE HL.nombreUsuario = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) logros.add(rs.getString("nombre"));
		} catch (SQLException e) {}
		return logros;
	}

	public ArrayList<String> obtenerTodosLosLogros() {
		ArrayList<String> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT nombre FROM LOGROS"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(rs.getString("nombre"));
		} catch (SQLException e) {}
		return lista;
	}

	public boolean asignarLogroAUsuario(String usuario, String nombreLogro) {
		try {
			PreparedStatement ps1 = conexion.prepareStatement("SELECT id_logro FROM LOGROS WHERE nombre = ?");
			ps1.setString(1, nombreLogro);
			ResultSet rs = ps1.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id_logro");
				PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO HACER_LOGRO (nombreUsuario, id_logro, fecha_desbloqueo) VALUES (?, ?, CURRENT_DATE)");
				ps2.setString(1, usuario); ps2.setInt(2, id);
				return ps2.executeUpdate() > 0;
			}
		} catch (SQLException e) {}
		return false;
	}

	public boolean completarLogroEnBD(String usuario, String nombreLogro) {
		try {
			PreparedStatement psId = conexion.prepareStatement("SELECT id_logro FROM LOGROS WHERE nombre = ?");
			psId.setString(1, nombreLogro);
			ResultSet rs = psId.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id_logro");
				PreparedStatement psIns = conexion.prepareStatement("INSERT INTO HACER_LOGRO (nombreUsuario, id_logro, fecha_desbloqueo, esta_hecho, progreso) VALUES (?, ?, CURRENT_DATE, 1, 100) ON DUPLICATE KEY UPDATE esta_hecho = 1, progreso = 100, fecha_desbloqueo = CURRENT_DATE");
				psIns.setString(1, usuario); psIns.setInt(2, id);
				return psIns.executeUpdate() > 0;
			}
		} catch (SQLException e) {}
		return false;
	}

	public boolean eliminarLogroUsuario(String usuario, String nombreLogro) {
		String sql = "DELETE HL FROM HACER_LOGRO HL JOIN LOGROS L ON HL.id_logro = L.id_logro WHERE HL.nombreUsuario = ? AND L.nombre = ?";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario); ps.setString(2, nombreLogro);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	// =========================================================================
	// 10. GRUPOS Y RANKINGS
	// =========================================================================

	/**
	 * Realiza la creación de un nuevo grupo y la vinculación automática del creador.
	 * Este método implementa una **transacción SQL** para asegurar la integridad referencial:
	 * 1. Inserta el registro en la tabla GRUPO y recupera el ID generado automáticamente.
	 * 2. Inserta la relación en la tabla PERTENECE_GRUPO usando dicho ID.
	 * Si cualquiera de los dos pasos falla, se ejecuta un **rollback** para evitar 
	 * que queden grupos "huérfanos" sin miembros o registros incompletos.
	 * * @param nombre Nombre único del clan.
	 * @param descripcion Texto descriptivo del propósito del grupo.
	 * @param esPrivado Define si el grupo requiere contraseña para unirse.
	 * @param contrasena Clave de acceso (null si el grupo es público).
	 * @param creador Nombre del usuario que realiza la acción.
	 * @return true si la transacción completa se confirmó (commit); false en caso de error.
	 */
	public boolean crearGrupo(String nombre, String descripcion, boolean esPrivado, String contrasena, String creador) {
		try {
			conexion.setAutoCommit(false);
			PreparedStatement ps1 = conexion.prepareStatement("INSERT INTO GRUPO (nombre, descripcion, es_privado, contrasena) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps1.setString(1, nombre); ps1.setString(2, descripcion); ps1.setBoolean(3, esPrivado); ps1.setString(4, esPrivado ? contrasena : null);
			ps1.executeUpdate();

			ResultSet rs = ps1.getGeneratedKeys();
			int idGrupo = -1;
			if (rs.next()) idGrupo = rs.getInt(1);
			ps1.close();

			if (idGrupo != -1) {
				PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO PERTENECE_GRUPO (nombreUsuario, id_grupo) VALUES (?, ?)");
				ps2.setString(1, creador); ps2.setInt(2, idGrupo);
				ps2.executeUpdate(); ps2.close();
				conexion.commit();
				return true;
			}
		} catch (SQLException e) {
			try { conexion.rollback(); } catch (SQLException ex) {}
		} finally {
			try { conexion.setAutoCommit(true); } catch (SQLException ex) {}
		}
		return false;
	}

	public String[] obtenerGruposDisponibles(String usr) {
		return obtenerDatosColumna("SELECT nombre FROM GRUPO WHERE id_grupo NOT IN (SELECT id_grupo FROM PERTENECE_GRUPO WHERE nombreUsuario = ?)", "nombre", usr);
	}

	public String unirseGrupo(String usr, String nombreGrupo, String passIntentada) {
		try {
			PreparedStatement ps = conexion.prepareStatement("SELECT id_grupo, es_privado, contrasena FROM GRUPO WHERE nombre = ?");
			ps.setString(1, nombreGrupo);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int idGrupo = rs.getInt("id_grupo");
				boolean privado = rs.getBoolean("es_privado");
				String passReal = rs.getString("contrasena");
				ps.close();

				if (privado && (passReal != null && !passReal.equals(passIntentada))) return "Contraseña incorrecta.";

				PreparedStatement psUnir = conexion.prepareStatement("INSERT INTO PERTENECE_GRUPO (nombreUsuario, id_grupo) VALUES (?, ?)");
				psUnir.setString(1, usr); psUnir.setInt(2, idGrupo);
				psUnir.executeUpdate(); psUnir.close();
				return "EXITO";
			}
		} catch (SQLException e) { return "Error de base de datos."; }
		return "El grupo no existe.";
	}

	public boolean abandonarGrupo(String usr, String nombreGrupo) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM PERTENECE_GRUPO WHERE nombreUsuario = ? AND id_grupo = (SELECT id_grupo FROM GRUPO WHERE nombre = ?)")) {
			ps.setString(1, usr); ps.setString(2, nombreGrupo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public int obtenerPuestoMundial(String usuario) {
		try (PreparedStatement ps = conexion.prepareStatement("SELECT COUNT(*) + 1 AS puesto FROM USERS WHERE puntos > (SELECT puntos FROM USERS WHERE USR = ?)")) {
			ps.setString(1, usuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("puesto");
		} catch (SQLException e) {}
		return 0;
	}

	public int obtenerPuestoSemanal(String usuario) {
		String sql = "SELECT COUNT(*) + 1 AS puesto FROM (SELECT nombreUsuario, SUM(A.puntos) as total FROM COMPLETAR_ACTIVIDAD CA JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad WHERE YEARWEEK(CA.fecha_realizacion, 1) = YEARWEEK(CURRENT_DATE(), 1) GROUP BY nombreUsuario) AS ranking_semanal WHERE total > (SELECT SUM(A2.puntos) FROM COMPLETAR_ACTIVIDAD CA2 JOIN ACTIVIDADES A2 ON CA2.id_actividad = A2.id_actividad WHERE CA2.nombreUsuario = ? AND YEARWEEK(CA2.fecha_realizacion, 1) = YEARWEEK(CURRENT_DATE(), 1))";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("puesto");
		} catch (SQLException e) { return 0; }
		return 1;
	}

	// =========================================================================
	// 11. GESTIÓN DE NOTICIAS
	// =========================================================================

	public ArrayList<Object[]> obtenerNoticias() {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement ps = conexion.prepareStatement("SELECT tipo, titulo FROM NOTICIAS"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) lista.add(new Object[] { "[" + rs.getString("tipo") + "] " + rs.getString("titulo") });
		} catch (SQLException e) {}
		return lista;
	}

	public ArrayList<Object[]> getNoticiasAdmin() {
		ArrayList<Object[]> lista = new ArrayList<>();
		try (PreparedStatement pstmt = conexion.prepareStatement("SELECT titulo, tipo FROM NOTICIAS"); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) lista.add(new Object[] { rs.getString("titulo"), rs.getString("tipo") });
		} catch (SQLException e) {}
		return lista;
	}

	public boolean añadirNoticia(String titulo, String tipo) {
		try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO NOTICIAS (titulo, tipo) VALUES (?, ?)")) {
			ps.setString(1, titulo); ps.setString(2, tipo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}

	public boolean eliminarNoticia(String titulo) {
		try (PreparedStatement ps = conexion.prepareStatement("DELETE FROM NOTICIAS WHERE titulo = ?")) {
			ps.setString(1, titulo);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) { return false; }
	}
}