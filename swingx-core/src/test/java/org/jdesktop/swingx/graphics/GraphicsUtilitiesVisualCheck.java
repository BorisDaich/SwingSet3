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
import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.icon.ChevronsIcon;
import org.jdesktop.swingx.icon.CircleIcon;
import org.jdesktop.swingx.icon.PlayIcon;
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
     * show CircleIcon different sizes and colors
     */
    public void interactiveCircleIcon() throws Exception {
    	CircleIcon redS;
    	redS = new CircleIcon(SizingConstants.SMALL_ICON, CircleIcon.RED);
        JButton redSButton = new JButton("redS", redS);
        
    	CircleIcon redM;
    	redM = new CircleIcon(SizingConstants.ACTION_ICON, CircleIcon.RED);
        JButton redMButton = new JButton("redM", redM);
        
    	CircleIcon redXXL;
    	redXXL = new CircleIcon(SizingConstants.XXL, CircleIcon.RED);
        JButton redXXLButton = new JButton("redXXL", redXXL);
        
    	CircleIcon chevrons;
    	chevrons = new CircleIcon();
        JButton chevronsButton = new JButton("default", chevrons);
        
    	JPanel panel = new JPanel(new BorderLayout());
    	panel.add(redSButton, BorderLayout.WEST);
    	panel.add(redXXLButton, BorderLayout.EAST);
    	panel.add(chevronsButton, BorderLayout.CENTER);
    	panel.add(redMButton, BorderLayout.SOUTH);
        showInFrame(panel, "interactiveCircleIcon");
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
    	northChevrons = new ChevronsIcon(SizingConstants.ACTION_ICON); // SizingConstants.M
    	northChevrons.setDirection(SwingConstants.NORTH);
        JButton northButton = new JButton("north M", northChevrons);
        
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
        showInFrame(panel, "interactiveChevronsIcon");
    }
    /**
     * show ArrowIcon with all compass directions 
     * and different sizes and colors
     */
    public void interactiveArrowIcon() throws Exception {
    	ArrowIcon south;
    	south = new ArrowIcon(SizingConstants.SMALL_ICON);
    	south.setDirection(SwingConstants.SOUTH);
        JButton southButton = new JButton("south S", south);
        
        ArrowIcon north;
    	north = new ArrowIcon(SizingConstants.ACTION_ICON); // SizingConstants.M
    	north.setDirection(SwingConstants.NORTH);
        JButton northButton = new JButton("north M", north);
        
        ArrowIcon west;
    	west = new ArrowIcon(SizingConstants.XXL, Color.BLUE);
    	west.setDirection(SwingConstants.WEST);
        JButton westButton = new JButton("west XXL", west);
        
        ArrowIcon east;
    	east = new ArrowIcon(SizingConstants.XXL, Color.RED);
    	east.setDirection(SwingConstants.EAST);
        JButton eastButton = new JButton("east XXL", east);
        
        ArrowIcon northwest;
    	northwest = new ArrowIcon(SizingConstants.XL, Color.BLUE);
    	northwest.setDirection(SwingConstants.NORTH_WEST);
        JButton northwestButton = new JButton("north west XL", northwest);
        
        ArrowIcon northeast;
    	northeast = new ArrowIcon(SizingConstants.XL, Color.RED);
    	northeast.setDirection(SwingConstants.NORTH_EAST);
        JButton northeastButton = new JButton("north east XL", northeast);
        
        ArrowIcon southwest;
    	southwest = new ArrowIcon(SizingConstants.L, Color.BLUE);
    	southwest.setDirection(SwingConstants.SOUTH_WEST);
        JButton southwestButton = new JButton("south west L", southwest);
        
        ArrowIcon southeast;
    	southeast = new ArrowIcon(SizingConstants.L, Color.RED);
    	southeast.setDirection(SwingConstants.SOUTH_EAST);
        JButton southeastButton = new JButton("south east L", southeast);
        
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel northpanel = new JPanel(new BorderLayout());
    	JPanel southpanel = new JPanel(new BorderLayout());
    	panel.add(new JLabel(" arrows, blue in west, red in east "), BorderLayout.CENTER);
    	northpanel.add(northButton, BorderLayout.CENTER);
    	northpanel.add(northwestButton, BorderLayout.WEST);
    	northpanel.add(northeastButton, BorderLayout.EAST);
    	panel.add(northpanel, BorderLayout.NORTH);
    	
    	southpanel.add(southButton, BorderLayout.CENTER);
    	southpanel.add(southwestButton, BorderLayout.WEST);
    	southpanel.add(southeastButton, BorderLayout.EAST);
    	panel.add(southpanel, BorderLayout.SOUTH);
    	
    	panel.add(westButton, BorderLayout.WEST);
    	panel.add(eastButton, BorderLayout.EAST);
        showInFrame(panel, "interactiveArrowIcon");
    }
    
    /**
     * show PlayIcon, a subclass of ArrowIcon with different sizes and colors
     */
    public void interactivePlayIcon() throws Exception {
    	PlayIcon south;
    	south = new PlayIcon(SizingConstants.SMALL_ICON);
    	south.setDirection(SwingConstants.SOUTH);
        JButton southButton = new JButton("south S", south);
        
        PlayIcon north;
    	north = new PlayIcon(SizingConstants.ACTION_ICON); // SizingConstants.M
    	north.setDirection(SwingConstants.NORTH);
        JButton northButton = new JButton("north M", north);
        
        PlayIcon west;
    	west = new PlayIcon(SizingConstants.XXL, Color.BLUE);
    	west.setDirection(SwingConstants.WEST);
        JButton westButton = new JButton("west XXL", west);
        
        PlayIcon east;
    	east = new PlayIcon(SizingConstants.XXL, Color.RED);
    	east.setDirection(SwingConstants.EAST);
        JButton eastButton = new JButton("east XXL", east);
        
        PlayIcon northwest;
    	northwest = new PlayIcon(SizingConstants.XL, Color.BLUE);
    	northwest.setDirection(SwingConstants.NORTH_WEST);
        JButton northwestButton = new JButton("north west XL", northwest);
        
        PlayIcon northeast;
    	northeast = new PlayIcon(SizingConstants.XL, Color.RED);
    	northeast.setDirection(SwingConstants.NORTH_EAST);
        JButton northeastButton = new JButton("north east XL", northeast);
        
        PlayIcon southwest;
    	southwest = new PlayIcon(SizingConstants.L, Color.BLUE);
    	southwest.setDirection(SwingConstants.SOUTH_WEST);
        JButton southwestButton = new JButton("south west L", southwest);
        
        PlayIcon southeast;
    	southeast = new PlayIcon(SizingConstants.L, Color.RED);
    	southeast.setDirection(SwingConstants.SOUTH_EAST);
        JButton southeastButton = new JButton("south east L", southeast);
        
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel northpanel = new JPanel(new BorderLayout());
    	JPanel southpanel = new JPanel(new BorderLayout());
    	panel.add(new JLabel(" PlayIcon, blue in west, red in east "), BorderLayout.CENTER);
    	northpanel.add(northButton, BorderLayout.CENTER);
    	northpanel.add(northwestButton, BorderLayout.WEST);
    	northpanel.add(northeastButton, BorderLayout.EAST);
    	panel.add(northpanel, BorderLayout.NORTH);
    	
    	southpanel.add(southButton, BorderLayout.CENTER);
    	southpanel.add(southwestButton, BorderLayout.WEST);
    	southpanel.add(southeastButton, BorderLayout.EAST);
    	panel.add(southpanel, BorderLayout.SOUTH);
    	
    	panel.add(westButton, BorderLayout.WEST);
    	panel.add(eastButton, BorderLayout.EAST);
        showInFrame(panel, "interactivePlayIcon");

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
        showInFrame(ipa, "interactiveMistargetedKeyStrokes");
    }

    /**
     * do nothing test - keep the test runner happy.
     */
    public void testDummy() {
    }

}
