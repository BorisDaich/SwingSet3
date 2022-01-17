package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Stop icon (used for Players). Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
public class StopIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public StopIcon() {
    }

    public StopIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public StopIcon(int size) {
    	this(size, null);
    }

    protected StopIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected StopIcon(Dimension size) {
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
		g2d.fillRect(x + (width / 8), y + (height / 8), 3*width/4, 3*height/4);
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
