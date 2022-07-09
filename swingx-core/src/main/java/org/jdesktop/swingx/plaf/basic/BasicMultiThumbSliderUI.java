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
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.multislider.Thumb;
import org.jdesktop.swingx.multislider.ThumbRenderer;
import org.jdesktop.swingx.multislider.TrackRenderer;
import org.jdesktop.swingx.plaf.MultiThumbSliderUI;

/**
 * Basic implementation of MultiThumbSliderUI
 * with inner classes
 *  BasicThumbRenderer and BasicTrackRenderer
 *  
 * @author m158417 Joshua Marinacci joshy
 */
public class BasicMultiThumbSliderUI extends MultiThumbSliderUI {
    
	private static final Logger LOG = Logger.getLogger(BasicMultiThumbSliderUI.class.getName());

	/** the slider for this UI */
    protected JXMultiThumbSlider<?> slider;
    
    /**
     * factory
     * @param c JComponent not used
     * @return new instance of BasicMultiThumbSliderUI
     */
    public static ComponentUI createUI(JComponent c) {
        return new BasicMultiThumbSliderUI();
    }
    
    @Override
    public void installUI(JComponent c) {
    	LOG.info("install BasicThumbRenderer and BasicTrackRenderer");
        slider = (JXMultiThumbSlider<?>)c;
        slider.setThumbRenderer(new BasicThumbRenderer());
        slider.setTrackRenderer(new BasicTrackRenderer());        
    }
    @Override
    public void uninstallUI(JComponent c) {
        slider = null;
    }

    private class BasicThumbRenderer extends JComponent implements ThumbRenderer {
    	
    	boolean selected;
        
        public BasicThumbRenderer() {
        	super();
            LOG.info("ctor isOpaque="+isOpaque());
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(selected ? Color.GREEN : Color.RED);
            JComponent thumb = this;
            // paint a green/red dimond:
            Polygon poly = new Polygon();
            poly.addPoint(thumb.getWidth()/2, 0);
            poly.addPoint(0, thumb.getHeight()/2);
            poly.addPoint(thumb.getWidth()/2, thumb.getHeight());
            poly.addPoint(thumb.getWidth(), thumb.getHeight()/2);
            g.fillPolygon(poly);
        }

        /**
         * {@inheritDoc}
         */
        @Override // implements ThumbRenderer
        public JComponent getThumbRendererComponent(JXMultiThumbSlider<?> slider, int index, boolean selected) {
        	this.selected = selected;
            return this;
        }
    }

    private class BasicTrackRenderer extends JComponent implements TrackRenderer {
    	
        private JXMultiThumbSlider<?> slider = null;
        
        BasicTrackRenderer() {
        	super();
//        	super.setOpaque(true);
            LOG.info("BasicTrackRenderer ctor isOpaque="+isOpaque());
        }
        
        /* Invoked by Swing to draw components. Overrides JComponent.paint
         */
        @Override
        public void paint(Graphics g) {
            paintComponent(g);
        }
        @Override
        public void paintComponent(Graphics g) {
            g.setColor(slider.getBackground());
            int h = slider.getHeight();
            int w = slider.getWidth();
            g.fillRect(0, 0, w, h); // paints a filled rectangle with Background color of slider
            // draw the track: two horizontal lines
//            g.setColor(Color.black);
            g.setColor(slider.getForeground());
            g.drawLine(0, (int)(0+h/2), w, (int)(0+h/2));
            g.drawLine(0, (int)(1+h/2), w, (int)(1+h/2));
        }

        /**
         * {@inheritDoc}
         */
        @Override // implements TrackRenderer
        public JComponent getRendererComponent(JXMultiThumbSlider<?> slider) {
            this.slider = slider;
            return this;
        }
    }
}
