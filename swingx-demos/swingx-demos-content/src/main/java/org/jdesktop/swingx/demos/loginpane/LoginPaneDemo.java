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
import java.awt.Font;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.demos.painter.PainterDemo;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;
import org.jdesktop.swingxset.SwingXSet;

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
    private JXComboBox<DisplayLocale> localeBox; // DisplayLocale is a wrapper for Locale
    
    /**
     * main method allows us to run as a standalone demo.
     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JFrame frame = new JFrame(LoginPaneDemo.class.getAnnotation(DemoProperties.class).value());
//                
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.getContentPane().add(new LoginPaneDemo());
//                frame.setPreferredSize(new Dimension(800, 600));
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
//    }
    /*
     * damit diese Klasse als einzige im SwingXSet gestartet werden kann (Application.launch),
     * muss sie in einem file stehen (==>onlyXButtonDemo).
     * Dieses file wird dann vom DemoCreator eingelesen, "-a"/"-augment" erweitert den demo-Vorrat.
     */
    public static void main(String[] args) {
    	Application.launch(SwingXSet.class, new String[] {"META-INF/onlyLoginDemo"});
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
//        List<String> servers = new ArrayList<String>();
//        servers.add("A");
//        servers.add("B");
//        loginPane.setServers(servers);
        
        // customization:
//        loginPane.setBanner(null); // No banner (customization)
        loginPane.setBanner(new MoonLoginPaneUI(loginPane).getBanner());
//        loginPane.setBannerText("BannerText");
        
        loginLauncher = new JButton();
        loginLauncher.setName("launcher");
        add(loginLauncher, BorderLayout.NORTH);
        
        JPanel p = new JPanel(new VerticalLayout());
        add(p);
        
        allowLogin = new JToggleButton();
        allowLogin.setName("allowLogin");
        p.add(allowLogin);
        
        JLabel langLabel = new JXLabel("select language for Login Screen:", SwingConstants.CENTER);
        
        localeBox = new JXComboBox<DisplayLocale>();
        localeBox.setModel(createDisplayLocaleList());
        Font font = new Font("SansSerif", Font.PLAIN, 16);
        
        langLabel.setFont(font);
        p.add(langLabel);
        
        localeBox.setFont(font);
        localeBox.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));
        localeBox.addActionListener(event -> {
        	Locale selected = ((DisplayLocale)localeBox.getSelectedItem()).getLocale();
        	loginPane.setLocale(selected);
        });
        p.add(localeBox);
    }
    
//    class LocaleLang extends Locale { // cannot subclass : final class Locale
//    }
    public class DisplayLocale { // wrapper, wie org.jdesktop.swingx.binding.DisplayInfo<T>
        private final Locale locale;
        
        public DisplayLocale(String lang) {
            this.locale = new Locale(lang);
        }
        public DisplayLocale(Locale item) {
            this.locale = item;
        }
        
        public Locale getLocale() {
            return locale;
        }

        // used in JXComboBox
        public String toString() {
			return locale.toString() + " " + locale.getDisplayLanguage(locale) + "/" +locale.getDisplayLanguage();  	
        }
    }
    
    private ComboBoxModel<DisplayLocale> createDisplayLocaleList() {
        DefaultComboBoxModel<DisplayLocale> model = new DefaultComboBoxModel<DisplayLocale>();
        model.addElement(new DisplayLocale(Locale.ENGLISH));
        model.addElement(new DisplayLocale("cs"));
        model.addElement(new DisplayLocale("es"));
        model.addElement(new DisplayLocale(Locale.FRENCH));
        model.addElement(new DisplayLocale(Locale.GERMAN));
//        model.addElement(new DisplayLocale(new Locale("de", "CH")));
//        model.addElement(new DisplayLocale(new Locale("fr", "CH")));
//        model.addElement(new DisplayLocale(new Locale("it", "CH")));
//        model.addElement(new DisplayLocale(new Locale("rm", "CH")));
        model.addElement(new DisplayLocale(Locale.ITALIAN));
        model.addElement(new DisplayLocale("nl"));
        model.addElement(new DisplayLocale("pl"));
        model.addElement(new DisplayLocale(new Locale("pt", "BR")));
        model.addElement(new DisplayLocale("sv"));
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
