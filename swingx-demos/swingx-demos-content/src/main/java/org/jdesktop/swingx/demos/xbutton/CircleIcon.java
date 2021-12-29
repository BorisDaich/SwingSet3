package org.jdesktop.swingx.demos.xbutton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

// UIResource hat keine Methoden
// SizingConstants extends SwingConstants
public class CircleIcon  implements Icon, UIResource, SizingConstants {
	
    private int width = SizingConstants.M;
    private int height = SizingConstants.M;
    private Color color;

    public CircleIcon() {
    	this(SizingConstants.M, null);
    }
    public CircleIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }
    
    // implements interface Icon:
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
//		Graphics2D g2 = (Graphics2D) g;
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
        JLabel label = new JLabel(new CircleIcon(SizingConstants.XL, Color.RED));
        frame.getContentPane().setSize(300, 300);
        frame.getContentPane().add(BorderLayout.CENTER, label);
        frame.pack();
        frame.setVisible(true);  
    }

}
