package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.Border;

import org.jdesktop.swingx.JYList;

// es gibt noch den DefaultListRenderer !!! XXX dokumentieren
@SuppressWarnings("serial")
public class YListCellRenderer extends DefaultListCellRenderer {

    public YListCellRenderer() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
    }

    private Border getNoFocusBorder() {
    	Border border = JYList.getBorder(this, ui, "List.cellNoFocusBorder");
    	if (border != null) return border;
    	return DefaultListCellRenderer.noFocusBorder;
    }

    /**
     * {@inheritDoc} <p>
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent
     */
    @Override // implements public interface ListCellRenderer<E>
    // original in super: not accessible: sun.swing.DefaultLookup 
	public Component getListCellRendererComponent(JList<?> list, Object value
			, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = JYList.getColor(this, ui, "List.dropCellBackground");
            fg = JYList.getColor(this, ui, "List.dropCellForeground");

            isSelected = true;
        }
        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon)value);
            setText("");
        }
        else {
            setIcon(null);
            setText((value == null) ? "" : value.toString());
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
		Border border = null;
		if (cellHasFocus) {
			if (isSelected) {
				border = JYList.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
			}
			border = JYList.getBorder(this, ui, "List.focusCellHighlightBorder");
		} else {
			border = getNoFocusBorder();
		}
		setBorder(border);
		return this;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g); // mit ui.update(scratchGraphics, this); ui ist MetallLabelUI
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }

    }
}
