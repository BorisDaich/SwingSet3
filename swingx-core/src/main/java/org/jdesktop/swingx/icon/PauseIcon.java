package org.jdesktop.swingx.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Pause icon (used for Players). Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
public class PauseIcon implements Icon, UIResource, SizingConstants {

    private BasicStroke stroke = new BasicStroke(2);

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public PauseIcon() {
    }

    public PauseIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public PauseIcon(int size) {
    	this(size, null);
    }

    protected PauseIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected PauseIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc}
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(color==null ? c.getForeground() : color);
		g2d.setStroke(stroke);
		g2d.fillRect(x + (2 * width / 9), y + 4, (2 * width / 9), height - 8);
		g2d.fillRect(x + 5 * width / 9, y + 4, (2 * width / 9), height - 8);
//		g2d.dispose(); NO dispose! wg. https://github.com/homebeaver/SwingSet/issues/19
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

}
