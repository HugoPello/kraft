// @Autor: Hugo / Carloss
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _10_CrearEventoAdmin extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;

	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	// Colores de la app
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Controlador miControlador;
	private Modelo miModelo;
	private JTable table;
	private JTextField txtNombreEvento;
	private JTextField txtPuntosEvento;
	private JSpinner spinner;

	public _10_CrearEventoAdmin() {
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

		// --- 2. CABECERA (HEADER) UNIFICADA ---
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
		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> miControlador.cambiarVentana(10, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(10, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(10, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(10, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(10, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(10, 11));

		headerRight.add(btnHome);
		headerRight.add(btnPerfil);
		headerRight.add(btnActividades);
		headerRight.add(btnCarrito);
		headerRight.add(btnTrofeo);
		headerRight.add(btnAjustes);

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
		panelCentral.setLayout(null);
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBounds(558, 150, 448, 420);
		panelCentral.add(panel);
		panel.setBackground(GRIS_RECTANGULO);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 230, 236);
		panel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 9));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Misión", "Tipo", "Puntos" }) {
			// Con esto se bloquea la edición de celdas
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Cambiamos el tamaño de las filas manualmente
		java.awt.EventQueue.invokeLater(() -> {
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(400);

			// Columna 1: Tipo (Bloqueada a 100)
			table.getColumnModel().getColumn(1).setMinWidth(100);
			table.getColumnModel().getColumn(1).setMaxWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);

			// Columna 2: Puntos (Bloqueada a 80)
			table.getColumnModel().getColumn(2).setMinWidth(80);
			table.getColumnModel().getColumn(2).setMaxWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(80);
		});

		table.setForeground(NARANJA);
		table.setBackground(GRIS_FONDO);
		scrollPane.setViewportView(table);

		JLabel lblNombreEvento = new JLabel("Nombre de la misión");
		lblNombreEvento.setBounds(250, 39, 149, 12);
		lblNombreEvento.setForeground(NARANJA);
		panel.add(lblNombreEvento);

		txtNombreEvento = new JTextField();
		txtNombreEvento.setBounds(250, 60, 171, 18);
		panel.add(txtNombreEvento);
		txtNombreEvento.setColumns(10);

		JLabel lblTipoEvento = new JLabel("Tipo de Misión");
		lblTipoEvento.setBounds(250, 106, 135, 12);
		lblTipoEvento.setForeground(NARANJA);
		panel.add(lblTipoEvento);

		JComboBox cbxTipoEvento = new JComboBox();
		cbxTipoEvento.setModel(new DefaultComboBoxModel(new String[] { "Diaria", "Semanal" }));
		cbxTipoEvento.setBounds(250, 128, 171, 18);
		panel.add(cbxTipoEvento);

		JLabel lblPuntosEvento = new JLabel("Puntos de la Misión");
		lblPuntosEvento.setBounds(250, 172, 171, 12);
		lblPuntosEvento.setForeground(NARANJA);
		panel.add(lblPuntosEvento);

		txtPuntosEvento = new JTextField();
		txtPuntosEvento.setBounds(250, 194, 171, 18);
		panel.add(txtPuntosEvento);
		txtPuntosEvento.setColumns(10);

		JButton btnAñadirEvento = new JButton("Añadir Misión");
		btnAñadirEvento.setText("Añadir Misión"); // Cámbiale el nombre al objeto si quieres
		btnAñadirEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String desc = txtNombreEvento.getText();
					String tipo = cbxTipoEvento.getSelectedItem().toString();
					int recompensa = Integer.parseInt(txtPuntosEvento.getText());

					if (miControlador.añadirMision(desc, tipo, recompensa)) {
						JOptionPane.showMessageDialog(null, "Misión guardada con éxito");
						txtNombreEvento.setText("");
						txtPuntosEvento.setText("");
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "La recompensa debe ser un número");
				}
			}
		});
		btnAñadirEvento.setBounds(306, 303, 115, 34);
		btnAñadirEvento.setBackground(GRIS_FONDO);
		btnAñadirEvento.setForeground(NARANJA);
		panel.add(btnAñadirEvento);

		JButton btnEliminarEvento = new JButton("Eliminar Misión");
		btnEliminarEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila != -1) {
					// Recuperamos el ID (columna 0)
					int idMision = (int) table.getValueAt(fila, 0);
					String nombre = table.getValueAt(fila, 1).toString();

					int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar " + nombre + "?");
					if (confirm == JOptionPane.YES_OPTION) {
						if (miControlador.eliminarMision(idMision)) {
							JOptionPane.showMessageDialog(null, "Misión eliminada");
						}
					}
				}
			}
		});
		btnEliminarEvento.setBounds(91, 303, 135, 34);
		btnEliminarEvento.setBackground(GRIS_FONDO);
		btnEliminarEvento.setForeground(NARANJA);
		panel.add(btnEliminarEvento);

		JButton btnVolverPanel = new JButton("Volver al panel");
		btnVolverPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(10, 11);
			}
		});
		btnVolverPanel.setBounds(159, 376, 135, 34);
		btnVolverPanel.setBackground(GRIS_FONDO);
		btnVolverPanel.setForeground(NARANJA);
		panel.add(btnVolverPanel);

		JLabel lblFechaEvento = new JLabel("Fecha Límite");
		lblFechaEvento.setBounds(250, 235, 149, 12);
		lblFechaEvento.setForeground(NARANJA);
		panel.add(lblFechaEvento);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerDateModel(new Date(1777240800000L), null, null, Calendar.DAY_OF_YEAR));
		spinner.setBounds(250, 257, 171, 18);
		panel.add(spinner);

		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentShown(java.awt.event.ComponentEvent ce) {
				cargarTablaMisiones();
			}
		});
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

	public void cargarTablaMisiones() {
		if (miControlador != null) {
			ArrayList<Object[]> datos = miControlador.getMisionesAdmin();
			// El modelo debe coincidir con: [ID, Misión, Tipo, Puntos]
			DefaultTableModel modelo = new DefaultTableModel(new Object[][] {},
					new String[] { "ID", "Misión", "Tipo", "Puntos" }) {
				@Override
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			};

			for (Object[] fila : datos) {
				modelo.addRow(fila);
			}
			table.setModel(modelo);

			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			// Columna 0: ID (Oculta)
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setPreferredWidth(0);

			// Columna 1: Misión (Muy ancha)
			table.getColumnModel().getColumn(1).setPreferredWidth(160); // Ajusta según el tamaño de tu panel

			// Columna 2: Tipo (Fija)
			table.getColumnModel().getColumn(2).setMinWidth(80);
			table.getColumnModel().getColumn(2).setMaxWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(80);

			// Columna 3: Puntos (Fija)
			table.getColumnModel().getColumn(3).setMinWidth(60);
			table.getColumnModel().getColumn(3).setMaxWidth(60);
			table.getColumnModel().getColumn(3).setPreferredWidth(60);
		}
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