package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.UIResource;

public class StopIcon implements Icon, UIResource, SizingConstants {

    private int width = SizingConstants.ACTION_ICON;
    private int height = SizingConstants.ACTION_ICON;
    private Color color;

    public StopIcon() {
    }

    public StopIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public StopIcon(int size) {
    	this(size, null);
    }

    protected StopIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected StopIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
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
        JLabel label = new JLabel(new StopIcon(SizingConstants.XL, Color.RED));
        frame.getContentPane().setSize(300, 300);
        frame.getContentPane().add(BorderLayout.CENTER, label);
        frame.pack();
        frame.setVisible(true);  
    }

}
