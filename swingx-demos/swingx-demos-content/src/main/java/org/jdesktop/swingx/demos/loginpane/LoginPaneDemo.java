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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.demos.painter.PainterDemo;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;

import com.sun.swingset3.DemoProperties;

/**
 * A demo for the {@code JXLoginPane}.
 *
 * @author Karl George Schaefer
 * @author rah003 (original JXLoginPaneDemo)
 * @author hb https://github.com/homebeaver (locale lang selector + custom moon banner)
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
    private JComboBox<Locale> localeBox; // TODO  JXComboBox is not generic
    
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
        
        // customization:
//        loginPane.setBanner(null); // No banner (customization)
//        loginPane.setBanner(new MoonLoginPaneUI(loginPane).getBanner());
//        loginPane.setBannerText("BannerText");
        
        loginLauncher = new JButton();
        loginLauncher.setName("launcher");
        add(loginLauncher, BorderLayout.NORTH);
        
        JPanel p = new JPanel(new VerticalLayout());
        add(p);
        
        allowLogin = new JToggleButton();
        allowLogin.setName("allowLogin");
        p.add(allowLogin);
        
        p.add(new JXLabel("select language for Login Screen:", SwingConstants.CENTER));
        localeBox = new JComboBox<Locale>();
        localeBox.setModel(createLocaleList());
        localeBox.addActionListener(event -> {
        	Locale selected = (Locale)localeBox.getSelectedItem();
        	loginPane.setLocale(selected);
        });
        p.add(localeBox);
    }
    
    private ComboBoxModel<Locale> createLocaleList() {
        DefaultComboBoxModel<Locale> model = new DefaultComboBoxModel<Locale>();
//    private ComboBoxModel<E> createFilterList() {
//        DefaultComboBoxModel<E> model = new DefaultComboBoxModel<E>();
        model.addElement(Locale.ENGLISH);
        model.addElement(new Locale("cs"));
        model.addElement(new Locale("es"));
        model.addElement(Locale.FRENCH);
        model.addElement(Locale.GERMAN);
        model.addElement(Locale.ITALIAN);
        model.addElement(new Locale("nl"));
        model.addElement(new Locale("pl"));
        model.addElement(new Locale("pt", "BR"));
        model.addElement(new Locale("sv"));
		return model;
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
    
    public class MoonLoginPaneUI extends BasicLoginPaneUI {

        public MoonLoginPaneUI(JXLoginPane dlg) {
            super(dlg);
        }

        /**
         * the original (super) default 400x60 banner is replaced by part of the moon
         */
        @Override
        public Image getBanner() {
        	try {
        		BufferedImage im = ImageIO.read(PainterDemo.class.getResourceAsStream("moon.jpg"));
        		return im.getSubimage(100, 300, 400, 60);
        	} catch (IOException e) {
        		LOG.warning("cannot read resource moon.jpg");
        	}
        	return super.getBanner();
        }
    }

}
