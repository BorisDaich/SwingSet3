package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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

	/**
	 * Orientation aka Direction - PlayIcon defaults to RIGHT / EAST
	 */
	public enum Direction {
		/** RIGHT or EAST, defaults for PlayIcon */
		EAST,
		/** LEFT or WEST */
		WEST,
		/** UP or NORTH */
		NORTH,
		/** DOWN or SOUTH, the reverse to NORTH,UP */
		SOUTH
	}

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;
    private Direction direction = Direction.EAST;
    private BufferedImage arrowImage;

    public ArrowIcon() {
    }

    public ArrowIcon(Direction direction, int size, Color color) {
    	this.direction = direction;
    	width = size;
    	height = size;
    	this.color = color;
    }
    
    public ArrowIcon(Direction direction) {
    	this.direction = direction;
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

    protected Image getArrowImage() {
        if (arrowImage == null) {
        	int size = getIconWidth();
            arrowImage = GraphicsUtilities.createCompatibleTranslucentImage(size, size);
            AffineTransform atx = direction != Direction.SOUTH? new AffineTransform() : null;
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
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(getArrowImage(), x, y, c);
//		Graphics2D g2d = (Graphics2D) g;
//
//		g2d.setColor(color==null ? c.getForeground() : color);
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
        panel.add(new JLabel("north", new ArrowIcon(Direction.NORTH), JLabel.CENTER));
        panel.add(new JLabel("west", new ArrowIcon(Direction.WEST), JLabel.CENTER));
        panel.add(new JLabel("south", new ArrowIcon(Direction.SOUTH), JLabel.CENTER));
        panel.add(new JLabel("east", new ArrowIcon(Direction.EAST), JLabel.CENTER));
        // zum Vergleich: PlayIcon ist dunkler, schärfer und spitzer:
        panel.add(new JLabel("PlayIcon", new PlayIcon(), JLabel.CENTER));
        panel.add(new JLabel("east-10", new ArrowIcon(Direction.EAST, SizingConstants.XS, Color.blue), JLabel.CENTER));
        
        frame.pack();
        frame.setVisible(true);
    }

}
