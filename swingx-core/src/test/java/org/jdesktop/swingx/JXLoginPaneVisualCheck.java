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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXLoginPane.JXLoginFrame;
import org.jdesktop.swingx.JXLoginPane.SaveMode;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.auth.LoginService;
import org.jdesktop.swingx.auth.SimpleLoginService;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;
import org.jdesktop.swingx.plaf.basic.CapsLockSupport;
import org.jdesktop.swingx.util.GraphicsUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Simple tests to ensure that the {@code JXLoginPane} can be instantiated and displayed.
 *
 * @author Karl Schaefer
 */
@RunWith(JUnit4.class)
public class JXLoginPaneVisualCheck extends InteractiveTestCase {
	
    private static final Logger LOG = Logger.getLogger(JXLoginPaneVisualCheck.class.getName());

    public JXLoginPaneVisualCheck() {
        super("JXLoginPane Test");
    }

    public static void main(String[] args) throws Exception {
        // setSystemLF(true);
        JXLoginPaneVisualCheck test = new JXLoginPaneVisualCheck();

        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("xinteractive.*"); // ???
//            test.runInteractiveTests("interactiveDefaultBannerDisplay"); 
//            test.runInteractiveTests("interactiveCustomBannerDisplay");
//            test.runInteractiveTests("interactiveNullBannerDisplay");         
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

//    private JXFrame frame; // JXLoginFrame
//    private JMenuBar manubar; // menu bar with L&F, Locales
//    private JXLoginPane loginPane;
    
    private static final String FRAME_TITLE_ENGLISH = "Login";
    private static final String FRAME_TITLE_FRANCE = "Identifiant";
    
//    @Override
//    @Before
//       public void setUp() {
//        JComponent.setDefaultLocale(Locale.FRANCE);
//        loginPane = new JXLoginPane();
////        LOG.info("-------------------------------");
//        manubar = createAndFillMenuBar(loginPane); // menu bar with L&F, Locales
//        frame = JXLoginPane.showLoginFrame(loginPane);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setJMenuBar(manubar);
//    }
    
    /**
     * Issue #538-swingx Failure to set locale at runtime
     * Issue https://github.com/homebeaver/SwingSet/issues/16
     * 
     * @throws InterruptedException 
     * @throws InvocationTargetException 
     */
    @Test
    public void interactiveDefaultBannerDisplay() throws InvocationTargetException, InterruptedException {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run ui test - headless environment");
            return;
        }
        JComponent.setDefaultLocale(Locale.FRANCE);
        JXLoginPane panel = new JXLoginPane();
        JMenuBar manubar = createAndFillMenuBar(panel); // menu bar with L&F, Locales
        JXFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(manubar);
//        JXLoginPane panel = loginPane;
        
        JXRootPane rootPane = frame.getRootPaneExt();
        LOG.info("rootPane:"+rootPane);
//        if(rootPane!=null) {
//        	LOG.info("\n rootPane.ToolBar:"+rootPane.getToolBar()
//        		+ "\n rootPane.StatusBar:"+rootPane.getStatusBar());
//        }
// TODO test addComponentOrientationToggle
        
        panel.setSaveMode(SaveMode.BOTH);

        String customBannerText = "String text";
        panel.setBannerText(customBannerText); // expected : this is ignored for customized banner
        assertEquals(customBannerText, panel.getBannerText());

        frame.pack();
        frame.setVisible(true);
        LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
        assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
        
        SwingUtilities.invokeLater(new Runnable() {
//        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
                assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
                
                LOG.info("(expected "+customBannerText+") customBannerText="+panel.getBannerText());
                assertEquals("customBannerText", customBannerText, panel.getBannerText());
            }
        });
        
    }

    /**
     * Issue https://github.com/homebeaver/SwingSet/issues/16
     * Custom banner (null) : setBannerText is ignored
     * 
     * @throws InterruptedException 
     * @throws InvocationTargetException 
     */
    @Test
    public void interactiveNullBannerDisplay() throws InvocationTargetException, InterruptedException {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run ui test - headless environment");
            return;
        }
        JComponent.setDefaultLocale(Locale.FRANCE);
        JXLoginPane panel = new JXLoginPane();
        JMenuBar manubar = createAndFillMenuBar(panel); // menu bar with L&F, Locales
        JXFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(manubar);
//        JXLoginPane panel = loginPane;
        panel.setSaveMode(SaveMode.BOTH);

        panel.setBanner(null); // remove Banner, custom no (null) Banner
        String customBannerText = "CustomNullBanner";
        panel.setBannerText(customBannerText); // expected : this is ignored for customized banner
        assertEquals("", panel.getBannerText());

        frame.pack();
        frame.setVisible(true);
        LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
        assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
        
        SwingUtilities.invokeLater(new Runnable() {
//        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
                assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
                
                LOG.info("(expected empty) customBannerText="+panel.getBannerText());
                assertEquals("customBannerText", "", panel.getBannerText());
            }
        });
        
    }

    /**
     * Issue https://github.com/homebeaver/SwingSet/issues/16
     * Custom banner : setBannerText is ignored
     * 
     * @throws InterruptedException 
     * @throws InvocationTargetException 
     */
//    @Test
    public void interactiveCustomBannerDisplay() throws InvocationTargetException, InterruptedException {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run ui test - headless environment");
            return;
        }
        JComponent.setDefaultLocale(Locale.FRANCE);
        JXLoginPane panel = new JXLoginPane();
        JMenuBar manubar = createAndFillMenuBar(panel); // menu bar with L&F, Locales
        JXFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(manubar);
//        JXLoginPane panel = loginPane;
        panel.setSaveMode(SaveMode.BOTH);

        panel.setBanner(new DummyLoginPaneUI(panel).getBanner()); // custom Banner
        String customBannerText = "CustomBanner";
        panel.setBannerText(customBannerText); // expected : this is ignored
        assertEquals("", panel.getBannerText());

        frame.pack();
        frame.setVisible(true);
        LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
        assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
        
        SwingUtilities.invokeLater(new Runnable() {
//		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
                LOG.info("locale(exp:"+FRAME_TITLE_FRANCE+") frame title="+frame.getTitle());
                assertEquals("locale frame title", FRAME_TITLE_FRANCE, frame.getTitle());
                
                panel.setLocale(Locale.ENGLISH);
                LOG.info("locale en (exp:"+FRAME_TITLE_ENGLISH+") frame title="+frame.getTitle());
                assertEquals("english frame title", FRAME_TITLE_ENGLISH, frame.getTitle());

                LOG.info("(expected empty) customBannerText="+panel.getBannerText());
                assertEquals("customBannerText", "", panel.getBannerText());
			}
		});
    }

    /**
     * Issue #538-swingx Failure to set locale at runtime
     *
     */
    public void interactiveDisplayFixedUser() {
        JComponent.setDefaultLocale(Locale.FRANCE);
        JXLoginPane panel = new JXLoginPane();
        JFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createAndFillMenuBar(panel));

        panel.setSaveMode(SaveMode.BOTH);
        panel.setUserName("aGuy");
        panel.setUserNameEnabled(false);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Issue #538-swingx Failure to set locale at runtime
     *
     */
    public void interactiveSetBackground() {
        JXLoginPane panel = new JXLoginPane();
        panel.setBackgroundPainter(new MattePainter(Color.RED, true));
        JFrame frame = JXLoginPane.showLoginFrame(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createAndFillMenuBar(panel));

        panel.setSaveMode(SaveMode.BOTH);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Issue #636-swingx Unexpected resize on long exception message.
     *
     */
    public void interactiveError() {
        JComponent.setDefaultLocale(Locale.ITALIAN);
        final JXLoginPane panel = new JXLoginPane(new LoginService() {

                        @Override
                        public boolean authenticate(String name, char[] password,
                                        String server) throws Exception {
                                        throw new Exception("Ex.");
                        }});
        final JXLoginFrame frame = JXLoginPane.showLoginFrame(panel);
        // if uncommented dialog will disappear immediately due to invocation of login action
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createAndFillMenuBar(panel));
        panel.setErrorMessage("TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO Unexpected resize on long exception message. Unexpected resize on long exception message.");

        panel.setSaveMode(SaveMode.BOTH);

        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                                evaluateChildren(frame.getContentPane().getComponents());
                        }});

    }

    /**
     * Issue #636-swingx Unexpected resize on long exception message.
     *
     */
    public void interactiveBackground() {
        JComponent.setDefaultLocale(Locale.FRANCE);
        final JXLoginPane panel = new JXLoginPane(new LoginService() {

                        @Override
                        public boolean authenticate(String name, char[] password,
                                        String server) throws Exception {
                                        throw new Exception("Ex.");
                        }});
        final JXLoginFrame frame = JXLoginPane.showLoginFrame(panel);
        // if uncomented dialog will disappear immediatelly dou to invocation of login action
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createAndFillMenuBar(panel));
        panel.setErrorMessage("TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO TO Unexpected resize on long exception message. Unexpected resize on long exception message.");

        panel.setSaveMode(SaveMode.BOTH);
        frame.getContentPane().setBackgroundPainter(new MattePainter(
                new GradientPaint(0, 0, Color.BLUE, 1, 0, Color.YELLOW), true));

        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                                evaluateChildren(frame.getContentPane().getComponents());
                        }});

    }

    /**
     * Progress message test.
     */
    public void interactiveProgress() {
        final JXLoginPane panel = new JXLoginPane();
        final JFrame frame = JXLoginPane.showLoginFrame(panel);
        panel.setLoginService(new LoginService() {

			@Override
            public boolean authenticate(String name, char[] password, String server) throws Exception {
				panel.startLogin();
				Thread.sleep(5000);
				return true;
			}});

        frame.setJMenuBar(createAndFillMenuBar(panel));

        panel.setSaveMode(SaveMode.BOTH);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				evaluateChildren(frame.getContentPane().getComponents());
			}});

    }

    private boolean evaluateChildren(Component[] components) {
        for (Component c: components) {
        	if (c instanceof JButton && "login".equals(((JButton) c).getActionCommand())) {
        		((JButton) c).doClick();

        		return true;
        	} else if (c instanceof Container) {
        		if (evaluateChildren(((Container) c).getComponents()) ){
        			return true;
        		}
        	}
        }
        return false;

	}


    public class DummyLoginPaneUI extends BasicLoginPaneUI {

        public DummyLoginPaneUI(JXLoginPane dlg) {
            super(dlg);
        }

        /**
         * the original (super) default 400x60 banner is been resized to a (0, 0, 200, 100)-rectangle
         */
        @Override
        public Image getBanner() {
            Image banner = super.getBasicBanner("dummy");
            BufferedImage im = GraphicsUtilities.createCompatibleTranslucentImage(banner.getWidth(null), banner.getHeight(null));
            Graphics2D g2 = im.createGraphics();
            
            try {
                g2.setComposite(AlphaComposite.Src);
                g2.drawImage(banner, 0, 0, 200, 100, null);
            } finally {
                g2.dispose();
            }
            
            return im;
        }
    }

    @Override
    protected void createAndAddMenus(JMenuBar menuBar, final JComponent component) {
        super.createAndAddMenus(menuBar, component);
        JMenu menu = new JMenu("Locales");
        menu.add(new AbstractAction("Change Locale") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (component.getLocale() == Locale.FRANCE) {
                    component.setLocale(Locale.ENGLISH);
                } else {
                    component.setLocale(Locale.FRANCE);
                }
            }});
        menuBar.add(menu);
    }

    /**
     * swingx-917
     * @throws Exception
     */
    public void interactiveBrokenLayoutAfterFailedLogin() throws Exception {
        // PENDING JW: removed while fixing #1186-swingx (no dependency on sun packages)
        // revisit: why do we do this at all? If really needed replace
//        sun.awt.AppContext.getAppContext().put("JComponent.defaultLocale", Locale.FRANCE);
        Map<String, char[]> aMap = new HashMap<String, char[]>();
        aMap.put("asdf", "asdf".toCharArray());
        JXLoginPane panel = new JXLoginPane(new SimpleLoginService(aMap));
        panel.setSaveMode(JXLoginPane.SaveMode.BOTH);
        panel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(
                    PropertyChangeEvent thePropertyChangeEvent) {
                System.err.println(thePropertyChangeEvent.getPropertyName()
                        + " " + thePropertyChangeEvent.getOldValue()
                        + " -> " + thePropertyChangeEvent.getNewValue());
            }
        });
        JFrame frame = JXLoginPane.showLoginFrame(panel);

        frame.pack();
        frame.setVisible(true);
    }

    // what it is for?
    public void xinteractiveComparativeDialogs() {
        JXDialog dialog = new JXDialog(new JXLoginPane());
        dialog.pack();
        dialog.setVisible(true);
        LOG.info("JXDialog "+dialog);
        
        Status status = JXLoginPane.showLoginDialog(null, new JXLoginPane()); // block until login or cancel
        LOG.info("LoginDialog status="+status);
    }

    public void interactiveCapsLockTest() {
        CapsLockSupport cls = CapsLockSupport.getInstance();
        cls.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt);
                System.out.println(CapsLockSupport.getInstance().isCapsLockEnabled());
                System.out.println();
            }
        });
        showInFrame(new JTextField(), "Caps Lock Test");
    }
    
	/**
     * Do nothing, make the test runner happy
     * (would output a warning without a test fixture).
     *  
     */
//    public void testDummy() {
//    }
    
}
