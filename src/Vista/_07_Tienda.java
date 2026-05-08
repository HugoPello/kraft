//@Autor : Diego / Carloss
package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controlador.Controlador;
import Modelo.Modelo;

public class _07_Tienda extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral; 
	
	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	// Colores
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);
	private final Color GRIS_TARJETA = new Color(100, 100, 100);
	
	private Controlador miControlador;
	private Modelo miModelo;

	public _07_Tienda() {
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

		JLabel lblKraft = new JLabel("KRAFT - TIENDA");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);
		
		// Header Derecha (Navegación y Control)
		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		// Instanciamos botones con el estilo de Carlos
		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> miControlador.cambiarVentana(7, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(7, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(7, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(7, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(7, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(7, 11));

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
		panelCentral.setBorder(new EmptyBorder(30, 30, 30, 30));
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new GridLayout(2, 3, 20, 20));
		
		// Añadir las tarjetas de la tienda
		panelCentral.add(crearTarjetaProducto("Banner - Bosque Oscuro", "🌲", "Un fondo misterioso para tu perfil.", "800 Monedas", "Banner", new Color(34, 139, 34)));
		panelCentral.add(crearTarjetaProducto("Marco - Oro Puro", "🖼️", "Un borde dorado brillante para resaltar tu foto.", "1500 Monedas", "Borde de Marco", new Color(255, 215, 0)));
		panelCentral.add(crearTarjetaProducto("Título - 'El Imparable'", "👑", "Muestra quién manda bajo tu nombre.", "500 Monedas", "Título de Jugador", Color.RED));
		panelCentral.add(crearTarjetaProducto("Efecto - Rayos Azules", "⚡", "Chispas eléctricas al entrar a la app.", "1200 Monedas", "Efecto Visual", Color.BLUE));
		panelCentral.add(crearTarjetaProducto("Banner - Atardecer", "🌅", "Un paisaje cálido y relajante.", "750 Monedas", "Banner", Color.ORANGE));
		panelCentral.add(crearTarjetaProducto("Marco - Neón Cyberpunk", "🟣", "Un marco futurista con luces neón.", "1000 Monedas", "Borde de Marco", Color.MAGENTA));
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
			@Override public void mouseEntered(MouseEvent e) { boton.setBackground(colorHover); }
			@Override public void mouseExited(MouseEvent e) { boton.setBackground(new Color(15, 15, 15)); }
		});
	}

	// --- MÉTODO PARA CREAR TARJETAS (ADAPTADO A PORTÁTILES) ---

	private JPanel crearTarjetaProducto(String titulo, String emoji, String desc, String precio, String tipo, Color colorPreview) {
		// Usamos BorderLayout para la tarjeta (Norte, Centro, Sur)
		JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
		tarjeta.setBackground(GRIS_TARJETA);
		// CompoundBorder pone un borde negro por fuera y le da "aire" por dentro para que no se pegue el texto
		tarjeta.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 3), new EmptyBorder(15, 15, 15, 15)));
		
		// ARRIBA: Título (Izquierda) y Color (Derecha)
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);
		
		JLabel lblTit = new JLabel(titulo);
		lblTit.setFont(new Font("Arial", Font.BOLD, 18));
		topPanel.add(lblTit, BorderLayout.WEST);

		JPanel preview = new JPanel();
		preview.setBackground(colorPreview);
		preview.setBorder(new LineBorder(Color.BLACK, 2));
		preview.setPreferredSize(new Dimension(40, 40));
		topPanel.add(preview, BorderLayout.EAST);
		
		tarjeta.add(topPanel, BorderLayout.NORTH);
		
		// CENTRO: Emoji gigante centrado automáticamente
		JLabel lblEmoji = new JLabel(emoji, SwingConstants.CENTER);
		lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 70)); // Bajado un pelín para que no desborde en portátiles
		tarjeta.add(lblEmoji, BorderLayout.CENTER);
		
		// ABAJO: Descripción, Etiqueta y Botón apilados (BoxLayout vertical)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		
		JTextArea txtDesc = new JTextArea(desc);
		txtDesc.setFont(new Font("Arial", Font.PLAIN, 15));
		txtDesc.setLineWrap(true);
		txtDesc.setWrapStyleWord(true);
		txtDesc.setOpaque(true);
		txtDesc.setBackground(Color.WHITE);
		txtDesc.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 5, 5, 5)));
		txtDesc.setEditable(false);
		txtDesc.setFocusable(false);
		txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel lblTipo = new JLabel("TipoCosm: " + tipo, SwingConstants.CENTER);
		lblTipo.setOpaque(true);
		lblTipo.setBackground(Color.WHITE);
		lblTipo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(5, 10, 5, 10)));
		lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton btnComprar = new JButton(precio);
		btnComprar.setBackground(NARANJA);
		btnComprar.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnComprar.setBorder(new LineBorder(Color.BLACK, 2));
		btnComprar.setFocusPainted(false);
		btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnComprar.setMaximumSize(new Dimension(200, 45)); // Evita que el botón se ensanche a lo bestia
		
		// Añadimos espacios (Strut) entre los elementos
		bottomPanel.add(txtDesc);
		bottomPanel.add(Box.createVerticalStrut(15));
		bottomPanel.add(lblTipo);
		bottomPanel.add(Box.createVerticalStrut(15));
		bottomPanel.add(btnComprar);
		
		tarjeta.add(bottomPanel, BorderLayout.SOUTH);
		
		return tarjeta;
	}

	@Override public void setModelo(Modelo miModelo) { this.miModelo = miModelo; }

	@Override public void setControlador(Controlador miControlador) { this.miControlador = miControlador; }
}