package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controlador.Controlador;
import Modelo.Modelo;

public class _14_CrearActividad extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;
	
	// Colores
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	// MVC
	private Controlador miControlador;
	private Modelo miModelo;

	// Componentes de formulario (Globales para sacar sus datos luego)
	private JTextField txtNombre;
	private JComboBox<String> cbxTipo;
	private JComboBox<String> cbxDificultad;
	private JSpinner spnPuntos;
	private JComboBox<String> cbxFrecuencia;
	private JComboBox<String> cbxUnidad;

	public _14_CrearActividad() {
		setBounds(0, 0, 1920, 1080);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// --- HEADER ---
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		contentPane.add(header, BorderLayout.NORTH);

		JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
		headerLeft.setOpaque(false);
		header.add(headerLeft, BorderLayout.WEST);

		JLabel lblKraft = new JLabel("KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);

		JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		headerRight.setOpaque(false);
		header.add(headerRight, BorderLayout.EAST);

		JButton btnCerrar = new JButton("X");
		btnCerrar.setPreferredSize(new Dimension(45, 40));
		btnCerrar.setBackground(new Color(15, 15, 15));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 18));
		btnCerrar.setBorder(null);
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		// --- PANEL CENTRAL ---
		panelCentral = new JPanel(new GridBagLayout()); 
		panelCentral.setBackground(GRIS_FONDO);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		
		JPanel cajaCrear = new JPanel(null);
		cajaCrear.setBackground(GRIS_RECTANGULO);
		cajaCrear.setBorder(new LineBorder(NARANJA, 3));
		cajaCrear.setPreferredSize(new Dimension(800, 650)); // Altura ampliada
		panelCentral.add(cajaCrear);
		
		JLabel tituloAjustes = new JLabel("➕ NUEVA ACTIVIDAD");
		tituloAjustes.setFont(new Font("Arial Black", Font.BOLD, 30));
		tituloAjustes.setForeground(Color.WHITE);
		tituloAjustes.setBounds(50, 30, 500, 40);
		cajaCrear.add(tituloAjustes);
		
		JPanel linea = new JPanel();
		linea.setBackground(Color.DARK_GRAY);
		linea.setBounds(50, 80, 700, 2);
		cajaCrear.add(linea);

		// 1. NOMBRE
		JLabel lblNombre = new JLabel("Nombre de la Actividad:");
		lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
		lblNombre.setForeground(Color.LIGHT_GRAY);
		lblNombre.setBounds(50, 110, 300, 30);
		cajaCrear.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 16));
		txtNombre.setBounds(50, 140, 700, 35);
		cajaCrear.add(txtNombre);

		// 2. TIPO
		JLabel lblTipo = new JLabel("Tipo de Deporte:");
		lblTipo.setFont(new Font("Arial", Font.BOLD, 16));
		lblTipo.setForeground(Color.LIGHT_GRAY);
		lblTipo.setBounds(50, 190, 300, 30);
		cajaCrear.add(lblTipo);
		
		cbxTipo = new JComboBox<>(new String[]{"Montaña", "Agua", "Cancha", "Dieta", "Running"});
		cbxTipo.setFont(new Font("Arial", Font.PLAIN, 16));
		cbxTipo.setBounds(50, 220, 330, 35);
		cajaCrear.add(cbxTipo);

		// 3. DIFICULTAD
		JLabel lblDif = new JLabel("Dificultad:");
		lblDif.setFont(new Font("Arial", Font.BOLD, 16));
		lblDif.setForeground(Color.LIGHT_GRAY);
		lblDif.setBounds(420, 190, 300, 30);
		cajaCrear.add(lblDif);
		
		cbxDificultad = new JComboBox<>(new String[]{"Baja", "Media", "Alta", "Extrema"});
		cbxDificultad.setFont(new Font("Arial", Font.PLAIN, 16));
		cbxDificultad.setBounds(420, 220, 330, 35);
		cajaCrear.add(cbxDificultad);

		// 4. PUNTOS
		JLabel lblPuntos = new JLabel("Puntos de Recompensa:");
		lblPuntos.setFont(new Font("Arial", Font.BOLD, 16));
		lblPuntos.setForeground(Color.LIGHT_GRAY);
		lblPuntos.setBounds(50, 270, 300, 30);
		cajaCrear.add(lblPuntos);
		
		// Spinner de 10 en 10, empezando en 50
		spnPuntos = new JSpinner(new SpinnerNumberModel(50, 10, 10000, 10));
		spnPuntos.setFont(new Font("Arial", Font.BOLD, 16));
		spnPuntos.setBounds(50, 300, 330, 35);
		cajaCrear.add(spnPuntos);

		// 5. FRECUENCIA
		JLabel lblFrec = new JLabel("Frecuencia:");
		lblFrec.setFont(new Font("Arial", Font.BOLD, 16));
		lblFrec.setForeground(Color.LIGHT_GRAY);
		lblFrec.setBounds(420, 270, 300, 30);
		cajaCrear.add(lblFrec);
		
		cbxFrecuencia = new JComboBox<>(new String[]{"Única", "Diaria", "Semanal", "Mensual"});
		cbxFrecuencia.setFont(new Font("Arial", Font.PLAIN, 16));
		cbxFrecuencia.setBounds(420, 300, 330, 35);
		cajaCrear.add(cbxFrecuencia);

		// 6. UNIDAD DE MEDIDA
		JLabel lblUnidad = new JLabel("Unidad de Medida (Meta):");
		lblUnidad.setFont(new Font("Arial", Font.BOLD, 16));
		lblUnidad.setForeground(Color.LIGHT_GRAY);
		lblUnidad.setBounds(50, 350, 300, 30);
		cajaCrear.add(lblUnidad);
		
		cbxUnidad = new JComboBox<>(new String[]{"Km", "Minutos", "Horas", "Repeticiones", "Kcal", "Sesión Completa"});
		cbxUnidad.setFont(new Font("Arial", Font.PLAIN, 16));
		cbxUnidad.setBounds(50, 380, 700, 35);
		cajaCrear.add(cbxUnidad);

		// --- BOTONES ---
		JButton btnCrear = new JButton("CREAR ACTIVIDAD");
		btnCrear.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnCrear.setBackground(NARANJA);
		btnCrear.setForeground(Color.BLACK);
		btnCrear.setBounds(550, 540, 200, 50);
		btnCrear.setFocusPainted(false);
		btnCrear.addActionListener(e -> {
			if(miControlador != null) miControlador.guardarNuevaActividad();
		});
		cajaCrear.add(btnCrear);
		
		JButton btnVolver = new JButton("CANCELAR");
		btnVolver.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnVolver.setBackground(Color.GRAY);
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(330, 540, 200, 50);
		btnVolver.setFocusPainted(false);
		btnVolver.addActionListener(e -> {
			if(miControlador != null) miControlador.cambiarVentana(14, 3);
		});
		cajaCrear.add(btnVolver);
	}

	// MÉTODOS DE LA INTERFAZ VISTA
	@Override public void setModelo(Modelo miModelo) { this.miModelo = miModelo; }
	@Override public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;
		
	}

		public String getNombreActividad() { return txtNombre.getText(); }
		public String getTipoActividad() { return (String) cbxTipo.getSelectedItem(); }
		public String getDificultadActividad() { return (String) cbxDificultad.getSelectedItem(); }
		public int getPuntosActividad() { return (Integer) spnPuntos.getValue(); }
		public String getFrecuenciaActividad() { return (String) cbxFrecuencia.getSelectedItem(); }
		public String getUnidadActividad() { return (String) cbxUnidad.getSelectedItem(); }

		public void mostrarMensaje(String mensaje) {
			JOptionPane.showMessageDialog(this, mensaje, "Gestión de Actividades", JOptionPane.INFORMATION_MESSAGE);
		}
		
		public void limpiarFormulario() {
			txtNombre.setText("");
			spnPuntos.setValue(50);
			cbxTipo.setSelectedIndex(0);
			cbxDificultad.setSelectedIndex(0);
			cbxFrecuencia.setSelectedIndex(0);
			cbxUnidad.setSelectedIndex(0);
		}
}