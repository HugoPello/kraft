//@Autor : Carlos / Carloss
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _12_VistaDeEvento extends JFrame implements Vista {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral; 
	
	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Modelo miModelo;
	private Controlador miControlador;

	public _12_VistaDeEvento() {
		// --- 1. CONFIGURACIÓN DE VENTANA ---
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 1920, 1080);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		contentPane.setLayout(new BorderLayout()); // Layout para admitir Header al Norte
		setContentPane(contentPane);

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

		// Header Derecha (Navegación y Control)
		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		// Instanciamos botones con el estilo unificado
		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> miControlador.cambiarVentana(12, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(12, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(12, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(12, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(12, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(12, 11));

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

		// Botones de Control
		btnMin = new JButton("-");
		estilizarBotonControl(btnMin, 26, Color.GRAY);
		btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
		headerRight.add(btnMin);

		btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		// --- 3. PANEL CENTRAL (Contenido de Tabla + Detalle) ---
		// Contenedor intermedio para permitir el posicionado Absolute Responsivo
		JPanel containerWrapper = new JPanel(null);
		containerWrapper.setOpaque(false);
		contentPane.add(containerWrapper, BorderLayout.CENTER);

		panelCentral = new JPanel();
		panelCentral.setOpaque(false);
		panelCentral.setSize(1200, 600); 
		panelCentral.setLayout(null);
		containerWrapper.add(panelCentral);

		JLabel lblTabla = new JLabel("EVENTOS ACTIVOS");
		lblTabla.setForeground(Color.GRAY);
		lblTabla.setBounds(0, 10, 200, 20);
		panelCentral.add(lblTabla);

		// --- TABLA ---
		String[] columnas = { "Evento", "Pts" };
		Object[][] datos = { { "Cruzar el país", "1200" }, { "Maratón", "800" }, { "Escalada", "500" }, { "Ciclismo", "900" } };
		DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
		JTable tabla = new JTable(modelo);
		tabla.setBackground(GRIS_RECTANGULO);
		tabla.setForeground(Color.WHITE);
		tabla.setRowHeight(35);
		tabla.setSelectionBackground(NARANJA);

		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(0, 40, 280, 450);
		scroll.getViewport().setBackground(GRIS_RECTANGULO);
		panelCentral.add(scroll);

		// --- PANEL DE DETALLE ---
		JPanel panelDetalle = new JPanel();
		panelDetalle.setBackground(GRIS_RECTANGULO);
		panelDetalle.setBounds(310, 40, 850, 520);
		panelDetalle.setLayout(null);
		panelCentral.add(panelDetalle);

		JLabel lblTitulo = new JLabel("CRUZAR EL PAIS");
		lblTitulo.setForeground(NARANJA);
		lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 36));
		lblTitulo.setBounds(30, 30, 500, 50);
		panelDetalle.add(lblTitulo);

		JLabel lblNew = new JLabel("1200 PTS");
		lblNew.setForeground(Color.WHITE);
		lblNew.setFont(new Font("Arial Black", Font.BOLD, 30));
		lblNew.setBounds(30, 100, 200, 40);
		panelDetalle.add(lblNew);

		JButton btnP = new JButton("PARTICIPAR");
		btnP.setBackground(NARANJA);
		btnP.setFont(new Font("Arial Black", Font.BOLD, 18));
		btnP.setBounds(30, 410, 200, 50);
		btnP.setFocusPainted(false);
		btnP.setBorder(null);
		panelDetalle.add(btnP);

		// --- LISTENER PARA RESPONSIVE ---
		containerWrapper.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int x = (containerWrapper.getWidth() - panelCentral.getWidth()) / 2;
				int y = (containerWrapper.getHeight() - panelCentral.getHeight()) / 2;
				panelCentral.setLocation(x, Math.max(y, 20));
			}
		});
	}

	// --- MÉTODOS DE SOPORTE PARA EL HEADER (ESTILO CARLOS) ---

	private JButton crearBotonIcono(String icono) {
		JButton btn = new JButton(icono);
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(GRIS_RECTANGULO);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btn.setBorder(null);
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private void estilizarBotonControl(JButton btn, int tamano, Color hover) {
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(new Color(15, 15, 15));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, tamano));
		btn.setBorder(null);
		btn.setFocusPainted(false);
		btn.addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
			@Override public void mouseExited(MouseEvent e) { btn.setBackground(new Color(15, 15, 15)); }
		});
	}

	@Override public void setModelo(Modelo miModelo) { this.miModelo = miModelo; }
	@Override public void setControlador(Controlador miControlador) { this.miControlador = miControlador; }
}