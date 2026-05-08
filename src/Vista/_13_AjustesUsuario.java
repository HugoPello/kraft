package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Controlador.Controlador;
import Modelo.Modelo;

public class _13_AjustesUsuario extends JFrame implements Vista {

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

	public _13_AjustesUsuario() {
		// CONFIGURACIÓN VENTANA
		setBounds(0, 0, 1920, 1080);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

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

		JPanel headerLeft = new JPanel();
		headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
		headerLeft.setOpaque(false);
		header.add(headerLeft, BorderLayout.WEST);

		JLabel lblKraft = new JLabel("KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);

		JPanel headerRight = new JPanel();
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		headerRight.setOpaque(false);
		header.add(headerRight, BorderLayout.EAST);

		JButton btnCerrar = new JButton("X");
		btnCerrar.setPreferredSize(new Dimension(45, 40));
		btnCerrar.setBackground(new Color(15, 15, 15));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 18));
		btnCerrar.setBorder(null);
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		headerRight.add(btnCerrar);

		// PANEL CENTRAL (Ajustes)
		panelCentral = new JPanel();
		panelCentral.setBackground(GRIS_FONDO);
		panelCentral.setLayout(new GridBagLayout()); 
		contentPane.add(panelCentral, BorderLayout.CENTER);
		
		// CAJA PRINCIPAL DE AJUSTES
		JPanel cajaAjustes = new JPanel();
		cajaAjustes.setBackground(GRIS_RECTANGULO);
		cajaAjustes.setBorder(new LineBorder(NARANJA, 3));
		cajaAjustes.setPreferredSize(new Dimension(800, 700));
		cajaAjustes.setLayout(null);
		panelCentral.add(cajaAjustes);
		
		JLabel tituloAjustes = new JLabel("⚙ AJUSTES DE PERFIL");
		tituloAjustes.setFont(new Font("Arial Black", Font.BOLD, 30));
		tituloAjustes.setForeground(Color.WHITE);
		tituloAjustes.setBounds(50, 40, 500, 40);
		cajaAjustes.add(tituloAjustes);
		
		JPanel linea = new JPanel();
		linea.setBackground(Color.DARK_GRAY);
		linea.setBounds(50, 100, 700, 2);
		cajaAjustes.add(linea);

		// 1. Cambiar Nombre
		JLabel lblNombre = new JLabel("Cambiar Nombre de Usuario:");
		lblNombre.setFont(new Font("Arial", Font.BOLD, 18));
		lblNombre.setForeground(Color.LIGHT_GRAY);
		lblNombre.setBounds(50, 130, 300, 30);
		cajaAjustes.add(lblNombre);
		
		JTextField txtNombre = new JTextField("Mohammed");
		txtNombre.setFont(new Font("Arial", Font.PLAIN, 18));
		txtNombre.setBounds(50, 165, 500, 40);
		cajaAjustes.add(txtNombre);

		// 2. Cambiar Email
		JLabel lblEmail = new JLabel("Cambiar Email:");
		lblEmail.setFont(new Font("Arial", Font.BOLD, 18));
		lblEmail.setForeground(Color.LIGHT_GRAY);
		lblEmail.setBounds(50, 230, 300, 30);
		cajaAjustes.add(lblEmail);
		
		JTextField txtEmail = new JTextField("usuario@correo.com");
		txtEmail.setFont(new Font("Arial", Font.PLAIN, 18));
		txtEmail.setBounds(50, 265, 500, 40);
		cajaAjustes.add(txtEmail);

		// 3. Cambiar Contraseña
		JLabel lblPass = new JLabel("Nueva Contraseña:");
		lblPass.setFont(new Font("Arial", Font.BOLD, 18));
		lblPass.setForeground(Color.LIGHT_GRAY);
		lblPass.setBounds(50, 330, 300, 30);
		cajaAjustes.add(lblPass);
		
		JPasswordField txtPass = new JPasswordField();
		txtPass.setFont(new Font("Arial", Font.PLAIN, 18));
		txtPass.setBounds(50, 365, 500, 40);
		cajaAjustes.add(txtPass);

		// 4. Botón Cambiar Foto
		JLabel lblFoto = new JLabel("Foto de Perfil:");
		lblFoto.setFont(new Font("Arial", Font.BOLD, 18));
		lblFoto.setForeground(Color.LIGHT_GRAY);
		lblFoto.setBounds(50, 430, 300, 30);
		cajaAjustes.add(lblFoto);
		
		JButton btnSubirFoto = new JButton("Subir nueva imagen...");
		btnSubirFoto.setFont(new Font("Arial", Font.PLAIN, 16));
		btnSubirFoto.setBackground(Color.DARK_GRAY);
		btnSubirFoto.setForeground(Color.WHITE);
		btnSubirFoto.setBounds(50, 465, 200, 40);
		btnSubirFoto.setFocusPainted(false);
		cajaAjustes.add(btnSubirFoto);

		// 5. Botones Guardar y Cancelar
		JButton btnGuardar = new JButton("GUARDAR CAMBIOS");
		btnGuardar.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnGuardar.setBackground(NARANJA);
		btnGuardar.setForeground(Color.BLACK);
		btnGuardar.setBounds(550, 600, 200, 50);
		btnGuardar.setFocusPainted(false);
		cajaAjustes.add(btnGuardar);
		
		JButton btnVolver = new JButton("VOLVER");
		btnVolver.setFont(new Font("Arial Black", Font.BOLD, 16));
		btnVolver.setBackground(Color.GRAY);
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(330, 600, 200, 50);
		btnVolver.setFocusPainted(false);
		cajaAjustes.add(btnVolver);
		
		// Evento para volver al Perfil
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.cambiarVentana(14, 8);
			}
		});
	}

	// MÉTODOS DE LA INTERFAZ VISTA
	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;
	}
}