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
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.jdesktop.application.Application;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.auth.PasswordStore;
import org.jdesktop.swingx.auth.UserNameStore;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.demos.painter.PainterDemo;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingxset.DefaultDemoPanel;
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
//abstract class DefaultDemoPanel extends JXPanel
public class LoginPaneDemo extends DefaultDemoPanel {
	
    private static final Logger LOG = Logger.getLogger(LoginPaneDemo.class.getName());

    private PasswordStore ps;
    private UserNameStore us = null; // not used ==> DefaultUserNameStore
    private DemoLoginService service;
    private JXLoginPane loginPane;
    // controler:
    private JXLabel statusLabel;
    private JToggleButton allowLogin;
    private JXComboBox<DisplayLocale> localeBox; // DisplayLocale is a wrapper for Locale
    private Locale selectedLocale;
    private JXButton loginLauncher;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    /*
     * damit diese Klasse als einzige im SwingXSet gestartet werden kann (Application.launch),
     * muss sie in einem file stehen (==>onlyXButtonDemo).
     * Dieses file wird dann vom DemoCreator eingelesen, "-a"/"-augment" erweitert den demo-Vorrat.
     */
    public static void main(String[] args) {
    	Application.launch(SwingXSet.class, new String[] {"META-INF/onlyLoginDemo"});
    }

    public LoginPaneDemo() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    // implements abstract void DefaultDemoPanel.createDemo() called in super ctor
	@Override
	protected void createDemo() {
		LOG.config("ctor");
        setLayout(new BorderLayout());
	}

	@Override
    protected void injectResources() {
        createLoginPaneControler();
        super.injectResources();
    }

	@Override
    protected void bind() {
        // bind source allowLogin button to target service.validLogin erst nach createLoginPaneDemo()!
		// d.h. bind() registriert nur den event Observer aka Listener
    	loginLauncher.addActionListener(event -> {
    		if(loginPane==null) {
    			createLoginPaneDemo();
    	        Bindings.createAutoBinding(READ, 
    	        		allowLogin, BeanProperty.create("selected"),
    	                service, BeanProperty.create("validLogin")
    	                ).bind();
    		}
    		if(selectedLocale!=null) loginPane.setLocale(this.selectedLocale);
    		
    		if(statusLabel.getText().equals(Status.SUCCEEDED.toString())) {
    			LOG.info("status:SUCCEEDED!!!! - do reset ...");
    			loginPane = null;
    			statusLabel.setText(Status.NOT_STARTED.toString());
    			localeBox.setSelectedItem(localeBox.getModel().getElementAt(0)); // Locale.0 is en
    			loginLauncher.setText("reset done, launch again.");
    			return;
    		}
    		
    		Status status = JXLoginPane.showLoginDialog(LoginPaneDemo.this, loginPane);
    		statusLabel.setText(status.toString());
    		
    		if(status==Status.SUCCEEDED) {
    			LOG.fine("isRememberPassword? : "+loginPane.isRememberPassword());
    			if(loginPane.isRememberPassword()) {
    				ps.set(loginPane.getUserName(), null, loginPane.getPassword());
    			}
				((FilePasswordStore)ps).store(); // make ps persistent
				
    			loginPane.setVisible(false);
    			loginLauncher.setText("login "+Status.SUCCEEDED.toString());
    			// kein disable -> launch kann beliebig of wiederholt werden -> reset
    			//loginLauncher.setEnabled(false);
    		}
    	});   	
    }
    
    private void createLoginPaneDemo() {
        service = new DemoLoginService();
    	ps = new FilePasswordStore();
    	us = null; //new DefaultUserNameStore();
        
        loginPane = new JXLoginPane(service, ps, us);
        LOG.info("banner:"+loginPane.getBanner());
        loginPane.addPropertyChangeListener("status", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				JXLoginPane.Status status = (JXLoginPane.Status)evt.getNewValue();
				JXLoginPane.Status old = (JXLoginPane.Status)evt.getOldValue();
				LOG.info("new status is "+status + " old:"+old);
				statusLabel.setText(status.toString());
				LoginPaneDemo.this.validate();
			}
        	
        });
        
        // customization:
//        loginPane.setBanner(null); // No banner (customization)
        loginPane.setBanner(new MoonLoginPaneUI(loginPane).getBanner());
//        loginPane.setBannerText("BannerText");
        
        // nicht notwendig: wird anhand ps+us gesetzt:
//        loginPane.setSaveMode(SaveMode.PASSWORD);
    }
    
    private void createLoginPaneControler() {
        Font font = new Font("SansSerif", Font.PLAIN, 16);

        loginLauncher = new JXButton();
        loginLauncher.setName("launcher"); // den text aus prop "launcher.text" holen
        loginLauncher.setFont(font);
        final Painter<?> orangeBgPainter = new MattePainter(PaintUtils.ORANGE_DELIGHT, true);
        loginLauncher.setBackgroundPainter(orangeBgPainter);
        loginLauncher.addMouseListener(new MouseAdapter() { // disable BG painter
            @Override
            public void mouseEntered(MouseEvent e) {
            	loginLauncher.setBackgroundPainter(null);
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	loginLauncher.setBackgroundPainter(orangeBgPainter);
            }          
        });
        
        JXPanel n = new JXPanel(new HorizontalLayout());
        JXLabel status = new JXLabel("Status:");
        status.setFont(font);
        status.setHorizontalAlignment(SwingConstants.RIGHT);
        n.add(status);
        statusLabel = new JXLabel(loginPane==null ? Status.NOT_STARTED.toString() : loginPane.getStatus().name());
        statusLabel.setFont(font);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        n.add(statusLabel);
        add(n, BorderLayout.NORTH);
        add(loginLauncher, BorderLayout.SOUTH);
        
        JXPanel p = new JXPanel(new VerticalLayout());
        add(p);
        
        allowLogin = new JRadioButton(); // JRadioButton extends JToggleButton
        allowLogin.setFont(font);
        allowLogin.setName("allowLogin");
        JRadioButton disallowLogin = new JRadioButton("disallow"); // JRadioButton extends JToggleButton
        disallowLogin.setFont(font);
      //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(allowLogin);
        group.add(disallowLogin);
        JPanel radioPanel = new JPanel(new java.awt.GridLayout( 1, 2 ));
        radioPanel.add(allowLogin);
        radioPanel.add(disallowLogin);
        p.add(radioPanel);
        
        JLabel langLabel = new JXLabel("select language for Login Screen:", SwingConstants.LEFT);
        
        localeBox = new JXComboBox<DisplayLocale>();
        localeBox.setModel(createDisplayLocaleList());
        
        langLabel.setFont(font);
        p.add(langLabel);
        
        localeBox.setFont(font);
        localeBox.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));
        localeBox.addActionListener(event -> {
        	Locale selected = ((DisplayLocale)localeBox.getSelectedItem()).getLocale();
        	LOG.info("Locale selected:"+selected + ", loginPane:"+loginPane);
            // an dieser Stelle ist loginPane immer null, daher macht der Observer so wenig Sinn
        	//if(loginPane!=null) loginPane.setLocale(selected);
        	selectedLocale = selected;
        });
        p.add(localeBox);
    }
    
    /**
     * wrapper for class Locale
     * <p>
     * class Locale is final, so cannot subclass it
     *
     */
    public class DisplayLocale {
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
