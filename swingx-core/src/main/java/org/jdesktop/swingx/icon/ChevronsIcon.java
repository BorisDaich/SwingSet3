package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Chevron icon (v). Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
public class ChevronsIcon implements Icon, UIResource, SizingConstants {

	public enum Orientation {
		/* this is the default for chevrons : double v */
		DOWN,
		// TODO:
//		RIGHT,
//		LEFT,
		/* the reverse to DOWN */
		UP
	}
	
//	private static final boolean UP = false; // default chevrons are down: v
    private int width = SizingConstants.SMALL_ICON;
    private int height = SizingConstants.SMALL_ICON;
    private Color color;
    
    private Orientation orientation = Orientation.DOWN;
    public void setOrientation(Orientation orientation) {
    	this.orientation = orientation;
    }

    public ChevronsIcon() {
    }

    public ChevronsIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public ChevronsIcon(int size) {
    	this(size, null);
    }

    protected ChevronsIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected ChevronsIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc}
     * 
chevrons up:
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
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color==null ? c.getForeground() : color);
        if (orientation==Orientation.UP) {
        	g.drawLine(x+(int)(17*width/24), y+(int)(11*height/24), x+(int)(12*width/24), y+(int)( 6*height/24));
        	g.drawLine(x+(int)(12*width/24), y+(int)( 6*height/24), x+(int)( 7*width/24), y+(int)(11*height/24));
        	g.drawLine(x+(int)(17*width/24), y+(int)(18*height/24), x+(int)(12*width/24), y+(int)(13*height/24));
        	g.drawLine(x+(int)(12*width/24), y+(int)(13*height/24), x+(int)( 7*width/24), y+(int)(18*height/24));
        } else {
// nicht schön      	
//            g.drawLine(x                 , y                  , x+(int)(width*0.5), y+(int)(height*0.9)); // \
//            g.drawLine(x+(int)(width*0.5), y+(int)(height*0.9), x+width           , y                  ); //  /
//            g.drawLine(x+(int)(width*0.1), y                  , x+(int)(width*0.5), y+(int)(height*0.8)); // \
//            g.drawLine(x+(int)(width*0.5), y+(int)(height*0.8), x+(int)(width*0.9), y                  ); //  /
        	g.drawLine(x+(int)( 7*width/24), y+(int)(13*height/24), x+(int)(12*width/24), y+(int)(18*height/24));
        	g.drawLine(x+(int)(12*width/24), y+(int)(18*height/24), x+(int)(17*width/24), y+(int)(13*height/24));
        	g.drawLine(x+(int)( 7*width/24), y+(int)( 6*height/24), x+(int)(12*width/24), y+(int)(11*height/24));
        	g.drawLine(x+(int)(12*width/24), y+(int)(11*height/24), x+(int)(17*width/24), y+(int)( 6*height/24));
        }
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

	// visual test:
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ChevronsIcon(SizingConstants.XL, Color.RED));
        frame.getContentPane().setSize(300, 300);
        frame.getContentPane().add(java.awt.BorderLayout.CENTER, label);
        frame.pack();
        frame.setVisible(true);  
    }

}
