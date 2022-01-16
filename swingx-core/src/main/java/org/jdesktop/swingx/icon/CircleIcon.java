package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Simple Icon class for rendering a circle. Can be filled with colors to implement f.i. traffic lights
 *
 * @author EUG https://github.com/homebeaver/
 */
// UIResource hat keine Methoden
// SizingConstants extends SwingConstants
public class CircleIcon implements Icon, UIResource, SizingConstants {
	
	public static final Color OOUT_OF_ORDER = null;
	// to implement traffic lights:
	public static final Color RED = Color.RED;
	public static final Color YELLOW = Color.YELLOW;
	public static final Color GREEN = Color.GREEN;
	
    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public CircleIcon() {
    	this(SizingConstants.ACTION_ICON, OOUT_OF_ORDER);
    }

    public CircleIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public CircleIcon(int size) {
    	this(size, null);
    }

    protected CircleIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected CircleIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(c.getForeground());

/* https://openbook.rheinwerk-verlag.de/java8/11_002.html#u11.2.3 :
Bei der Methode drawOval(…) müssen wir immer daran denken, dass die Ellipse oder im Spezialfall 
der Kreis in ein Rechteck mit Startkoordinaten und mit Breite und Höhe gezeichnet wird. 
Dies ist nicht immer die natürliche Vorstellung von einer Ellipse bzw. einem Kreis. 
Einen Kreis bzw. eine Ellipse um den Mittelpunkt x, y mit den Radien rx und ry zeichnet:

	g.drawOval( x – rx, y – ry, rx + rx, ry + ry );

 */
        g.drawOval(x, y, width-1, height-1);

        g.setColor(this.color==null ? c.getBackground() : this.color);
        g.fillOval(x+1, y+1, width-2, height-2);
//		g.dispose(); NO dispose! wg. https://github.com/homebeaver/SwingSet/issues/19
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
