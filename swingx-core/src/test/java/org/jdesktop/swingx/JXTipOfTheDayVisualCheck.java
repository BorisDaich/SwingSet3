/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
import org.jdesktop.swingx.tips.DefaultTip;
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel;
import org.jdesktop.swingx.tips.TipOfTheDayModel;

/**
 * Simple tests to ensure that the {@code JXTipOfTheDay} can be instantiated and displayed.
 * 
 * @author Karl Schaefer
 */
public class JXTipOfTheDayVisualCheck extends InteractiveTestCase {
	
    private static final Logger LOG = Logger.getLogger(JXTipOfTheDayVisualCheck.class.getName());

    public JXTipOfTheDayVisualCheck() {
        super("JXTipOfTheDay Test");
    }

    public static void main(String[] args) throws Exception {
        // setSystemLF(true);
        JXTipOfTheDayVisualCheck test = new JXTipOfTheDayVisualCheck();
        
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }
    
    
    /**
     * Issue #538-swingx Failure to set locale at runtime
     *
     */
    public void interactivePopUp() {
    	LOG.info("Pops up a Tip of the day dialog.");
        JXTipOfTheDay tip = new JXTipOfTheDay(createTipOfTheDayModel());
        tip.showDialog(new JFrame());
//        showInFrame(new JXTipOfTheDay(), "tip");
    }
    
    JXFrame frame = null;
    JXTipOfTheDay tip = new JXTipOfTheDay(createTipOfTheDayModel());
    public void interactiveShowInFrame() {
		Object textPaneFont = UIManager.get("TextPane.font");
		Object labelFont = UIManager.get("Label.font"); //name=Dialog,style=bold,size=12
	      //defaults.add("TipOfTheDay.tipFont", getFontUIResource("Label.font"));
		Object tofdTipFont = UIManager.get("TipOfTheDay.tipFont"); //name=Dialog,style=bold,size=13
    	LOG.info("ShowInFrame. tip:"+tip +
            "\n TextPane.Font:"+textPaneFont +
        	"\n Label.Font==tipFont:"+labelFont+"=="+tofdTipFont +
    		"\n UI:"+tip.getUI());
//        tip = new JXTipOfTheDay(createTipOfTheDayModel());
        frame = showInFrame(tip, "tip", true);
        
        Font textPane = (Font)textPaneFont;
        Font font = (Font)(UIManager.get("TipOfTheDay.font"));
        Font label = (Font)labelFont;
        Font tipFont = (Font)tofdTipFont;
        assertEquals(textPane.getName(), font.getName());
        assertEquals(textPane.getStyle(), font.getStyle());
        assertEquals(textPane.getSize(), font.getSize()-1); // TipOfTheDay.font size=13
        assertEquals(label.getName(), tipFont.getName());
        assertEquals(label.getStyle(), tipFont.getStyle());
        assertEquals(label.getSize(), tipFont.getSize()-1); // tipFont size=13
    }
    
    public void interactiveDisplay() {
    	LOG.info("doRun JXTipOfTheDay in extra frame");
    	SwingUtilities.invokeLater( () -> {
    		JXFrame frame = new JXFrame("invokeLater Frame", true);
            frame.getContentPane().add(tip);
    		frame.pack();
    		//frame.setJMenuBar(super.createAndFillMenuBar(tip));
    		frame.setVisible(true);
    	});
    }

    private TipOfTheDayModel createTipOfTheDayModel() {
        // Create a tip model with some tips
        DefaultTipOfTheDayModel tips = new DefaultTipOfTheDayModel();
        
        // plain text
        tips.add(new DefaultTip(
            "tip1",
            "This is the first tip " +
            "This is the first tip " +
            "This is the first tip " +
            "This is the first tip " +
            "This is the first tip " +
            "This is the first tip\n" +
            "This is the first tip " +
            "This is the first tip"));
        
        // html text
        tips.add(new DefaultTip(
            "tip2",
            "<html>This is an html <b>TIP</b><br><center>" +
            "<table border=\"1\">" +
            "<tr><td>1</td><td>entry 1</td></tr>" +
            "<tr><td>2</td><td>entry 2</td></tr>" +
            "<tr><td>3</td><td>entry 3</td></tr>" +
            "</table>"));
        
        // a Component
        tips.add(new DefaultTip("tip3", new JTree()));

        // an Icon
        tips.add(new DefaultTip(
            "tip 4",
            new ImageIcon(BasicTipOfTheDayUI.class.getResource("resources/TipOfTheDay24.gif"))));

        return tips;
    }

    AbstractActionExt setWindowsLaf;
    @Override 
    protected JMenu createPlafMenu(Window target) {
    	UIManager.LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        LOG.info(">>>>>>>>> No of InstalledLookAndFeels "+plafs.length);
        JMenu menu = new JMenu("Set L&F");
        
        for (UIManager.LookAndFeelInfo info : plafs) {
            LOG.info(info.getName()+" "+info.getClassName()+" "+target);
            AbstractActionExt aae = InteractiveTestCase.createPlafAction(info.getName(), info.getClassName(), target);
            if("Windows".equals(info.getName())) {
//            	setWindowsLaf = new AbstractActionExt(aae);
            	setWindowsLaf = aae;
            }
            menu.add(aae);
        }
        return menu;
    }

    @Override // ich will Kontrolle über "Change Locale"
    protected void createAndAddMenus(JMenuBar menuBar, final JComponent component) {
        super.createAndAddMenus(menuBar, component);
        JMenu menu = new JMenu("Locales");
        menu.add(new AbstractAction("Change Locale") {

            public void actionPerformed(ActionEvent e) {
            	LOG.info("Locale is "+component.getLocale() + " component:"+component);
                if (component.getLocale() == Locale.GERMAN) {
                    component.setLocale(null);
                	LOG.info("Locale set to null");
                } else if (component.getLocale() == Locale.FRENCH) {
                    component.setLocale(Locale.ENGLISH);
                	LOG.info("Locale set to ENGLISH");
                } else {
                    component.setLocale(Locale.FRENCH);
                	LOG.info("Locale set to FRENCH");
                }
                LOG.info("UI:"+tip.getUI());
                tip.updateUI();
//                tip.repaint();
            }});
        menuBar.add(menu);
    }
    

    /**
     * Do nothing, make the test runner happy
     * (would output a warning without a test fixture).
     *
     */
    public void testDummy() {
        
    }

}
