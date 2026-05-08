package Vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;
import Modelo.Modelo;

public class Botones extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;
	
	// Colores de la app
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);
	
	private Controlador miControlador;
	private	Modelo miModelo;

	public Botones() {
		// --- 1. CONFIGURACIÓN BÁSICA DE LA VENTANA ---
		setBounds(0, 0, 1920, 1080);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// --- 2. CABECERA (HEADER) FIJA PARA TODAS LAS VENTANAS ---
		JPanel header = new JPanel();
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		header.setLayout(new BorderLayout()); 
		contentPane.add(header, BorderLayout.NORTH);

		// Header Izquierda (Logo y Perfil)
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

		// Header Derecha (Iconos y Control de Ventana)
		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		JButton btnTienda = new JButton("🛒");
		btnTienda.setPreferredSize(new Dimension(45, 40)); 
		btnTienda.setBackground(GRIS_RECTANGULO);
		btnTienda.setForeground(Color.WHITE);
		btnTienda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btnTienda.setBorder(null);
		btnTienda.setFocusPainted(false);
		headerRight.add(btnTienda);

		JButton btnRanking = new JButton("🏆");
		btnRanking.setPreferredSize(new Dimension(45, 40)); 
		btnRanking.setBackground(GRIS_RECTANGULO);
		btnRanking.setForeground(Color.WHITE);
		btnRanking.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btnRanking.setBorder(null);
		btnRanking.setFocusPainted(false);
		headerRight.add(btnRanking);

		JButton btnAjustes = new JButton("⚙");
		btnAjustes.setPreferredSize(new Dimension(45, 40)); 
		btnAjustes.setBackground(GRIS_RECTANGULO);
		btnAjustes.setForeground(Color.WHITE);
		btnAjustes.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btnAjustes.setBorder(null);
		btnAjustes.setFocusPainted(false);
		headerRight.add(btnAjustes);

		JPanel separador = new JPanel();
		separador.setPreferredSize(new Dimension(20, 0));
		separador.setOpaque(false);
		headerRight.add(separador);

		JButton btnMin = new JButton("-");
		btnMin.setPreferredSize(new Dimension(45, 40));
		btnMin.setBackground(new Color(15, 15, 15));
		btnMin.setForeground(Color.WHITE);
		btnMin.setFont(new Font("Arial", Font.BOLD, 26));
		btnMin.setBorder(null);
		btnMin.setFocusPainted(false);
		btnMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});
		headerRight.add(btnMin);

		JButton btnCerrar = new JButton("X");
		btnCerrar.setPreferredSize(new Dimension(45, 40));
		btnCerrar.setBackground(new Color(15, 15, 15));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 18));
		btnCerrar.setBorder(null);
		btnCerrar.setFocusPainted(false);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		headerRight.add(btnCerrar);


		// --- 3. ÁREA DE TRABAJO (AQUÍ DISEÑAS CADA PANTALLA) ---
		panelCentral = new JPanel();
		panelCentral.setBackground(GRIS_FONDO);
		
		// Por defecto le pongo un layout null para que arrastres cosas libremente,
		// pero puedes cambiarlo a GridLayout, BorderLayout, etc. haciendo clic derecho en WindowBuilder.
		panelCentral.setLayout(null); 
		contentPane.add(panelCentral, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("Panel Admin");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 11);
			}
		});
		btnNewButton.setBounds(877, 224, 110, 32);
		panelCentral.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Pantalla Inicio");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 2);
			}
		});
		btnNewButton_1.setBounds(550, 147, 147, 32);
		panelCentral.add(btnNewButton_1);
		
		JButton btnLogros = new JButton("Logros");
		btnLogros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 5);
			}
		});
		btnLogros.setBounds(354, 224, 147, 32);
		panelCentral.add(btnLogros);
		
		JButton btnDentroDeActividades = new JButton("Dentro de Actividades");
		btnDentroDeActividades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 4);
			}
		});
		btnDentroDeActividades.setBounds(890, 147, 178, 32);
		panelCentral.add(btnDentroDeActividades);
		
		JButton btnActividades_1 = new JButton("Actividades");
		btnActividades_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 3);
			}
		});
		btnActividades_1.setBounds(732, 147, 147, 32);
		panelCentral.add(btnActividades_1);
		
		JButton btnPerfilUsuario = new JButton("Perfil Usuario");
		btnPerfilUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 8);
			}
		});
		btnPerfilUsuario.setBounds(536, 224, 147, 32);
		panelCentral.add(btnPerfilUsuario);
		
		JButton btnRegistro = new JButton("Registro");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 1);
			}
		});
		btnRegistro.setBounds(166, 224, 147, 32);
		panelCentral.add(btnRegistro);
		
		JButton btnInicioDeSesion = new JButton("Inicio de Sesión");
		btnInicioDeSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 0);
			}
		});
		btnInicioDeSesion.setBounds(166, 147, 161, 32);
		panelCentral.add(btnInicioDeSesion);
		
		JButton btnTienda_1 = new JButton("Tienda");
		btnTienda_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 7);
			}
		});
		btnTienda_1.setBounds(702, 224, 147, 32);
		panelCentral.add(btnTienda_1);
		
		JButton btnNewButton_1_1 = new JButton("Olvido Contraseña");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 9);
			}
		});
		btnNewButton_1_1.setBounds(354, 147, 161, 32);
		panelCentral.add(btnNewButton_1_1);
		
		JButton btnVistaEvento = new JButton("Vista de Evento");
		btnVistaEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 13);
			}
		});
		btnVistaEvento.setBounds(997, 224, 147, 32);
		panelCentral.add(btnVistaEvento);
		
		JButton btnNewButton_2 = new JButton("Ranking");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(12, 6);
			}
		});
		btnNewButton_2.setBounds(1106, 147, 97, 32);
		panelCentral.add(btnNewButton_2);
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