/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
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

/**
 * Icon class for rendering icon which indicates user control of column visibility.
 * @author Amy Fowler
 * @version 1.0
 * @author EUG (support sizes and color)
 */
public class ColumnControlIcon implements Icon, UIResource, SizingConstants {
	
    private int width = SizingConstants.XS;
    private int height = SizingConstants.XS;
    private Color color;

    public ColumnControlIcon() {
    }

    public ColumnControlIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }

    public ColumnControlIcon(int size) {
    	this(size, null);
    }

    protected ColumnControlIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected ColumnControlIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    // implements interface Icon:
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color==null ? c.getForeground() : color);

        // draw horizontal lines
        g.drawLine(x, y, x+(int)(width*0.8), y);
        g.drawLine(x, y+(int)(height*0.2), x+(int)(width*0.8), y+(int)(height*0.2));
        g.drawLine(x, y+(int)(height*0.8), x+(int)(width*0.2), y+(int)(height*0.8));

        // draw vertical lines
        g.drawLine(x, y+(int)(height*0.1), x, y+(int)(width*0.7));
        g.drawLine(x+(int)(width*0.4), y+(int)(height*0.1), x+(int)(width*0.4), y+(int)(height*0.4));
        g.drawLine(x+(int)(width*0.8), y+(int)(height*0.1), x+(int)(width*0.8), y+(int)(height*0.4));

//        // draw arrow
        g.drawLine(x+(int)(width*0.3), y+(int)(height*0.6), x+(int)(width*0.9), y+(int)(height*0.6));
        g.drawLine(x+(int)(width*0.4), y+(int)(height*0.7), x+(int)(width*0.8), y+(int)(height*0.7));
        g.drawLine(x+(int)(width*0.5), y+(int)(height*0.8), x+(int)(width*0.7), y+(int)(height*0.8));
        g.drawLine(x+(int)(width*0.6), y+(int)(height*0.9), x+(int)(width*0.6), y+(int)(height*0.9));

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


    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ColumnControlIcon());
        frame.getContentPane().add(BorderLayout.NORTH, label);
        JLabel biglabel = new JLabel(new ColumnControlIcon(XXL, Color.RED));
        frame.getContentPane().add(BorderLayout.CENTER, biglabel);
        frame.pack();
        frame.setVisible(true);  
    }

}
