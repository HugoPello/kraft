//@Autor : Ivan / Carloss
package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controlador.Controlador;
import Modelo.Modelo;

public class _09_OlvidoContraseña extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;

	// Colores de la app
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	// Componentes
	private JTextField txtUsu;
	private JTextField txtRes;
	private JTextField txtNuevaPass;
	private JTextField txtConfirmPass;
	private JComboBox<String> cbxPregunta;

	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	private Controlador miControlador;
	private Modelo miModelo;

	public _09_OlvidoContraseña() {
		// --- 1. CONFIGURACIÓN BÁSICA DE LA VENTANA ---
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 1920, 1080);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// --- 2. CABECERA (HEADER) UNIFICADO ---
		JPanel header = new JPanel();
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		header.setLayout(new BorderLayout());
		contentPane.add(header, BorderLayout.NORTH);

		// Header Izquierda (Logo)
		JPanel headerLeft = new JPanel();
		headerLeft.setOpaque(false);
		headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
		header.add(headerLeft, BorderLayout.WEST);

		JLabel lblKraft = new JLabel("KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);

		JPanel fotoPerfil = new JPanel();
		fotoPerfil.setBackground(Color.DARK_GRAY);
		fotoPerfil.setPreferredSize(new Dimension(40, 40));
		headerLeft.add(fotoPerfil);

		// Header Derecha (Navegación y Control)
		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		// Instanciamos botones con el estilo de Carlos
//		btnHome = crearBotonIcono("🏠");
//		btnHome.addActionListener(e -> miControlador.cambiarVentana(9, 2));
//
//		btnPerfil = crearBotonIcono("👤");
//		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(9, 8));
//
//		btnActividades = crearBotonIcono("🏃");
//		btnActividades.addActionListener(e -> miControlador.cambiarVentana(9, 3));
//
//		btnCarrito = crearBotonIcono("🛒");
//		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(9, 7));
//
//		btnTrofeo = crearBotonIcono("🏆");
//		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(9, 6));
//
//		btnAjustes = crearBotonIcono("⚙");
//		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(9, 11));
//
//		headerRight.add(btnHome);
//		headerRight.add(btnPerfil);
//		headerRight.add(btnActividades);
//		headerRight.add(btnCarrito);
//		headerRight.add(btnTrofeo);
//		headerRight.add(btnAjustes);

		JPanel separador = new JPanel();
		separador.setPreferredSize(new Dimension(10, 0));
		separador.setOpaque(false);
		headerRight.add(separador);

		btnMin = new JButton("-");
		estilizarBotonControl(btnMin, 26, Color.GRAY);
		btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
		headerRight.add(btnMin);

		btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		// --- 3. ÁREA DE TRABAJO ---
		panelCentral = new JPanel();
		panelCentral.setBackground(GRIS_FONDO);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(null);

		JLabel lblRec = new JLabel("RECUPERACIÓN DE CONTRASEÑA", JLabel.CENTER);
		lblRec.setForeground(NARANJA);
		lblRec.setFont(new Font("Arial Black", Font.BOLD, 45));
		lblRec.setBounds(408, 39, 901, 84);
		panelCentral.add(lblRec);

		JLabel lblUsu = new JLabel("Usuario:");
		lblUsu.setBounds(631, 221, 95, 36);
		lblUsu.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblUsu.setForeground(Color.WHITE);
		panelCentral.add(lblUsu);

		txtUsu = new JTextField();
		txtUsu.setBounds(799, 233, 250, 25);
		panelCentral.add(txtUsu);

		JLabel lblPregunta = new JLabel("Pregunta:");
		lblPregunta.setBounds(613, 279, 100, 36);
		lblPregunta.setForeground(Color.WHITE);
		lblPregunta.setFont(new Font("Arial Black", Font.PLAIN, 16));
		panelCentral.add(lblPregunta);

		cbxPregunta = new JComboBox<String>();
		cbxPregunta.setModel(new DefaultComboBoxModel<String>(new String[] { "--Seleccione una opción--",
				"¿Cuál es el nombre de tu madre?", "¿Cuál es el nombre de tu padre?",
				"¿Cuál es el nombre de tu primera mascota?", "¿Cuál fue tu primer colegio?" }));
		cbxPregunta.setBounds(799, 290, 250, 25);
		panelCentral.add(cbxPregunta);

		JLabel lblRespuesta = new JLabel("Respuesta:");
		lblRespuesta.setForeground(Color.WHITE);
		lblRespuesta.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblRespuesta.setBounds(613, 330, 100, 36);
		panelCentral.add(lblRespuesta);

		txtRes = new JTextField();
		txtRes.setBounds(799, 340, 250, 25);
		panelCentral.add(txtRes);

		JLabel lblNuevaContrasena = new JLabel("Nueva contraseña:");
		lblNuevaContrasena.setForeground(Color.WHITE);
		lblNuevaContrasena.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNuevaContrasena.setBounds(560, 380, 183, 36);
		panelCentral.add(lblNuevaContrasena);

		txtNuevaPass = new JTextField();
		txtNuevaPass.setBounds(799, 390, 250, 25);
		panelCentral.add(txtNuevaPass);

		JLabel lblConfirmarContrasena = new JLabel("Confirmar contraseña:");
		lblConfirmarContrasena.setForeground(Color.WHITE);
		lblConfirmarContrasena.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblConfirmarContrasena.setBounds(529, 430, 216, 36);
		panelCentral.add(lblConfirmarContrasena);

		txtConfirmPass = new JTextField();
		txtConfirmPass.setBounds(799, 440, 250, 25);
		panelCentral.add(txtConfirmPass);

		JButton btnVolver = new JButton("Volver al Login");
		btnVolver.addActionListener(e -> miControlador.cambiarVentana(9, 0));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnVolver.setBounds(625, 520, 160, 40);
		btnVolver.setBackground(GRIS_RECTANGULO);
		btnVolver.setBorder(null);
		panelCentral.add(btnVolver);

		JButton btnRContraseña = new JButton("Cambiar contraseña");
		btnRContraseña.addActionListener(e -> {
			if (miControlador != null)
				miControlador.restablecerContrasena();
		});
		btnRContraseña.setForeground(Color.BLACK);
		btnRContraseña.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnRContraseña.setBounds(810, 520, 240, 40);
		btnRContraseña.setBackground(NARANJA);
		btnRContraseña.setBorder(null);
		panelCentral.add(btnRContraseña);

	}

	// --- MÉTODOS DE SOPORTE PARA EL HEADER ---

	private JButton crearBotonIcono(String icono) {
		JButton boton = new JButton(icono);
		boton.setPreferredSize(new Dimension(45, 40));
		boton.setBackground(GRIS_RECTANGULO);
		boton.setForeground(Color.WHITE);
		boton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		boton.setBorder(null);
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return boton;
	}

	private void estilizarBotonControl(JButton boton, int tamanoFuente, Color colorHover) {
		boton.setPreferredSize(new Dimension(45, 40));
		boton.setBackground(new Color(15, 15, 15));
		boton.setForeground(Color.WHITE);
		boton.setFont(new Font("Arial", Font.BOLD, tamanoFuente));
		boton.setBorder(null);
		boton.setFocusPainted(false);
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(colorHover);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(new Color(15, 15, 15));
			}
		});
	}

	// --- GETTERS Y SETTERS PARA EL CONTROLADOR ---
	public String getUsuario() {
		return txtUsu.getText();
	}

	public int getIndicePregunta() {
		return cbxPregunta.getSelectedIndex();
	}

	public String getRespuestaSeguridad() {
		return txtRes.getText();
	}

	public String getNuevaPass() {
		return txtNuevaPass.getText();
	}

	public String getConfirmPass() {
		return txtConfirmPass.getText();
	}

	public void mostrarMensaje(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	public void limpiarCampos() {
		txtUsu.setText("");
		txtRes.setText("");
		txtNuevaPass.setText("");
		txtConfirmPass.setText("");
		cbxPregunta.setSelectedIndex(0);
	}

	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;
	}
}