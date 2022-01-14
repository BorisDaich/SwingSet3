/*
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.demos.xbutton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImageOp;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.application.Application;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.CircleIcon;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.image.FastBlurFilter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingxset.DefaultDemoPanel;
import org.jdesktop.swingxset.SwingXSet;

import com.sun.swingset3.DemoProperties;

/**
 * A demo for the {@code JXButton}.
 *
 * @author Karl George Schaefer (create)
 * @author Eugen Hanussek https://github.com/homebeaver (implemetation)
 */
@DemoProperties(
    value = "JXButton Demo",
    category = "Controls",
    description = "Demonstrates JXButton, an extended button control",
    sourceFiles = {
        "org/jdesktop/swingx/demos/xbutton/XButtonDemo.java",
        "org/jdesktop/swingx/demos/xbutton/resources/XButtonDemo.properties",
        "org/jdesktop/swingx/demos/xbutton/resources/XButtonDemo.html",
        "org/jdesktop/swingx/demos/xbutton/resources/images/XButtonDemo.png"
    }
)
@SuppressWarnings("serial")
public class XButtonDemo extends DefaultDemoPanel {
    
    private static final Logger LOG = Logger.getLogger(XButtonDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     */
    /*
     * damit diese Klasse als einzige im SwingXSet gestartet werden kann (Application.launch),
     * muss sie in einem file stehen (==>onlyXButtonDemo).
     * Dieses file wird dann vom DemoCreator eingelesen, "-a"/"-augment" erweitert den demo-Vorrat.
     */
    public static void main(String[] args) {
    	Application.launch(SwingXSet.class, new String[] {"META-INF/onlyXButtonDemo"});
    }

    public XButtonDemo() {
    	super();
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

    /*
     * aus JXButtonVisualCheck
     */
    private void interactiveFontAndBackgroundCheck() {
        Font font = Font.decode("Arial-BOLDITALIC-14");
//        Color background = Color.LIGHT_GRAY;
        Color background = new Color(168, 204, 241); // LIGHT_BLUE
        Color orangeBG = Color.ORANGE; // new Color(255, 200, 0);
        final Painter<Component> aerithBgPainter = new MattePainter(PaintUtils.AERITH, true);
        final Painter<Component> orangeBgPainter = new MattePainter(PaintUtils.ORANGE_DELIGHT, true);
 
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
        button5.setBackground(orangeBG);
        JButton button6 = new JButton("Font and Background changed");
        button6.setFont(font);
        button6.setBackground(orangeBG);
//        button6.setBackgroundPainter(backgroundPainter);

        JXButton xbutton1 = new JXButton("Default (push)");
        //<snip>Add action listener using Lambda expression
    	RingArray ringArray = new RingArray(new String[] {"Hello", "Goodbye", "SwingLabs", "Turkey Bowl"});
    	xbutton1.addActionListener(event -> {
    		xbutton1.setText(ringArray.get());
        });
        //</snip>
        
        JXButton xbutton2 = new JXButton("Font changed (blur when cursor over)");
        Color foreground = xbutton2.getForeground();
        xbutton2.setFont(font);

        final MattePainter fp = new MattePainter(foreground);
        xbutton2.setForegroundPainter(fp); 
        
        JXButton xbutton3 = new JXButton("Background changed");
        xbutton3.setBackground(background);
        JXButton xbutton4 = new JXButton("BackgroundPainter changed");
        xbutton4.setBackgroundPainter(aerithBgPainter);
        
        JXButton xbutton5 = new JXButton("Font and Background changed");
        xbutton5.setFont(font);
        xbutton5.setBackground(orangeBG);
        
        JXButton xbutton6 = new JXButton("Font and BackgroundPainter changed");
        xbutton6.setFont(font);
        xbutton6.setBackgroundPainter(orangeBgPainter);

        // mouse over effects:
        xbutton2.addMouseListener(new MouseAdapter() { // blur
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                fp.setFilters(new FastBlurFilter());
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseExited(MouseEvent e) {
                fp.setFilters((BufferedImageOp[]) null);
            }          
        });

        xbutton4.addMouseListener(new MouseAdapter() { // disable BG painter
            @Override
            public void mouseEntered(MouseEvent e) {
                xbutton4.setBackgroundPainter(null);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                xbutton4.setBackgroundPainter(aerithBgPainter);
            }          
        });

        xbutton6.addMouseListener(new MouseAdapter() { // disable BG painter
            @Override
            public void mouseEntered(MouseEvent e) {
                xbutton6.setBackgroundPainter(null);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                xbutton6.setBackgroundPainter(orangeBgPainter);
            }          
        });

        JPanel panel = new JXPanel(new GridLayout(7, 2, 1, 1));
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

        add(panel);
        
        // some iconized buttons:
        JPanel iconPanel = new JXPanel(new GridLayout(1, 4, 1, 1));
        add(iconPanel, BorderLayout.SOUTH);
        
        CircleIcon red = new CircleIcon(SizingConstants.ACTION_ICON, Color.RED);
        JButton rButton = new JButton("M red Circle", red);
        LOG.info("cButton.getForeground() ="+rButton.getForeground());
        iconPanel.add(rButton);
        CircleIcon green = new CircleIcon(SizingConstants.S, Color.GREEN);
        JButton gButton = new JButton("S green Circle", green);
        iconPanel.add(gButton);
        
        ColumnControlIcon ccIcon = new ColumnControlIcon();
        JButton ccButton = new JButton("XS ColumnControl", ccIcon);
        iconPanel.add(ccButton);
        
        PainterIcon pi = new PainterIcon(ccIcon.getIconWidth(), ccIcon.getIconHeight());
        pi.setPainter(orangeBgPainter);
        JXButton piButton = new JXButton("PainterIcon", pi);
        iconPanel.add(piButton);        
    }

    /**
     * {@inheritDoc}
     */
    protected void bind() {
    	/* aus XLabelDemo
    	 * param READ :	strategy the update strategy for the binding
    	 * param lineWrap: sourceObject the source object
    	 * param sourceProperty the source property
    	 * param label : targetObject the target object
    	 * param targetProperty the target property
    	 */
//        Bindings.createAutoBinding(READ, lineWrap, BeanProperty.create("selected"), label, BeanProperty.create("lineWrap")).bind();

    	// TODO Auto-generated method stub       
    }

    /**
     * {@inheritDoc}
     */
	@Override
	protected void createDemo() {
		LOG.info("ctor");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        
        interactiveFontAndBackgroundCheck();
        
	}
}
