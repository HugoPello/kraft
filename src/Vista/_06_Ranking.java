//@autor: Ivan / Carloss
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _06_Ranking extends JFrame implements Vista {

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

	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JLabel lblRankingGrupal;
	private JComboBox<String> cbxGrupos;

	private Controlador miControlador;
	private Modelo miModelo;
	private JLabel lblTuPuestoEs_Izq;
	private JLabel lblTuPuestoEs;
	private JLabel lblTuPuestoEs_Der;

	public _06_Ranking() {
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
		btnHome.addActionListener(e -> miControlador.cambiarVentana(6, 2));

		btnPerfil = crearBotonIcono("👤");
		btnPerfil.addActionListener(e -> miControlador.cambiarVentana(6, 8));

		btnActividades = crearBotonIcono("🏃");
		btnActividades.addActionListener(e -> miControlador.cambiarVentana(6, 3));

		btnCarrito = crearBotonIcono("🛒");
		btnCarrito.addActionListener(e -> miControlador.cambiarVentana(6, 7));

		btnTrofeo = crearBotonIcono("🏆");
		btnTrofeo.addActionListener(e -> miControlador.cambiarVentana(6, 6));

		btnAjustes = crearBotonIcono("⚙");
		btnAjustes.addActionListener(e -> miControlador.cambiarVentana(6, 11));

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

		// --- 3. ÁREA DE TRABAJO (CONTENIDO) ---
		panelCentral = new JPanel();
		panelCentral.setBackground(GRIS_FONDO);
		panelCentral.setLayout(null); 
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JLabel lblRanking = new JLabel("RANKING", JLabel.CENTER);
		lblRanking.setForeground(NARANJA);
		lblRanking.setFont(new Font("Arial Black", Font.BOLD, 45));
		lblRanking.setBounds(562, 20, 400, 60);
		panelCentral.add(lblRanking);

		// --- TABLA RANKING SEMANAL (CENTRO) ---
		JLabel lblRanking2 = new JLabel("RANKING SEMANAL");
		lblRanking2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRanking2.setForeground(NARANJA);
		lblRanking2.setBounds(691, 100, 165, 26);
		panelCentral.add(lblRanking2);

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setEnabled(false);
		scrollPane2.setBounds(618, 140, 290, 428);
		panelCentral.add(scrollPane2);
		table_2 = new JTable(crearModeloEjemplo());
		scrollPane2.setViewportView(table_2);
		table_2.setBackground(GRIS_RECTANGULO);
		table_2.setForeground(NARANJA);

		lblTuPuestoEs = new JLabel("Tu Puesto es: 50");
		lblTuPuestoEs.setForeground(Color.WHITE);
		lblTuPuestoEs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTuPuestoEs.setBounds(682, 580, 146, 35);
		panelCentral.add(lblTuPuestoEs);

		// --- TABLA RANKING MUNDIAL (IZQUIERDA) ---
		JLabel lblRanking1 = new JLabel("RANKING MUNDIAL");
		lblRanking1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRanking1.setForeground(NARANJA);
		lblRanking1.setBounds(244, 100, 165, 26);
		panelCentral.add(lblRanking1);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setEnabled(false);
		scrollPane1.setBounds(170, 140, 290, 428);
		panelCentral.add(scrollPane1);
		table = new JTable(crearModeloEjemplo());
		scrollPane1.setViewportView(table);
		table.setBackground(GRIS_RECTANGULO);
		table.setForeground(NARANJA);
		
		lblTuPuestoEs_Izq = new JLabel("Tu Puesto es: 1500");
		lblTuPuestoEs_Izq.setForeground(Color.WHITE);
		lblTuPuestoEs_Izq.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTuPuestoEs_Izq.setBounds(235, 580, 146, 35);
		panelCentral.add(lblTuPuestoEs_Izq);

		// --- TABLA RANKING GRUPAL (DERECHA) ---
		lblRankingGrupal = new JLabel("RANKING GRUPAL");
		lblRankingGrupal.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRankingGrupal.setForeground(NARANJA);
		lblRankingGrupal.setBounds(1032, 100, 150, 26);
		panelCentral.add(lblRankingGrupal);

		// Creamos el desplegable vacío (se llenará al cargar la ventana)
		cbxGrupos = new JComboBox<String>();
		cbxGrupos.setBounds(1170, 100, 150, 26);
		cbxGrupos.setFont(new Font("Arial", Font.PLAIN, 12));
		cbxGrupos.setBackground(GRIS_RECTANGULO);
		cbxGrupos.setForeground(Color.WHITE);
		panelCentral.add(cbxGrupos);

		JScrollPane scrollPane3 = new JScrollPane();
		scrollPane3.setEnabled(false);
		scrollPane3.setBounds(1032, 140, 290, 428);
		panelCentral.add(scrollPane3);
		table_1 = new JTable(new DefaultTableModel(new Object[][] {}, new String[] { "USUARIO", "PUNTOS" }));
		scrollPane3.setViewportView(table_1);
		table_1.setBackground(GRIS_RECTANGULO);
		table_1.setForeground(NARANJA);

		lblTuPuestoEs_Der = new JLabel("Tu Puesto es: 10");
		lblTuPuestoEs_Der.setForeground(Color.WHITE);
		lblTuPuestoEs_Der.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTuPuestoEs_Der.setBounds(1123, 580, 146, 35);
		panelCentral.add(lblTuPuestoEs_Der);
		
		
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
	        @Override
	        public void componentShown(java.awt.event.ComponentEvent ce) {
	            if (miControlador != null) {
	                try {
	                    String[] columnas = { "USUARIO", "PUNTOS" };
	                    String user = miControlador.getUsuarioLogueado();

	                    // 1. Tabla Mundial 
	                    Object[][] datosMundial = miControlador.getRankingMundial();
	                    table.setModel(new javax.swing.table.DefaultTableModel(datosMundial, columnas));
	                    
	                    // 2. Tabla Semanal 
	                    Object[][] datosSemanal = miControlador.getRankingSemanal();
	                    table_2.setModel(new javax.swing.table.DefaultTableModel(datosSemanal, columnas));
	                    
	                    // 3. Desplegable de Grupos
	                    String[] misGrupos = miControlador.getMisGrupos();
	                    cbxGrupos.setModel(new javax.swing.DefaultComboBoxModel<>(misGrupos));
	                    
	                    // Actualizar Label Mundial
	                    int pMundial = miControlador.getMiPuestoMundial(user);
	                    lblTuPuestoEs_Izq.setText("Tu Puesto es: " + (pMundial > 0 ? pMundial : "---"));

	                    // Actualizar Label Semanal
	                    int pSemanal = miControlador.getMiPuestoSemanal(user);
	                    if (pSemanal > 0) {
	                        lblTuPuestoEs.setText("Tu Puesto es: " + pSemanal);
	                    } else {
	                        lblTuPuestoEs.setText("Sin actividad esta semana");
	                    }
	                    
	                    // 4. Tabla Grupal
	                    if (misGrupos.length > 0 && !misGrupos[0].equals("No hay datos")) {
	                        Object[][] datosGrupal = miControlador.getRankingGrupalEspecifico(misGrupos[0]);
	                        table_1.setModel(new javax.swing.table.DefaultTableModel(datosGrupal, columnas));
	                    } else {
	                        table_1.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{{"Sin Grupo", ""}}, columnas));
	                    }
	                } catch (Exception ex) {
	                    System.err.println("Error al cargar rankings: " + ex.getMessage());
	                }
	            }
	        }
	    });
	}

	// --- MÉTODOS DE SOPORTE ---

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
		btn.setFocusPainted(false);
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

	private DefaultTableModel crearModeloEjemplo() {
		return new DefaultTableModel(
				new Object[][] { { "Juan", 5000 }, { "María", 4950 }, { "Carlos", 4900 }, { "Elena", 4850 },
						{ "Luis", 4800 }, { "Ana", 4750 }, { "Pedro", 4700 }, { "Lucía", 4650 } },
				new String[] { "USUARIO", "PUNTOS" });
	}

	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;

		// 2. EVENTO AL CAMBIAR DE GRUPO EN EL DESPLEGABLE (Ahora fuera del ComponentShown)
		cbxGrupos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String grupoSeleccionado = (String) cbxGrupos.getSelectedItem();
				if (grupoSeleccionado != null && !grupoSeleccionado.equals("No hay datos")) {
					String[] columnas = { "USUARIO", "PUNTOS" };
					Object[][] datosGrupal = miControlador.getRankingGrupalEspecifico(grupoSeleccionado);
					table_1.setModel(new DefaultTableModel(datosGrupal, columnas));
				}
			}
		});
	}
}
