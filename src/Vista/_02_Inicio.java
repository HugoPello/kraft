//@Autor : Carlos / Ivan
package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _02_Inicio extends JFrame implements Vista {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Controlador miControlador;
	private Modelo miModelo;

	private DefaultTableModel modeloNoticias;
	private DefaultTableModel modeloDiarias;
	private DefaultTableModel modeloSemanales;

	private final Color NARANJA = new Color(255, 128, 0);
	private final Color GRIS_FONDO = new Color(25, 25, 25);
	private final Color GRIS_RECTANGULO = new Color(40, 40, 40);

	public _02_Inicio() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBackground(GRIS_FONDO);
		setContentPane(contentPane);

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(15, 15, 15));
		header.setPreferredSize(new Dimension(0, 70));
		contentPane.add(header, BorderLayout.NORTH);

		JLabel lblKraft = new JLabel(" KRAFT");
		lblKraft.setForeground(NARANJA);
		lblKraft.setFont(new Font("Arial Black", Font.BOLD, 26));
		header.add(lblKraft, BorderLayout.WEST);

		JPanel panelBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 15));
		panelBotonesHeader.setOpaque(false);
		// Panel de iconos en el header
		panelBotonesHeader.add(crearBotonIcono("🏠", 2)); // Inicio
		panelBotonesHeader.add(crearBotonIcono("👤", 8)); // Perfil
		panelBotonesHeader.add(crearBotonIcono("🏃", 3)); // Actividades
		panelBotonesHeader.add(crearBotonIcono("🛒", 7)); // Tienda
		panelBotonesHeader.add(crearBotonIcono("🏅", 5)); // <--- NUEVO: Logros (_05_Logros)
		panelBotonesHeader.add(crearBotonIcono("🏆", 6)); // Ranking (_06_Ranking)
		panelBotonesHeader.add(crearBotonIcono("⚙", 11)); // Panel Admin / Ajustes

		panelBotonesHeader.add(Box.createHorizontalStrut(20));
		panelBotonesHeader.add(crearBotonControl("-", Color.GRAY, e -> setState(JFrame.ICONIFIED)));
		panelBotonesHeader.add(crearBotonControl("X", new Color(220, 40, 40), e -> System.exit(0)));

		header.add(panelBotonesHeader, BorderLayout.EAST);

		JPanel panelCentral = new JPanel(new GridLayout(1, 2, 20, 0));
		panelCentral.setBackground(GRIS_FONDO);
		panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.add(panelCentral, BorderLayout.CENTER);

		// Columna Izquierda:Noticias
		JPanel rectIzquierdo = new JPanel(new BorderLayout());
		rectIzquierdo.setBackground(GRIS_RECTANGULO);
		rectIzquierdo.setBorder(new EmptyBorder(15, 15, 15, 15));

		JLabel lblTitNoticias = new JLabel("NOTICIAS Y NOVEDADES");
		lblTitNoticias.setForeground(NARANJA);
		lblTitNoticias.setFont(new Font("Tahoma", Font.BOLD, 18));
		rectIzquierdo.add(lblTitNoticias, BorderLayout.NORTH);
		rectIzquierdo.add(crearTablaNoticiasMasiva(), BorderLayout.CENTER);

		panelCentral.add(rectIzquierdo);

		// Columna Derecha: Misiones
		JPanel rectDerecho = new JPanel();
		rectDerecho.setLayout(new BoxLayout(rectDerecho, BoxLayout.Y_AXIS));
		rectDerecho.setBackground(GRIS_RECTANGULO);
		rectDerecho.setBorder(new EmptyBorder(15, 15, 15, 15));

		// Inicializamos los modelos que declaramos como atributos de clase
		modeloDiarias = new DefaultTableModel(null, new String[] { "Misión", "Estado", "Recompensa" }) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		modeloSemanales = new DefaultTableModel(null, new String[] { "Misión", "Estado", "Recompensa" }) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		// Añadimos los componentes al panel
		rectDerecho.add(crearLabelSeccion("MISIONES DIARIAS"));
		rectDerecho.add(crearScrollMision(modeloDiarias)); // Usamos el modelo, no datos vacíos
		rectDerecho.add(Box.createVerticalStrut(20));
		rectDerecho.add(crearLabelSeccion("MISIONES SEMANALES"));
		rectDerecho.add(crearScrollMision(modeloSemanales));
		panelCentral.add(rectDerecho);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				// Este código solo se ejecuta cuando la ventana se vuelve visible
				if (miControlador != null) {
					cargarNoticias();
					miControlador.gestionarMisionesDiarias();

				}
			}
		});
	}

	private JButton crearBotonIcono(String icono, int ventanaDestino) {
		JButton btn = new JButton(icono);
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(GRIS_RECTANGULO);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		btn.setFocusPainted(false);
		btn.setBorder(null);
		btn.addActionListener(e -> {
			if (miControlador != null)
				miControlador.cambiarVentana(2, ventanaDestino);
		});
		return btn;
	}

	private JButton crearBotonControl(String texto, Color colorHover, ActionListener accion) {
		JButton btn = new JButton(texto);
		btn.setPreferredSize(new Dimension(45, 40));
		btn.setBackground(new Color(15, 15, 15));
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Arial", Font.BOLD, 16));
		btn.setBorder(null);
		btn.addActionListener(accion);
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(colorHover);
			}

			public void mouseExited(MouseEvent e) {
				btn.setBackground(new Color(15, 15, 15));
			}
		});
		return btn;
	}

	private JScrollPane crearTablaNoticiasMasiva() {
		String[] columnas = { "Novedades de la App" };
		// Inicializamos el modelo vacío
		modeloNoticias = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable tabla = new JTable(modeloNoticias);
		tabla.setBackground(new Color(50, 50, 50));
		tabla.setForeground(Color.WHITE);
		tabla.setRowHeight(45);
		tabla.setFont(new Font("Tahoma", Font.PLAIN, 14));

		// Quitar las líneas de la cuadrícula para un look más moderno
		tabla.setShowGrid(false);
		tabla.setIntercellSpacing(new Dimension(0, 0));

		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(new Color(50, 50, 50));
		return scroll;
	}

	private JLabel crearLabelSeccion(String texto) {
		JLabel lbl = new JLabel(texto);
		lbl.setForeground(NARANJA);
		lbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
		return lbl;
	}

	private JScrollPane crearScrollMision(DefaultTableModel modelo) {
		JTable tabla = new JTable(modelo); // Usamos el modelo pasado por parámetro
		tabla.setBackground(new Color(60, 60, 60));
		tabla.setForeground(Color.WHITE);
		tabla.setRowHeight(40);
		tabla.setShowGrid(false); // Para seguir la estética de las noticias

		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setPreferredSize(new Dimension(0, 250));
		scroll.setBorder(null);
		scroll.getViewport().setBackground(new Color(60, 60, 60));
		return scroll;
	}

	@Override
	public void setModelo(Modelo miModelo) {
		this.miModelo = miModelo;
	}

	public void cargarNoticias() {
		// Pedimos los datos al controlador
		ArrayList<Object[]> datos = miControlador.getNoticias();

		modeloNoticias.setRowCount(0);

		for (Object[] fila : datos) {
			modeloNoticias.addRow(fila);
		}
	}

	public void cargarMisiones() {
		if (miModelo != null && miModelo.getUsuarioLogueado() != null) {
			String usr = miModelo.getUsuarioLogueado();

			// Cargar Diarias
			ArrayList<Object[]> diarias = miModelo.obtenerMisionesHoy(usr, "Diaria");
			modeloDiarias.setRowCount(0);
			for (Object[] fila : diarias)
				modeloDiarias.addRow(fila);

			// Cargar Semanales
			ArrayList<Object[]> semanales = miModelo.obtenerMisionesHoy(usr, "Semanal");
			modeloSemanales.setRowCount(0);
			for (Object[] fila : semanales)
				modeloSemanales.addRow(fila);
		}
	}

	@Override
	public void setControlador(Controlador miControlador) {
		this.miControlador = miControlador;
	}
}