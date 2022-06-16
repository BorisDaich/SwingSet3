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
import java.awt.geom.Path2D;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering an Arrow icon (an arrow subclass PlayIcon is used for Players). 
 * Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
/*
 * replaces (swingx-demos) com.sun.swingset3.utilities.ArrowIcon
 */
public class ArrowIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;
    private boolean filled = false; // to be used for PlayIcon
    protected void setFilled(boolean filled) {
    	this.filled = filled;
    }
    private int direction = EAST; // Compass-direction EAST == Orientation.RIGHT
    /**
     * You can change the orientation from DOWN to UP
     * @param direction Compass-direction SOUTH == Orientation.DOWN
     */
    public void setDirection(int direction) {
    	this.direction = direction;
    }

    public ArrowIcon() {
    }

    public ArrowIcon(int direction, int size, Color color) {
    	setDirection(direction);
    	width = size;
    	height = size;
    	this.color = color;
    }
    
    public ArrowIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public ArrowIcon(int size) {
    	this(size, null);
    }
    
    protected ArrowIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected ArrowIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc}
     */
    /* example arrow up:
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
  <line x1="12" y1="19" x2="12" y2="5" />
  <polyline points="5 12 12 5 19 12" />
</svg> 
     * </code></pre>
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color==null ? c.getForeground() : color);
		
		// creates a solid stroke with line width is 2
		Stroke stroke = new BasicStroke(2f*width/24, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND); // default is CAP_SQUARE, JOIN_MITER
		g2d.setStroke(stroke);
		
		AffineTransform saveAT = g2d.getTransform();
		g2d.rotate(Math.PI*(direction-1)/4, x+width/2, y+height/2);
		if(filled) {
			Path2D.Float arrowShape = new Path2D.Float();
			arrowShape.moveTo(( 3f*width/24)+x, (17f*height/24)+y);
			arrowShape.lineTo((13f*width/24)+x, ( 1f*height/24)+y);
			arrowShape.lineTo((21f*width/24)+x, (17f*height/24)+y);
			arrowShape.lineTo(( 3f*width/24)+x, (17f*height/24)+y);
			g2d.fill(arrowShape);
		} else {
			g2d.draw(new Line2D.Float((12f*width/24)+x, (19f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));
			g2d.draw(new Line2D.Float(( 5f*width/24)+x, (12f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));
			g2d.draw(new Line2D.Float((19f*width/24)+x, (12f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));
		}

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
//    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        JPanel panel = new JPanel();
//        frame.add(panel);
//        
//        // dedault size = SizingConstants.ACTION_ICON:
//        panel.add(new JLabel("north", new ArrowIcon(NORTH, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
//        panel.add(new JLabel("west", new ArrowIcon(WEST, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
//        panel.add(new JLabel("south", new ArrowIcon(SOUTH, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
//        panel.add(new JLabel("east", new ArrowIcon(EAST, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
//        // zum Vergleich: PlayIcon ist dunkler, schärfer und spitzer:
//        panel.add(new JLabel("PlayIcon", new PlayIcon(), JLabel.CENTER));
//        panel.add(new JLabel("east-10", new ArrowIcon(EAST, SizingConstants.XS, Color.blue), JLabel.CENTER));
//        
//        frame.pack();
//        frame.setVisible(true);
//    }

}
