package org.jdesktop.swingx.demos.xbutton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

public class StoppIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.M;
    private int height = SizingConstants.M;
    private Color color;

    public StoppIcon() {
    	this(SizingConstants.M, null);
    }
    public StoppIcon(int size, Color color) {
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
		g.setColor(color==null ? c.getForeground() : color);
		g.fillRect(width/4, height/4, width/2, height/2);
		g.dispose();
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
        JLabel label = new JLabel(new StoppIcon(SizingConstants.XL, Color.RED));
        frame.getContentPane().setSize(300, 300);
        frame.getContentPane().add(BorderLayout.CENTER, label);
        frame.pack();
        frame.setVisible(true);  
    }

}
