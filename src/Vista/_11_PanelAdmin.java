// @Autor: Hugo / Carloss
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _11_PanelAdmin extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCentral;

	// Botones del Header
	private JButton btnCerrar, btnMin;
	private JButton btnHome, btnPerfil, btnActividades, btnAjustes, btnTrofeo, btnCarrito;

	// Colores de la app
	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private JTextField txtUsrPts;
	private JTextField txtUsrLogros;
	private JTextField textField_1;

	private Controlador miControlador;
	private Modelo miModelo;
	private JTable tablaUsuarios;
	private JTable tablaGestion;
	private JTextField txtUsuario;
	private JTextField txtPassword;
	private JButton btnAlta;
	private JButton btnModificar;
	private JButton btnBaja;
	private DefaultTableModel modelo;
	private JTable tableNoticias;
	private JTextField txtTitNoticia;
	private JComboBox cbxDarLogro;
	private JButton btnDarLogro;
	private JButton btnQtrLogro;

	public _11_PanelAdmin() {
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

		// --- 2. CABECERA (HEADER) UNIFICADA ---
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

		JPanel fotoPerfil = new JPanel();
		fotoPerfil.setBackground(Color.DARK_GRAY);
		fotoPerfil.setPreferredSize(new Dimension(40, 40));
		headerLeft.add(fotoPerfil);

		// Header Derecha (Navegación y Control)
		JPanel headerRight = new JPanel();
		headerRight.setOpaque(false);
		headerRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		header.add(headerRight, BorderLayout.EAST);

		// Instanciamos botones con el estilo de Carlos
		btnHome = crearBotonIcono("🏠");
		btnHome.addActionListener(e -> miControlador.cambiarVentana(11, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(11, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(11, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(11, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(11, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(11, 11));

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

		// Botones de control
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
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(null);
		panelCentral.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tablaGestion.getSelectionModel().clearSelection();
				limpiarCampos();
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(176, 162, 200, 185);
		panelCentral.add(scrollPane);
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tablaGestion.getSelectionModel().clearSelection();
				limpiarCampos();
			}
		});

		tablaUsuarios = new JTable();
		tablaUsuarios.setModel(new DefaultTableModel(
				new Object[][] {  },
				new String[] { "Lista de Usuarios" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Devuelve false para que ninguna celda sea editable
			}
		});
		tablaUsuarios.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        int fila = tablaUsuarios.getSelectedRow();
		        if (fila != -1) {
		            String seleccionado = tablaUsuarios.getValueAt(fila, 0).toString();
		            
		            txtUsrPts.setText(seleccionado);
		            txtUsrLogros.setText(seleccionado);
		            
		            actualizarVistaPuntos(seleccionado);
		            
		            // ESTA ES LA CLAVE:
		            filtrarLogrosDisponibles(seleccionado);
		        }
		    }
		});
		tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tablaUsuarios);
		tablaUsuarios.setBackground(GRIS_FONDO);
		tablaUsuarios.setForeground(NARANJA);

		// Paneles del TabbedPane (Puntos y Logros)
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(GRIS_RECTANGULO);
		tabbedPane.setForeground(NARANJA);
		tabbedPane.setBounds(468, 163, 579, 388);
		panelCentral.add(tabbedPane);

		JPanel panelGestion = new JPanel();
		tabbedPane.addTab("Gestión Usuarios", null, panelGestion, null);
		panelGestion.setBackground(GRIS_RECTANGULO);
		panelGestion.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(116, 70, 361, 144);
		panelGestion.add(scrollPane_1);
		scrollPane_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				tablaGestion.getSelectionModel().clearSelection();
				limpiarCampos();
			}
		});

		tablaGestion = new JTable();
		tablaGestion.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int fila = tablaGestion.getSelectedRow();
				txtUsuario.setText((String) tablaGestion.getValueAt(fila, 0));
				txtPassword.setText((String) tablaGestion.getValueAt(fila, 1));
				updateBaja();
				updateModificar();
			}
		});
		modelo = new DefaultTableModel(new Object[][] {}, new String[] { "Nombre", "Contrase\u00F1a" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Devuelve false para que ninguna celda sea editable
			}
		};
		tablaGestion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaGestion.setModel(modelo);
		scrollPane_1.setViewportView(tablaGestion);

		JLabel lblUsuarios = new JLabel("Usuarios");
		lblUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lblUsuarios.setBounds(188, 10, 272, 44);
		lblUsuarios.setForeground(NARANJA);
		panelGestion.add(lblUsuarios);

		txtUsuario = new JTextField();
		txtUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateAlta();
				updateModificar();
			}
		});
		txtUsuario.setBounds(116, 252, 164, 29);
		panelGestion.add(txtUsuario);
		txtUsuario.setColumns(10);

		txtPassword = new JTextField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateAlta();
				updateModificar();
			}
		});
		txtPassword.setColumns(10);
		txtPassword.setBounds(313, 252, 164, 29);
		panelGestion.add(txtPassword);

		btnBaja = new JButton("Baja");
		btnBaja.setEnabled(false);
		btnBaja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int filaSeleccionada = tablaGestion.getSelectedRow();

				if (filaSeleccionada != -1) { // Comprobamos que haya algo seleccionado
					// Obtenemos datos de la tabla
					DefaultTableModel modeloTabla = (DefaultTableModel) tablaGestion.getModel();
					String usr = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
					String pwd = modeloTabla.getValueAt(filaSeleccionada, 1).toString();

					// Llamar al controlador para borrar en la BD
					int filasBorradas = miControlador.borrarUsuario(usr, pwd);

					if (filasBorradas > 0) {
						// Si se borró en la BD, lo quitamos de la vista
						modeloTabla.removeRow(filaSeleccionada);
						limpiarCampos();
						// Ventana Emergente si todo sale correctamente
						JOptionPane.showMessageDialog(null, "El usuario '" + usr + "' ha sido eliminado correctamente.",
								"Usuario Eliminado", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// Ventana Emergente si da error
						JOptionPane.showMessageDialog(null, "No se pudo eliminar el usuario de la base de datos.",
								"Error de Eliminación", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		});
		btnBaja.setBounds(116, 302, 104, 29);
		btnBaja.setBackground(GRIS_FONDO);
		btnBaja.setForeground(NARANJA);
		panelGestion.add(btnBaja);

		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int fila = tablaGestion.getSelectedRow();
		        
		        if (fila != -1) {
		            // Obtenemos el nombre del usuario de la tabla
		            // y la nueva contraseña de los campos de texto
		            String usr = tablaGestion.getValueAt(fila, 0).toString();
		            String nuevaPwd = txtPassword.getText();

		            // Ejecutamos la modificación en la BD
		            int exito = miControlador.modificarUsuario(usr, nuevaPwd);

		            if (exito > 0) {
		                // Si la BD se actualizó, actualizamos la tabla visual
		                modelo.setValueAt(txtUsuario.getText(), fila, 0);
		                modelo.setValueAt(nuevaPwd, fila, 1);
		                
		                // Limpiamos y avisamos
		                tablaGestion.getSelectionModel().clearSelection();
		                limpiarCampos();
		                JOptionPane.showMessageDialog(null, "Datos actualizados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		            } else {
		                JOptionPane.showMessageDialog(null, "No se pudo actualizar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    }
		});
		btnModificar.setBounds(249, 302, 104, 29);
		btnModificar.setBackground(GRIS_FONDO);
		btnModificar.setForeground(NARANJA);
		panelGestion.add(btnModificar);

		btnAlta = new JButton("Alta");
		btnAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Recogemos los datos de los campos de texto
				String usr = txtUsuario.getText().trim();
				String pwd = txtPassword.getText().trim();

				// Intentamos guardar en la Base de Datos a través del Controlador
				// checkFunciono recoge el valor que nos da el Modelo en insertarUsuarioAdmin, si es 0 = false, 1 = true
				boolean checkFunciono = miControlador.altaUsuario(usr, pwd);

				if (checkFunciono) {
					// Si la BD acepta el insert, lo añadimos a la tabla
					modelo.addRow(new String[] { usr, pwd });

					// Limpiamos los campos y avisamos al usuario
					limpiarCampos();
					JOptionPane.showMessageDialog(null, "Usuario '" + usr + "' dado de alta correctamente.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// Si falla, por ejemplo, usuario duplicado, avisamos del error
					JOptionPane.showMessageDialog(null, "Error: No se pudo añadir el usuario. Comprueba si ya existe.", "Error de Alta", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAlta.setEnabled(false);
		btnAlta.setBounds(373, 302, 104, 29);
		btnAlta.setBackground(GRIS_FONDO);
		btnAlta.setForeground(NARANJA);
		panelGestion.add(btnAlta);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(116, 224, 136, 20);
		panelGestion.add(lblUsuario);
		lblUsuario.setForeground(NARANJA);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(313, 222, 136, 20);
		panelGestion.add(lblPassword);
		lblPassword.setForeground(NARANJA);

		JPanel panelPts = new JPanel();
		panelPts.setBackground(GRIS_RECTANGULO);
		tabbedPane.addTab("Puntos", null, panelPts, null);
		panelPts.setLayout(null);

		JLabel lblNomUsr = new JLabel("Nombre Usuario");
		lblNomUsr.setForeground(NARANJA);
		lblNomUsr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomUsr.setBounds(93, 58, 113, 20);
		panelPts.add(lblNomUsr);

		txtUsrPts = new JTextField();
		txtUsrPts.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtUsrPts.setBounds(93, 88, 125, 20);
		panelPts.add(txtUsrPts);
		txtUsrPts.setColumns(10);

		JLabel lblAgrPts = new JLabel("Puntos a añadir");
		lblAgrPts.setForeground(NARANJA);
		lblAgrPts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAgrPts.setBounds(93, 117, 113, 29);
		panelPts.add(lblAgrPts);
		

		JSpinner spnPtsAñadir = new JSpinner();
		spnPtsAñadir.setBounds(94, 156, 124, 20);
		panelPts.add(spnPtsAñadir);

		JButton btnAgrPts = new JButton("Añadir puntos");
		btnAgrPts.setBackground(GRIS_FONDO);
		btnAgrPts.setForeground(NARANJA);
		btnAgrPts.setBounds(285, 229, 113, 36);
		panelPts.add(btnAgrPts);
		btnAgrPts.addActionListener(e -> {
		    String usr = txtUsrPts.getText().trim();
		    int pts = (int) spnPtsAñadir.getValue();
		    
		    if(!usr.isEmpty() && pts > 0) {
		        if(miControlador.gestionarPuntos(usr, pts)) {
		            JOptionPane.showMessageDialog(null, "¡" + pts + " puntos añadidos a " + usr + "!");
		            llenarTabla(); // Refrescamos las tablas
		        } else {
		            JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		JLabel lblPtsUsr = new JLabel("Puntos del Usuario");
		lblPtsUsr.setForeground(NARANJA);
		lblPtsUsr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPtsUsr.setBounds(273, 59, 149, 18);
		panelPts.add(lblPtsUsr);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setText("120");
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_1.setBounds(273, 87, 125, 18);
		panelPts.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnQtrPts_1 = new JButton("Quitar puntos");
		btnQtrPts_1.setForeground(new Color(255, 128, 0));
		btnQtrPts_1.setBackground(new Color(25, 25, 25));
		btnQtrPts_1.setBounds(105, 229, 113, 36);
		panelPts.add(btnQtrPts_1);
		btnQtrPts_1.addActionListener(e -> {
		    String usr = txtUsrPts.getText().trim();
		    int pts = (int) spnPtsAñadir.getValue();
		    
		    if(!usr.isEmpty() && pts > 0) {
		        // Pasamos los puntos en negativo para restar
		        if(miControlador.gestionarPuntos(usr, -pts)) {
		            JOptionPane.showMessageDialog(null, "Se han detraído " + pts + " puntos a " + usr);
		            llenarTabla();
		        }
		    }
		});

		JPanel panelLogro = new JPanel();
		panelLogro.setBackground(GRIS_RECTANGULO);
		tabbedPane.addTab("Logros", null, panelLogro, null);
		panelLogro.setLayout(null);

		JLabel lblNomUsrLogro = new JLabel("Nombre Usuario");
		lblNomUsrLogro.setForeground(NARANJA);
		lblNomUsrLogro.setBounds(93, 75, 113, 20);
		lblNomUsrLogro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelLogro.add(lblNomUsrLogro);

		txtUsrLogros = new JTextField();
		txtUsrLogros.setBounds(93, 111, 125, 20);
		panelLogro.add(txtUsrLogros);
		txtUsrLogros.setColumns(10);

		JLabel lblLogro = new JLabel("Logro");
		lblLogro.setForeground(NARANJA);
		lblLogro.setBounds(318, 72, 113, 29);
		lblLogro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelLogro.add(lblLogro);

		cbxDarLogro = new JComboBox();
		cbxDarLogro.setModel(new DefaultComboBoxModel(new String[] {}));
		cbxDarLogro.setBounds(318, 111, 189, 20);
		panelLogro.add(cbxDarLogro);

		btnDarLogro = new JButton("Dar Logro");
		btnDarLogro.setBackground(GRIS_FONDO);
		btnDarLogro.setForeground(NARANJA);
		btnDarLogro.setBounds(329, 229, 113, 36);
		panelLogro.add(btnDarLogro);
		btnDarLogro.addActionListener(e -> {
		    String usr = txtUsrLogros.getText().trim();
		    String logro = cbxDarLogro.getSelectedItem().toString();
		    
		    if(!usr.isEmpty()) {
		        boolean ok = miControlador.darLogro(usr, logro);
		        if(ok) {
		            JOptionPane.showMessageDialog(null, "Logro '" + logro + "' asignado a " + usr);
		        } else {
		            JOptionPane.showMessageDialog(null, "El usuario ya tiene este logro o no existe.", "Aviso", JOptionPane.WARNING_MESSAGE);
		        }
		    }
		});
		
		btnQtrLogro = new JButton("Quitar Logro");
		btnQtrLogro.setForeground(new Color(255, 128, 0));
		btnQtrLogro.setBackground(new Color(25, 25, 25));
		btnQtrLogro.setBounds(105, 229, 113, 36);
		panelLogro.add(btnQtrLogro);
		btnQtrLogro.addActionListener(e -> {
		    String usr = txtUsrLogros.getText().trim();
		    if (usr.isEmpty()) return;

		    // 1. Recuperamos los logros que SÍ tiene el usuario
		    ArrayList<String> poseidos = miControlador.getLogrosUsuario(usr);

		    if (poseidos.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Este usuario no tiene logros para quitar.");
		        return;
		    }

		    // 2. Mostramos un selector rápido (Input para elegir cuál quitar)
		    String[] opciones = poseidos.toArray(new String[0]);
		    String seleccionado = (String) JOptionPane.showInputDialog(null, 
		            "Selecciona el logro a eliminar para " + usr,
		            "Quitar Logro", 
		            JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

		    // 3. Si elige uno, lo borramos
		    if (seleccionado != null) {
		        if (miControlador.quitarLogro(usr, seleccionado)) {
		            JOptionPane.showMessageDialog(null, "Logro eliminado correctamente.");
		            // 4. IMPORTANTE: Refrescamos el ComboBox de "Dar" porque ahora este logro vuelve a estar disponible
		            filtrarLogrosDisponibles(usr);
		        }
		    }
		});
		
		JPanel panelNoticias = new JPanel();
		tabbedPane.addTab("Noticias", null, panelNoticias, null);
		panelNoticias.setBackground(GRIS_RECTANGULO);
		panelNoticias.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(51, 33, 220, 176);
		panelNoticias.add(scrollPane_2);
		
		tableNoticias = new JTable();
		tableNoticias.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Noticia", "Tipo"
			}
		));
		tableNoticias.getColumnModel().getColumn(0).setPreferredWidth(180);
		tableNoticias.getColumnModel().getColumn(1).setPreferredWidth(80);
		scrollPane_2.setViewportView(tableNoticias);
		
		JLabel lblTitNoticia = new JLabel("Título Noticia");
		lblTitNoticia.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTitNoticia.setBounds(306, 36, 152, 20);
		panelNoticias.add(lblTitNoticia);
		lblTitNoticia.setForeground(NARANJA);
		
		txtTitNoticia = new JTextField();
		txtTitNoticia.setBounds(306, 58, 191, 20);
		panelNoticias.add(txtTitNoticia);
		txtTitNoticia.setColumns(10);
		
		JLabel lblTipoNoticia = new JLabel("Tipo");
		lblTipoNoticia.setForeground(new Color(255, 128, 0));
		lblTipoNoticia.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTipoNoticia.setBounds(306, 88, 152, 20);
		panelNoticias.add(lblTipoNoticia);
		
		JComboBox cbxTipoNoticia = new JComboBox();
		cbxTipoNoticia.setModel(new DefaultComboBoxModel(new String[] {"Evento", "Aviso", "Novedad", "Info"}));
		cbxTipoNoticia.setBounds(306, 118, 191, 20);
		panelNoticias.add(cbxTipoNoticia);
		
		JButton btnAñadirNoticia = new JButton("Añadir Noticia");
		btnAñadirNoticia.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        String titulo = txtTitNoticia.getText().trim();
		        String tipo = cbxTipoNoticia.getSelectedItem().toString();

		        if (!titulo.isEmpty()) {
		            boolean exito = miControlador.añadirNoticia(titulo, tipo);
		            if (exito) {
		                // El controlador ya llama a actualizarTodasLasTablas(), así que la tabla se refresca sola
		                txtTitNoticia.setText("");
		                txtTitNoticia.requestFocus(); // Devuelve el foco al cuadro de texto
		                JOptionPane.showMessageDialog(null, "Noticia añadida correctamente.");
		            } else {
		                JOptionPane.showMessageDialog(null, "Error al añadir la noticia.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "El título no puede estar vacío.");
		        }
		    }
		});
		btnAñadirNoticia.setBounds(367, 243, 130, 42);
		panelNoticias.add(btnAñadirNoticia);
		btnAñadirNoticia.setBackground(GRIS_FONDO);
		btnAñadirNoticia.setForeground(NARANJA);
		
		JButton btnQuitarNoticia = new JButton("Quitar Noticia");
		btnQuitarNoticia.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int fila = tableNoticias.getSelectedRow();
		        
		        if (fila != -1) {
		            String titulo = tableNoticias.getValueAt(fila, 0).toString();
		            
		            int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar noticia: " + titulo + "?");
		            if (confirm == JOptionPane.YES_OPTION) {
		                // El controlador borrará en la BD Y llamará a actualizarTodasLasTablas()
		                boolean eliminado = miControlador.eliminarNoticia(titulo);
		                
		                if (eliminado) {
		                    JOptionPane.showMessageDialog(null, "Noticia eliminada correctamente.");
		                } else {
		                    JOptionPane.showMessageDialog(null, "Error al eliminar de la base de datos.");
		                }
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Selecciona una noticia para eliminar.");
		        }
		    }
		});
		btnQuitarNoticia.setBounds(141, 243, 130, 42);
		panelNoticias.add(btnQuitarNoticia);
		btnQuitarNoticia.setBackground(GRIS_FONDO);
		btnQuitarNoticia.setForeground(NARANJA);

		// Botones auxiliares inferiores
		JButton btnIrCrearEvento = new JButton("Ir a Crear Misión");
		btnIrCrearEvento.addActionListener(e -> miControlador.cambiarVentana(11, 10));
		btnIrCrearEvento.setBackground(GRIS_FONDO);
		btnIrCrearEvento.setForeground(NARANJA);
		btnIrCrearEvento.setBounds(667, 580, 165, 34);
		panelCentral.add(btnIrCrearEvento);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowOpened(java.awt.event.WindowEvent ce) {
	            // Solo cargamos si el controlador ya ha sido asignado por el Main
	            if (miControlador != null) {
	                llenarTabla();
	            }
	        }
	    });

	}

	// --- MÉTODOS DE SOPORTE PARA EL HEADER (ESTILO CARLOS) ---

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

	public String getTxtUsuario() {
		return txtUsuario.getText();
	}

	public String getTxtPassword() {
		return txtPassword.getText();
	}

	private JPanel crearPanelTab() {
		JPanel p = new JPanel();
		p.setBackground(GRIS_FONDO);
		p.setLayout(null);
		return p;
	}

	public void llenarTabla() {
	    if (miControlador == null) return;

	    try {
	        // --- SECCIÓN USUARIOS ---
	        ArrayList<Object[]> datosUsr = miControlador.getDatosUsuarios();
	        DefaultTableModel modeloGestionUsrs = (DefaultTableModel) tablaGestion.getModel();
	        DefaultTableModel modeloListaUsrs = (DefaultTableModel) tablaUsuarios.getModel();

	        modeloGestionUsrs.setRowCount(0);
	        modeloListaUsrs.setRowCount(0);

	        for (Object[] fila : datosUsr) {
	            modeloGestionUsrs.addRow(fila);
	            modeloListaUsrs.addRow(new Object[] { fila[0] });
	        }

	        // --- SECCIÓN NOTICIAS ---
	        ArrayList<Object[]> datosNoticias = miControlador.getNoticiasAdmin(); 
	        DefaultTableModel modeloNoticiasAdmin = (DefaultTableModel) tableNoticias.getModel();
	        
	        modeloNoticiasAdmin.setRowCount(0);
	        for (Object[] noticia : datosNoticias) {
	            modeloNoticiasAdmin.addRow(noticia);
	        }
	    } catch (Exception ex) {
	        System.err.println("Error al llenar tablas del Panel Admin: " + ex.getMessage());
	    }
	}

	public void updateBaja() {
		if (tablaGestion.getSelectedRow() == -1) {
			btnBaja.setEnabled(false);
		} else
			btnBaja.setEnabled(true);

	}

	public void updateModificar() {
		if (txtUsuario.getText().length() == 0 || txtPassword.getText().length() == 0
				|| tablaGestion.getSelectedRow() == -1) {
			btnModificar.setEnabled(false);
		} else
			btnModificar.setEnabled(true);
	}

	public void updateAlta() {
		if (txtUsuario.getText().length() == 0 || txtPassword.getText().length() == 0) {
			btnAlta.setEnabled(false);
		} else {
			btnAlta.setEnabled(true);
		}
	}

	private void limpiarCampos() {
		txtUsuario.setText(null);
		txtPassword.setText(null);
		updateAlta();
		updateBaja();
		updateModificar();
	}
	
	private void actualizarVistaPuntos(String usuario) {
	    if (miControlador != null) {
	        int puntosActuales = miControlador.getPuntosUsuario(usuario);
	        textField_1.setText(String.valueOf(puntosActuales));
	    }
	}
	
	private void actualizarVistaLogros(String usuario) {
	    ArrayList<String> logros = miControlador.getLogrosUsuario(usuario);
	    
	    if (logros.isEmpty()) {
	        System.out.println("El usuario " + usuario + " no tiene logros aún.");
	    } else {
	        // Ejemplo: Podrías imprimirlo en un JLabel de estado o simplemente tenerlo listo
	        // para que el botón "Dar Logro" sepa si ya existe.
	        String lista = String.join(", ", logros);
	        System.out.println("Logros de " + usuario + ": " + lista);
	    }
	}
	
	private void filtrarLogrosDisponibles(String usuario) {
	    // 1. Obtenemos todos los logros del sistema
	    ArrayList<String> todos = miControlador.getTodosLosLogros(); // Tienes que crear este puente en el controlador
	    
	    // 2. Obtenemos los que ya tiene el usuario seleccionado
	    ArrayList<String> poseidos = miControlador.getLogrosUsuario(usuario);
	    
	    // 3. Creamos un nuevo modelo para el ComboBox
	    DefaultComboBoxModel<String> nuevoModelo = new DefaultComboBoxModel<>();
	    
	    for (String logro : todos) {
	        if (!poseidos.contains(logro)) {
	            nuevoModelo.addElement(logro);
	        }
	    }
	    
	    // 4. Si no le quedan logros por ganar, avisamos
	    if (nuevoModelo.getSize() == 0) {
	        nuevoModelo.addElement("Todos los logros obtenidos");
	        btnDarLogro.setEnabled(false);
	    } else {
	        btnDarLogro.setEnabled(true);
	    }
	    
	    cbxDarLogro.setModel(nuevoModelo);
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