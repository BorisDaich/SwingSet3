package org.jdesktop.swingx.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Chevrons icon (v). Can be filled with any color, default is component foreground (black).
 * Used in Task Pane.
 *
 * @author EUG https://github.com/homebeaver/
 */
public class ChevronsIcon implements Icon, UIResource, SizingConstants {

//	private static final Logger LOG = Logger.getLogger(ChevronsIcon.class.getName());
	
    private int width = SizingConstants.SMALL_ICON;
    private int height = SizingConstants.SMALL_ICON;
    private Color color;
    
    private int direction = SOUTH; // Compass-direction SOUTH == Orientation.DOWN
    /**
     * You can change the orientation from DOWN to UP
     * @param direction Compass-direction SOUTH == Orientation.DOWN
     */
    public void setDirection(int direction) {
    	this.direction = direction;
    }

    /**
     * ctor for default Chevrons - this is SMALL_ICON and Foreground Color of the Component to paint in
     */
    public ChevronsIcon() {
    }

    /**
     * ctor with icon size and color
     * @param size width and height
     * @param color Foreground Color
     */
    public ChevronsIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    /**
     * ctor with icon size
     * @param size width and height
     */
    public ChevronsIcon(int size) {
    	this(size, null);
    }

    /**
     * optional ctor with width and height
     * @param width of the icon
     * @param height of the icon
     */
    protected ChevronsIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * convenient ctor with Dimension
     * @param size Dimension
     */
    protected ChevronsIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc} 
     */
    /* example chevrons up:
     * <pre><code>
<svg
  xmlns="http://www.w3.org/2000/svg"
  width="24"
  height="24"
  viewBox="0 0 24 24"
  fill="none"
  stroke="currentColor"
  stroke-width="2"
  stroke-linecap="round"
  stroke-linejoin="round"
>
  <polyline points="17 11 12 6 7 11" />
  <polyline points="17 18 12 13 7 18" />
</svg> 
     * </code></pre>
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
//    	LOG.info("direction="+direction + ", x="+x + ",y="+y);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color==null ? c.getForeground() : color);
		
		// creates a solid stroke with line width is 2
		Stroke stroke = new BasicStroke(2f*width/24, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER); // default is CAP_SQUARE, JOIN_MITER
		g2d.setStroke(stroke);
		
		AffineTransform saveAT = g2d.getTransform();
		switch (direction) {
		case NORTH: // 1
//	    	LOG.info("NORTH direction="+direction + " no rotation ");
            break;
		case NORTH_EAST:
			g2d.rotate(Math.PI/4, x+width/2, y+height/2);
			break;
		case EAST:
			g2d.rotate(Math.PI/2, x+width/2, y+height/2);
			break;
		case SOUTH_EAST:
			g2d.rotate(Math.PI*3/4, x+width/2, y+height/2);
			break;
		case SOUTH: // 5
//	    	LOG.info("SOUTH direction="+direction + ", x="+x + ",y="+y);
			g2d.rotate(Math.PI, x+width/2, y+height/2);
            break;
        case SOUTH_WEST:
        	g2d.rotate(-(Math.PI*3/4), x+width/2, y+height/2);
            break;
        case WEST:
        	g2d.rotate(-(Math.PI/2), x+width/2, y+height/2);
            break;
        case NORTH_WEST:
        	g2d.rotate(-(Math.PI/4), x+width/2, y+height/2);
            break;
		default: { /* no xform */ }
		}
		g2d.draw(new Line2D.Float((17f*width/24)+x, (11f*height/24)+y, (12f*width/24)+x, ( 6f*height/24)+y));
		g2d.draw(new Line2D.Float((12f*width/24)+x, ( 6f*height/24)+y, ( 7f*width/24)+x, (11f*height/24)+y));
		g2d.draw(new Line2D.Float((17f*width/24)+x, (18f*height/24)+y, (12f*width/24)+x, (13f*height/24)+y));
		g2d.draw(new Line2D.Float((12f*width/24)+x, (13f*height/24)+y, ( 7f*width/24)+x, (18f*height/24)+y));

		g2d.setTransform(saveAT);
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	// visual test: use GraphicsUtilitiesVisualCheck

}
