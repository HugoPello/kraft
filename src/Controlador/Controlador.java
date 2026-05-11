package Controlador;

import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mysql.cj.jdbc.CallableStatement;

import Modelo.Modelo;
import Vista.Vista;
import Vista._00_Login;
import Vista._01_Registro;
import Vista._02_Inicio;
import Vista._04_DentroActividad;
import Vista._08_PerfilUsuario;
import Vista._09_OlvidoContraseña;
import Vista._10_CrearEventoAdmin;
import Vista._11_PanelAdmin;
import Vista._13_AjustesUsuario;
import Vista._14_CrearActividad;
import Vista._15_Configuracion;
import Vista._16_Grupos;
import Vista._17_CrearGrupo;

public class Controlador {

	private Modelo miModelo;
	private Vista[] misVistas;

	// =========================================================================
	// 1. CONFIGURACIÓN INICIAL
	// =========================================================================

	public void setVista(Vista[] misVistas) {
		this.misVistas = misVistas;
	}

	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	// =========================================================================
	// 2. NAVEGACIÓN Y CONTROL DE VENTANAS
	// =========================================================================

	public void cambiarVentana(int desde, int hasta) {
		if (hasta == 11 && !miModelo.esUsuarioAdmin()) {
			javax.swing.JOptionPane.showMessageDialog(null, "Acceso denegado: Se requieren permisos de administrador.",
					"Seguridad del Sistema", javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}
		((JFrame) misVistas[desde]).setVisible(false);
		((JFrame) misVistas[hasta]).setVisible(true);
	}

	public void abrirConfiguracionPopUp() {
		// Solo hacemos visible la de configuración, sin ocultar la anterior
		((Window) misVistas[16]).setVisible(true);
	}

	public void irARegistro() {
		// Intentamos conectar usando los datos actuales del .ini
		if (miModelo.conectarBD()) {
			// Si la conexión es exitosa, cambiamos de ventana
			this.cambiarVentana(0, 1);
		} else {
			// Si falla, avisamos en la propia pantalla de Login
			((_00_Login) misVistas[0]).mostrarError("Error: No se pudo conectar a la BD.");
		}
	}

	// =========================================================================
	// 3. GESTIÓN DE USUARIOS Y SESIÓN
	// =========================================================================

	public void login() {
		_00_Login vista = (_00_Login) misVistas[0];

		if (!miModelo.conectarBD()) {
			vista.mostrarError("Error: No se pudo conectar a la base de datos. Revisa la configuración.");
			return;
		}

		String usr = vista.getTxtUsu();
		String pwd = vista.getTxtPwd();

		miModelo.loginSeguro(usr, pwd);
		String resultado = miModelo.getResultado();

		// Protección anti NullPointer por si acaso
		if (resultado == null) {
			vista.mostrarError("Error crítico de comunicación con la Base de Datos.");
			return;
		}

		if (resultado.equals("Correcto")) {
			miModelo.setUsuarioLogueado(usr);
			vista.limpiarMensaje();
			if (miModelo.isAdmin()) {
				cambiarVentana(0, 11);
			} else {
				cambiarVentana(0, 2);
			}
		} else if (resultado.equals("Incorrecto")) {
			vista.mostrarError("Usuario o contraseña incorrectos");
		} else if (resultado.equals("ErrorBD")) {
			vista.mostrarError("La Base de datos rechazó la consulta. Revisa tu consola de Eclipse.");
		} else {
			System.exit(0);
		}
	}

	// @Author: Hugo
	public void registrarUsuario() {
	    _01_Registro vistaReg = (_01_Registro) misVistas[1];

	    // 1. Extraer todos los datos de la vista
	    String usr = vistaReg.getTxtNombre();
	    String emailDestino = vistaReg.getTxtEmail();
	    String pwd = vistaReg.getTxtPass();
	    String pwdRepetir = vistaReg.getPasswordFieldRepetir();
	    String fecha = vistaReg.getTxtFechaNac();
	    int idPregunta = vistaReg.getIndicePregunta();
	    String respuesta = vistaReg.getTxtRespuesta();
	    boolean esAdmin = vistaReg.isModoAdmin();
	    String adminPass = vistaReg.getPassAdmin();
	    
	    // Datos del Captcha
	    String captchaUsuario = vistaReg.getTxtCaptcha();
	    String captchaReal = vistaReg.getCaptchaGenerado();

	    // 2. Validaciones de campos obligatorios
	    if (usr.trim().isEmpty() || pwd.isEmpty() || respuesta.trim().isEmpty() || emailDestino.trim().isEmpty()) {
	        vistaReg.mostrarMensaje("Error: Nombre, Email, Contraseña y Respuesta son obligatorios.");
	        return;
	    }

	    // 3. Validación de Captcha (Añadido)
	    if (!captchaUsuario.equalsIgnoreCase(captchaReal)) {
	        vistaReg.mostrarMensaje("Error: El código Captcha no coincide.");
	        return;
	    }

	    // 4. Validación de coincidencia de contraseñas [Tu código original]
	    if (!pwd.equals(pwdRepetir)) {
	        vistaReg.mostrarMensaje("Error: Las contraseñas no coinciden.");
	        return;
	    }

	    // 5. Validación de formato de fecha [Tu código original]
	    if (!fecha.isEmpty()) {
	        try {
	            java.sql.Date.valueOf(fecha);
	        } catch (Exception e) {
	            vistaReg.mostrarMensaje("Error: El formato de la fecha debe ser YYYY-MM-DD.");
	            return;
	        }
	    }

	    // 6. Lógica de Rol y Contraseña de Administrador
	    boolean rolFinal = false;
	    if (esAdmin) {
	        if (!adminPass.equals("1234")) {
	            vistaReg.mostrarMensaje("Error: Contraseña de Administrador incorrecta.");
	            return;
	        }
	        rolFinal = true;
	    }

	    // 7. Comprobación de existencia previa
	    if (miModelo.existeUsuario(usr)) {
	        vistaReg.mostrarMensaje("Error: El nombre de usuario '" + usr + "' ya está en uso.");
	        return;
	    }

	    // 8. Llamada al modelo (7 parámetros: incluye el correo)
	    boolean insertadoOK = miModelo.registroCompleto(usr, pwd, rolFinal, fecha, idPregunta, respuesta, emailDestino);
	    
	    if (insertadoOK) {
	        // 9. Recuperar el OTP generado automáticamente por el Trigger en MySQL
	        String otpGenerado = miModelo.obtenerUltimoOTP(usr);
	        
	        if (otpGenerado != null) {
	            // 10. Envío del email real mediante el servicio de mensajería
	            Servicios.Email miCorreo = new Servicios.Email();
	            miCorreo.enviarCorreoOTP(emailDestino, otpGenerado);
	            
	            vistaReg.mostrarMensaje("¡Registro casi listo! Revisa el código enviado a " + emailDestino);
	            
	            // 11. Cambiar a la ventana de verificación (Ventana 18)
	            cambiarVentana(1, 18); 
	        } else {
	            vistaReg.mostrarMensaje("Usuario creado, pero hubo un error generando el código de seguridad.");
	        }
	    } else {
	        vistaReg.mostrarMensaje("Error Crítico: No se pudo guardar en la base de datos.");
	    }
	}

	public ArrayList<Object[]> getDatosUsuarios() {
		return miModelo.obtenerDatosUsuarios();
	}

	public int borrarUsuario(String usr, String pwd) {
		return miModelo.borrar(usr, pwd);
	}

	public boolean altaUsuario(String usr, String pwd) {
		return miModelo.insertarUsuarioAdmin(usr, pwd);
	}

	public int modificarUsuario(String usr, String pwd) {
		return miModelo.modificar(usr, pwd);
	}
	
	// =========================================================================
	// GESTIÓN DE FICHEROS (FOTO DE PERFIL)
	// =========================================================================

	public void subirFotoPerfil() {
		// 1. Abrir explorador de archivos de Windows
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
		chooser.setFileFilter(filter);

		int seleccion = chooser.showOpenDialog(null);

		if (seleccion == JFileChooser.APPROVE_OPTION) {
			File archivoOrigen = chooser.getSelectedFile();
			String usr = miModelo.getUsuarioLogueado();

			try {
				// 2. Crear carpeta "perfiles" en el proyecto si no existe
				File carpeta = new File("perfiles");
				if (!carpeta.exists()) {
					carpeta.mkdir();
				}

				// 3. Copiar la imagen y llamarla como el usuario (ej: perfiles/Ivan.jpg)
				File archivoDestino = new File("perfiles/" + usr + ".jpg");
				Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(),
						StandardCopyOption.REPLACE_EXISTING);

				JOptionPane.showMessageDialog(null, "¡Foto de perfil actualizada con éxito!");

			} catch (Exception e) {
				System.err.println("Error al guardar fichero: " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Error al guardar la imagen.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	

	// =========================================================================
	// 8. RECUPERACIÓN DE CONTRASEÑA Y PERFIL
	// =========================================================================

	public void restablecerContrasena() {
		_09_OlvidoContraseña vista = (_09_OlvidoContraseña) misVistas[9];

		String usr = vista.getUsuario();
		int idPregunta = vista.getIndicePregunta();
		String respuesta = vista.getRespuestaSeguridad();
		String nuevaPass = vista.getNuevaPass();
		String confirmPass = vista.getConfirmPass();

		if (usr.isEmpty() || respuesta.isEmpty() || nuevaPass.isEmpty()) {
			vista.mostrarMensaje("Por favor, rellena todos los campos.");
			return;
		}
		if (idPregunta == 0) {
			vista.mostrarMensaje("Por favor, selecciona una pregunta de seguridad.");
			return;
		}
		if (!nuevaPass.equals(confirmPass)) {
			vista.mostrarMensaje("Las contraseñas no coinciden.");
			return;
		}
		if (!miModelo.existeUsuario(usr)) {
			vista.mostrarMensaje("El usuario indicado no existe.");
			return;
		}

		// Comprobamos en el Modelo si la respuesta es correcta
		boolean esCorrecta = miModelo.verificarRespuestaSeguridad(usr, idPregunta, respuesta);

		if (esCorrecta) {
			int filasAfectadas = miModelo.modificar(usr, nuevaPass);
			if (filasAfectadas > 0) {
				vista.mostrarMensaje("Contraseña actualizada con éxito. Ya puedes iniciar sesión.");
				vista.limpiarCampos();
				cambiarVentana(9, 0); // Volver al login
			} else {
				vista.mostrarMensaje("Error al actualizar la contraseña en la base de datos.");
			}
		} else {
			vista.mostrarMensaje("La respuesta a la pregunta de seguridad es incorrecta.");
		}
	}

	public void alAbrirPerfil() {
		_08_PerfilUsuario vista = (_08_PerfilUsuario) misVistas[8];
		String usr = miModelo.getUsuarioLogueado();

		// 1. Extraemos las estadísticas de la BD
		int[] stats = miModelo.obtenerEstadisticasUsuario(usr);
		int puntos = stats[0];
		int totalActividades = stats[1];

		// 2. Calculamos lógica de negocio (Ej: Sube de nivel cada 500 puntos)
		int nivel = (puntos / 500) + 1;
		int monedas = puntos; // Asumimos que 1 punto = 1 moneda para la tienda

		// 3. Inyectamos los datos en el banner
		vista.setDatosBanner(usr.toUpperCase(), nivel, monedas, totalActividades);

		// 4. Inyectamos las tablas
		Object[][] datosLogros = getTablaLogrosPerfil();
		Object[][] datosInv = getTablaInventarioPerfil();
		vista.setTablasPerfil(datosLogros, datosInv);
		
		File fotoFile = new File("perfiles/" + usr + ".jpg");
		if (fotoFile.exists()) {
			try {
				// Leemos la imagen y la escalamos para que encaje perfecta en el cuadro de 120x120
				BufferedImage imgOriginal = ImageIO.read(fotoFile);
				Image imgEscalada = imgOriginal.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
				vista.setImagenPerfil(new ImageIcon(imgEscalada));
			} catch (Exception e) {
				vista.setImagenPerfil(null);
			}
		} else {
			// Si el usuario no ha subido foto nunca, lo dejamos vacío
			vista.setImagenPerfil(null);
		}
	}

	// =========================================================================
	// 4. NOTICIAS Y MISIONES
	// =========================================================================

	public ArrayList<Object[]> getNoticias() {
		return miModelo.obtenerNoticias();
	}

	public ArrayList<Object[]> getNoticiasAdmin() {
		return miModelo.getNoticiasAdmin();
	}

	public boolean añadirNoticia(String titulo, String tipo) {
		boolean exito = miModelo.añadirNoticia(titulo, tipo);
		if (exito)
			actualizarTodasLasTablas();
		return exito;
	}

	public boolean eliminarNoticia(String titulo) {
		boolean exito = miModelo.eliminarNoticia(titulo);
		if (exito)
			actualizarTodasLasTablas();
		return exito;
	}

	private void actualizarTodasLasTablas() {
		if (misVistas[11] instanceof _11_PanelAdmin) {
			((_11_PanelAdmin) misVistas[11]).llenarTabla();
		}
		if (misVistas[2] instanceof _02_Inicio) {
			((_02_Inicio) misVistas[2]).cargarNoticias();
		}
	}

	public ArrayList<Object[]> getMisiones(String tipo) {
		return miModelo.obtenerMisiones(tipo);
	}

	// =========================================================================
	// 5. EVENTOS, COSMÉTICOS Y LOGROS
	// =========================================================================

	public ArrayList<Object[]> getEventos() {
		return miModelo.obtenerEventos();
	}

	public boolean añadirEvento(String nombre, String tipo, int puntos, String fecha) {
		boolean exito = miModelo.insertarEvento(nombre, tipo, puntos, fecha);
		if (exito) {
			((_10_CrearEventoAdmin) misVistas[10]).cargarTablaEventos();
		}
		return exito;
	}

	public boolean eliminarEvento(String nombre) {
		boolean exito = miModelo.eliminarEvento(nombre);
		if (exito) {
			((_10_CrearEventoAdmin) misVistas[10]).cargarTablaEventos();
		}
		return exito;
	}

	public String[] getPreguntasSeguridad() {
		String sql = "SELECT enunciado FROM PREGUNTA_SEGURIDAD ORDER BY id_pregunta ASC";
		return miModelo.obtenerDatosColumna(sql, "enunciado");
	}

	public String[] getCatalogoCosmeticos() {
		String sql = "SELECT nombre FROM COSMETICOS";
		return miModelo.obtenerDatosColumna(sql, "nombre");
	}

	public Object[][] getTablaLogrosPerfil() {
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT L.nombre, DATE_FORMAT(HL.fecha_desbloqueo, '%d/%m/%Y') " + "FROM LOGROS L "
				+ "JOIN HACER_LOGRO HL ON L.id_logro = HL.id_logro " + "WHERE HL.nombreUsuario = ?";
		return miModelo.obtenerDatosTabla(sql, 2, usr);
	}

	public Object[][] getTablaInventarioPerfil() {
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT C.nombre, C.tipo_personalizacion " + "FROM COSMETICOS C "
				+ "JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico " + "WHERE TC.nombreUsuario = ?";
		return miModelo.obtenerDatosTabla(sql, 2, usr);
	}

	// =========================================================================
	// 6. RANKINGS Y GRUPOS
	// =========================================================================

	public Object[][] getRankingMundial() {
		String sql = "SELECT USR, puntos FROM USERS ORDER BY puntos DESC LIMIT 10";
		return miModelo.obtenerDatosTabla(sql, 2);
	}

	public Object[][] getRankingSemanal() {
		String sql = "SELECT CA.nombreUsuario, SUM(A.puntos) as puntos_semanales " + "FROM COMPLETAR_ACTIVIDAD CA "
				+ "JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad "
				+ "WHERE YEARWEEK(CA.fecha_realizacion, 1) = YEARWEEK(CURRENT_DATE(), 1) "
				+ "GROUP BY CA.nombreUsuario " + "ORDER BY puntos_semanales DESC " + "LIMIT 10";
		return miModelo.obtenerDatosTabla(sql, 2);
	}

	public String[] getMisGrupos() {
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT G.nombre FROM GRUPO G " + "JOIN PERTENECE_GRUPO PG ON G.id_grupo = PG.id_grupo "
				+ "WHERE PG.nombreUsuario = ?";
		return miModelo.obtenerDatosColumna(sql, "nombre", usr);
	}

	public Object[][] getRankingGrupalEspecifico(String nombreGrupo) {
		String sql = "SELECT U.USR, U.puntos " + "FROM USERS U "
				+ "JOIN PERTENECE_GRUPO PG ON U.USR = PG.nombreUsuario " + "JOIN GRUPO G ON PG.id_grupo = G.id_grupo "
				+ "WHERE G.nombre = ? " + "ORDER BY U.puntos DESC LIMIT 10";
		return miModelo.obtenerDatosTabla(sql, 2, nombreGrupo);
	}

	public void guardarNuevoGrupo() {
		_17_CrearGrupo vista = (_17_CrearGrupo) misVistas[17];
		String nombre = vista.getNombre();
		String desc = vista.getDescripcion();
		boolean privado = vista.isPrivado();
		String contrasena = vista.getContrasena();
		String creador = miModelo.getUsuarioLogueado();

		if (nombre.trim().isEmpty()) {
			vista.mostrarMensaje("El nombre del grupo es obligatorio.");
			return;
		}
		if (privado && contrasena.trim().isEmpty()) {
			vista.mostrarMensaje("Si el grupo es privado, debes introducir una contraseña.");
			return;
		}

		boolean exito = miModelo.crearGrupo(nombre, desc, privado, contrasena, creador);

		if (exito) {
			vista.mostrarMensaje("¡Grupo '" + nombre + "' fundado con éxito!");
			vista.limpiarFormulario();
			cambiarVentana(17, 12); // El índice 12 es _16_Grupos
			// Refrescamos la vista de grupos
			((_16_Grupos) misVistas[12]).cargarConfiguracionInicial();
		} else {
			vista.mostrarMensaje("Error: El nombre ya existe o hubo un fallo en la BD.");
		}
	}

	public void mostrarDialogoUnirse() {
		String usr = miModelo.getUsuarioLogueado();
		String[] gruposDisponibles = miModelo.obtenerGruposDisponibles(usr);

		if (gruposDisponibles.length == 0 || gruposDisponibles[0].equals("No hay datos")) {
			javax.swing.JOptionPane.showMessageDialog(null, "No hay grupos nuevos a los que unirse.");
			return;
		}

		String seleccion = (String) javax.swing.JOptionPane.showInputDialog(null,
				"Selecciona el clan al que quieres unirte:", "Unirse a un Grupo",
				javax.swing.JOptionPane.QUESTION_MESSAGE, null, gruposDisponibles, gruposDisponibles[0]);

		if (seleccion != null) {
			String pass = "";
			Object[][] datos = miModelo.obtenerDatosTabla("SELECT es_privado FROM GRUPO WHERE nombre = ?", 1,
					seleccion);
			boolean esPrivado = (Boolean) datos[0][0];

			if (esPrivado) {
				pass = javax.swing.JOptionPane.showInputDialog(null, "Este grupo es PRIVADO. Introduce la contraseña:");
				if (pass == null)
					return;
			}

			String resultado = miModelo.unirseGrupo(usr, seleccion, pass);
			if (resultado.equals("EXITO")) {
				javax.swing.JOptionPane.showMessageDialog(null, "¡Bienvenido a " + seleccion + "!");
				((_16_Grupos) misVistas[12]).cargarConfiguracionInicial(); // Refrescamos
			} else {
				javax.swing.JOptionPane.showMessageDialog(null, "Error: " + resultado, "Fallo",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void alSeleccionarGrupoEnSede(String nombreGrupo) {
		// 1. Localizamos la Vista (Índice 12 es _16_Grupos)
		_16_Grupos vista = (_16_Grupos) misVistas[12];

		// 2. Extraemos la Info General (Descripción y Puntos del Clan)
		String sqlInfo = "SELECT descripcion, SUM(U.puntos) FROM GRUPO G "
				+ "JOIN PERTENECE_GRUPO PG ON G.id_grupo = PG.id_grupo " + "JOIN USERS U ON PG.nombreUsuario = U.USR "
				+ "WHERE G.nombre = ? GROUP BY G.descripcion";
		Object[][] info = miModelo.obtenerDatosTabla(sqlInfo, 2, nombreGrupo);

		if (info.length > 0 && !info[0][0].toString().equals("Sin registros")) {
			// INYECTAMOS EN LA VISTA
			vista.setInfoGrupo(info[0][0].toString(), info[0][1].toString());
		}

		// 3. Extraemos a los Miembros
		Object[][] datosMiembros = getRankingGrupalEspecifico(nombreGrupo);
		vista.setTablaMiembros(datosMiembros); // INYECTAMOS

		// 4. Extraemos el Feed de Actividades
		String sqlFeed = "SELECT CA.nombreUsuario, A.nombre, DATE_FORMAT(CA.fecha_realizacion, '%d/%m %H:%i') "
				+ "FROM COMPLETAR_ACTIVIDAD CA " + "JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad "
				+ "JOIN PERTENECE_GRUPO PG ON CA.nombreUsuario = PG.nombreUsuario "
				+ "JOIN GRUPO G ON PG.id_grupo = G.id_grupo "
				+ "WHERE G.nombre = ? ORDER BY CA.fecha_realizacion DESC LIMIT 15";
		Object[][] datosFeed = miModelo.obtenerDatosTabla(sqlFeed, 3, nombreGrupo);
		vista.setTablaActividades(datosFeed); // INYECTAMOS
	}

	public void abandonarGrupoSeleccionado() {
		// 1. Localizamos la Vista
		_16_Grupos vista = (_16_Grupos) misVistas[12];
		String grupoActual = vista.getGrupoSeleccionado();

		// 2. Validamos que haya un grupo seleccionado
		if (grupoActual == null || grupoActual.equals("No hay datos") || grupoActual.trim().isEmpty()) {
			javax.swing.JOptionPane.showMessageDialog(null, "No tienes ningún grupo seleccionado actualmente.");
			return;
		}

		// 3. Confirmación de seguridad
		int confirmacion = javax.swing.JOptionPane.showConfirmDialog(null,
				"¿Estás completamente seguro de que deseas abandonar el clan '" + grupoActual + "'?",
				"Confirmar Salida", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);

		if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
			String usr = miModelo.getUsuarioLogueado();

			// 4. Mandamos la orden al Modelo
			boolean exito = miModelo.abandonarGrupo(usr, grupoActual);

			if (exito) {
				javax.swing.JOptionPane.showMessageDialog(null, "Has abandonado el clan exitosamente.");
				// 5. Refrescamos la vista para que el grupo desaparezca del desplegable
				vista.cargarConfiguracionInicial();
			} else {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Error en la base de datos al intentar salir del grupo.", "Error",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	// =========================================================================
	// 7. GESTIÓN DE ACTIVIDADES
	// =========================================================================

	public void abrirActividadEspecifica(String tipoDeporte) {
		miModelo.setTipoActividadSeleccionada(tipoDeporte);
		cambiarVentana(3, 4);
	}

	public Object[][] getTablaActividadesPorTipo() {
		String tipo = miModelo.getTipoActividadSeleccionada();
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT A.nombre, A.dificultad " + "FROM ACTIVIDADES A " + "WHERE A.tipo = ? "
				+ "AND A.id_actividad NOT IN (" + "    SELECT CA.id_actividad " + "    FROM COMPLETAR_ACTIVIDAD CA "
				+ "    WHERE CA.nombreUsuario = ?" + ")";
		return miModelo.obtenerDatosTabla(sql, 2, tipo, usr);
	}

	public Object[][] getTablaHistorialUsuario() {
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT A.nombre, DATE_FORMAT(CA.fecha_realizacion, '%d/%m/%Y %H:%i') " + "FROM ACTIVIDADES A "
				+ "JOIN COMPLETAR_ACTIVIDAD CA ON A.id_actividad = CA.id_actividad "
				+ "WHERE CA.nombreUsuario = ? ORDER BY CA.fecha_realizacion DESC";
		return miModelo.obtenerDatosTabla(sql, 2, usr);
	}

	public void guardarNuevaActividad() {
		_14_CrearActividad vistaCrear = (_14_CrearActividad) misVistas[15];
		String nombre = vistaCrear.getNombreActividad();
		String tipo = vistaCrear.getTipoActividad();
		String dif = vistaCrear.getDificultadActividad();
		int pts = vistaCrear.getPuntosActividad();
		String frec = vistaCrear.getFrecuenciaActividad();
		String unidad = vistaCrear.getUnidadActividad();

		if (nombre == null || nombre.trim().isEmpty()) {
			vistaCrear.mostrarMensaje("Error: El nombre de la actividad no puede estar vacío.");
			return;
		}

		boolean exito = miModelo.crearActividad(nombre, tipo, dif, pts, frec, unidad);
		if (exito) {
			vistaCrear.mostrarMensaje("¡Actividad '" + nombre + "' creada correctamente!");
			vistaCrear.limpiarFormulario();
			cambiarVentana(14, 3);
		} else {
			vistaCrear.mostrarMensaje("Error en la Base de Datos. No se pudo crear la actividad.");
		}
	}

	public void completarActividad() {
		_04_DentroActividad vista4 = (_04_DentroActividad) misVistas[4];
		String actividadElegida = vista4.getActividadSeleccionada();

		if (actividadElegida == null) {
			vista4.mostrarMensaje("Por favor, selecciona una actividad de la tabla derecha primero.");
			return;
		}

		String usr = miModelo.getUsuarioLogueado();
		boolean exito = miModelo.registrarActividadCompletada(usr, actividadElegida);

		if (exito) {
			vista4.mostrarMensaje("¡Enhorabuena! Has completado: " + actividadElegida);
			vista4.cargarDatosTablas();
		} else {
			vista4.mostrarMensaje("Hubo un error al registrar la actividad.");
		}
	}

	public void cargarConfiguracionAjustes() {
		_15_Configuracion vista = (_15_Configuracion) misVistas[16];
		vista.setDatosConfig(miModelo.getLoginDB(), miModelo.getPwdDB(), miModelo.getUrlDB());
	}

	public void guardarConfiguracionAjustes() {
		_15_Configuracion vista = (_15_Configuracion) misVistas[16];
		miModelo.guardarConfig(vista.getUsrConfig(), vista.getPwdConfig(), vista.getUrlConfig());
		vista.mostrarMensaje("Configuración guardada. La conexión se ha reiniciado.");
		((javax.swing.JFrame) misVistas[16]).dispose();
	}

	public void guardarAjustes() {
		_13_AjustesUsuario vista = (_13_AjustesUsuario) misVistas[14];
		
		// 1. Recogemos los datos
		String usrActual = miModelo.getUsuarioLogueado();
		String nuevoUsr = vista.getNuevoNombre();
		String nuevoCorreo = vista.getNuevoEmail();
		String nuevaPwd = vista.getNuevaPass();

		// 2. Validaciones básicas
		if (nuevoUsr.trim().isEmpty() || nuevoCorreo.trim().isEmpty()) {
			vista.mostrarMensaje("El nombre de usuario y el correo no pueden estar vacíos.");
			return;
		}

		
		if (!usrActual.equals(nuevoUsr)) {
			File fotoVieja = new File("perfiles/" + usrActual + ".jpg");
			if (fotoVieja.exists()) {
				File fotoNueva = new File("perfiles/" + nuevoUsr + ".jpg");
				
				boolean renombrado = fotoVieja.renameTo(fotoNueva);
				if (!renombrado) {
					System.err.println("No se pudo renombrar la foto de perfil.");
				}
			}
		}
		boolean exito = miModelo.actualizarAjustes(usrActual, nuevoUsr, nuevoCorreo, nuevaPwd);
		
		if (exito) {
		
			miModelo.setUsuarioLogueado(nuevoUsr); 
			
			vista.mostrarMensaje("Perfil actualizado correctamente.");
			vista.setDatosActuales(nuevoUsr, nuevoCorreo);
			cambiarVentana(14, 8); // Volvemos al perfil
		} else {
			vista.mostrarMensaje("Error al actualizar. Es posible que el nombre de usuario ya esté en uso.");
		}
	}

	public void alAbrirAjustes() {
		_13_AjustesUsuario vista = (_13_AjustesUsuario) misVistas[14];
		String nombre = miModelo.getUsuarioLogueado();
		String email = miModelo.sacarEmail();
		vista.setDatosActuales(nombre, email);
	}
	
	public void validarCodigoOTP(String codigoIntroducido) {
	    // Obtenemos el usuario que se acaba de registrar (puedes guardarlo en una variable global al registrar)
	    String usr = miModelo.getUltimoUsuarioRegistrado(); 

	    try (CallableStatement cst = (CallableStatement) miModelo.getConexion().prepareCall("{call validate_otp(?, ?)}")) {
	        cst.setString(1, usr);
	        cst.setString(2, codigoIntroducido);
	        cst.execute(); // Ejecuta el procedimiento que cambia is_verified a 1

	        JOptionPane.showMessageDialog(null, "¡Cuenta activada! Ya puedes iniciar sesión.");
	        cambiarVentana(18, 0); // Volver al login tras el éxito

	    } catch (SQLException e) {
	        // Aquí capturamos el SIGNAL SQLSTATE '45000' que configuramos en MySQL
	        JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Verificación", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
}