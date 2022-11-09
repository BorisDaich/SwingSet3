/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Painter;

/**
 * Impelmentation of <a href="https://jdesktop.wordpress.com/2022/11/09/jxicon/">JXIcon interface</a>
 * 
 * @author EUG https://github.com/homebeaver (rotation + point/axis reflection)
 */
public class PainterIcon implements JXIcon {
	
    private int width;
    private int height;
    
	// implement point/axis reflection
    private int rsfx = 1, rsfy = 1;
    /**
     * {@inheritDoc}
     */
    @Override
    public void setReflection(boolean horizontal, boolean vertical) {
    	this.rsfx = vertical ? -1 : 1;
    	this.rsfy = horizontal ? -1 : 1;
    }
    /**
     * override the default defined in JXIcon (no reflection)
     * @return true if there is any reflection, point reflection or mirroring
     */
    @Override
    public boolean isReflection() {
		return rsfx==-1 || rsfy==-1;
	}
    
    // implement rotation
    private double theta = 0;
    /**
     * {@inheritDoc}
     */
    public void setRotation(double theta) {
    	this.theta = theta;
    }    
    /**
     * override the constant default (no rotation) defined in JXIcon
     * @return the angle of rotation in radians
     */
    @Override
    public double getRotation() {
		return theta;
	}
    
    // we accept JComponent and Component, but not Container
    private Painter<? extends Component> painter;
    
    public PainterIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public PainterIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    }

    public Painter<? extends Component> getPainter() {
        return painter;
    }

    public void setPainter(Painter<? extends Component> painter) {
        this.painter = painter;
    }
   
    /**
     * {@inheritDoc}
     */
    // implements interface Icon: paintIcon(Component c, Graphics g, int x, int y);
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (getPainter() != null && g instanceof Graphics2D) {
        	Graphics2D g2d = (Graphics2D) g.create();

        	if(getRotation()!=0) {
                g2d.rotate(getRotation(), x+width/2, y+height/2);
            }
            if(isReflection()) {
            	g2d.translate(x+width/2, y+height/2);
            	g2d.scale(this.rsfx, this.rsfy);
            	g2d.translate(-x-width/2, -y-height/2);
            }

            try {
            	g2d.translate(x, y);
                Painter<Component> p = (Painter<Component>)getPainter();
                p.paint(g2d, c, width, height);
                g2d.translate(-x, -y);
            } finally {
            	g2d.dispose();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconHeight() {
        return height;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getIconWidth() {
        return width;
    }    

}