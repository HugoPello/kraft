package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
        String[] columnas = {"LOGRO", "DESCRIPCIÓN", "RECOMPENSA", "ESTADO"};
        Object[][] datos = {
            {"Apisonadora", "Camina un total de 50km y conviértete en el loco del cardio", "500 XP", "Pendiente"},
            {"Correcaminos", "Corre al menos 21 días en un solo mes", "Gold Badge", "Pendiente"},
            {"Phelps", "Nada un total de 25km en un mes", "Aleta de Oro", "Pendiente"},
            {"Ironman?", "Nada 3km, Bici 181km y Carrera 42km", "Título Legendario", "Pendiente"},
            {"Salud Cardíaca", "Sal a caminar todos los días durante una semana", "100 XP", "Pendiente"},
            {"Explorador", "Aguanta 2 días de acampada seguidos", "Tienda Pro", "Pendiente"},
            {"Madrugador", "Entrena antes de las 7:00 AM", "Café Virtual", "Pendiente"},
            {"Hidratación Pro", "Bebe 2L de agua diarios durante 15 días", "Skin Botella", "Pendiente"}
        };

        modeloTabla = new DefaultTableModel(datos, columnas) {
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

    private void accionarLogro() {
        int fila = tablaLogros.getSelectedRow();
        if (fila != -1) {
            String nombreLogro = (String) modeloTabla.getValueAt(fila, 0);
            String estado = (String) modeloTabla.getValueAt(fila, 3);

            if (estado.equals("Completado")) {
                JOptionPane.showMessageDialog(this, "Este logro ya ha sido desbloqueado anteriormente.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar tabla
                modeloTabla.setValueAt("Completado", fila, 3);
                
                // Mensaje personalizado
                UIManager.put("OptionPane.background", GRIS_RECTANGULO);
                UIManager.put("Panel.background", GRIS_RECTANGULO);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                
                JOptionPane.showMessageDialog(
                    this, 
                    "¡FELICIDADES!\nHas desbloqueado: " + nombreLogro.toUpperCase(), 
                    "LOGRO DESBLOQUEADO", 
                    JOptionPane.PLAIN_MESSAGE,
                    null
                );
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un logro de la tabla primero.", "Atención", JOptionPane.WARNING_MESSAGE);
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
            if (miControlador != null) miControlador.cambiarVentana(5, destino);
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