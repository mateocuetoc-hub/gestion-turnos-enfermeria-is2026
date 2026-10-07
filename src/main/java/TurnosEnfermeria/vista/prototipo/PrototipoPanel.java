package TurnosEnfermeria.vista.prototipo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import static TurnosEnfermeria.vista.prototipo.TemaPrototipo.*;

/** Navegación del bosquejo. Elegir una vista no autentica ni concede permisos. */
public final class PrototipoPanel extends JPanel {
    private final CardLayout cards = new CardLayout();
    private final JPanel vistas = new JPanel(cards);
    private final JPanel espacio = new JPanel(new BorderLayout());
    private final JPanel menu = vertical();
    private final JLabel contexto = texto("", 13, false, SUAVE);
    private final JLabel identidad = texto("", 13, true, ACENTO);
    private final Map<String, JButton> botones = new LinkedHashMap<>();
    private String rol;
    private String paginaActual;

    public PrototipoPanel() {
        super(new BorderLayout());
        setBackground(FONDO);
        JLabel demo = texto("BOSQUEJO VISUAL  ·  Datos ficticios  ·  Sin autenticación ni guardado de cambios", 12, true, AMBAR);
        demo.setOpaque(true);
        demo.setBackground(new Color(255, 244, 216));
        demo.setBorder(new EmptyBorder(10, 22, 10, 22));
        add(demo, BorderLayout.NORTH);
        JScrollPane inicio = new JScrollPane(selector());
        inicio.setBorder(null);
        inicio.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inicio.getVerticalScrollBar().setUnitIncrement(24);
        vistas.add(inicio, "selector");
        vistas.add(escritorio(), "escritorio");
        add(vistas, BorderLayout.CENTER);
        cards.show(vistas, "selector");
    }

    private JPanel selector() {
        JPanel page = new SelectorPanel();
        page.setPreferredSize(new Dimension(1100, 760));
        page.setBackground(FONDO);
        page.setBorder(new EmptyBorder(40, 44, 32, 44));
        JPanel heading = vertical();
        agregar(heading, texto("INGENIERÍA DE SOFTWARE · PUCV · 2026", 12, true, ACENTO));
        heading.add(Box.createVerticalStrut(15));
        agregar(heading, texto("Gestión de turnos de enfermería", 32, true, TINTA));
        heading.add(Box.createVerticalStrut(12));
        agregar(heading, parrafo("Explora las vistas propuestas para un servicio piloto. Cada rol tiene un recorrido y responsabilidades diferentes.", SUAVE));
        heading.add(Box.createVerticalStrut(22));
        agregar(heading, aviso("Selecciona una vista de demostración",
                "Este selector permite revisar el diseño. Las cuentas, los permisos y las operaciones reales se desarrollarán en etapas posteriores."));
        page.add(heading, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridLayout(1, 3, 20, 0));
        options.setOpaque(false);
        options.add(opcion("01", "Enfermero/a", "E-001 · Persona 01",
                "Consultar turnos propios", "Comunicar ausencias (por validar)", "Seguir solicitudes",
                "enfermeria", "Enfermero/a"));
        options.add(opcion("02", "Coordinador/a de turnos", "Coordinación · Área piloto A",
                "Organizar las asignaciones", "Revisar ausencias y conflictos", "Preparar sustituciones",
                "coordinacion", "Coordinador/a de turnos"));
        options.add(opcion("03", "Jefe/a del servicio", "Jefatura · Área piloto A",
                "Consultar cobertura por bloque", "Revisar déficits e incidencias", "Consultar informes del período",
                "jefatura", "Jefe/a del servicio"));
        page.add(options, BorderLayout.CENTER);
        JPanel footer = vertical();
        agregar(footer, texto("ESCENARIO COMPARTIDO", 11, true, SUAVE));
        footer.add(Box.createVerticalStrut(8));
        agregar(footer, parrafo("Área piloto A · " + DatosPrototipo.PERIODO
                + ". Una ausencia necesita cobertura; las tres vistas muestran la misma situación de ejemplo.", SUAVE));
        page.add(footer, BorderLayout.SOUTH);
        return page;
    }

    private JPanel opcion(String number, String title, String user, String first, String second,
            String third, String role, String button) {
        JPanel card = tarjeta(null, null);
        agregar(card, texto(number, 36, true, ACENTO));
        card.add(Box.createVerticalStrut(24));
        // Se permite partir el título para mantener legibilidad en pantallas pequeñas.
        String[] words = title.split(" de ", 2);
        agregar(card, texto(words[0], 21, true, TINTA));
        if (words.length > 1) agregar(card, texto("de " + words[1], 21, true, TINTA));
        card.add(Box.createVerticalStrut(14));
        agregar(card, texto(user, 12, false, SUAVE));
        card.add(Box.createVerticalStrut(25));
        for (String line : new String[]{first, second, third}) {
            agregar(card, parrafo("• " + line, TINTA));
            card.add(Box.createVerticalStrut(14));
        }
        card.add(Box.createVerticalGlue());
        agregar(card, boton(button, true, () -> mostrarRol(role)));
        return card;
    }

    private JPanel escritorio() {
        JPanel desk = new JPanel(new BorderLayout());
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(AZUL);
        sidebar.setPreferredSize(new Dimension(235, 0));
        sidebar.setBorder(new EmptyBorder(26, 18, 22, 18));
        JPanel brand = vertical();
        agregar(brand, texto("TURNOS", 25, true, Color.WHITE));
        brand.add(Box.createVerticalStrut(7));
        agregar(brand, texto("Enfermería · servicio piloto", 12, false, new Color(190, 208, 222)));
        brand.add(Box.createVerticalStrut(34));
        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(menu, BorderLayout.CENTER);
        JPanel bottom = vertical();
        agregar(bottom, texto("ÁREA PILOTO A", 11, true, new Color(190, 208, 222)));
        bottom.add(Box.createVerticalStrut(9));
        agregar(bottom, texto("Diseño propuesto · versión 0.1", 11, false, new Color(190, 208, 222)));
        bottom.add(Box.createVerticalStrut(22));
        agregar(bottom, boton("Cambiar vista de rol", false, this::mostrarSelector));
        sidebar.add(bottom, BorderLayout.SOUTH);
        desk.add(sidebar, BorderLayout.WEST);

        JPanel body = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout(20, 0));
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(19, 28, 19, 28));
        top.add(contexto, BorderLayout.WEST);
        top.add(identidad, BorderLayout.EAST);
        body.add(top, BorderLayout.NORTH);
        body.add(espacio, BorderLayout.CENTER);
        JLabel foot = texto("Vista de ejemplo · Las acciones abren formularios y explican la implementación prevista.", 11, false, SUAVE);
        foot.setBorder(new EmptyBorder(10, 28, 10, 28));
        body.add(foot, BorderLayout.SOUTH);
        desk.add(body, BorderLayout.CENTER);
        return desk;
    }

    void mostrarRol(String role) {
        rol = role;
        menu.removeAll();
        botones.clear();
        String[] pages;
        switch (role) {
            case "enfermeria":
                pages = new String[]{"Mi agenda", "Mis solicitudes"};
                contexto.setText("Personal de enfermería · " + DatosPrototipo.PERIODO);
                identidad.setText("E-001 · Persona 01");
                break;
            case "coordinacion":
                pages = new String[]{"Resumen operativo", "Planificación", "Personal", "Ausencias y cobertura"};
                contexto.setText("Coordinación de turnos · " + DatosPrototipo.PERIODO);
                identidad.setText("Área piloto A");
                break;
            case "jefatura":
                pages = new String[]{"Cobertura del servicio", "Informes"};
                contexto.setText("Jefatura del servicio · " + DatosPrototipo.PERIODO);
                identidad.setText("Vista de consulta");
                break;
            default: throw new IllegalArgumentException("Rol de demostración desconocido: " + role);
        }
        for (String page : pages) {
            JButton button = boton(page, false, () -> mostrarPagina(page));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            button.setPreferredSize(new Dimension(200, 45));
            button.setBorder(new EmptyBorder(12, 12, 12, 12));
            botones.put(page, button);
            agregar(menu, button);
            menu.add(Box.createVerticalStrut(8));
        }
        menu.revalidate();
        cards.show(vistas, "escritorio");
        mostrarPagina(pages[0]);
        revalidate(); repaint();
    }

    void mostrarPagina(String page) {
        if (!botones.containsKey(page)) throw new IllegalArgumentException("Pantalla ajena a la vista seleccionada.");
        paginaActual = page;
        for (Map.Entry<String, JButton> entry : botones.entrySet()) {
            boolean selected = entry.getKey().equals(page);
            entry.getValue().setBackground(selected ? ACENTO : AZUL);
            entry.getValue().setForeground(selected ? Color.WHITE : new Color(205, 220, 232));
        }
        espacio.removeAll();
        JScrollPane scroll = new JScrollPane(PantallasPrototipo.crear(page, this::abrirFormulario));
        scroll.setBorder(null);
        scroll.getViewport().setBackground(FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        espacio.add(scroll, BorderLayout.CENTER);
        espacio.revalidate(); espacio.repaint();
    }

    void mostrarSelector() {
        rol = null;
        paginaActual = null;
        cards.show(vistas, "selector");
        revalidate(); repaint();
    }

    String getRol() { return rol; }
    String getPaginaActual() { return paginaActual; }

    private void abrirFormulario(String id) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, PantallasPrototipo.tituloFormulario(id), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(PantallasPrototipo.formulario(id));
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        content.add(scroll, BorderLayout.CENTER);
        JPanel actions = acciones(boton("Ver acción prevista", true, () ->
                JOptionPane.showMessageDialog(dialog,
                    "<html><body style='width:380px'>" + PantallasPrototipo.accionPrevista(id)
                    + "<br><br><b>Este bosquejo no ejecuta ni guarda la operación.</b></body></html>",
                    "Implementación prevista", JOptionPane.INFORMATION_MESSAGE)),
                boton("Cerrar", false, dialog::dispose));
        actions.setBorder(new EmptyBorder(12, 18, 18, 18));
        content.add(actions, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.setSize(700, 670);
        dialog.setMinimumSize(new Dimension(560, 480));
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    private static final class SelectorPanel extends JPanel implements Scrollable {
        SelectorPanel() { super(new BorderLayout(0, 32)); }
        @Override public Dimension getPreferredScrollableViewportSize() { return new Dimension(1100, 760); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return Math.max(24, r.height - 24); }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() >= getPreferredSize().height;
        }
    }
}
