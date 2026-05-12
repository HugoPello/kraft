package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _05_Logros extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaLogros;
	private DefaultTableModel modeloTabla;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	private Controlador miControlador;
	private Modelo miModelo;

	public _05_Logros() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(GRIS_FONDO);
		setContentPane(contentPane);

		// --- HEADER ---
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		contentPane.add(header, BorderLayout.NORTH);

		JLabel lblKraft = new JLabel(" KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		header.add(lblKraft, BorderLayout.WEST);

		JPanel panelBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
		panelBotonesHeader.setOpaque(false);
		header.add(panelBotonesHeader, BorderLayout.EAST);

		panelBotonesHeader.add(crearBotonIcono("🏠", 2));
		panelBotonesHeader.add(crearBotonIcono("👤", 8));
		panelBotonesHeader.add(crearBotonIcono("🏃", 3));
		panelBotonesHeader.add(crearBotonIcono("🛒", 7));
		panelBotonesHeader.add(crearBotonIcono("🏅", 5)); // Icono de logros
		panelBotonesHeader.add(crearBotonIcono("🏆", 6));
		panelBotonesHeader.add(crearBotonIcono("⚙", 11));

		panelBotonesHeader.add(Box.createHorizontalStrut(15));
		panelBotonesHeader.add(crearBotonControl("-", Color.GRAY, e -> setState(JFrame.ICONIFIED)));
		panelBotonesHeader.add(crearBotonControl("X", new Color(220, 40, 40), e -> System.exit(0)));

		// --- PANEL CENTRAL ---
		JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
		panelCentral.setOpaque(false);
		panelCentral.setBorder(new EmptyBorder(30, 50, 30, 50));
		contentPane.add(panelCentral, BorderLayout.CENTER);

		JLabel lblLogros = new JLabel("PANEL DE LOGROS", SwingConstants.CENTER);
		lblLogros.setForeground(NARANJA);
		lblLogros.setFont(new Font("Arial Black", Font.BOLD, 45));
		panelCentral.add(lblLogros, BorderLayout.NORTH);

		// --- TABLA GRANDE DE LOGROS ---
		String[] columnas = { "LOGRO", "DESCRIPCIÓN", "RECOMPENSA", "PROGRESO" };
		modeloTabla = new DefaultTableModel(null, columnas) { // null para que no haya datos fijos
		    @Override
		    public boolean isCellEditable(int row, int column) { return false; }
		};

		tablaLogros = new JTable(modeloTabla);
		configurarEstiloTabla();

		JScrollPane scrollPane = new JScrollPane(tablaLogros);
		scrollPane.setBorder(BorderFactory.createLineBorder(NARANJA, 1));
		scrollPane.getViewport().setBackground(GRIS_RECTANGULO);
		panelCentral.add(scrollPane, BorderLayout.CENTER);

		// --- BOTÓN DE COMPLETAR ---
		JButton btnCompletar = new JButton("MARCAR LOGRO COMO COMPLETADO");
		btnCompletar.setFont(new Font("Arial Black", Font.BOLD, 18));
		btnCompletar.setBackground(NARANJA);
		btnCompletar.setForeground(Color.BLACK);
		btnCompletar.setFocusPainted(false);
		btnCompletar.setPreferredSize(new Dimension(0, 60));

		btnCompletar.addActionListener(e -> accionarLogro());
		panelCentral.add(btnCompletar, BorderLayout.SOUTH);
	}

	private void configurarEstiloTabla() {
		tablaLogros.setBackground(GRIS_RECTANGULO);
		tablaLogros.setForeground(Color.WHITE);
		tablaLogros.setRowHeight(60);
		tablaLogros.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tablaLogros.getTableHeader().setBackground(Color.BLACK);
		tablaLogros.getTableHeader().setForeground(NARANJA);
		tablaLogros.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
		tablaLogros.setSelectionBackground(NARANJA);
		tablaLogros.setSelectionForeground(Color.BLACK);
		tablaLogros.setGridColor(new Color(60, 60, 60));
		tablaLogros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * Refresca la tabla con los logros pendientes obtenidos desde el controlador.
	 */

	// En el botón de completar de la vista:
	private void accionarLogro() {
		int fila = tablaLogros.getSelectedRow();
		if (fila != -1) {
			String nombre = (String) modeloTabla.getValueAt(fila, 0);
			miControlador.marcarLogroCompletado(nombre);
			JOptionPane.showMessageDialog(this, "¡Logro movido a tu perfil!");
		}
	}

	// --- MÉTODOS DE APOYO (IDÉNTICOS A TU ESTRUCTURA) ---

	private JButton crearBotonIcono(String icono, int destino) {
		JButton btn = new JButton(icono);
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(GRIS_RECTANGULO);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btn.setBorder(null);
		btn.setFocusPainted(false);
		btn.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(5, destino);
		});
		return btn;
	}

	private JButton crearBotonControl(String texto, Color hover, ActionListener accion) {
		JButton btn = new JButton(texto);
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(new Color(15, 15, 15));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 18));
		btn.setBorder(null);
		btn.addActionListener(accion);
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
		return btn;
	}

	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
		    @Override
		    public void componentShown(java.awt.event.ComponentEvent e) {
		        cargarDatosLogros(); // Solo se ejecuta cuando la ventana se vuelve visible
		    }
		});
	}
	
	public void cargarDatosLogros() {
	    String[] cabecera = {"LOGRO", "DESCRIPCIÓN", "RECOMPENSA", "PROGRESO"};
	    
	    // Obtenemos los datos desde el controlador
	    Object[][] datosRaw = miControlador.getTablaLogrosDisponibles();
	    
	    // Formateamos el % solo si hay registros reales
	    if (datosRaw != null && datosRaw.length > 0 && !datosRaw[0][0].equals("Sin registros")) {
	        for (int i = 0; i < datosRaw.length; i++) {
	            datosRaw[i][3] = datosRaw[i][3] + "%"; 
	        }
	    }
	    
	    // Actualizamos la tabla
	    modeloTabla.setDataVector(datosRaw, cabecera);
	    configurarEstiloTabla();
	    // ELIMINA la línea de cargarDatosLogros() que había aquí al final
	}
	
}
