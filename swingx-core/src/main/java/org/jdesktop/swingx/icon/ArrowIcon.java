package org.jdesktop.swingx.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.util.GraphicsUtilities;
import org.jdesktop.swingx.util.PaintUtils;

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

//	/**
//	 * Orientation aka Direction - PlayIcon defaults to RIGHT / EAST
//	 */
//	@Deprecated // wg. https://github.com/homebeaver/SwingSet/issues/22
//	public enum Direction {
//		/** RIGHT or EAST, defaults for PlayIcon */
//		EAST,
//		/** LEFT or WEST */
//		WEST,
//		/** UP or NORTH */
//		NORTH,
//		/** DOWN or SOUTH, the reverse to NORTH,UP */
//		SOUTH
//	}

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;
//    private Direction direction = Direction.EAST;
    private int direction = EAST; // Compass-direction EAST == Orientation.RIGHT
    /**
     * You can change the orientation from DOWN to UP
     * @param direction Compass-direction SOUTH == Orientation.DOWN
     */
    public void setDirection(int direction) {
    	this.direction = direction;
    }
    private BufferedImage arrowImage;

    public ArrowIcon() {
    }

    public ArrowIcon(int direction, int size, Color color) {
    	setDirection(direction);
    	width = size;
    	height = size;
    	this.color = color;
    }
    
//    public ArrowIcon(Direction direction) {
//    	this.direction = direction;
//    }

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

    protected Image getArrowImage() {
        if (arrowImage == null) {
        	int size = getIconWidth();
            arrowImage = GraphicsUtilities.createCompatibleTranslucentImage(size, size);
            AffineTransform atx = direction != SOUTH? new AffineTransform() : null;
            switch(direction ) {
                case NORTH:
                    atx.setToRotation(Math.PI, size/2, size/2);
                    break;
                case EAST:
                    atx.setToRotation(-(Math.PI/2), size/2, size/2);
                    break;
                case WEST:
                    atx.setToRotation(Math.PI/2, size/2, size/2);
                case SOUTH:
                default:{ /* no xform*/ }                   
            }       
            Graphics2D ig = (Graphics2D)arrowImage.getGraphics();
            if (atx != null) {
                ig.setTransform(atx);
            }
            int width = size;
            int height = size/2 + 1;
            int xx = (size - width)/2;
            int yy = (size - height + 1)/2;

            Color base = color != null? color : UIManager.getColor("controlDkShadow").darker(); 

            paintArrow(ig, base, xx, yy); // paintArrow(Graphics2D g, Color base, int x, int y)
            paintArrowBevel(ig, base, xx, yy);
            paintArrowBevel(ig, PaintUtils.deriveColorHSB(base, 0f, 0f, .20f), xx, yy + 1);
        }
        return arrowImage;
    }
    
    protected void paintArrow(Graphics2D g, Color base, int x, int y) {
        g.setColor(base);
        /*
        Path2D.Float arrowShape = new Path2D.Float();
        arrowShape.moveTo(x, y-1);
        System.out.println("moveTo "+(x)+","+(y-1));
        arrowShape.lineTo(size-1, y-1);
        System.out.println("lineTo "+(size-1)+","+(y-1));
        arrowShape.lineTo(size/2, y+(size/2));
        System.out.println("lineTo "+(size/2)+","+(y+(size/2)));
        arrowShape.lineTo(size/2 - 1, y+(size/2));
        System.out.println("lineTo "+ (size/2 - 1)+","+(y+(size/2)));
        arrowShape.lineTo(x, y-1);
        System.out.println("lineTo "+(x)+","+(y-1));
        g.fill(arrowShape);
*/       
        int len = getIconWidth() - 2;
        int xx = x;
        int yy = y-1;
        while (len >= 2) {
            xx++;
            yy++;
            g.fillRect(xx, yy, len, 1);
            len -= 2;
        }
    }

    private static final float DB = -.06f;
    protected void paintArrowBevel(Graphics g, Color base, int x, int y) {
        int len = getIconWidth();
        int xx = x;
        int yy = y;
        Color c2 = PaintUtils.deriveColorHSB(base, 0f, 0f, (-DB)*(getIconWidth()/2));
        while (len >= 2) {
            c2 = PaintUtils.deriveColorHSB(c2, 0f, 0f, DB);
            g.setColor(c2);
            g.fillRect(xx, yy, 1, 1);
            g.fillRect(xx + len - 1, yy, 1, 1);
            len -= 2;
            xx++;
            yy++;
        }
    }

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
//		switch (direction) {
//		case NORTH: // 1
//			g2d.rotate(0, x+width/2, y+height/2);
//            break;
//		case NORTH_EAST: // 2
//			g2d.rotate(Math.PI*(direction-1)/4, x+width/2, y+height/2);
//			break;
//		case EAST: // 3
//			g2d.rotate(Math.PI/2, x+width/2, y+height/2);
//			break;
//		case SOUTH: // 5
//			g2d.rotate(Math.PI, x+width/2, y+height/2);
//            break;
//        case WEST: // 7
//        	g2d.rotate(-(Math.PI/2), x+width/2, y+height/2);
//            break;
//		default: { /* no xform */ }
//		}
		g2d.rotate(Math.PI*(direction-1)/4, x+width/2, y+height/2);
		g2d.draw(new Line2D.Float((12f*width/24)+x, (19f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));
		g2d.draw(new Line2D.Float(( 5f*width/24)+x, (12f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));
		g2d.draw(new Line2D.Float((19f*width/24)+x, (12f*height/24)+y, (12f*width/24)+x, ( 5f*height/24)+y));

		g2d.setTransform(saveAT);
	}
	public void paintIconXXX(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color==null ? c.getForeground() : color);
		//g.drawImage(getArrowImage(), x, y, c);
		
		// creates a solid stroke with line width is 2
//		Stroke stroke = new BasicStroke(2f*width/24, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER); // default is CAP_SQUARE, JOIN_MITER
//		g2d.setStroke(stroke);
		
		AffineTransform saveAT = g2d.getTransform();
		switch (direction) {
		case NORTH: // 1
//	    	LOG.info("NORTH direction="+direction + " no rotation ");
			g2d.translate(0, -height/3);
			g2d.rotate(Math.PI, x+width/2, y+height/2);
            break;
		case EAST: // 3
			g2d.translate(width/3, 0);
        	g2d.rotate(-(Math.PI/2), x+width/2, y+height/2);
			break;
		case SOUTH: // 5
//	    	LOG.info("SOUTH direction="+direction + ", x="+x + ",y="+y);
//			g2d.rotate(Math.PI, x+width/2, y+height/2);
			g2d.translate(0, height/3);
            break;
        case WEST: // 7
			g2d.translate(-width/3, 0);
			g2d.rotate(Math.PI/2, x+width/2, y+height/2);
            break;
		default: { /* no xform */ }
		}

        Path2D.Float arrowShape = new Path2D.Float();
        arrowShape.moveTo(x, y-1);
//        arrowShape.moveTo(x, y-height/3);
        arrowShape.lineTo(width-1, y-1);
//        arrowShape.lineTo(width-1, y-height/3);
        arrowShape.lineTo(width/2, y+(height/2));
        arrowShape.lineTo(width/2 - 1, y+(height/2));
        arrowShape.lineTo(x, y-1);
        g2d.fill(arrowShape);
        
		g2d.setTransform(saveAT);

//		GeneralPath path = new GeneralPath();
//		path.moveTo(x + 2, y + 2);
//		path.lineTo(x + width - 2, y + height / 2);
//		path.lineTo(x + 2, y + height - 2);
//		path.lineTo(x + 2, y + 2);
//		g2d.fill(path);
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel);
        
        // dedault size = SizingConstants.ACTION_ICON:
        panel.add(new JLabel("north", new ArrowIcon(NORTH, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
        panel.add(new JLabel("west", new ArrowIcon(WEST, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
        panel.add(new JLabel("south", new ArrowIcon(SOUTH, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
        panel.add(new JLabel("east", new ArrowIcon(EAST, SizingConstants.ACTION_ICON, (Color)null), JLabel.CENTER));
        // zum Vergleich: PlayIcon ist dunkler, schärfer und spitzer:
        panel.add(new JLabel("PlayIcon", new PlayIcon(), JLabel.CENTER));
        panel.add(new JLabel("east-10", new ArrowIcon(EAST, SizingConstants.XS, Color.blue), JLabel.CENTER));
        
        frame.pack();
        frame.setVisible(true);
    }

}
