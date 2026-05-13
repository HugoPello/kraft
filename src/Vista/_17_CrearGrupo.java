package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Controlador.Controlador;
import Modelo.Modelo;

public class _17_CrearGrupo extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextArea txtDesc;
	private JCheckBox chkPrivado;
	private JPasswordField txtPass;

	private Controlador miControlador;
	private Modelo miModelo;

	public _17_CrearGrupo() {
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().setBackground(new Color(25, 25, 25));
		getContentPane().setLayout(new GridBagLayout());

		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(500, 600));
		panel.setBackground(new Color(40, 40, 40));
		panel.setBorder(new LineBorder(new Color(255, 128, 0), 2));
		getContentPane().add(panel);

		JLabel lblTitulo = new JLabel("FUNDAR NUEVO CLAN", SwingConstants.CENTER);
		lblTitulo.setForeground(new Color(255, 128, 0));
		lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 22));
		lblTitulo.setBounds(50, 40, 400, 40);
		panel.add(lblTitulo);

		JLabel lblNom = new JLabel("Nombre del Grupo:");
		lblNom.setForeground(Color.WHITE);
		lblNom.setBounds(50, 120, 200, 25);
		panel.add(lblNom);

		txtNombre = new JTextField();
		txtNombre.setBounds(50, 150, 400, 35);
		panel.add(txtNombre);

		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setBounds(50, 210, 200, 25);
		panel.add(lblDesc);

		txtDesc = new JTextArea();
		txtDesc.setLineWrap(true);
		txtDesc.setBorder(new LineBorder(Color.GRAY));
		txtDesc.setBounds(50, 240, 400, 100);
		panel.add(txtDesc);

		chkPrivado = new JCheckBox("Hacer grupo privado");
		chkPrivado.setForeground(Color.WHITE);
		chkPrivado.setOpaque(false);
		chkPrivado.setBounds(50, 360, 200, 30);
		panel.add(chkPrivado);

		JButton btnCrear = new JButton("FUNDAR");
		btnCrear.setBackground(new Color(255, 128, 0));
		btnCrear.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnCrear.setBounds(50, 450, 400, 45);
		btnCrear.addActionListener(e -> miControlador.guardarNuevoGrupo());
		panel.add(btnCrear);

		JButton btnVolver = new JButton("CANCELAR");
		btnVolver.setBackground(Color.DARK_GRAY);
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setBounds(50, 510, 400, 35);
		btnVolver.addActionListener(e -> miControlador.cambiarVentana(17, 16));
		panel.add(btnVolver);

		JLabel lblPass = new JLabel("Contraseña del Grupo:");
		lblPass.setForeground(Color.GRAY);
		lblPass.setBounds(50, 400, 200, 25);
		panel.add(lblPass);

		txtPass = new JPasswordField();
		txtPass.setBounds(50, 430, 400, 35);
		txtPass.setEnabled(false); // Apagado por defecto
		panel.add(txtPass);

		// Haz que el Checkbox active la contraseña
		chkPrivado.addActionListener(e -> {
			boolean privado = chkPrivado.isSelected();
			txtPass.setEnabled(privado);
			lblPass.setForeground(privado ? Color.WHITE : Color.GRAY);
			if (!privado)
				txtPass.setText(""); // Limpiar si desmarca
		});
	}

	public String getNombre() {
		return txtNombre.getText();
	}

	public String getDescripcion() {
		return txtDesc.getText();
	}

	public String getContrasena() {
		return new String(txtPass.getPassword());
	}

	public boolean isPrivado() {
		return chkPrivado.isSelected();
	}

	public void mostrarMensaje(String m) {
		JOptionPane.showMessageDialog(this, m);
	}

	public void limpiarFormulario() {
		txtNombre.setText("");
		txtDesc.setText("");
		chkPrivado.setSelected(false);
	}

	@Override
	public void setModelo(Modelo m) {
		this.miModelo = m;
	}

	@Override
	public void setControlador(Controlador c) {
		this.miControlador = c;
	}
}