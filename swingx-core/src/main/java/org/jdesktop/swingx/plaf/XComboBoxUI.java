package org.jdesktop.swingx.plaf;

import javax.swing.Icon;
import javax.swing.plaf.ComboBoxUI;

/*

public abstract class javax.swing.plaf.ComponentUI {
    public ComponentUI() {}
    public void installUI(JComponent c) {}
    public void uninstallUI(JComponent c) {}
    public void paint(Graphics g, JComponent c) {}
    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(),c.getHeight());
        }
        paint(g, c);
    }
    public Dimension getPreferredSize(JComponent c) {
        return null;
    }
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }
    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }
    @SuppressWarnings("deprecation")
    public boolean contains(JComponent c, int x, int y) {
        return c.inside(x, y);
    }
    public static ComponentUI createUI(JComponent c) {
        throw new Error("ComponentUI.createUI not implemented.");
    }
    public int getBaseline(JComponent c, int width, int height) {
        if (c == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException(
                    "Width and height must be >= 0");
        }
        return -1;
    }
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
        if (c == null) {
            throw new NullPointerException("Component must be non-null");
        }
        return Component.BaselineResizeBehavior.OTHER;
    }
    public int getAccessibleChildrenCount(JComponent c) {
        return SwingUtilities.getAccessibleChildrenCount(c);
    }
    public Accessible getAccessibleChild(JComponent c, int i) {
        return SwingUtilities.getAccessibleChild(c, i);
    }

public abstract class javax.swing.plaf.ComboBoxUI extends ComponentUI {
    protected ComboBoxUI() {}
    public abstract void setPopupVisible( JComboBox<?> c, boolean v );
    public abstract boolean isPopupVisible( JComboBox<?> c );
    public abstract boolean isFocusTraversable( JComboBox<?> c );

***
erweitert die UI um Methoden zum Handling von ComboBox Button (aka arrowButton) 

 */
public abstract class XComboBoxUI extends ComboBoxUI {
	
	/**
     * Creates and initializes the ComboBox Button (aka arrowButton).
     * If no icon is provided, an UI depended dafault is created.
	 * 
	 * @param i optional Icon for the ComboBox Button
	 */
	public abstract void installButton(Icon i);
	/**
	 * Defines an icon for the ComboBox Button (aka arrowButton) that appears when popup is visible.
	 * @param i
	 */
	public abstract void setIsShowingPopupIcon(Icon i);
	/**
	 * Unconfiger and uninstall the ComboBox Button (aka arrowButton).
	 */
	public abstract void uninstallButton();
	
}
