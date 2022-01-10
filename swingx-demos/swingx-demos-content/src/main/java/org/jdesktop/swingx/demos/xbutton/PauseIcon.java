package org.jdesktop.swingx.demos.xbutton;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

public class PauseIcon implements Icon, UIResource, SizingConstants {

	// TODO Stopp Pause Play
    private int width = SizingConstants.M;
    private int height = SizingConstants.M;
    private Color color;
    private BasicStroke stroke = new BasicStroke(2);

    public PauseIcon() {
    	this(SizingConstants.M, null);
    }
    public PauseIcon(int size, Color color) {
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
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(color==null ? c.getForeground() : color);
		g2d.setStroke(stroke);
		g2d.fillRect(x + (2 * width / 9), y + 4, (2 * width / 9), height - 8);
		g2d.fillRect(x + 5 * width / 9, y + 4, (2 * width / 9), height - 8);
		g2d.dispose();		
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
        JLabel label = new JLabel(new PauseIcon(SizingConstants.XL, Color.RED));
        frame.getContentPane().setSize(300, 300);
        frame.getContentPane().add(BorderLayout.CENTER, label);
        frame.pack();
        frame.setVisible(true);  
    }

}
