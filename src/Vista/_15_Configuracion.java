package Vista;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import Controlador.Controlador;
import Modelo.Modelo;

public class _15_Configuracion extends JFrame implements Vista {

	private Modelo miModelo;
	private Controlador miControlador;
	private JTextField txtUsuario;
	private JTextField txtPwd;
	private JTextField txtUrl;

	public _15_Configuracion() {
		setTitle("Configuracion");
		getContentPane().setLayout(null);
		setUndecorated(false);
		setSize(450, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsuario.setBounds(53, 59, 71, 12);
		getContentPane().add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setBounds(134, 57, 205, 20);
		getContentPane().add(txtUsuario);

		JLabel lblPwd = new JLabel("Contraseña:");
		lblPwd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPwd.setBounds(53, 109, 117, 12);
		getContentPane().add(lblPwd);

		txtPwd = new JTextField();
		txtPwd.setBounds(134, 107, 205, 20);
		getContentPane().add(txtPwd);

		JLabel lblUrl = new JLabel("URL:");
		lblUrl.setBounds(53, 155, 97, 12);
		getContentPane().add(lblUrl);

		txtUrl = new JTextField();
		txtUrl.setBounds(134, 152, 205, 20);
		getContentPane().add(txtUrl);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBounds(183, 197, 97, 20);
		btnModificar.addActionListener(e -> {
			if (miControlador != null)
				miControlador.guardarConfiguracionAjustes();
		});
		getContentPane().add(btnModificar);

		JButton btnSalir = new JButton("Salir");
		btnSalir.setBounds(342, 233, 84, 20);
		btnSalir.addActionListener(e -> dispose());
		getContentPane().add(btnSalir);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				if (miControlador != null)
					miControlador.cargarConfiguracionAjustes();
			}
		});
	}

	// GETTERS Y SETTERS (Para que el Controlador inyecte y lea)
	public String getUsrConfig() {
		return txtUsuario.getText();
	}

	public String getPwdConfig() {
		return txtPwd.getText();
	}

	public String getUrlConfig() {
		return txtUrl.getText();
	}

	public void setDatosConfig(String usr, String pwd, String url) {
		txtUsuario.setText(usr);
		txtPwd.setText(pwd);
		txtUrl.setText(url);
	}

	public void mostrarMensaje(String m) {
		JOptionPane.showMessageDialog(this, m);
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