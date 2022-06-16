package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	
	/** without color: traffic lights are out of order */
	public static final Color OUT_OF_ORDER = null;
	// to implement traffic lights:
	/** red light */
	public static final Color RED = Color.RED;
	/** yellow light */
	public static final Color YELLOW = Color.YELLOW;
	/** green light */
	public static final Color GREEN = Color.GREEN;
	
    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    /**
     * the default is sized like ACTION_ICON and with no color
     */
    public CircleIcon() {
    	this(SizingConstants.ACTION_ICON, OUT_OF_ORDER);
    }

    /**
     * ctor
     * @param size icon width and length
     * @param color circle is filled with
     */
    public CircleIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    /**
     * convienient ctor
     * @param size icon width and length
     */
    public CircleIcon(int size) {
    	this(size, null);
    }

    /**
     * convienient ctor
     * @param width of the icon
     * @param height of the icon
     */
    protected CircleIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * convienient ctor with Dimension
     * @param size Dimension
     */
    protected CircleIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(c.getForeground());
		
		// creates a solid stroke with line width is 2
		float s = 2f*width/24;
//		Stroke stroke = new BasicStroke(2f*width/24);
//		g2d.setStroke(stroke);
		
/* https://openbook.rheinwerk-verlag.de/java8/11_002.html#u11.2.3 :
Bei der Methode drawOval(…) müssen wir immer daran denken, dass die Ellipse oder im Spezialfall 
der Kreis in ein Rechteck mit Startkoordinaten und mit Breite und Höhe gezeichnet wird. 
Dies ist nicht immer die natürliche Vorstellung von einer Ellipse bzw. einem Kreis. 
Einen Kreis bzw. eine Ellipse um den Mittelpunkt x, y mit den Radien rx und ry zeichnet:

	g.drawOval( x – rx, y – ry, rx + rx, ry + ry );

 */
//		g2d.drawOval(x, y, width-1, height-1);
		g2d.fillOval(x, y, width-1, height-1);

		g2d.setColor(color==null ? c.getForeground() : color);
		g2d.fillOval(x+(int)(s/2), y+(int)(s/2), width-1-(int)s, height-1-(int)s);
		
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
