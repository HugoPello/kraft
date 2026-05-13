package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _16_Grupos extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Controlador miControlador;
	private Modelo miModelo; // Lo requiere tu interfaz, pero NO lo usamos para consultas

	private JComboBox<String> cbxMisGrupos;
	private JTextArea txtDescripcion;
	private JLabel lblPuntosTotales;
	private JTable tablaMiembros, tablaActividades;

	public _16_Grupos() {
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(GRIS_FONDO);
		setContentPane(contentPane);

		// --- HEADER ---
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));

		JLabel lblTitulo = new JLabel("  KRAFT");
		lblTitulo.setForeground(NARANJA);
		lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 24));
		header.add(lblTitulo, BorderLayout.WEST);

		JButton btnHome = new JButton("🏠");
		btnHome.setBackground(GRIS_RECTANGULO);
		btnHome.setForeground(Color.WHITE);
		btnHome.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btnHome.setBorder(new EmptyBorder(0, 20, 0, 20));
		btnHome.setFocusPainted(false);
		btnHome.addActionListener(e -> miControlador.cambiarVentana(12, 2)); // El índice 12 es _16_Grupos en tu Main
		header.add(btnHome, BorderLayout.EAST);
		contentPane.add(header, BorderLayout.NORTH);

		// --- CUERPO CENTRAL ---
		JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
		panelPrincipal.setOpaque(false);
		panelPrincipal.setBorder(new EmptyBorder(20, 40, 20, 40));
		contentPane.add(panelPrincipal, BorderLayout.CENTER);

		// Panel Superior: Selector e Info
		JPanel panelTop = new JPanel(new BorderLayout(20, 0));
		panelTop.setBackground(GRIS_RECTANGULO);
		panelTop.setBorder(new LineBorder(NARANJA, 2));
		panelTop.setPreferredSize(new Dimension(0, 100));

		JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
		panelSelector.setOpaque(false);
		JLabel lblViendo = new JLabel("VIENDO GRUPO:");
		lblViendo.setForeground(Color.WHITE);
		lblViendo.setFont(new Font("Arial", Font.BOLD, 16));
		panelSelector.add(lblViendo);

		cbxMisGrupos = new JComboBox<>();
		cbxMisGrupos.setPreferredSize(new Dimension(200, 30));
		// EVENTO: Avisar al controlador que el usuario ha cambiado de grupo
		cbxMisGrupos.addActionListener(e -> cargarDatosGrupoSeleccionado());
		panelSelector.add(cbxMisGrupos);
		panelTop.add(panelSelector, BorderLayout.WEST);

		txtDescripcion = new JTextArea();
		txtDescripcion.setEditable(false);
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setBackground(GRIS_RECTANGULO);
		txtDescripcion.setForeground(Color.LIGHT_GRAY);
		txtDescripcion.setFont(new Font("Arial", Font.ITALIC, 16));
		txtDescripcion.setBorder(new EmptyBorder(20, 10, 20, 10));
		panelTop.add(txtDescripcion, BorderLayout.CENTER);

		lblPuntosTotales = new JLabel("PUNTOS TOTALES: 0  ");
		lblPuntosTotales.setForeground(NARANJA);
		lblPuntosTotales.setFont(new Font("Arial Black", Font.BOLD, 20));
		panelTop.add(lblPuntosTotales, BorderLayout.EAST);

		panelPrincipal.add(panelTop, BorderLayout.NORTH);

		// Panel Tablas (Miembros y Actividades)
		JPanel panelTablas = new JPanel(new GridLayout(1, 2, 20, 0));
		panelTablas.setOpaque(false);

		tablaMiembros = new JTable();
		JScrollPane scrollMiembros = new JScrollPane(tablaMiembros);
		scrollMiembros.setBorder(BorderFactory.createTitledBorder(new LineBorder(NARANJA), "MIEMBROS DEL GRUPO", 0, 0,
				new Font("Arial", Font.BOLD, 14), GRIS_FONDO));
		panelTablas.add(scrollMiembros);
		tablaMiembros.setBackground(GRIS_RECTANGULO);
		tablaMiembros.setForeground(NARANJA);
		scrollMiembros.getViewport().setBackground(GRIS_RECTANGULO); 
		tablaMiembros.getTableHeader().setBackground(new Color(15, 15, 15));
		tablaMiembros.getTableHeader().setForeground(GRIS_FONDO);

		tablaActividades = new JTable();
		JScrollPane scrollActividades = new JScrollPane(tablaActividades);
		scrollActividades.setBorder(BorderFactory.createTitledBorder(new LineBorder(NARANJA), "ÚLTIMAS ACTIVIDADES",
				0, 0, new Font("Arial", Font.BOLD, 14), GRIS_FONDO));
		panelTablas.add(scrollActividades);
		tablaActividades.setBackground(GRIS_RECTANGULO);
		tablaActividades.setForeground(NARANJA);
		scrollActividades.getViewport().setBackground(GRIS_RECTANGULO);
		tablaActividades.getTableHeader().setBackground(new Color(15, 15, 15));
		tablaActividades.getTableHeader().setForeground(GRIS_FONDO);

		panelPrincipal.add(panelTablas, BorderLayout.CENTER);

		// --- PANEL BOTONES INFERIORES ---
		JPanel panelBotones = new JPanel(new GridLayout(1, 2, 20, 0));
		panelBotones.setOpaque(false);
		panelBotones.setBorder(new EmptyBorder(0, 40, 20, 40));

		JButton btnCrear = new JButton("➕ CREAR NUEVO GRUPO");
		btnCrear.setBackground(NARANJA);
		btnCrear.setForeground(Color.BLACK);
		btnCrear.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnCrear.setPreferredSize(new Dimension(0, 50));
		btnCrear.addActionListener(e -> miControlador.cambiarVentana(12, 17)); // 12 es Grupos, 17 CrearGrupo
		panelBotones.add(btnCrear);

		JButton btnUnirse = new JButton("🤝 UNIRSE A UN GRUPO");
		btnUnirse.setBackground(Color.WHITE);
		btnUnirse.setForeground(Color.BLACK);
		btnUnirse.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnUnirse.addActionListener(e -> miControlador.mostrarDialogoUnirse());
		panelBotones.add(btnUnirse);
		
		JButton btnSalir = new JButton("🚪 ABANDONAR CLAN");
		btnSalir.setBackground(new Color(220, 40, 40)); // Color rojo
		btnSalir.setForeground(Color.WHITE);
		btnSalir.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnSalir.addActionListener(e -> miControlador.abandonarGrupoSeleccionado()); 
		panelBotones.add(btnSalir);

		contentPane.add(panelBotones, BorderLayout.SOUTH);
	}

	// ==========================================================
	// EVENTOS DE LA VISTA (Solo avisan al Controlador)
	// ==========================================================

	public void cargarConfiguracionInicial() {
		String[] grupos = miControlador.getMisGrupos();
		cbxMisGrupos.setModel(new DefaultComboBoxModel<>(grupos));

		if (grupos.length > 0 && !grupos[0].equals("No hay datos")) {
			cargarDatosGrupoSeleccionado();
		} else {
			// Vista vacía si no hay grupos
			txtDescripcion.setText("No perteneces a ningún grupo.");
			lblPuntosTotales.setText("PUNTOS TOTALES: 0  ");
			tablaMiembros.setModel(new DefaultTableModel());
			tablaActividades.setModel(new DefaultTableModel());
		}
	}

	private void cargarDatosGrupoSeleccionado() {
		String grupo = (String) cbxMisGrupos.getSelectedItem();
		if (grupo != null && !grupo.equals("No hay datos")) {
			// ¡AQUÍ ESTÁ LA MAGIA! La vista solo le dice al controlador: "Cárgame este
			// grupo"
			miControlador.alSeleccionarGrupoEnSede(grupo);
		}
	}

	// ==========================================================
	// SETTERS PARA QUE EL CONTROLADOR INYECTE LOS DATOS (MVC PASIVO)
	// ==========================================================

	public void setInfoGrupo(String descripcion, String puntosTotales) {
		txtDescripcion.setText(descripcion);
		lblPuntosTotales.setText("PUNTOS TOTALES: " + puntosTotales + "  ");
	}

	public void setTablaMiembros(Object[][] datos) {
		tablaMiembros.setModel(new DefaultTableModel(datos, new String[] { "MIEMBRO", "PUNTOS" }));
	}

	public void setTablaActividades(Object[][] datos) {
		tablaActividades.setModel(new DefaultTableModel(datos, new String[] { "USUARIO", "ACTIVIDAD", "FECHA" }));
	}
	
	public String getGrupoSeleccionado() {
		return (String) cbxMisGrupos.getSelectedItem();
	}

	// ==========================================================
	// INTERFAZ VISTA
	// ==========================================================
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
				cargarConfiguracionInicial();
			}
		});
	}
}