package TurnosEnfermeria.vista.prototipo;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Componentes visuales compartidos por el bosquejo; no accede al modelo real. */
final class TemaPrototipo {
    static final Color FONDO = new Color(244, 247, 250);
    static final Color TINTA = new Color(27, 43, 62);
    static final Color SUAVE = new Color(94, 112, 131);
    static final Color BORDE = new Color(218, 227, 235);
    static final Color AZUL = new Color(18, 42, 65);
    static final Color ACENTO = new Color(0, 112, 122);
    static final Color VERDE = new Color(20, 112, 80);
    static final Color AMBAR = new Color(145, 88, 13);
    static final Color ROJO = new Color(164, 47, 66);

    private TemaPrototipo() { }

    static void configurar() {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) { /* El tema del JDK sigue siendo utilizable. */ }
        Font normal = fuente(13, false);
        for (String key : new String[]{"Label.font", "Button.font", "ComboBox.font", "TextField.font",
                "TextArea.font", "Table.font", "TableHeader.font", "OptionPane.messageFont"}) {
            UIManager.put(key, normal);
        }
        UIManager.put("Panel.background", FONDO);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ScrollBar.width", 12);
    }

    static Font fuente(int size, boolean bold) {
        return new Font(Font.SANS_SERIF, bold ? Font.BOLD : Font.PLAIN, size);
    }

    static JLabel texto(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(fuente(size, bold));
        label.setForeground(color);
        return label;
    }

    static JPanel vertical() {
        JPanel panel = new JPanel() {
            @Override public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    static JPanel tarjeta(String title, String subtitle) {
        JPanel panel = vertical();
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new LineBorder(BORDE), new EmptyBorder(20, 22, 20, 22)));
        if (title != null) {
            agregar(panel, texto(title, 17, true, TINTA));
            if (subtitle != null) {
                panel.add(Box.createVerticalStrut(7));
                agregar(panel, parrafo(subtitle, SUAVE));
            }
            panel.add(Box.createVerticalStrut(16));
        }
        return panel;
    }

    static void agregar(JPanel panel, JComponent child) {
        child.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(child);
    }

    static JTextArea parrafo(String value, Color color) {
        JTextArea area = new JTextArea(value) {
            @Override public Dimension getPreferredSize() {
                int width = getWidth();
                if (width <= 0 && getParent() != null) {
                    Insets insets = getParent().getInsets();
                    width = getParent().getWidth() - insets.left - insets.right;
                }
                if (width <= 0) width = 600;
                FontMetrics metrics = getFontMetrics(getFont());
                int lines = 0;
                for (String paragraph : getText().split("\\n", -1)) {
                    int used = 0;
                    lines++;
                    for (String word : paragraph.split(" ")) {
                        int length = metrics.stringWidth(word + " ");
                        if (used > 0 && used + length > width) { lines++; used = 0; }
                        used += length;
                    }
                }
                return new Dimension(width, lines * metrics.getHeight() + 3);
            }
            @Override public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
            @Override public Dimension getMinimumSize() {
                return new Dimension(40, getPreferredSize().height);
            }
        };
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFont(fuente(13, false));
        area.setForeground(color);
        area.setFocusable(false);
        return area;
    }

    static JButton boton(String title, boolean primary, Runnable action) {
        JButton button = new JButton(title);
        button.setFont(fuente(13, true));
        button.setForeground(primary ? Color.WHITE : ACENTO);
        button.setBackground(primary ? ACENTO : Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(true);
        button.setBorder(new CompoundBorder(new LineBorder(primary ? ACENTO : BORDE),
                new EmptyBorder(11, 17, 11, 17)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.run());
        return button;
    }

    static JPanel acciones(JComponent... components) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        for (JComponent component : components) panel.add(component);
        return panel;
    }

    static JPanel aviso(String title, String message) {
        JPanel panel = vertical();
        panel.setOpaque(true);
        panel.setBackground(new Color(255, 247, 225));
        panel.setBorder(new CompoundBorder(new MatteBorder(0, 4, 0, 0, new Color(210, 153, 36)),
                new EmptyBorder(13, 16, 13, 16)));
        agregar(panel, texto(title, 13, true, AMBAR));
        panel.add(Box.createVerticalStrut(5));
        agregar(panel, parrafo(message, TINTA));
        return panel;
    }

    static JPanel indicadores(String[][] values) {
        JPanel row = new JPanel(new GridLayout(1, values.length, 14, 0));
        row.setOpaque(false);
        for (String[] value : values) {
            JPanel card = tarjeta(null, null);
            agregar(card, texto(value[0], 12, true, SUAVE));
            card.add(Box.createVerticalStrut(10));
            agregar(card, texto(value[1], 30, true, ACENTO));
            card.add(Box.createVerticalStrut(7));
            agregar(card, parrafo(value[2], SUAVE));
            row.add(card);
        }
        return row;
    }

    static JScrollPane tabla(String[] columns, String[][] rows) {
        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(fuente(13, false));
        table.setRowHeight(42);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(225, 243, 245));
        table.setSelectionForeground(TINTA);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(fuente(12, true));
        table.getTableHeader().setBackground(new Color(237, 242, 247));
        table.getTableHeader().setForeground(SUAVE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                    boolean focused, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(t, value, selected, focused, row, column);
                cell.setBorder(new EmptyBorder(0, 12, 0, 8));
                cell.setFont(fuente(13, false));
                cell.setForeground(TINTA);
                if (!selected) cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 251, 253));
                String text = String.valueOf(value);
                if (text.contains("Pendiente") || text.contains("Ausencia") || text.contains("Por validar") || text.equals("En revisión")) {
                    cell.setForeground(AMBAR);
                } else if (text.contains("Ocupado") || text.contains("Déficit")) {
                    cell.setForeground(ROJO);
                } else if (text.equals("Disponible") || text.equals("Completa") || text.equals("Asignado")) {
                    cell.setForeground(VERDE);
                }
                cell.setToolTipText(text);
                return cell;
            }
        });
        int height = 38 + Math.min(8, rows.length) * 42 + 3;
        JScrollPane scroll = new JScrollPane(table);
        scroll.setColumnHeaderView(table.getTableHeader());
        scroll.setBorder(new LineBorder(BORDE));
        scroll.setPreferredSize(new Dimension(820, height));
        scroll.setMinimumSize(new Dimension(100, height));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        return scroll;
    }

    static JPanel campo(String title, JComponent input) {
        JPanel panel = vertical();
        agregar(panel, texto(title, 12, true, SUAVE));
        panel.add(Box.createVerticalStrut(7));
        input.setFont(fuente(14, false));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, input instanceof JTextArea ? 95 : 38));
        input.setPreferredSize(new Dimension(360, input instanceof JTextArea ? 95 : 38));
        input.setBorder(new CompoundBorder(new LineBorder(BORDE), new EmptyBorder(7, 9, 7, 9)));
        agregar(panel, input);
        panel.add(Box.createVerticalStrut(16));
        return panel;
    }
}
