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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Modelo.Modelo;

public class _07_Tienda extends JFrame implements Vista {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaTienda;
    private DefaultTableModel modeloTabla;
    
    private final Color NARANJA = new Color(255, 128, 0);
    private final Color GRIS_FONDO = new Color(25, 25, 25);
    private final Color GRIS_RECTANGULO = new Color(40, 40, 40);
    
    private Controlador miControlador;
    private Modelo miModelo;

    public _07_Tienda() {
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

        JLabel lblKraft = new JLabel(" KRAFT - TIENDA");
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
        panelBotonesHeader.add(crearBotonIcono("🏅", 5));
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

        JLabel lblTitulo = new JLabel("CATÁLOGO DE COSMÉTICOS", SwingConstants.CENTER);
        lblTitulo.setForeground(NARANJA);
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 45));
        panelCentral.add(lblTitulo, BorderLayout.NORTH);

        // --- TABLA DINÁMICA ---
        String[] columnas = {"ÍTEM", "TIPO", "PRECIO", "ESTADO"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaTienda = new JTable(modeloTabla);
        configurarEstiloTabla();

        JScrollPane scrollPane = new JScrollPane(tablaTienda);
        scrollPane.setBorder(BorderFactory.createLineBorder(NARANJA, 1));
        scrollPane.getViewport().setBackground(GRIS_RECTANGULO);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        // --- BOTÓN DE COMPRAR ---
        JButton btnComprar = new JButton("COMPRAR ARTÍCULO SELECCIONADO");
        btnComprar.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnComprar.setBackground(NARANJA);
        btnComprar.setForeground(Color.BLACK);
        btnComprar.setFocusPainted(false);
        btnComprar.setPreferredSize(new Dimension(0, 60));
        
        btnComprar.addActionListener(e -> accionarCompra());
        panelCentral.add(btnComprar, BorderLayout.SOUTH);

        // --- ESCUCHADOR DE CARGA ---
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                cargarDatosTienda();
            }
        });
    }

    public void cargarDatosTienda() {
        if (miControlador == null) return;
        String[] cabecera = {"ÍTEM", "TIPO", "PRECIO", "ESTADO"};
        Object[][] datosRaw = miControlador.getTablaCatalogoTienda();
        modeloTabla.setDataVector(datosRaw, cabecera);
        configurarEstiloTabla();
    }

    private void configurarEstiloTabla() {
        tablaTienda.setBackground(GRIS_RECTANGULO);
        tablaTienda.setForeground(Color.WHITE);
        tablaTienda.setRowHeight(60);
        tablaTienda.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tablaTienda.getTableHeader().setBackground(Color.BLACK);
        tablaTienda.getTableHeader().setForeground(NARANJA);
        tablaTienda.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        tablaTienda.setSelectionBackground(NARANJA);
        tablaTienda.setSelectionForeground(Color.BLACK);
        tablaTienda.setGridColor(new Color(60, 60, 60));
        tablaTienda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void accionarCompra() {
        int fila = tablaTienda.getSelectedRow();
        if (fila != -1) {
            String estado = (String) modeloTabla.getValueAt(fila, 3);
            
            if (estado.equals("ADQUIRIDO")) {
                JOptionPane.showMessageDialog(this, "Ya posees este artículo en tu inventario.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreItem = (String) modeloTabla.getValueAt(fila, 0);
            String precioStr = (String) modeloTabla.getValueAt(fila, 2);
            
            // Limpiamos el texto "500 💰" para quedarnos solo con el número 500
            int precioInt = Integer.parseInt(precioStr.replaceAll("[^0-9]", ""));
            
            // Enviamos la orden de compra al controlador
            miControlador.comprarArticulo(nombreItem, precioInt);
            
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un artículo de la tabla primero.", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JButton crearBotonIcono(String icono, int destino) {
        JButton btn = new JButton(icono);
        btn.setPreferredSize(new Dimension(45, 40));
        btn.setBackground(GRIS_RECTANGULO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> {
            if (miControlador != null) miControlador.cambiarVentana(7, destino);
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
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(new Color(15, 15, 15)); }
        });
        return btn;
    }

    @Override public void setModelo(Modelo miModelo) { this.miModelo = miModelo; }
    @Override public void setControlador(Controlador miControlador) { this.miControlador = miControlador; }
}