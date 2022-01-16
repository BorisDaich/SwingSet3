package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Play icon (used for Players). Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
public class PlayIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public PlayIcon() {
    }

    public PlayIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public PlayIcon(int size) {
    	this(size, null);
    }

    protected PlayIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected PlayIcon(Dimension size) {
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
		GeneralPath path = new GeneralPath();
		path.moveTo(x + 2, y + 2);
		path.lineTo(x + width - 2, y + height / 2);
		path.lineTo(x + 2, y + height - 2);
		path.lineTo(x + 2, y + 2);
		g2d.fill(path);
//		g2d.dispose(); no dispose! wg. https://github.com/homebeaver/SwingSet/issues/19
		
//		Alternativ: ganz links
//		g.setColor(color==null ? c.getForeground() : color);
//		g.fillPolygon(new int[] { (int) (0), width, (int) (0) }, new int[] { 0,
//				(int) (height * 0.5), height }, 3);
//		//g.dispose();
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
