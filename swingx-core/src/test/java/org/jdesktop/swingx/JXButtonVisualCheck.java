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
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.icon.PauseIcon;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.image.FastBlurFilter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;

/**
 * Visual tests of JXButton issues.
 * @author rah003
 *
 */
//@SuppressWarnings("nls")
public class JXButtonVisualCheck extends InteractiveTestCase {

    private static final Logger LOG = Logger.getLogger(JXButtonVisualCheck.class.getName());
    private static final String[] buttonText = new String[] {"Hello", "Goodbye", "SwingLabs", "Turkey Bowl"};

    /**
     * Test for issue #761.
     */
    public void interactiveButton() {
    	JPanel center = new JPanel();
    	JButton pause = new JButton("ACTION_ICON size", PauseIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON));
    	center.add(pause);
    	
    	JPanel control = new JPanel();
        JButton b = new JButton("Start");
        control.add(b);
        
    	JPanel pane = new JPanel(new BorderLayout());
    	pane.add(center, BorderLayout.CENTER);
    	pane.add(control, BorderLayout.SOUTH);
    	
        JXFrame frame = wrapInFrame(pane, "Text Button (Start) at SOUTH");
        show(frame);
    }

    /**
     * Test for issue 849
     */
    public void interactiveActionButton() {
        @SuppressWarnings("serial")
		AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	LOG.info("//do nothing "+e);          
            }
        };
        action.putValue(Action.NAME, "My Action");
    	RadianceIcon pauseIcon = PauseIcon.of(RadianceIcon.LAUNCHER_ICON, RadianceIcon.LAUNCHER_ICON);
    	pauseIcon.setColorFilter(color -> Color.MAGENTA);
        action.putValue(Action.LARGE_ICON_KEY, pauseIcon);
        action.setEnabled(true);

        final JFrame f = new JFrame();
        f.setSize(600, 200);
        JPanel jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        
    	JButton pause = new JButton(action);
    	pause.setIcon(PauseIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON));
    	pause.setText("ACTION_ICON size");
    	jContentPane.add(pause, BorderLayout.WEST);
        
    	JButton xl = new JButton(action);
    	jContentPane.add(xl, BorderLayout.EAST);
        
        f.setContentPane(jContentPane);
        f.setTitle("ActionButtons");
        f.setVisible(true);
    }
    
    /**
     * SwingX Issue 1158
     */
    public void interactiveStatusBarCheck() {
        final JXButton button = new JXButton("Sample");
        MattePainter p = new MattePainter(PaintUtils.BLUE_EXPERIENCE, true);
        button.setForegroundPainter(p);
        BufferedImage im;
        try {
            im = ImageIO.read(JXButton.class.getResource("plaf/basic/resources/error16.png"));
        } catch (IOException ignore) {
            System.out.println(ignore);
            im = null;
        }
        button.setIcon(new ImageIcon(im));
        
        JXFrame frame = wrapInFrame(button, "StatusBarCheck");
        frame.setStatusBar(new JXStatusBar());
        show(frame);
    }
    
    public void interactiveForegroundCheck() {
        final JXButton button = new JXButton("Sample with ring text");
//        MattePainter p = new MattePainter(PaintUtils.AERITH, true);
        final MattePainter p = new MattePainter(PaintUtils.BLUE_EXPERIENCE, true);
        p.setFilters(new FastBlurFilter());
        button.setForegroundPainter(p);
        
        // Add action listener using Lambda expression
    	RingArray ringArray = new RingArray(buttonText);
    	button.addActionListener(event -> {
    		button.setText(ringArray.get());
        });

        BufferedImage im;
        try {
            im = ImageIO.read(JXButton.class.getResource("plaf/basic/resources/error16.png"));
        } catch (IOException ignore) {
            System.out.println(ignore);
            im = null;
        }
        button.setIcon(new ImageIcon(im));
        button.addMouseListener(new MouseAdapter() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                p.setFilters((BufferedImageOp[]) null);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseExited(MouseEvent e) {
                p.setFilters(new FastBlurFilter());
            }
            
        });
        
        showInFrame(button, "ForegroundCheck");
    }

    public void interactiveBackgroundCheck() {
        final JXButton button = new JXButton("PlayIcon with GradientPaint AERITH and ring text");
        MattePainter p = new MattePainter(PaintUtils.AERITH, true);
        button.setBackgroundPainter(p);
        
        // Add action listener using Lambda expression
    	RingArray ringArray = new RingArray(buttonText);
    	button.addActionListener(event -> {
    		LOG.info("button:"+button);
    		button.setText(ringArray.get());
        });
    	
    	RadianceIcon play = PlayIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
        button.setIcon(play);
        
        showInFrame(button, "BackgroundCheck");
    }
    
    /**
     * SWINGX-1449: Ensure that the font displays correctly when the background or background painter is set.
     */
    public void interactiveFontAndBackgroundCheck() {
        Font font = Font.decode("Arial-BOLDITALIC-14");
        Color background = Color.LIGHT_GRAY;
        Painter<Component> backgroundPainter = new MattePainter(background);

        JButton button1 = new JButton("Default");
        JButton button2 = new JButton("Font changed");
        button2.setFont(font);
        JButton button3 = new JButton("Background changed");
        button3.setBackground(background);
        JButton button4 = new JButton("Background changed");
        button4.setBackground(background);
//        button4.setBackgroundPainter(backgroundPainter);
        JButton button5 = new JButton("Font and Background changed");
        button5.setFont(font);
        button5.setBackground(background);
        JButton button6 = new JButton("Font and Background changed");
        button6.setFont(font);
        button6.setBackground(background);
//        button6.setBackgroundPainter(backgroundPainter);

        JXButton xbutton1 = new JXButton("Default");
        JXButton xbutton2 = new JXButton("Font changed");
        xbutton2.setFont(font);
        JXButton xbutton3 = new JXButton("Background changed");
        xbutton3.setBackground(background);
        JXButton xbutton4 = new JXButton("BackgroundPainter changed");
        xbutton4.setBackgroundPainter(backgroundPainter);
        JXButton xbutton5 = new JXButton("Font and Background changed");
        xbutton5.setFont(font);
        xbutton5.setBackground(background);
        JXButton xbutton6 = new JXButton("Font and BackgroundPainter changed");
        xbutton6.setFont(font);
        xbutton6.setBackgroundPainter(backgroundPainter);

        JPanel panel = new JPanel(new GridLayout(7, 2, 1, 1));
        panel.add(new JXLabel(JButton.class.getSimpleName(), SwingConstants.CENTER));
        panel.add(new JXLabel(JXButton.class.getSimpleName(), SwingConstants.CENTER));
        panel.add(button1);
        panel.add(xbutton1);
        panel.add(button2);
        panel.add(xbutton2);
        panel.add(button3);
        panel.add(xbutton3);
        panel.add(button4);
        panel.add(xbutton4);
        panel.add(button5);
        panel.add(xbutton5);
        panel.add(button6);
        panel.add(xbutton6);

        showInFrame(panel, "Font and Background Check");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        JXButtonVisualCheck test = new JXButtonVisualCheck();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("interactiveActionButton");
//            test.runInteractiveTests("interactiveFontAndBackgroundCheck");
//            test.runInteractiveTests("interactiveForegroundCheck");
//            test.runInteractiveTests("interactiveBackgroundCheck");
          } catch (Exception e) {
              System.err.println("exception when executing interactive tests:");
              e.printStackTrace();
          }
    }

    /**
     * do nothing test - keep the testrunner happy.
     */
    public void testDummy() {
    }

    private static class RingArray {

    	private int i;
    	private String[] alValues;

    	public RingArray(String[] values) {
    		alValues = values;
    		i = alValues.length;
    	}
    	
    	public String get() {
    		i++;
    		if (i >= alValues.length) {
    			i = 0;
    		}
    		return alValues[i];
    	}
    }

}
