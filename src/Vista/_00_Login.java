/**
 * Interfaz gráfica de acceso al sistema (Login).
 * Esta clase gestiona la entrada de credenciales del usuario, la validación
 * visual de campos obligatorios y la comunicación inicial con el controlador
 * para el inicio de sesión y la configuración de la base de datos.
 * * @author Ivan, Carlos
 * @version 1.0
 */
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Controlador.Controlador;
import Modelo.Modelo;

public class _00_Login extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;
	private JTextField txtUsu;
	private JPasswordField txtPwd;

	private JButton btnCerrar, btnMin;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Controlador miControlador;
	private Modelo miModelo;
	private JLabel lblRespuesta;
	private JButton btnInicio;

	public _00_Login() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 1920, 1080);

		contentPane = new JPanel();
		contentPane.setBackground(GRIS_FONDO);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel header = new JPanel();
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		header.setLayout(new BorderLayout());
		contentPane.add(header, BorderLayout.NORTH);

		JPanel headerLeft = new JPanel();
		headerLeft.setOpaque(false);
		headerLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
		header.add(headerLeft, BorderLayout.WEST);

		JLabel lblKraft = new JLabel("KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		headerLeft.add(lblKraft);

		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		btnMin = new JButton("-");
		estilizarBotonControl(btnMin, 26, Color.GRAY);
		btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
		headerRight.add(btnMin);

		btnCerrar = new JButton("X");
		estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
		btnCerrar.addActionListener(e -> System.exit(0));
		headerRight.add(btnCerrar);

		panelCentral = new JPanel();
		panelCentral.setBackground(GRIS_FONDO);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(null);

		JLabel lblInicio = new JLabel("INICIO DE SESIÓN", JLabel.CENTER);
		lblInicio.setForeground(NARANJA);
		lblInicio.setFont(new Font("Arial Black", Font.BOLD, 45));
		lblInicio.setBounds(528, 37, 494, 60);
		panelCentral.add(lblInicio);

		JLabel lblUsu = new JLabel("Usuario:");
		lblUsu.setBounds(584, 180, 150, 30);
		lblUsu.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsu.setForeground(Color.WHITE);
		panelCentral.add(lblUsu);

		txtUsu = new JTextField();
		txtUsu.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (btnInicio.isEnabled()) {
						miControlador.login();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarBoton();
			}
		});
		txtUsu.setBounds(810, 180, 200, 30);
		panelCentral.add(txtUsu);

		JLabel lblPwd = new JLabel("Contraseña:");
		lblPwd.setBounds(584, 230, 150, 30);
		lblPwd.setForeground(Color.WHITE);
		lblPwd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelCentral.add(lblPwd);

		txtPwd = new JPasswordField();
		txtPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (btnInicio.isEnabled()) {
						miControlador.login();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarBoton();
			}
		});
		txtPwd.setBounds(810, 230, 200, 30);
		panelCentral.add(txtPwd);

		// AQUÍ ES DONDE EL BOTÓN LLAMA AL CONTROLADOR (Línea Mágica)
		btnInicio = new JButton("Iniciar sesión");
		btnInicio.setEnabled(false);
		btnInicio.addActionListener(e -> miControlador.login());
		btnInicio.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnInicio.setBounds(810, 303, 200, 40);
		btnInicio.setBackground(NARANJA);
		btnInicio.setFocusPainted(false);
		btnInicio.setBorder(null);
		panelCentral.add(btnInicio);

		JButton btnRContraseña = new JButton("Olvido de contraseña");
		btnRContraseña.addActionListener(e -> miControlador.cambiarVentana(0, 9));
		btnRContraseña.setForeground(Color.WHITE);
		btnRContraseña.setFont(new Font("Arial Black", Font.PLAIN, 12));
		btnRContraseña.setBounds(584, 304, 200, 40);
		btnRContraseña.setBackground(GRIS_FONDO);
		btnRContraseña.setBorder(new LineBorder(Color.GRAY));
		panelCentral.add(btnRContraseña);

		JButton btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.addActionListener(e -> miControlador.irARegistro());
		btnRegistrarse.setFont(new Font("Arial Black", Font.PLAIN, 10));
		btnRegistrarse.setBackground(Color.LIGHT_GRAY);
		btnRegistrarse.setBounds(699, 368, 200, 30);
		panelCentral.add(btnRegistrarse);

		lblRespuesta = new JLabel("");
		lblRespuesta.setForeground(new Color(255, 0, 0));
		lblRespuesta.setBounds(641, 270, 350, 20);
		panelCentral.add(lblRespuesta);
		
		JButton btnAjustes = new JButton("Ajustes");
		btnAjustes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miControlador.abrirConfiguracionPopUp();
			}
		});
		btnAjustes.setBounds(923, 107, 84, 20);
		btnAjustes.setBackground(GRIS_FONDO);
		btnAjustes.setForeground(NARANJA);
		panelCentral.add(btnAjustes);
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

	public String getTxtUsu() {
		return txtUsu.getText();
	}

	/**
     * Captura la contraseña introducida de forma segura.
     * @return String convertido desde el array de caracteres de JPasswordField.
     */
	public String getTxtPwd() {
		return String.valueOf(txtPwd.getPassword());
	}

	/**
	 * Muestra un mensaje de error o aviso en la etiqueta debajo de las contraseñas.
	 * (Este método es el que llama ahora el Controlador para mostrar "Contraseña Incorrecta")
	 */
	public void mostrarError(String msg) {
		lblRespuesta.setText(msg);
	}

	public void limpiarMensaje() {
		lblRespuesta.setText("");
	}

	/**
     * Actualiza el estado de habilitación del botón de inicio de sesión.
     * Implementa una lógica de validación en tiempo real que comprueba que los campos
     * de usuario y contraseña no estén vacíos ni contengan únicamente espacios.
     */
	public void actualizarBoton() {
		String user = txtUsu.getText();
		String pass = String.valueOf(txtPwd.getPassword());

		if (!user.trim().isEmpty() && !pass.trim().isEmpty()) {
			btnInicio.setEnabled(true);
		} else {
			btnInicio.setEnabled(false);
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