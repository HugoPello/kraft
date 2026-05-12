//@Autor : Diego / Carloss
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _04_DentroActividad extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;

	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Controlador miControlador;
	private Modelo miModelo;

	private JTable tablaHistorial;
	private JTable tablaDisponibles;

	public _04_DentroActividad() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 1920, 1080);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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

		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(4, 2);
		});
		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(4, 8);
		});
		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(4, 3);
		});

		headerRight.add(btnHome);
		headerRight.add(btnPerfil);
		headerRight.add(btnActividades);

		btnMin = new JButton("-");
		estilizarBotonControl(btnMin, 26, Color.GRAY);
		btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
		headerRight.add(btnMin);

		btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		// --- ÁREA CENTRAL ---
		panelCentral = new JPanel(new GridLayout(1, 2, 40, 0));
		panelCentral.setBackground(GRIS_FONDO);
		panelCentral.setBorder(new EmptyBorder(50, 50, 50, 50));
		contentPane.add(panelCentral, BorderLayout.CENTER);

		// LADO IZQUIERDO: ACTIVIDADES COMPLETADAS
		JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 10));
		panelIzquierdo.setOpaque(false);
		JLabel lblHistorial = new JLabel("HISTORIAL COMPLETADAS", JLabel.CENTER);
		lblHistorial.setForeground(Color.GRAY);
		lblHistorial.setFont(new Font("Arial Black", Font.BOLD, 18));
		panelIzquierdo.add(lblHistorial, BorderLayout.NORTH);

		tablaHistorial = crearTablaEstilizada(new Object[][] {}, new String[] { "ACTIVIDAD", "FECHA" });
		JScrollPane scrollIzquierdo = new JScrollPane(tablaHistorial);
		scrollIzquierdo.setBorder(new LineBorder(Color.DARK_GRAY, 1));
		panelIzquierdo.add(scrollIzquierdo, BorderLayout.CENTER);
		panelCentral.add(panelIzquierdo);

		// LADO DERECHO: SELECCIÓN Y COMPLETADO
		JPanel panelDerecho = new JPanel(new BorderLayout(0, 15));
		panelDerecho.setOpaque(false);
		JLabel lblSeleccion = new JLabel("SELECCIONAR NUEVA ACTIVIDAD", JLabel.CENTER);
		lblSeleccion.setForeground(NARANJA);
		lblSeleccion.setFont(new Font("Arial Black", Font.BOLD, 18));
		panelDerecho.add(lblSeleccion, BorderLayout.NORTH);

		tablaDisponibles = crearTablaEstilizada(new Object[][] {},
				new String[] { "ACTIVIDADES DISPONIBLES", "DIFICULTAD" });
		JScrollPane scrollDerecho = new JScrollPane(tablaDisponibles);
		scrollDerecho.setBorder(new LineBorder(NARANJA, 1));
		panelDerecho.add(scrollDerecho, BorderLayout.CENTER);

		// --- BOTÓN DE COMPLETAR ---
		JButton btnCompletar = new JButton("MARCAR COMO COMPLETADA");
		btnCompletar.setPreferredSize(new Dimension(0, 55));
		btnCompletar.setBackground(NARANJA);
		btnCompletar.setForeground(Color.BLACK);
		btnCompletar.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnCompletar.setFocusPainted(false);
		btnCompletar.addActionListener(e -> {
			if (miControlador != null)
				miControlador.completarActividad();
		});
		panelDerecho.add(btnCompletar, BorderLayout.SOUTH);

		panelCentral.add(panelDerecho);
	}

	private JTable crearTablaEstilizada(Object[][] datos, String[] columnas) {
		DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		JTable tabla = new JTable(modelo);
		tabla.setBackground(GRIS_RECTANGULO);
		tabla.setForeground(Color.WHITE);
		tabla.setRowHeight(45);
		tabla.setFont(new Font("Arial", Font.PLAIN, 16));
		tabla.setSelectionBackground(NARANJA);
		tabla.setSelectionForeground(Color.BLACK);
		tabla.getTableHeader().setBackground(new Color(40, 40, 40));
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tabla.setShowVerticalLines(false);
		tabla.setFillsViewportHeight(true);
		// Modo de selección único para evitar errores al completar
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return tabla;
	}

	private JButton crearBotonIcono(String icono) {
		// ... (Sin cambios) ...
		JButton boton = new JButton(icono);
		boton.setPreferredSize(new Dimension(45, 40));
		boton.setBackground(GRIS_RECTANGULO);
		boton.setForeground(Color.WHITE);
		boton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		boton.setBorder(null);
		boton.setFocusPainted(false);
		return boton;
	}

	private void estilizarBotonControl(JButton boton, int tamanoFuente, Color colorHover) {
		// ... (Sin cambios) ...
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

	// --- MÉTODOS AÑADIDOS PARA COMUNICACIÓN CON CONTROLADOR ---

	public String getActividadSeleccionada() {
		int filaSeleccionada = tablaDisponibles.getSelectedRow();
		// Si es -1, significa que no ha seleccionado nada
		if (filaSeleccionada == -1) {
			return null;
		}
		// Devolvemos el texto de la columna 0 (el nombre)
		return tablaDisponibles.getValueAt(filaSeleccionada, 0).toString();
	}

	public void mostrarMensaje(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Información de Actividad", JOptionPane.INFORMATION_MESSAGE);
	}

	public void cargarDatosTablas() {
		// 1. Llenar tabla disponibles con nueva información
		Object[][] datosFiltrados = miControlador.getTablaActividadesPorTipo();
		tablaDisponibles.setModel(new DefaultTableModel(
				datosFiltrados, 
				new String[] { "ACTIVIDAD", "DIF.", "PROGRESO / META", "RECOMPENSA" }
		));

		// 2. Llenar tabla historial
		Object[][] datosHistorial = miControlador.getTablaHistorialUsuario();
		tablaHistorial.setModel(new DefaultTableModel(datosHistorial, new String[] { "HISTORIAL RECIENTE", "FECHA" }));
	}

	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				cargarDatosTablas();
			}
		});
	}
}