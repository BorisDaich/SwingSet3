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
 *
 */
package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.plaf.SafeBorder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Visual Test to show SwingX Default Icons.
 * 
 * @author EUG https://github.com/homebeaver
 */
/*
 * TODO: umschalten von LAF funktioniert nicht
 * Metal -> Nimbus : Metal icons bleiben stehen
 * Nimbus -> Metal : icons verschwinden (bis auf html.*)
 */
@RunWith(JUnit4.class)
public class DefaultIconVisualTest extends InteractiveTestCase {
    
    public static void main(String args[]) {
    	UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
        DefaultIconVisualTest test = new DefaultIconVisualTest();
		try {
//			InteractiveTestCase.setLookAndFeel("Nimbus"); // start with Nimbus
//			InteractiveTestCase.setLookAndFeel("Motif"); // start with Motif
//			InteractiveTestCase.setLookAndFeel("Windows"); // start with Windows
			if(InteractiveTestCase.hasLookAndFeel("Metal")) {
				System.err.println("set OceanTheme:");
				Class<?> themeClass = Class.forName("javax.swing.plaf.metal.OceanTheme"); // throws ClassNotFoundException
				MetalTheme theme = (MetalTheme)themeClass.getDeclaredConstructor().newInstance();
				MetalLookAndFeel.setCurrentTheme(theme);
			} else {
				System.err.println("LAF:"+UIManager.getLookAndFeel().getClass().getSimpleName());			
			}
			
			test.runInteractiveTests();
//			test.runInteractiveTests("interactive.*Mark.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
    }
    
    @Test
    public void testSafeBorderBehavingDelegate() {
        JComponent comp = new JButton();
        AbstractBorder delegate = new BevelBorder(BevelBorder.LOWERED);
        SafeBorder border = new SafeBorder(delegate);
        assertNotNull(border.getBorderInsets(comp));
        assertNotNull(border.getBorderInsets(comp, null));
    }
    
    private static Dimension HGAP15 = new Dimension(15,1);
    private static Dimension VGAP15 = new Dimension(1,15);
    private static Dimension HGAP30 = new Dimension(30,1);
    private static Dimension VGAP30 = new Dimension(1,30);
	private static final String CONTROLLER_ICON_POSITION = BorderLayout.WEST; // good: NORTH WEST
    
    private static Component createButton(String propertyName) {
    	Icon icon = DefaultIcons.getIcon(propertyName);
    	assertNotNull(icon); // DefaultIcons.DIRECTORY + ... null in Motif
//		JLabel iconLabel = new JLabel((Icon)icon);
//		iconLabel.getInsets((Insets)null);
		JLabel label = new JLabel(propertyName, icon, SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
//        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(label, BorderLayout.CENTER);
//		b.setIconTextGap(2); // The default value of this property is 4 pixels.
    	return b;
        //return label;
    }
    private static JPanel createTP() {
    	JPanel tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.X_AXIS));
        tp.add(createButton(DefaultIcons.ASCENDING));	tp.add(Box.createRigidArea(HGAP15));
        tp.add(createButton(DefaultIcons.DESCENDING));	tp.add(Box.createRigidArea(HGAP30));
    	return tp;
    }
    JPanel bp;
    JPanel tp;
    private static class MyPanel extends JPanel {
    	Component tp;
    	public Component addXXX(Component comp) {
    		tp = comp;
    		return super.add(comp);
    	}
    	public void invalidate() {
			System.err.println("DO invalidate Valid="+isValid());
//			if(isValid()) add(new JLabel(DefaultIcons.getIcon(DefaultIcons.INFORMATION)));
        	super.invalidate();
    	}
    	public void validate() {
			System.err.println("DO validate");
        	super.validate();
    	}
    	private int repaintDone = -1;
        public void repaint() {
			System.err.println("DO repaint Valid="+isValid() + " LAF:"+UIManager.getLookAndFeel().getClass());
			if(UIManager.getLookAndFeel().getClass().getSimpleName().equals("MetalLookAndFeel")) {
				if(repaintDone==-1) { // beim ersten repaint
					repaintDone = 0;
					// das zeigt, dass nach ändern der LAF andere icons herangezogen werden
					//add(new JLabel(DefaultIcons.getIcon(DefaultIcons.INFORMATION)));
					remove(tp);
					add(createTP());
					// allerdings wird das repaint 5 mal gemacht! Warum?
				}
			}
			super.repaint();
        }   	
    }
    private JPanel getBoxPane() {
//    	JPanel demo = new MyPanel();
//        demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));
//
//        @SuppressWarnings("serial")
//        JPanel bp = new JPanel() {
//            public Dimension getMaximumSize() {
//                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
//            }
//        };
    	bp = new MyPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));

        bp.add(Box.createRigidArea(VGAP30));

        @SuppressWarnings("serial")
        JPanel vp = new JPanel() {
//            public Dimension getMaximumSize() {
//                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
//            }
        };
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
        vp.add(createButton(DefaultIcons.PENDING));	vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.MISSING));	vp.add(Box.createRigidArea(HGAP30));
//        bp.add(createButton(DefaultIcons.PENDING));	bp.add(Box.createRigidArea(VGAP15));
//        bp.add(createButton(DefaultIcons.MISSING));	bp.add(Box.createRigidArea(VGAP30));
        //vp.add(Box.createHorizontalGlue());
        bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
        
        vp = new JPanel();
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
        vp.add(createButton(DefaultIcons.ERROR));		vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.INFORMATION));	vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.WARNING));		vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.QUESTION));	vp.add(Box.createRigidArea(HGAP30));
        bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
//      bp.add(createButton(DefaultIcons.ERROR));		bp.add(Box.createRigidArea(VGAP15));
//      bp.add(createButton(DefaultIcons.INFORMATION));	bp.add(Box.createRigidArea(VGAP15));
//      bp.add(createButton(DefaultIcons.WARNING));		bp.add(Box.createRigidArea(VGAP15));
//      bp.add(createButton(DefaultIcons.QUESTION));	bp.add(Box.createRigidArea(VGAP30));

        if(UIManager.getLookAndFeel().getClass().getSimpleName().equals("MotifLookAndFeel")) {
            // DefaultIcons.DIRECTORY+ ... null in Motif:
        } else {
            vp = new JPanel();
            vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
            vp.add(createButton(DefaultIcons.DIRECTORY));	vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.FILE));		vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.COMPUTER));	vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.HARDDRIVE));	vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.FLOPPYDRIVE));	vp.add(Box.createRigidArea(HGAP30));
            bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
            
            vp = new JPanel();
            vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
            vp.add(createButton(DefaultIcons.NEWFOLDER));	vp.add(Box.createRigidArea(VGAP15));
            vp.add(createButton(DefaultIcons.UPFOLDER));	vp.add(Box.createRigidArea(VGAP15));
            if(UIManager.getLookAndFeel().getClass().getSimpleName().equals("WindowsLookAndFeel")) {
                // not in Windows
            } else {
                vp.add(createButton(DefaultIcons.HOMEFOLDER));	vp.add(Box.createRigidArea(VGAP15)); // not in Windows
            }
            vp.add(createButton(DefaultIcons.DETAILVIEW));	vp.add(Box.createRigidArea(VGAP15));
//            vp.add(createButton(DefaultIcons.VIEWMENU));	vp.add(Box.createRigidArea(VGAP15)); // failed
            vp.add(createButton(DefaultIcons.LISTVIEW));	vp.add(Box.createRigidArea(VGAP30));
            bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
        }

        vp = new JPanel();
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
        vp.add(createButton(DefaultIcons.ASCENDING));	vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.DESCENDING));	vp.add(Box.createRigidArea(HGAP30));
        bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));

        vp = new JPanel();
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
        vp.add(createButton(DefaultIcons.COLLAPSED));	vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.EXPANDED));	vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.OPEN));		vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.CLOSED));		vp.add(Box.createRigidArea(HGAP15));
        vp.add(createButton(DefaultIcons.LEAF));		vp.add(Box.createRigidArea(HGAP30));
        bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));


        tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.X_AXIS));
        tp.add(createButton(DefaultIcons.ASCENDING));	tp.add(Box.createRigidArea(HGAP15));
        tp.add(createButton(DefaultIcons.DESCENDING));	tp.add(Box.createRigidArea(HGAP30));
        if(bp instanceof MyPanel) {
        	((MyPanel)bp).addXXX(createTP());
        } else {
        	bp.add(tp);	
        }
        bp.add(Box.createRigidArea(VGAP30));

        vp = new JPanel();
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
//        vp.add(createButton(DefaultIcons.MAXIMIZE));	vp.add(Box.createRigidArea(HGAP15));
//        vp.add(createButton(DefaultIcons.MINIMIZE));	vp.add(Box.createRigidArea(HGAP15));
        if(UIManager.getLookAndFeel().getClass().getSimpleName().equals("MetalLookAndFeel")) {
            vp.add(createButton(DefaultIcons.HORIZONTALTHUMB));	vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.VERTICALTHUMB));	vp.add(Box.createRigidArea(HGAP15));
            vp.add(createButton(DefaultIcons.JAVACUP16));		vp.add(Box.createRigidArea(HGAP30));
            bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
            bp.add(Box.createVerticalGlue());
        } else if(UIManager.getLookAndFeel().getClass().getSimpleName().equals("WindowsLookAndFeel")) {
            vp.add(createButton(DefaultIcons.JAVACUP16));		vp.add(Box.createRigidArea(HGAP30));
            bp.add(vp);	bp.add(Box.createRigidArea(VGAP30));
            bp.add(Box.createVerticalGlue());
        } else {
        	// not in Nimbus , Motif
        }

        return bp;
//        demo.add(Box.createHorizontalGlue());
//        demo.add(bp);
//        demo.add(Box.createHorizontalGlue());
//        
//        return demo;
    }

//    public void interactiveAAA() {
//        final JLabel label = new JLabel("...TODO............ with icon border");
//        final JXFrame frame = wrapInFrame(label, "IconBorder");
//        frame.setSize(200, 400);
//        frame.setVisible(true);
//    }

    public void interactiveDefalultIconsShowcase() {
        final JXFrame frame = wrapInFrame(getBoxPane(), "Defalult Icons");
        frame.setSize(1400, 800);
        frame.setVisible(true);
    }

}
