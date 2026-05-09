//@Autor : Carlos/Diego
package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import Controlador.Controlador;
import Modelo.Modelo;

public class _08_PerfilUsuario extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel contenedorReal;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);
	private final Color GRIS_TABLA = new Color(45, 45, 45);

	private Controlador miControlador;
	private Modelo miModelo;

	private JLabel lblNombreUserBanner;
	private JLabel lblStatsUserBanner;

	// --- AÑADIDO: Guardamos las tablas como variables para poder actualizarlas ---
	private JTable tablaLogros;
	private JTable tablaCosmeticos;

	public _08_PerfilUsuario() {
		Dimension tamanoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int anchoPantalla = tamanoPantalla.width;
		int altoPantalla = tamanoPantalla.height;

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, anchoPantalla, altoPantalla);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// HEADER
		JPanel header = new JPanel();
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		header.setLayout(new BorderLayout());
		contentPane.add(header, BorderLayout.NORTH);

		JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
		headerLeft.setOpaque(false);
		JLabel lblKraft = new JLabel("KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);
		header.add(headerLeft, BorderLayout.WEST);

		JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		headerRight.setOpaque(false);

		// BOTÓN GRUPOS (AÑADIDO)
		JButton btnGrupos = crearBotonIcono("👥");
		btnGrupos.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(8, 12);
		});
		headerRight.add(btnGrupos);

		JButton btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(8, 2);
		});
		headerRight.add(btnHome);

		JButton btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);
		header.add(headerRight, BorderLayout.EAST);

		// LIENZO
		contenedorReal = new JPanel();
		contenedorReal.setBackground(GRIS_FONDO);
		contenedorReal.setLayout(null);
		contenedorReal.setPreferredSize(new Dimension(1450, 880));

		JPanel panelCentrador = new JPanel(new GridBagLayout());
		panelCentrador.setBackground(GRIS_FONDO);
		panelCentrador.add(contenedorReal);

		JScrollPane scrollPane = new JScrollPane(panelCentrador);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// BANNER
		JPanel panelBanner = new JPanel();
		panelBanner.setBackground(NARANJA);
		panelBanner.setBorder(new LineBorder(Color.BLACK, 3));
		panelBanner.setBounds(25, 20, 1400, 180);
		panelBanner.setLayout(null);
		contenedorReal.add(panelBanner);

		JPanel fotoP = new JPanel();
		fotoP.setBackground(Color.DARK_GRAY);
		fotoP.setBounds(30, 30, 120, 120);
		panelBanner.add(fotoP);

		lblNombreUserBanner = new JLabel("CARGANDO...");
		lblNombreUserBanner.setFont(new Font("Arial Black", Font.BOLD, 30));
		lblNombreUserBanner.setForeground(Color.WHITE);
		lblNombreUserBanner.setBounds(170, 45, 400, 40);
		panelBanner.add(lblNombreUserBanner);

		lblStatsUserBanner = new JLabel("Cargando estadísticas...");
		lblStatsUserBanner.setFont(new Font("Arial", Font.BOLD, 18));
		lblStatsUserBanner.setForeground(Color.BLACK);
		lblStatsUserBanner.setBounds(170, 95, 600, 25);
		panelBanner.add(lblStatsUserBanner);

		JButton btnAjustesBanner = new JButton("⚙");
		btnAjustesBanner.setBounds(1330, 20, 50, 50);
		btnAjustesBanner.setBackground(GRIS_RECTANGULO);
		btnAjustesBanner.setForeground(Color.WHITE);
		btnAjustesBanner.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
		btnAjustesBanner.setBorder(new LineBorder(Color.BLACK, 2));
		btnAjustesBanner.setFocusPainted(false);
		btnAjustesBanner.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(8, 14);
		});
		panelBanner.add(btnAjustesBanner);

		JButton btnCerrarSesion = new JButton("Log Out");
		btnCerrarSesion.setBounds(1239, 119, 141, 50);
		btnCerrarSesion.setBackground(GRIS_RECTANGULO);
		btnCerrarSesion.setForeground(Color.WHITE);
		btnCerrarSesion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
		btnCerrarSesion.setBorder(new LineBorder(Color.BLACK, 2));
		btnCerrarSesion.setFocusPainted(false);
		btnCerrarSesion.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(8, 0);
		});
		panelBanner.add(btnCerrarSesion);

		// CAJAS STATS
		contenedorReal.add(crearCajaEstadistica("🏆", "18 / 50", "Logros", 25, 220));
		contenedorReal.add(crearCajaEstadistica("👕", "12 / 20", "Cosméticos", 505, 220));
		contenedorReal.add(crearCajaEstadistica("🎯", "34", "Retos Completados", 985, 220));

		JPanel panelEquipado = new JPanel(null);
		panelEquipado.setBackground(NARANJA);
		panelEquipado.setBorder(new LineBorder(Color.BLACK, 3));
		panelEquipado.setBounds(25, 420, 1400, 60);
		contenedorReal.add(panelEquipado);

		JLabel tEq = new JLabel("EQUIPADO: Zapatillas Running, Camiseta Básica Negra, Aura de Fuego.");
		tEq.setFont(new Font("Arial Black", Font.PLAIN, 14));
		tEq.setForeground(Color.WHITE);
		tEq.setBounds(20, 15, 1300, 30);
		panelEquipado.add(tEq);

		// --- TABLAS VACÍAS AL INICIO ---
		String[] colL = { "Logro Desbloqueado", "Fecha" };
		Object[][] datL = {}; // Vacío, lo llenará la BD

		// Guardamos la referencia del JPanel y sacamos la tabla para guardarla
		JPanel panelTablaLogros = crearCajaTabla("LOGROS RECIENTES", datL, colL, 25, 500);
		contenedorReal.add(panelTablaLogros);

		String[] colC = { "Ítem", "Tipo de Cosmético" };
		Object[][] datC = {}; // Vacío, lo llenará la BD

		JPanel panelTablaCosmeticos = crearCajaTabla("INVENTARIO COSMÉTICO", datC, colC, 745, 500);
		contenedorReal.add(panelTablaCosmeticos);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				if (miControlador != null) {
					try {
						miControlador.alAbrirPerfil();
						// 1. Llenamos la tabla Logros
						Object[][] datosLogros = miControlador.getTablaLogrosPerfil();
						tablaLogros.setModel(new javax.swing.table.DefaultTableModel(datosLogros,
								new String[] { "Logro", "Fecha" }));

						// 2. Llenamos la tabla Inventario
						Object[][] datosInv = miControlador.getTablaInventarioPerfil();
						tablaCosmeticos.setModel(
								new javax.swing.table.DefaultTableModel(datosInv, new String[] { "Ítem", "Tipo" }));
					} catch (Exception ex) {
						System.err.println("Error al cargar datos del perfil: " + ex.getMessage());
					}
				}
			}
		});
	}

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
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(hover);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btn.setBackground(new Color(15, 15, 15));
			}
		});
	}

	private JPanel crearCajaEstadistica(String emoji, String valor, String etiqueta, int x, int y) {
		JPanel caja = new JPanel(null);
		caja.setBackground(NARANJA);
		caja.setBorder(new LineBorder(Color.BLACK, 3));
		caja.setBounds(x, y, 440, 180);

		JLabel val = new JLabel(valor);
		val.setFont(new Font("Arial Black", Font.BOLD, 30));
		val.setForeground(Color.WHITE);
		val.setHorizontalAlignment(SwingConstants.CENTER);
		val.setBounds(0, 60, 440, 40);
		caja.add(val);

		JLabel txt = new JLabel(emoji + " " + etiqueta);
		txt.setFont(new Font("Arial Black", Font.PLAIN, 16));
		txt.setForeground(Color.BLACK);
		txt.setHorizontalAlignment(SwingConstants.CENTER);
		txt.setBounds(0, 110, 440, 30);
		caja.add(txt);

		return caja;
	}

	// --- AÑADIDO: Modificamos el método para que guarde las JTable generadas ---
	private JPanel crearCajaTabla(String titulo, Object[][] datos, String[] col, int x, int y) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(NARANJA);
		p.setBorder(new LineBorder(Color.BLACK, 3));
		p.setBounds(x, y, 680, 350);

		JLabel t = new JLabel("  " + titulo);
		t.setForeground(Color.WHITE);
		t.setFont(new Font("Arial Black", Font.PLAIN, 18));
		t.setBorder(new EmptyBorder(10, 0, 10, 0));
		p.add(t, BorderLayout.NORTH);

		DefaultTableModel model = new DefaultTableModel(datos, col) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		JTable tabla = new JTable(model);
		tabla.setBackground(GRIS_TABLA);
		tabla.setForeground(Color.WHITE);
		tabla.setRowHeight(45);
		tabla.setGridColor(Color.BLACK);
		tabla.setSelectionBackground(NARANJA);
		tabla.setFont(new Font("Arial", Font.PLAIN, 16));

		// Guardamos la tabla generada en nuestra variable global
		if (titulo.equals("LOGROS RECIENTES")) {
			tablaLogros = tabla;
		} else {
			tablaCosmeticos = tabla;
		}

		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBorder(new LineBorder(Color.BLACK, 2));
		scroll.getViewport().setBackground(GRIS_TABLA);

		JPanel cont = new JPanel(new BorderLayout());
		cont.setOpaque(false);
		cont.setBorder(new EmptyBorder(0, 15, 15, 15));
		cont.add(scroll, BorderLayout.CENTER);
		p.add(cont, BorderLayout.CENTER);

		return p;
	}

	public void setDatosBanner(String usr, int nivel, int monedas, int actividades) {
		lblNombreUserBanner.setText(usr);
		lblStatsUserBanner
				.setText("Nivel Usuario: " + nivel + "  |  Monedas: " + monedas + "  |  Actividades: " + actividades);
	}

	public void setTablasPerfil(Object[][] datosLogros, Object[][] datosInventario) {
		tablaLogros.setModel(new DefaultTableModel(datosLogros, new String[] { "Logro", "Fecha" }));
		tablaCosmeticos.setModel(new DefaultTableModel(datosInventario, new String[] { "Ítem", "Tipo" }));
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