
//@Autor : Diego / Carlos
package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controlador.Controlador;
import Modelo.Modelo;

public class _03_Actividades extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	private Controlador miControlador;
	private Modelo miModelo;

	public _03_Actividades() {
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

		// Header Izquierda (Logo y botón auxiliar)
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

		// Instanciamos botones de navegación con el estilo de Carlos
		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> miControlador.cambiarVentana(3, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(3, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(3, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(3, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(3, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(3, 11));

		headerRight.add(btnHome);
		headerRight.add(btnPerfil);
		headerRight.add(btnActividades);
		headerRight.add(btnCarrito);
		headerRight.add(btnTrofeo);
		headerRight.add(btnAjustes);

		// Separador visual
		JPanel separador = new JPanel();
		separador.setPreferredSize(new Dimension(10, 0));
		separador.setOpaque(false);
		headerRight.add(separador);

		// Botones de control (Cerrar/Minimizar)
		btnMin = new JButton("-");
		estilizarBotonControl(btnMin, 26, Color.GRAY);
		btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
		headerRight.add(btnMin);

		btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		// --- 3. PANEL CENTRAL DE ACTIVIDADES ---
		JPanel panelCentro = new JPanel();
		panelCentro.setBackground(GRIS_FONDO);
		contentPane.add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new GridLayout(1, 6, 10, 0));

		// Actividad 1
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 10));
		panel_1.setBackground(GRIS_FONDO);
		JButton btnMontana = new JButton("Deportes de Montaña");
		btnMontana.setBackground(GRIS_RECTANGULO);
		btnMontana.setForeground(Color.WHITE);
		btnMontana.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnMontana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miControlador != null) miControlador.abrirActividadEspecifica("Montaña");
			}
		});
		panel_1.add(btnMontana, BorderLayout.CENTER);
		JProgressBar prog_1 = new JProgressBar();
		prog_1.setForeground(new Color(128, 255, 0));
		prog_1.setValue(0);
		prog_1.setPreferredSize(new Dimension(0, 30));
		panel_1.add(prog_1, BorderLayout.SOUTH);
		panelCentro.add(panel_1);

		// Actividad 2
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout(0, 10));
		panel_2.setBackground(GRIS_FONDO);
		JButton btnAgua = new JButton("Deportes de Agua");
		btnAgua.setBackground(GRIS_RECTANGULO);
		btnAgua.setForeground(Color.WHITE);
		btnAgua.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnAgua.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miControlador != null) miControlador.abrirActividadEspecifica("Agua");
			}
		});
		panel_2.add(btnAgua, BorderLayout.CENTER);
		JProgressBar prog_2 = new JProgressBar();
		prog_2.setForeground(new Color(128, 255, 0));
		prog_2.setValue(60);
		prog_2.setPreferredSize(new Dimension(0, 30));
		panel_2.add(prog_2, BorderLayout.SOUTH);
		panelCentro.add(panel_2);

		// Actividad 3
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout(0, 10));
		panel_3.setBackground(GRIS_FONDO);
		JButton btnCancha = new JButton("Deportes de Cancha");
		btnCancha.setBackground(GRIS_RECTANGULO);
		btnCancha.setForeground(Color.WHITE);
		btnCancha.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnCancha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miControlador != null) miControlador.abrirActividadEspecifica("Cancha");
			}
		});
		panel_3.add(btnCancha, BorderLayout.CENTER);
		JProgressBar prog_3 = new JProgressBar();
		prog_3.setForeground(new Color(128, 255, 0));
		prog_3.setValue(10);
		prog_3.setPreferredSize(new Dimension(0, 30));
		panel_3.add(prog_3, BorderLayout.SOUTH);
		panelCentro.add(panel_3);

		// Actividad 4
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(new BorderLayout(0, 10));
		panel_4.setBackground(GRIS_FONDO);
		JButton btnDieta = new JButton("Dieta");
		btnDieta.setBackground(GRIS_RECTANGULO);
		btnDieta.setForeground(Color.WHITE);
		btnDieta.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnDieta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miControlador != null) miControlador.abrirActividadEspecifica("Dieta");
			}
		});
		panel_4.add(btnDieta, BorderLayout.CENTER);
		JProgressBar prog_4 = new JProgressBar();
		prog_4.setForeground(new Color(128, 255, 0));
		prog_4.setValue(50);
		prog_4.setPreferredSize(new Dimension(0, 30));
		panel_4.add(prog_4, BorderLayout.SOUTH);
		panelCentro.add(panel_4);

		// Actividad 5
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BorderLayout(0, 10));
		panel_5.setBackground(GRIS_FONDO);
		JButton btnRunning = new JButton("Running");
		btnRunning.setBackground(GRIS_RECTANGULO);
		btnRunning.setForeground(Color.WHITE);
		btnRunning.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnRunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(miControlador != null) miControlador.abrirActividadEspecifica("Running");
			}
		});
		panel_5.add(btnRunning, BorderLayout.CENTER);
		JProgressBar prog_5 = new JProgressBar();
		prog_5.setForeground(new Color(128, 255, 0));
		prog_5.setValue(22);
		prog_5.setPreferredSize(new Dimension(0, 30));
		panel_5.add(prog_5, BorderLayout.SOUTH);
		panelCentro.add(panel_5);

		// ===============================================
		// ACTIVIDAD 6 (NUEVA: CREAR ACTIVIDAD)
		// ===============================================
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(new BorderLayout(0, 10));
		panel_6.setBackground(GRIS_FONDO);

		// Botón limpio sin HTML
		JButton btnCrearActividad = new JButton("➕ Crear Actividad");
		btnCrearActividad.setBackground(new Color(30, 30, 30));
		btnCrearActividad.setForeground(NARANJA);
		btnCrearActividad.setFont(new Font("Arial", Font.BOLD, 18));
		btnCrearActividad.setBorder(new LineBorder(NARANJA, 2));
		btnCrearActividad.setFocusPainted(false);
		// EVENTO AÑADIDO
		btnCrearActividad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(3, 15);
			}
		});
		panel_6.add(btnCrearActividad, BorderLayout.CENTER);

		// Barra de progreso invisible para que la altura cuadre
		JProgressBar prog_6_invisible = new JProgressBar();
		prog_6_invisible.setPreferredSize(new Dimension(0, 30));
		prog_6_invisible.setOpaque(false);
		prog_6_invisible.setBorderPainted(false);
		panel_6.add(prog_6_invisible, BorderLayout.SOUTH);

		panelCentro.add(panel_6);
	}

	// --- MÉTODOS PRIVADOS DE SOPORTE PARA EL HEADER ---

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

	// Método auxiliar para no repetir código en el panel central
	private JPanel crearPanelActividad(String nombre, int progreso) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 10));
		panel.setBackground(GRIS_FONDO);

		JButton btn = new JButton(nombre);
		btn.setBackground(GRIS_RECTANGULO);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		panel.add(btn, BorderLayout.CENTER);

		JProgressBar pb = new JProgressBar();
		pb.setForeground(new Color(128, 255, 0));
		pb.setValue(progreso);
		pb.setPreferredSize(new Dimension(0, 30));
		panel.add(pb, BorderLayout.SOUTH);

		return panel;
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