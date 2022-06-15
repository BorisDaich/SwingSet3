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
package org.jdesktop.swingx.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.icon.ChevronsIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.util.GraphicsUtilities;

/**
 * GraphicsUtilities visual checks.
 * 
 * @author rah003
 */
public class GraphicsUtilitiesVisualCheck extends InteractiveTestCase {

    public static void main(String[] args) {
        GraphicsUtilitiesVisualCheck test = new GraphicsUtilitiesVisualCheck();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

    /**
     * issue https://github.com/homebeaver/SwingSet/issues/22
     * show chevrons with all compass directions 
     * and different sizes and colors
     */
    public void interactiveChevronsIcon() throws Exception {
    	ChevronsIcon chevrons;
    	chevrons = new ChevronsIcon();
        JButton chevronsButton = new JButton("south", chevrons);
        
    	ChevronsIcon northChevrons;
    	northChevrons = new ChevronsIcon();
    	northChevrons.setDirection(SwingConstants.NORTH);
        JButton northButton = new JButton("north", northChevrons);
        
    	ChevronsIcon westChevrons;
    	westChevrons = new ChevronsIcon(SizingConstants.XXL, Color.BLUE);
    	westChevrons.setDirection(SwingConstants.WEST);
        JButton westButton = new JButton("west XXL", westChevrons);
        
    	ChevronsIcon eastChevrons;
    	eastChevrons = new ChevronsIcon(SizingConstants.XXL, Color.RED);
    	eastChevrons.setDirection(SwingConstants.EAST);
        JButton eastButton = new JButton("east XXL", eastChevrons);
        
    	ChevronsIcon northwestChevrons;
    	northwestChevrons = new ChevronsIcon(SizingConstants.XL, Color.BLUE);
    	northwestChevrons.setDirection(SwingConstants.NORTH_WEST);
        JButton northwestButton = new JButton("north west XL", northwestChevrons);
        
    	ChevronsIcon northeastChevrons;
    	northeastChevrons = new ChevronsIcon(SizingConstants.XL, Color.RED);
    	northeastChevrons.setDirection(SwingConstants.NORTH_EAST);
        JButton northeastButton = new JButton("north east XL", northeastChevrons);
        
    	ChevronsIcon southwestChevrons;
    	southwestChevrons = new ChevronsIcon(SizingConstants.L, Color.BLUE);
    	southwestChevrons.setDirection(SwingConstants.SOUTH_WEST);
        JButton southwestButton = new JButton("south west L", southwestChevrons);
        
    	ChevronsIcon southeastChevrons;
    	southeastChevrons = new ChevronsIcon(SizingConstants.L, Color.RED);
    	southeastChevrons.setDirection(SwingConstants.SOUTH_EAST);
        JButton southeastButton = new JButton("south east L", southeastChevrons);
        
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel northpanel = new JPanel(new BorderLayout());
    	JPanel southpanel = new JPanel(new BorderLayout());
    	//chevrons.paintIcon(component, graphics, x, y);
    	panel.add(new JLabel(" chevrons, blue in west, red in east "), BorderLayout.CENTER);
    	northpanel.add(northButton, BorderLayout.CENTER);
    	northpanel.add(northwestButton, BorderLayout.WEST);
    	northpanel.add(northeastButton, BorderLayout.EAST);
    	panel.add(northpanel, BorderLayout.NORTH);
    	
    	southpanel.add(chevronsButton, BorderLayout.CENTER);
    	southpanel.add(southwestButton, BorderLayout.WEST);
    	southpanel.add(southeastButton, BorderLayout.EAST);
    	panel.add(southpanel, BorderLayout.SOUTH);
    	
    	panel.add(westButton, BorderLayout.WEST);
    	panel.add(eastButton, BorderLayout.EAST);
        showInFrame(panel, "default - for debugging only");
    }
    
    /**
     * Issue #524-swingx: Thumbnails not generated correctly.
     * 
     */
    public void interactiveMistargetedKeyStrokes() throws Exception {
        BufferedImage im = ImageIO.read(getClass().getResourceAsStream(
                "/org/jdesktop/swingx/resources/images/500by500.png"));
        System.out.println("size:" + im.getWidth() + ", " + im.getHeight());
        BufferedImage im2 = GraphicsUtilities.createThumbnail(im, 100);
        JXImageView ipa = new JXImageView();
        ipa.setImage(im2);
        showInFrame(ipa, "default - for debugging only");
    }

    /**
     * do nothing test - keep the test runner happy.
     */
    public void testDummy() {
    }

}
