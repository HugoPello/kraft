//@autor: Hugo/ Carlos
package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import Controlador.Controlador;
import Modelo.Modelo;

public class _01_Registro extends JFrame implements Vista {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel panelFormulario;
    
    private JButton btnCerrar, btnMin;
    private final Color NARANJA = new Color(255, 128, 0);
    private final Color GRIS_FONDO = new Color(25, 25, 25);
    private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

    private Controlador miControlador;
    private Modelo miModelo;
    private JPasswordField txtPass, passwordFieldRepetir, txtAdminPass;
    private JTextField txtNombre, txtCaptchaInput, txtRespuestaSeguridad, txtFecha;
    
    // --- AÑADIDO: Variables globales para poder usar los Getters ---
    private JComboBox<String> comboSeguridad;
    private JRadioButton rdbtnAdmin;
    private JButton btnRegistrar;

    public _01_Registro() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(1920,1080,0,0);

        contentPane = new JPanel();
        contentPane.setBackground(GRIS_FONDO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // --- PANEL SUPERIOR (SOLO BOTONES DE CONTROL) ---
        JPanel panelBotonesControl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotonesControl.setOpaque(false);
        contentPane.add(panelBotonesControl, BorderLayout.NORTH);

        btnMin = new JButton("-");
        estilizarBotonControl(btnMin, 26, Color.GRAY);
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        panelBotonesControl.add(btnMin);

        btnCerrar = new JButton("X");
        estilizarBotonControl(btnCerrar, 18, new Color(220, 40, 40));
        btnCerrar.addActionListener(e -> System.exit(0));
        panelBotonesControl.add(btnCerrar);

        // --- CONTENEDOR CENTRAL ---
        JPanel containerCentral = new JPanel(new GridBagLayout()); 
        containerCentral.setOpaque(false);
        contentPane.add(containerCentral, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        panelFormulario = new JPanel();
        panelFormulario.setOpaque(false);
        panelFormulario.setPreferredSize(new Dimension(600, 650)); 
        panelFormulario.setLayout(null); 
        
        containerCentral.add(panelFormulario, gbc);

        JLabel lblRegistro = new JLabel("REGISTRO", JLabel.CENTER);
        lblRegistro.setForeground(NARANJA);
        lblRegistro.setFont(new Font("Arial Black", Font.BOLD, 45));
        lblRegistro.setBounds(0, 0, 600, 60);
        panelFormulario.add(lblRegistro);

        // 1. NOMBRE
        JLabel lblUsuario = new JLabel("Nombre:");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        lblUsuario.setBounds(50, 80, 100, 30);
        panelFormulario.add(lblUsuario);

        txtNombre = new JTextField();
        txtNombre.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		actualizarBoton();
        	}
        });
        txtNombre.setBackground(GRIS_RECTANGULO);
        txtNombre.setForeground(new Color(255, 255, 255));
        txtNombre.setCaretColor(Color.WHITE);
        txtNombre.setBounds(200, 80, 330, 35);
        panelFormulario.add(txtNombre);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBounds(362, 176, 145, 13);
        panelFormulario.add(progressBar);

        // 2. CONTRASEÑA
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.BOLD, 16));
        lblPass.setBounds(50, 130, 120, 30);
        panelFormulario.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		actualizarBoton();
        	}
        });
        txtPass.setBackground(GRIS_RECTANGULO);
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(Color.WHITE);
        txtPass.setBounds(200, 130, 330, 35);
        txtPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Obtenemos la longitud de la contraseña actual
                int largo = txtPass.getPassword().length;

                if (largo == 0) {
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else {
                    progressBar.setVisible(true);
                    
                    
                    // Cambiamos el color de la barra dependiendo de la longitud de la contraseña
                    if (largo <= 4) {
                        progressBar.setValue(30);
                        progressBar.setForeground(Color.RED);
                    } else if (largo > 3 && largo <= 8) {
                        progressBar.setValue(60);
                        progressBar.setForeground(Color.ORANGE);
                    } else {
                        progressBar.setValue(100);
                        progressBar.setForeground(Color.GREEN);
                    }
                }
                
                // Forzamos a que el UI se actualice por si acaso
                progressBar.repaint();
            }
        });
        panelFormulario.add(txtPass);

        // 3. REPETIR CONTRASEÑA
        JLabel lblRepetir = new JLabel("Repetir:");
        lblRepetir.setForeground(Color.WHITE);
        lblRepetir.setFont(new Font("Arial", Font.BOLD, 16));
        lblRepetir.setBounds(50, 199, 150, 30);
        panelFormulario.add(lblRepetir);

        passwordFieldRepetir = new JPasswordField();
        passwordFieldRepetir.setBackground(GRIS_RECTANGULO);
        passwordFieldRepetir.setForeground(Color.WHITE);
        passwordFieldRepetir.setCaretColor(Color.WHITE);
        passwordFieldRepetir.setBounds(200, 199, 330, 35);
        panelFormulario.add(passwordFieldRepetir);

        // 4. PREGUNTA SEGURIDAD
  
        JLabel lblSeguridad = new JLabel("Pregunta:");
        lblSeguridad.setForeground(Color.WHITE);
        lblSeguridad.setFont(new Font("Arial", Font.BOLD, 16));
        lblSeguridad.setBounds(50, 249, 120, 30);
        panelFormulario.add(lblSeguridad);

        comboSeguridad = new JComboBox<String>();
        comboSeguridad.setBackground(GRIS_RECTANGULO);
        comboSeguridad.setForeground(Color.WHITE);
        comboSeguridad.setBounds(200, 249, 330, 35);
        panelFormulario.add(comboSeguridad);
        
        // 5. RESPUESTA SEGURIDAD
        JLabel lblRespuesta = new JLabel("Respuesta:");
        lblRespuesta.setForeground(Color.WHITE);
        lblRespuesta.setFont(new Font("Arial", Font.BOLD, 16));
        lblRespuesta.setBounds(50, 299, 120, 30);
        panelFormulario.add(lblRespuesta);

        txtRespuestaSeguridad = new JTextField();
        txtRespuestaSeguridad.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		actualizarBoton();
        	}
        });
        txtRespuestaSeguridad.setBackground(GRIS_RECTANGULO);
        txtRespuestaSeguridad.setForeground(new Color(255, 255, 255));
        txtRespuestaSeguridad.setCaretColor(Color.WHITE);
        txtRespuestaSeguridad.setBounds(200, 299, 330, 35);
        panelFormulario.add(txtRespuestaSeguridad);

        // 6. FECHA NACIMIENTO (Formato: YYYY-MM-DD)
        JLabel lblFecha = new JLabel("Fecha Nac:");
        lblFecha.setForeground(Color.WHITE);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 16));
        lblFecha.setBounds(50, 349, 120, 30);
        panelFormulario.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusGained(FocusEvent e) {
        		txtFecha.setText("");
        		txtFecha.setForeground(Color.white);
        	}
        });
        txtFecha.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		actualizarBoton();
        	}
        });
        txtFecha.setText("YYYY-MM-DD");
        txtFecha.setToolTipText("YYYY-MM-DD");
        txtFecha.setBackground(GRIS_RECTANGULO);
        txtFecha.setForeground(new Color(128, 128, 128));
        txtFecha.setCaretColor(Color.WHITE);
        txtFecha.setBounds(200, 349, 330, 35);
        panelFormulario.add(txtFecha);

        // 7. CAPTCHA
        JLabel lblCaptchaText = new JLabel("W6R2Q", JLabel.CENTER);
        lblCaptchaText.setOpaque(true);
        lblCaptchaText.setBackground(Color.LIGHT_GRAY);
        lblCaptchaText.setFont(new Font("Monospaced", Font.ITALIC | Font.BOLD, 20));
        lblCaptchaText.setBounds(50, 399, 120, 35);
        panelFormulario.add(lblCaptchaText);

        txtCaptchaInput = new JTextField();
        txtCaptchaInput.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent e) {
        		actualizarBoton();
        	}
        });
        txtCaptchaInput.setBackground(GRIS_RECTANGULO);
        txtCaptchaInput.setForeground(new Color(255, 255, 255));
        txtCaptchaInput.setCaretColor(Color.WHITE);
        txtCaptchaInput.setBounds(200, 399, 330, 35);
        panelFormulario.add(txtCaptchaInput);

        // 8. MODO ADMIN
        rdbtnAdmin = new JRadioButton("Habilitar Modo Administrador");
        rdbtnAdmin.setForeground(NARANJA);
        rdbtnAdmin.setBackground(GRIS_FONDO);
        rdbtnAdmin.setFocusPainted(false);
        rdbtnAdmin.setBounds(200, 449, 250, 30);
        panelFormulario.add(rdbtnAdmin);

        JLabel lblAdminPass = new JLabel("Contraseña Admin:");
        lblAdminPass.setForeground(Color.CYAN);
        lblAdminPass.setFont(new Font("Arial", Font.BOLD, 14));
        lblAdminPass.setBounds(50, 489, 150, 30);
        lblAdminPass.setVisible(false);
        panelFormulario.add(lblAdminPass);

        txtAdminPass = new JPasswordField();
        txtAdminPass.setBackground(GRIS_RECTANGULO);
        txtAdminPass.setForeground(Color.WHITE);
        txtAdminPass.setCaretColor(Color.WHITE);
        txtAdminPass.setBounds(200, 489, 330, 35);
        txtAdminPass.setVisible(false);
        panelFormulario.add(txtAdminPass);

        rdbtnAdmin.addActionListener(e -> {
            boolean visible = rdbtnAdmin.isSelected();
            lblAdminPass.setVisible(visible);
            txtAdminPass.setVisible(visible);
            panelFormulario.repaint();
        });

        // 9. BOTONES ACCIÓN
        JButton btnLogin = new JButton("VOLVER AL LOGIN");
        btnLogin.addActionListener(e -> {
        	if(miControlador != null) miControlador.cambiarVentana(1, 0);
        });
        estilizarBotonAccion(btnLogin, GRIS_RECTANGULO, Color.WHITE);
        btnLogin.setBounds(100, 550, 180, 45);
        panelFormulario.add(btnLogin);

        btnRegistrar = new JButton("REGISTRAR");
        btnRegistrar.setEnabled(false);
        btnRegistrar.addActionListener(e -> {
        	if(miControlador != null) miControlador.registrarUsuario();
        });
        estilizarBotonAccion(btnRegistrar, NARANJA, Color.BLACK);
        btnRegistrar.setBounds(320, 550, 180, 45);
        panelFormulario.add(btnRegistrar);
        
        
        JLabel lblSeguridadPass = new JLabel("Seguridad:");
        lblSeguridadPass.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblSeguridadPass.setForeground(new Color(255, 255, 255));
        lblSeguridadPass.setBounds(282, 175, 130, 14);
        panelFormulario.add(lblSeguridadPass);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                if (miControlador != null) {
                    try {
                        String[] preguntasBD = miControlador.getPreguntasSeguridad();
                        
                        comboSeguridad.removeAllItems();
                        for (String pregunta : preguntasBD) {
                            comboSeguridad.addItem(pregunta);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error al cargar preguntas de seguridad: " + ex.getMessage());
                    }
                }
            }
        });
        
    }

    private void estilizarBotonControl(JButton btn, int tamanoFuente, Color colorHover) {
        btn.setPreferredSize(new Dimension(45, 40));
        btn.setBackground(GRIS_FONDO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, tamanoFuente));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setOpaque(true); btn.setBackground(colorHover); }
            @Override public void mouseExited(MouseEvent e) { btn.setOpaque(false); btn.setBackground(GRIS_FONDO); }
        });
    }

    private void estilizarBotonAccion(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setBorder(new LineBorder(fondo.darker()));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    

   
	public int getIndicePregunta() {
		return comboSeguridad.getSelectedIndex() + 1; 
	} // +1 para que coincida con IDs SQL 1, 2, 3
	
	public String getTxtRespuesta() { 
		return txtRespuestaSeguridad.getText(); 
	
	}
	public String getTxtFechaNac() { 
		return txtFecha.getText(); 
	}
	
	public boolean isModoAdmin() { 
		return rdbtnAdmin.isSelected();
	}
	
    public String getTxtPass() {
    	return String.valueOf(txtPass.getPassword());
	}

	public String getPasswordFieldRepetir() {
		return String.valueOf(passwordFieldRepetir.getPassword());
	}

	public String getTxtNombre() {
		return txtNombre.getText();
	}
	
	public String getPassAdmin() {
		return String.valueOf(txtAdminPass.getPassword());
	}

	// Método para mostrar errores/éxitos
	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Información de Registro", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override 
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}
    @Override 
    public void setControlador(Controlador miControlador) { 
    	this.miControlador = miControlador; 
    }
    
    public void actualizarBoton() {
		String user = txtNombre.getText();
		String pass = String.valueOf(txtPass.getPassword());
		String respuesta = txtRespuestaSeguridad.getText();
		String fecha = txtFecha.getText();
		String captcha = txtCaptchaInput.getText();

		if (!user.trim().isEmpty() && !pass.trim().isEmpty() && !respuesta.trim().isEmpty() 
				&& !fecha.trim().isEmpty() && !captcha.trim().isEmpty()) {
			btnRegistrar.setEnabled(true);
		} else if (user.isEmpty() || pass.isEmpty() || respuesta.isEmpty() || fecha.isEmpty() || captcha.isEmpty()) {
			btnRegistrar.setEnabled(false);
		} else {
			btnRegistrar.setEnabled(false);
		}
	}
}