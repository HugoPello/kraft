package Controlador;

import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Modelo.Modelo;
import Vista.Vista;
import Vista._00_Login;
import Vista._01_Registro;
import Vista._02_Inicio;
import Vista._04_DentroActividad;
import Vista._05_Logros;
import Vista._07_Tienda;
import Vista._08_PerfilUsuario;
import Vista._09_OlvidoContraseña;
import Vista._10_CrearEventoAdmin;
import Vista._11_PanelAdmin;
import Vista._13_AjustesUsuario;
import Vista._14_CrearActividad;
import Vista._15_Configuracion;
import Vista._16_Grupos;
import Vista._17_CrearGrupo;

/**
 * Clase controladora que actúa como intermediaria entre la Vista y el Modelo.
 * * @author Hugo / Carlos / Diego
 * 
 * @version 2.0 (Código Refactorizado y Ordenado)
 */
public class Controlador {

	private Modelo miModelo;
	private Vista[] misVistas;

	// =========================================================================
	// 1. CONFIGURACIÓN Y NAVEGACIÓN
	// =========================================================================

	public void setVista(Vista[] misVistas) {
		this.misVistas = misVistas;
	}

	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	public void cambiarVentana(int desde, int hasta) {
		if (hasta == 11 && !miModelo.esUsuarioAdmin()) {
			JOptionPane.showMessageDialog(null, "Acceso denegado: Se requieren permisos de administrador.", "Seguridad",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Refresco automático de vistas conflictivas
		if (hasta == 5 && misVistas[5] instanceof _05_Logros) {
			((_05_Logros) misVistas[5]).cargarDatosLogros();
		}

		((JFrame) misVistas[desde]).setVisible(false);
		((JFrame) misVistas[hasta]).setVisible(true);
	}

	public void abrirConfiguracionPopUp() {
		((Window) misVistas[16]).setVisible(true);
	}

	public void cargarConfiguracionAjustes() {
		_15_Configuracion vista = (_15_Configuracion) misVistas[16];
		vista.setDatosConfig(miModelo.getLoginDB(), miModelo.getPwdDB(), miModelo.getUrlDB());
	}

	public void guardarConfiguracionAjustes() {
		_15_Configuracion vista = (_15_Configuracion) misVistas[16];
		miModelo.guardarConfig(vista.getUsrConfig(), vista.getPwdConfig(), vista.getUrlConfig());
		vista.mostrarMensaje("Configuración guardada. La conexión se ha reiniciado.");
		((JFrame) misVistas[16]).dispose();
	}

	// =========================================================================
	// 2. AUTENTICACIÓN Y SEGURIDAD
	// =========================================================================

	public void irARegistro() {
		if (miModelo.conectarBD())
			cambiarVentana(0, 1);
		else
			((_00_Login) misVistas[0]).mostrarError("Error: No se pudo conectar a la BD.");
	}

	public void login() {

		_00_Login vista = (_00_Login) misVistas[0];
		if (!miModelo.conectarBD()) {
			vista.mostrarError("Error de conexión BD.");
			return;
		}

		String resultado = miModelo.loginSeguro(vista.getTxtUsu(), vista.getTxtPwd());
		switch (resultado) {
		case "OTP_ENVIADO":
			new Servicios.Email().enviarCorreoOTP(miModelo.sacarEmail(), miModelo.obtenerUltimoOTP(vista.getTxtUsu()));
			cambiarVentana(0, 18);
			break;
		case "LOGIN_DIRECTO":
			cambiarVentana(0, miModelo.isAdmin() ? 11 : 2);
			break;
		case "INCORRECTO":
			vista.mostrarError("Usuario o contraseña incorrectos");
			break;
		case "CERRAR":
			JOptionPane.showMessageDialog(null, "Demasiados intentos fallidos.", "Bloqueo", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			break;
		}
	}

	public void validarCodigoOTP(String codigoIntroducido) {
		try (java.sql.CallableStatement cst = (java.sql.CallableStatement) miModelo.getConexion()
				.prepareCall("{call validate_otp(?, ?)}")) {
			cst.setString(1, miModelo.getUltimoUsuarioRegistrado());
			cst.setString(2, codigoIntroducido);
			cst.execute();
			miModelo.setUsuarioLogueado(miModelo.getUltimoUsuarioRegistrado());
			cambiarVentana(18, miModelo.isAdmin() ? 11 : 2);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error 2FA", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void registrarUsuario() {
		_01_Registro vista = (_01_Registro) misVistas[1];
		String usr = vista.getTxtNombre();
		String email = vista.getTxtEmail();
		String pwd = vista.getTxtPass();
		String pwdRepetir = vista.getPasswordFieldRepetir();
		String fecha = vista.getTxtFechaNac();
		String respuesta = vista.getTxtRespuesta();

		if (usr.trim().isEmpty() || pwd.isEmpty() || respuesta.trim().isEmpty() || email.trim().isEmpty()) {
			vista.mostrarMensaje("Faltan campos.");
			return;
		}
		if (!vista.getTxtCaptcha().equalsIgnoreCase(vista.getCaptchaGenerado())) {
			vista.mostrarMensaje("Captcha incorrecto.");
			return;
		}
		if (!pwd.equals(pwdRepetir)) {
			vista.mostrarMensaje("Contraseñas no coinciden.");
			return;
		}

		boolean esAdmin = vista.isModoAdmin() && vista.getPassAdmin().equals("1234");
		if (vista.isModoAdmin() && !esAdmin) {
			vista.mostrarMensaje("Clave Admin incorrecta.");
			return;
		}
		if (miModelo.existeUsuario(usr)) {
			vista.mostrarMensaje("Usuario en uso.");
			return;
		}

		if (miModelo.registroCompleto(usr, pwd, esAdmin, fecha, vista.getIndicePregunta(), respuesta, email)) {
			new Servicios.Email().enviarCorreoOTP(email, miModelo.obtenerUltimoOTP(usr));
			vista.mostrarMensaje("Registro casi listo. Revisa tu email.");
			cambiarVentana(1, 18);
		} else {
			vista.mostrarMensaje("Error en base de datos.");
		}
	}

	public void restablecerContrasena() {
		_09_OlvidoContraseña vista = (_09_OlvidoContraseña) misVistas[9];
		String usr = vista.getUsuario();
		String respuesta = vista.getRespuestaSeguridad();
		String nueva = vista.getNuevaPass();

		if (usr.isEmpty() || respuesta.isEmpty() || nueva.isEmpty()) {
			vista.mostrarMensaje("Faltan campos.");
			return;
		}
		if (!nueva.equals(vista.getConfirmPass())) {
			vista.mostrarMensaje("Las claves no coinciden.");
			return;
		}
		if (!miModelo.verificarRespuestaSoloPorUsuario(usr, respuesta)) {
			vista.mostrarMensaje("Respuesta incorrecta.");
			return;
		}

		if (miModelo.modificar(usr, nueva) > 0) {
			vista.mostrarMensaje("Contraseña actualizada.");
			vista.limpiarCampos();
			cambiarVentana(9, 0);
		}
	}

	// =========================================================================
	// 3. PERFIL Y AJUSTES DE USUARIO
	// =========================================================================

	public void alAbrirPerfil() {
	    _08_PerfilUsuario vista = (_08_PerfilUsuario) misVistas[8];
	    String usr = miModelo.getUsuarioLogueado();
	    
	    // 1. Estadísticas Base
	    int[] stats = miModelo.obtenerEstadisticasUsuario(usr);
	    vista.setDatosBanner(usr.toUpperCase(), (stats[0] / 500) + 1, stats[2], stats[1]);

	    // 2. ¡NUEVO! Progresión (Logros y Cosméticos X / Y)
	    int[] prog = miModelo.obtenerProgresoGlobal(usr);
	    vista.setProgresion(prog[0], prog[1], prog[2], prog[3]);

	    // 3. Tablas de Inventario y Logros
	    vista.setTablasPerfil(getTablaLogrosPerfil(), miModelo.obtenerDatosTabla(
	            "SELECT C.nombre, C.tipo_personalizacion FROM COSMETICOS C JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico WHERE TC.nombreUsuario = ?",
	            2, usr));

	    // 4. LÓGICA DE FOTO DE PERFIL (Evitamos SCALE_SMOOTH para que no se congele)
	    javax.swing.ImageIcon fotoFinal = null;
	    File fotoLocal = new File("perfiles/" + usr + ".jpg");
	    
	    try {
	        if (fotoLocal.exists()) {
	            fotoFinal = new javax.swing.ImageIcon(javax.imageio.ImageIO.read(fotoLocal).getScaledInstance(120, 120, java.awt.Image.SCALE_FAST));
	        }
	    } catch (Exception e) {}

	    // Avatar de la BD sobreescribe al local
	    byte[] avatarBytes = miModelo.obtenerImagenCosmetico("Avatar", usr);
	    if (avatarBytes != null && avatarBytes.length > 0) {
	        fotoFinal = new javax.swing.ImageIcon(new javax.swing.ImageIcon(avatarBytes).getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_FAST));
	    }
	    vista.setImagenPerfil(fotoFinal);

	    // 5. BANNER Y TEXTO EQUIPADO
	    byte[] bannerBytes = miModelo.obtenerImagenCosmetico("Banner", usr);
	    vista.aplicarBannerCosmetico(bannerBytes != null ? new javax.swing.ImageIcon(bannerBytes) : null);
	    
	    String sqlEquipado = "SELECT C.nombre FROM COSMETICOS C JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico WHERE TC.nombreUsuario = ? AND TC.equipado = true";
	    Object[][] itemsEquipados = miModelo.obtenerDatosTabla(sqlEquipado, 1, usr);
	    
	    if (itemsEquipados != null && itemsEquipados.length > 0 && !itemsEquipados[0][0].toString().equals("Sin registros")) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < itemsEquipados.length; i++) {
	            sb.append(itemsEquipados[i][0].toString());
	            if (i < itemsEquipados.length - 1) sb.append(", ");
	        }
	        vista.setTextoEquipado(sb.toString() + ".");
	    } else {
	        vista.setTextoEquipado("Nada en este momento.");
	    }
	}

	public void desequiparDesdePerfil() {
	    _08_PerfilUsuario vista = (_08_PerfilUsuario) misVistas[8];
	    String item = vista.getCosmeticoSeleccionado();
	    if (item == null) { JOptionPane.showMessageDialog(null, "Selecciona ítem."); return; }
	    
	    if (miModelo.desequiparCosmetico(miModelo.getUsuarioLogueado(), item)) {
	        JOptionPane.showMessageDialog(null, "¡Cosmético desequipado!");
	        alAbrirPerfil(); // Refresco instantáneo
	    }
	}

	public void alAbrirAjustes() {
		((_13_AjustesUsuario) misVistas[14]).setDatosActuales(miModelo.getUsuarioLogueado(), miModelo.sacarEmail());
	}

	public void guardarAjustes() {
		_13_AjustesUsuario vista = (_13_AjustesUsuario) misVistas[14];
		String usrAct = miModelo.getUsuarioLogueado();
		String nuevo = vista.getNuevoNombre();

		if (nuevo.trim().isEmpty()) {
			vista.mostrarMensaje("Nombre vacío.");
			return;
		}
		if (!usrAct.equals(nuevo))
			new File("perfiles/" + usrAct + ".jpg").renameTo(new File("perfiles/" + nuevo + ".jpg"));

		if (miModelo.actualizarAjustes(usrAct, nuevo, vista.getNuevoEmail(), vista.getNuevaPass())) {
			miModelo.setUsuarioLogueado(nuevo);
			vista.mostrarMensaje("Perfil actualizado.");
			cambiarVentana(14, 8);
		} else
			vista.mostrarMensaje("Error al actualizar.");
	}

	public void subirFotoPerfil() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				File dir = new File("perfiles");
				if (!dir.exists())
					dir.mkdir();
				Files.copy(chooser.getSelectedFile().toPath(),
						new File("perfiles/" + miModelo.getUsuarioLogueado() + ".jpg").toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				JOptionPane.showMessageDialog(null, "¡Foto actualizada!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al guardar foto.");
			}
		}
	}

	public Object[][] getTablaInventarioPerfil() {
		String usr = miModelo.getUsuarioLogueado();
		String sql = "SELECT C.nombre, C.tipo_personalizacion " + "FROM COSMETICOS C "
				+ "JOIN TIENE_COSMETICO TC ON C.id_cosmetico = TC.id_cosmetico " + "WHERE TC.nombreUsuario = ?";
		return miModelo.obtenerDatosTabla(sql, 2, usr);
	}

	// =========================================================================
	// 4. ACTIVIDADES
	// =========================================================================

	public void abrirActividadEspecifica(String tipoDeporte) {
		miModelo.setTipoActividadSeleccionada(tipoDeporte);
		cambiarVentana(3, 4);
	}

	public Object[][] getTablaActividadesPorTipo() {
		String sql = "SELECT A.nombre, A.dificultad, CONCAT(IFNULL((SELECT SUM(cantidad) FROM COMPLETAR_ACTIVIDAD WHERE id_actividad = A.id_actividad AND nombreUsuario = ?), 0), ' / ', A.meta_cantidad, ' ', A.unidad_medida), CONCAT(A.puntos, ' 💰') FROM ACTIVIDADES A WHERE A.tipo = ? AND IFNULL((SELECT SUM(cantidad) FROM COMPLETAR_ACTIVIDAD WHERE id_actividad = A.id_actividad AND nombreUsuario = ?), 0) < A.meta_cantidad";
		return miModelo.obtenerDatosTabla(sql, 4, miModelo.getUsuarioLogueado(),
				miModelo.getTipoActividadSeleccionada(), miModelo.getUsuarioLogueado());
	}

	public Object[][] getTablaHistorialUsuario() {
		String sql = "SELECT A.nombre, DATE_FORMAT(MAX(CA.fecha_realizacion), '%d/%m/%Y %H:%i') FROM ACTIVIDADES A JOIN COMPLETAR_ACTIVIDAD CA ON A.id_actividad = CA.id_actividad WHERE CA.nombreUsuario = ? GROUP BY A.id_actividad, A.nombre, A.meta_cantidad HAVING SUM(CA.cantidad) >= A.meta_cantidad ORDER BY MAX(CA.fecha_realizacion) DESC";
		return miModelo.obtenerDatosTabla(sql, 2, miModelo.getUsuarioLogueado());
	}

	public void guardarNuevaActividad() {
		_14_CrearActividad vista = (_14_CrearActividad) misVistas[15];
		if (vista.getNombreActividad().isEmpty()) {
			vista.mostrarMensaje("Falta nombre.");
			return;
		}

		if (miModelo.crearActividad(vista.getNombreActividad(), vista.getTipoActividad(),
				vista.getDificultadActividad(), vista.getPuntosActividad(), vista.getFrecuenciaActividad(),
				vista.getUnidadActividad(), vista.getCantidadMeta(), vista.getCategoriaActividad())) {
			vista.mostrarMensaje("Actividad creada.");
			vista.limpiarFormulario();
			cambiarVentana(14, 3);
		} else
			vista.mostrarMensaje("Error BD.");
	}

	public void completarActividad() {
		_04_DentroActividad vista = (_04_DentroActividad) misVistas[4];
		String act = vista.getActividadSeleccionada();
		if (act == null) {
			vista.mostrarMensaje("Selecciona actividad.");
			return;
		}

		String input = JOptionPane.showInputDialog("Cantidad realizada:");
		if (input != null && !input.isEmpty()) {
			try {
				double cant = Double.parseDouble(input.replace(",", "."));
				if (miModelo.registrarActividadCompletada(miModelo.getUsuarioLogueado(), act, cant)) {
					miModelo.actualizarProgresoMisiones(miModelo.getUsuarioLogueado(), act, cant);
					vista.mostrarMensaje("¡Registrado!");
					vista.cargarDatosTablas();
				} else
					vista.mostrarMensaje("Error BD.");
			} catch (Exception e) {
				vista.mostrarMensaje("Número inválido.");
			}
		}
	}

	// =========================================================================
	// 5. TIENDA Y COSMÉTICOS
	// =========================================================================

	public Object[][] getTablaCatalogoTienda() {
		return miModelo.obtenerCatalogoTienda(miModelo.getUsuarioLogueado());
	}

	public String[] getCatalogoCosmeticos() {
		return miModelo.obtenerDatosColumna("SELECT nombre FROM COSMETICOS", "nombre");
	}

	public void comprarArticulo(String nombreItem, int precio) {
		if (miModelo.comprarCosmetico(miModelo.getUsuarioLogueado(), nombreItem, precio)) {
			JOptionPane.showMessageDialog(null, "¡Compra de " + nombreItem + " exitosa!");
			if (misVistas[7] instanceof _07_Tienda)
				((_07_Tienda) misVistas[7]).cargarDatosTienda();
		} else
			JOptionPane.showMessageDialog(null, "Error o saldo insuficiente.");
	}

	public void equiparDesdePerfil() {
		_08_PerfilUsuario vista = (_08_PerfilUsuario) misVistas[8];
		String item = vista.getCosmeticoSeleccionado();
		if (item == null) {
			JOptionPane.showMessageDialog(null, "Selecciona ítem.");
			return;
		}
		if (miModelo.equiparCosmetico(miModelo.getUsuarioLogueado(), item)) {
			JOptionPane.showMessageDialog(null, "¡Equipado!");
			alAbrirPerfil();
		}
	}

	// =========================================================================
	// 6. LOGROS
	// =========================================================================

	public Object[][] getTablaLogrosDisponibles() {
		return miModelo.obtenerLogrosPendientes(miModelo.getUsuarioLogueado());
	}

	public Object[][] getTablaLogrosPerfil() {
		return miModelo.obtenerDatosTabla(
				"SELECT L.nombre, DATE_FORMAT(HL.fecha_desbloqueo, '%d/%m/%Y') FROM LOGROS L JOIN HACER_LOGRO HL ON L.id_logro = HL.id_logro WHERE HL.nombreUsuario = ? AND HL.esta_hecho = 1",
				2, miModelo.getUsuarioLogueado());
	}

	public ArrayList<String> getTodosLosLogros() {
		return miModelo.obtenerTodosLosLogros();
	}

	public ArrayList<String> getLogrosUsuario(String usuario) {
		return miModelo.obtenerLogrosUsuario(usuario);
	}

	public boolean darLogro(String usuario, String logro) {
		return miModelo.asignarLogroAUsuario(usuario, logro);
	}

	public boolean quitarLogro(String usuario, String logro) {
		return miModelo.eliminarLogroUsuario(usuario, logro);
	}

	public void marcarLogroCompletado(String nombre) {
		if (miModelo.completarLogroEnBD(miModelo.getUsuarioLogueado(), nombre))
			((_05_Logros) misVistas[5]).cargarDatosLogros();
	}

	// =========================================================================
	// 7. MISIONES, EVENTOS Y NOTICIAS
	// =========================================================================

	public void gestionarMisionesDiarias() {
		if (!miModelo.tieneMisionesHoy(miModelo.getUsuarioLogueado()))
			miModelo.generarMisionesDiarias(miModelo.getUsuarioLogueado());
		if (misVistas[2] instanceof _02_Inicio)
			((_02_Inicio) misVistas[2]).cargarMisiones();
	}

	public ArrayList<Object[]> getMisiones(String tipo) {
		return miModelo.obtenerMisiones(tipo);
	}

	public ArrayList<Object[]> getMisionesAdmin() {
		return miModelo.consultarMisionesAdmin();
	}

	public boolean añadirMision(String desc, String tipo, int rec) {
		boolean ex = miModelo.insertarMision(desc, tipo, rec);
		if (ex)
			actualizarTodasLasTablas();
		return ex;
	}

	public boolean eliminarMision(int id) {
		boolean ex = miModelo.eliminarMision(id);
		if (ex)
			actualizarTodasLasTablas();
		return ex;
	}

	public ArrayList<Object[]> getEventos() {
		return miModelo.obtenerEventos();
	}

	public boolean añadirEvento(String nom, String tipo, int pts, String f) {
		boolean ex = miModelo.insertarEvento(nom, tipo, pts, f);
		if (ex)
			((_10_CrearEventoAdmin) misVistas[10]).cargarTablaEventos();
		return ex;
	}

	public boolean eliminarEvento(String nom) {
		boolean ex = miModelo.eliminarEvento(nom);
		if (ex)
			((_10_CrearEventoAdmin) misVistas[10]).cargarTablaEventos();
		return ex;
	}

	public ArrayList<Object[]> getNoticias() {
		return miModelo.obtenerNoticias();
	}

	public ArrayList<Object[]> getNoticiasAdmin() {
		return miModelo.getNoticiasAdmin();
	}

	public boolean añadirNoticia(String titulo, String tipo) {
		boolean ex = miModelo.añadirNoticia(titulo, tipo);
		if (ex)
			actualizarTodasLasTablas();
		return ex;
	}

	public boolean eliminarNoticia(String titulo) {
		boolean ex = miModelo.eliminarNoticia(titulo);
		if (ex)
			actualizarTodasLasTablas();
		return ex;
	}

	private void actualizarTodasLasTablas() {
		if (misVistas[11] instanceof _11_PanelAdmin)
			((_11_PanelAdmin) misVistas[11]).llenarTabla();
		if (misVistas[2] instanceof _02_Inicio)
			((_02_Inicio) misVistas[2]).cargarNoticias();
	}

	// =========================================================================
	// 8. GRUPOS Y SOCIAL
	// =========================================================================

	public String[] getMisGrupos() {
		return miModelo.obtenerDatosColumna(
				"SELECT G.nombre FROM GRUPO G JOIN PERTENECE_GRUPO PG ON G.id_grupo = PG.id_grupo WHERE PG.nombreUsuario = ?",
				"nombre", miModelo.getUsuarioLogueado());
	}

	public Object[][] getRankingGrupalEspecifico(String ng) {
		return miModelo.obtenerDatosTabla(
				"SELECT U.USR, U.puntos FROM USERS U JOIN PERTENECE_GRUPO PG ON U.USR = PG.nombreUsuario JOIN GRUPO G ON PG.id_grupo = G.id_grupo WHERE G.nombre = ? ORDER BY U.puntos DESC LIMIT 10",
				2, ng);
	}

	public Object[][] getRankingMundial() {
		return miModelo.obtenerDatosTabla("SELECT USR, puntos FROM USERS ORDER BY puntos DESC LIMIT 10", 2);
	}

	public Object[][] getRankingSemanal() {
		return miModelo.obtenerDatosTabla(
				"SELECT CA.nombreUsuario, SUM(A.puntos) FROM COMPLETAR_ACTIVIDAD CA JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad WHERE YEARWEEK(CA.fecha_realizacion, 1) = YEARWEEK(CURRENT_DATE(), 1) GROUP BY CA.nombreUsuario ORDER BY SUM(A.puntos) DESC LIMIT 10",
				2);
	}

	public void guardarNuevoGrupo() {
		_17_CrearGrupo vista = (_17_CrearGrupo) misVistas[17];
		if (vista.getNombre().isEmpty()) {
			vista.mostrarMensaje("Falta nombre.");
			return;
		}
		if (miModelo.crearGrupo(vista.getNombre(), vista.getDescripcion(), vista.isPrivado(), vista.getContrasena(),
				miModelo.getUsuarioLogueado())) {
			vista.mostrarMensaje("Grupo Creado.");
			vista.limpiarFormulario();
			cambiarVentana(17, 12);
		} else
			vista.mostrarMensaje("Error BD.");
	}

	public void mostrarDialogoUnirse() {
		String[] grupos = miModelo.obtenerGruposDisponibles(miModelo.getUsuarioLogueado());
		if (grupos.length == 0 || grupos[0].equals("No hay datos")) {
			JOptionPane.showMessageDialog(null, "No hay grupos.");
			return;
		}
		String sel = (String) JOptionPane.showInputDialog(null, "Selecciona clan:", "Unirse",
				JOptionPane.QUESTION_MESSAGE, null, grupos, grupos[0]);
		if (sel != null) {
			String pass = ((Boolean) miModelo.obtenerDatosTabla("SELECT es_privado FROM GRUPO WHERE nombre = ?", 1,
					sel)[0][0]) ? JOptionPane.showInputDialog("Contraseña:") : "";
			if (pass == null)
				return;
			String res = miModelo.unirseGrupo(miModelo.getUsuarioLogueado(), sel, pass);
			JOptionPane.showMessageDialog(null, res.equals("EXITO") ? "¡Unido!" : res);
		}
	}

	public void abandonarGrupoSeleccionado() {
		_16_Grupos vista = (_16_Grupos) misVistas[12];
		if (vista.getGrupoSeleccionado() != null && !vista.getGrupoSeleccionado().isEmpty()) {
			if (JOptionPane.showConfirmDialog(null, "¿Seguro?", "Salir",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				if (miModelo.abandonarGrupo(miModelo.getUsuarioLogueado(), vista.getGrupoSeleccionado()))
					vista.cargarConfiguracionInicial();
			}
		}
	}

	public void alSeleccionarGrupoEnSede(String ng) {
		_16_Grupos vista = (_16_Grupos) misVistas[12];
		Object[][] info = miModelo.obtenerDatosTabla(
				"SELECT descripcion, SUM(U.puntos) FROM GRUPO G JOIN PERTENECE_GRUPO PG ON G.id_grupo = PG.id_grupo JOIN USERS U ON PG.nombreUsuario = U.USR WHERE G.nombre = ? GROUP BY G.descripcion",
				2, ng);
		if (info.length > 0 && !info[0][0].equals("Sin registros"))
			vista.setInfoGrupo(info[0][0].toString(), info[0][1].toString());
		vista.setTablaMiembros(getRankingGrupalEspecifico(ng));
		vista.setTablaActividades(miModelo.obtenerDatosTabla(
				"SELECT CA.nombreUsuario, A.nombre, DATE_FORMAT(CA.fecha_realizacion, '%d/%m %H:%i') FROM COMPLETAR_ACTIVIDAD CA JOIN ACTIVIDADES A ON CA.id_actividad = A.id_actividad JOIN PERTENECE_GRUPO PG ON CA.nombreUsuario = PG.nombreUsuario JOIN GRUPO G ON PG.id_grupo = G.id_grupo WHERE G.nombre = ? ORDER BY CA.fecha_realizacion DESC LIMIT 15",
				3, ng));
	}

	// =========================================================================
	// 9. ADMINISTRADOR Y MISC
	// =========================================================================

	public ArrayList<Object[]> getDatosUsuarios() {
		return miModelo.obtenerDatosUsuarios();
	}

	public boolean altaUsuario(String usr, String pwd) {
		return miModelo.insertarUsuarioAdmin(usr, pwd);
	}

	public int borrarUsuario(String usr, String pwd) {
		return miModelo.borrar(usr, pwd);
	}

	public int modificarUsuario(String usr, String pwd) {
		return miModelo.modificar(usr, pwd);
	}

	public boolean gestionarPuntos(String u, int c) {
		return miModelo.actualizarPuntos(u, c);
	}

	public int getPuntosUsuario(String u) {
		return miModelo.consultarPuntosUsuario(u);
	}

	public int getMiPuestoMundial(String u) {
		return miModelo.obtenerPuestoMundial(u);
	}

	public int getMiPuestoSemanal(String u) {
		return miModelo.obtenerPuestoSemanal(u);
	}

	public String getUsuarioLogueado() {
		return miModelo.getUsuarioActual();
	}

	public String[] getPreguntasSeguridad() {
		return miModelo.obtenerDatosColumna("SELECT enunciado FROM PREGUNTA_SEGURIDAD ORDER BY id_pregunta ASC",
				"enunciado");
	}

	public String recuperarPreguntaUsuario(String u) {
		if (miModelo.getConexion() == null)
			miModelo.conectarBD();
		return miModelo.obtenerPreguntaDeUsuario(u);
	}

	public void actualizarMisionesEnInicio() {
		if (misVistas[2] != null)
			((_02_Inicio) misVistas[2]).cargarMisiones();
	}
}