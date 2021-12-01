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
package org.jdesktop.swingx.demos.loginpane;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;
import org.jdesktop.swingx.util.GraphicsUtilities;

import com.sun.swingset3.DemoProperties;

/**
 * A demo for the {@code JXLoginPane}.
 *
 * @author Karl George Schaefer
 * @author rah003 (original JXLoginPaneDemo)
 */
@DemoProperties(
    value = "JXLoginPane Demo",
    category = "Controls",
    description = "Demonstrates JXLoginPane, a security login control.",
    sourceFiles = {
        "org/jdesktop/swingx/demos/loginpane/LoginPaneDemo.java",
        "org/jdesktop/swingx/demos/loginpane/resources/LoginPaneDemo.properties",
        "org/jdesktop/swingx/demos/loginpane/resources/LoginPaneDemo.html",
        "org/jdesktop/swingx/demos/loginpane/resources/images/LoginPaneDemo.png",
        "org/jdesktop/swingx/demos/loginpane/DemoLoginService.java"
    }
)
@SuppressWarnings("serial")
public class LoginPaneDemo extends JPanel {
	
    private static final Logger LOG = Logger.getLogger(LoginPaneDemo.class.getName());

    private DemoLoginService service;
    private JXLoginPane loginPane;
    private JButton loginLauncher;
    private JToggleButton allowLogin;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(LoginPaneDemo.class.getAnnotation(DemoProperties.class).value());
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new LoginPaneDemo());
                frame.setPreferredSize(new Dimension(800, 600));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    public LoginPaneDemo() {
        super(new BorderLayout());
        
        createLoginPaneDemo();
        
        Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
        
        bind();
    }

    private void createLoginPaneDemo() {
        service = new DemoLoginService();
        loginPane = new JXLoginPane(service);
        LOG.info("banner:"+loginPane.getBanner());
        List<String> servers = new ArrayList<String>();
        servers.add("A");
        servers.add("B");
        loginPane.setServers(servers);
        
//        loginPane.setBanner(null); // No banner (customizition)
        loginPane.setBanner(new DummyLoginPaneUI(loginPane).getBanner());
        loginPane.setLocale(Locale.ITALIAN);
        loginPane.setBannerText("BannerText"); // BUG: L&F change resets initial settings!
        
        loginLauncher = new JButton();
        loginLauncher.setName("launcher");
        add(loginLauncher, BorderLayout.NORTH);
        
        JPanel p = new JPanel(new VerticalLayout());
        add(p);
        
        allowLogin = new JToggleButton();
        allowLogin.setName("allowLogin");
        p.add(allowLogin);
    }
    
    private void bind() {
    	loginLauncher.addActionListener(event -> {
    		Status status = JXLoginPane.showLoginDialog(LoginPaneDemo.this, loginPane); // returns status
    		LOG.info("status:"+status);
    		// or per Frame:
//    		JFrame frame = JXLoginPane.showLoginFrame(loginPane);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////            frame.setJMenuBar(createAndFillMenuBar(panel));
////            loginPane.setSaveMode(SaveMode.BOTH);
//            frame.pack();
//            frame.setVisible(true);
    	});
        Bindings.createAutoBinding(READ,
                allowLogin, BeanProperty.create("selected"),
                service, BeanProperty.create("validLogin")).bind();
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
            Image banner = super.getBanner();
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

}
