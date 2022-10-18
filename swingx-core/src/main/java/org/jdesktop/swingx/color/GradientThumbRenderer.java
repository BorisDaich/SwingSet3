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
package org.jdesktop.swingx.color;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.multislider.ThumbRenderer;
import org.jdesktop.swingx.util.PaintUtils;

/**
 * A GradientThumbRenderer, together with GradientTrackRenderer
 * used in JXGradientChooser for Colors
 * 
 * @author jm158417 Joshua Marinacci joshy
 * @author EUG https://github.com/homebeaver/
 */
public class GradientThumbRenderer extends JComponent implements ThumbRenderer {

    private static final Logger LOG = Logger.getLogger(GradientThumbRenderer.class.getName());

//    private Image thumb_black;
//    private Image thumb_gray;
    private RadianceIcon arrowIcon;
    private RadianceIcon selectedIcon;

    /**
     * ctor
     */
    public GradientThumbRenderer() {
        super();
    
        arrowIcon = ArrowIcon.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON);
        arrowIcon.setRotation(SwingConstants.SOUTH);
        selectedIcon = PlayIcon.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON);
        selectedIcon.setRotation(Math.toRadians(90d));
        LOG.fine("ctor die icon png fehlen, daher ArrowIcon "+arrowIcon);
    }
    
    private boolean selected;
    
    /* Invoked by Swing to draw components. Overrides JComponent.paint
     * zuerst JComponent.paint, dann this.paintComponent
     * - nur so ist der Track vollständig zu sehen
     */
    @Override
    public void paint(Graphics g) {
    	if(selected) {
    		selectedIcon.paintIcon(this, g, 0, 0);
    	} else {
    		arrowIcon.paintIcon(this, g, 0, 0);
    	}
    }
//    @Override
//    protected void paintComponent(Graphics g) {
//        JComponent thumb = this;
//        int w = thumb.getWidth();
//        g.setColor(getForeground());
//        g.fillRect(0, 0, w - 1, w - 1);
//        if (selected) {
//            g.drawImage(thumb_black, 0, 0, null);
//        } else {
//            g.drawImage(thumb_gray, 0, 0, null);
//        }
//    }

    public JComponent getThumbRendererComponent(JXMultiThumbSlider<?> slider, int index, boolean selected) {
        Color c = (Color)slider.getModel().getThumbAt(index).getObject();
        c = PaintUtils.removeAlpha(c);
        this.setForeground(c);
        this.selected = selected;
        return this;
    }
}
