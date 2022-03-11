package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

/**
 * Rendering a Calendar icon.
 * Used in Date Picker.
 *
 * @author EUG https://github.com/homebeaver/
 */
public class CalendarIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.SMALL_ICON;
    private int height = SizingConstants.SMALL_ICON;
    private Color color;
    
    /**
     * ctor for default - this is a SMALL_ICON for Date Picker
     */
    public CalendarIcon() {
    }

    /**
     * ctor with icon size and color
     * @param size width and height
     * @param color Foreground Color
     */
    public CalendarIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    /**
     * ctor with icon size
     * @param size width and height
     */
    public CalendarIcon(int size) {
    	this(size, null);
    }

    /**
     * optional ctor with width and height
     * @param width of the icon
     * @param height of the icon
     */
    protected CalendarIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * convenient ctor with Dimension
     * @param size Dimension
     */
    protected CalendarIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:

    /**
     * {@inheritDoc} 
     */
    /* example:
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
  <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
  <line x1="16" y1="2" x2="16" y2="6" />
  <line x1="8" y1="2" x2="8" y2="6" />
  <line x1="3" y1="10" x2="21" y2="10" />
</svg>      * </code></pre>
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color==null ? c.getForeground() : color);
    	g.drawRect(x+(int)( 3*width/24), y+(int)( 4*height/24),   (int)(18*width/24),   (int)(18*height/24));
//    	g.drawLine(x+(int)( 3*width/24), y+(int)( 4*height/24), x+(int)( 3*width/24), y+(int)(20*height/24));
//    	g.drawLine(x+(int)( 3*width/24), y+(int)(20*height/24), x+(int)(21*width/24), y+(int)(20*height/24));
//    	g.drawLine(x+(int)(21*width/24), y+(int)(20*height/24), x+(int)(21*width/24), y+(int)( 4*height/24));
//    	g.drawLine(x+(int)(21*width/24), y+(int)( 4*height/24), x+(int)( 3*width/24), y+(int)( 4*height/24));
    	g.drawLine(x+(int)(16*width/24), y+(int)( 2*height/24), x+(int)(16*width/24), y+(int)( 6*height/24));
    	g.drawLine(x+(int)( 8*width/24), y+(int)( 2*height/24), x+(int)( 8*width/24), y+(int)( 6*height/24));
    	g.drawLine(x+(int)( 3*width/24), y+(int)(10*height/24), x+(int)(21*width/24), y+(int)(10*height/24));
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
//    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JLabel label = new JLabel(new CalendarIcon(SizingConstants.XL));
//        frame.getContentPane().setSize(300, 300);
//        frame.getContentPane().add(java.awt.BorderLayout.CENTER, label);
//        frame.pack();
//        frame.setVisible(true);  
//    }

}
