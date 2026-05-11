// @Author: Hugo
package Vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Controlador.Controlador;
import Modelo.Modelo;

public class _18_VerificarOTP extends JFrame implements Vista {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCodigo;
    private JButton btnVerificar, btnVolver;
    private Controlador miControlador;
    
    private final Color NARANJA = new Color(255, 128, 0);
    private final Color GRIS_FONDO = new Color(25, 25, 25);
    private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

    public _18_VerificarOTP() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(1920,1080,0,0);
        
        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(GRIS_FONDO);
        setContentPane(contentPane);

        JPanel panelCentral = new JPanel();
        panelCentral.setPreferredSize(new Dimension(500, 400));
        panelCentral.setBackground(GRIS_RECTANGULO);
        panelCentral.setLayout(null);
        panelCentral.setBorder(new LineBorder(NARANJA, 2));
        contentPane.add(panelCentral);

        JLabel lblTitulo = new JLabel("VERIFICACIÓN 2FA", JLabel.CENTER);
        lblTitulo.setForeground(NARANJA);
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblTitulo.setBounds(0, 30, 500, 40);
        panelCentral.add(lblTitulo);

        JLabel lblInfo = new JLabel("<html><center>Introduce el código de 6 caracteres<br>enviado a tu correo electrónico</center></html>", JLabel.CENTER);
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInfo.setBounds(50, 90, 400, 40);
        panelCentral.add(lblInfo);

        txtCodigo = new JTextField();
        txtCodigo.setHorizontalAlignment(JTextField.CENTER);
        txtCodigo.setFont(new Font("Monospaced", Font.BOLD, 40));
        txtCodigo.setBackground(GRIS_FONDO);
        txtCodigo.setForeground(Color.WHITE);
        txtCodigo.setCaretColor(Color.WHITE);
        txtCodigo.setBorder(new LineBorder(Color.GRAY));
        txtCodigo.setBounds(100, 160, 300, 60);
        panelCentral.add(txtCodigo);

        btnVerificar = new JButton("VERIFICAR CUENTA");
        estilizarBoton(btnVerificar, NARANJA, Color.BLACK);
        btnVerificar.setBounds(100, 250, 300, 45);
        btnVerificar.addActionListener(e -> {
            if (miControlador != null) {
                miControlador.validarCodigoOTP(txtCodigo.getText()); // Llamada al controlador
            }
        });
        panelCentral.add(btnVerificar);

        btnVolver = new JButton("VOLVER AL LOGIN");
        estilizarBoton(btnVolver, Color.DARK_GRAY, Color.WHITE);
        btnVolver.setBounds(100, 310, 300, 40);
        btnVolver.addActionListener(e -> miControlador.cambiarVentana(18, 0));
        panelCentral.add(btnVolver);
    }

    private void estilizarBoton(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(null);
    }

    @Override
    public void setModelo(Modelo miModelo) {}

    @Override
    public void setControlador(Controlador miControlador) {
        this.miControlador = miControlador;
    }
}