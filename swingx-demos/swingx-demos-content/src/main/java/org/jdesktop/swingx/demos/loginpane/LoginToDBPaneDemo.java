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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.SaveMode;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.VerticalLayout;
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
 * A demo for the {@code JXLoginPane} with custom banner
 *
 * @author hb https://github.com/homebeaver (locale lang selector + custom moon banner)
 */
@DemoProperties(
    value = "JXLoginDBPane Demo",
    category = "Controls",
    description = "Demonstrates JXLoginPane, a security login control.",
    sourceFiles = {
        "org/jdesktop/swingx/demos/loginpane/LoginToDBPaneDemo.java",
        "org/jdesktop/swingx/demos/loginpane/resources/LoginToDBPaneDemo.properties",
        "org/jdesktop/swingx/demos/loginpane/resources/LoginPaneDemo.html",
        "org/jdesktop/swingx/demos/loginpane/resources/images/LoginPaneDemo.png",
        "org/jdesktop/swingx/demos/loginpane/DemoLoginService.java"
    }
)
@SuppressWarnings("serial")
// abstract class DefaultDemoPanel extends JXPanel
public class LoginToDBPaneDemo extends DefaultDemoPanel implements ActionListener {
	
    private static final Logger LOG = Logger.getLogger(LoginToDBPaneDemo.class.getName());

    private DBLoginService service;
    private JXLoginPane loginPane;
    private JXButton loginLauncher;
    private JXComboBox<DisplayLocale> localeBox; // DisplayLocale is a wrapper for Locale
    private JXLabel statusLabel;
    
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
    
    /**
     * {@inheritDoc}
     */
    // implements abstract void DefaultDemoPanel.createDemo() called in super ctor
	@Override
	protected void createDemo() {
		LOG.info("ctor");
        setLayout(new BorderLayout());
	}

    public LoginToDBPaneDemo() {
    	super();
/* super:
        createDemo();
        injectResources(); 
        if (!injectRan) {
            throw new IllegalStateException("must call super.injectReources");
        }     
        map = Application.getInstance().getContext().getActionMap(this);
        bind();
 */
        
//        Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
        ResourceMap rm = Application.getInstance().getContext().getResourceMap(getClass());
        rm.keySet().forEach(key -> {
        	try {
//                LOG.info("key:"+key + " : " + rm.getObject(key, String.class));
/*
INFORMATION: key:Application.description : A demo showcase application for the SwingX GUI toolkit
INFORMATION: key:Application.description.short : [Application.description.short not specified]
INFORMATION: key:Application.homepage : https://swinglabs-demos.dev.java.net
INFORMATION: key:Application.icon : images/swingset3_icon.png
INFORMATION: key:Application.id : SwingLabsDemos
INFORMATION: key:Application.title : SwingLabs Demos
INFORMATION: key:Application.vendor : SwingLabs Team
INFORMATION: key:Application.vendorId : SwingLabs
INFORMATION: key:Application.version : 1.6
INFORMATION: key:BlockingDialog.cancelButton.text : &Cancel
INFORMATION: key:BlockingDialog.optionPane.message : Please wait...
INFORMATION: key:BlockingDialog.progressBar.string : %02d:%02d, %02d:%02d remaining
INFORMATION: key:BlockingDialog.progressBar.stringPainted : true
INFORMATION: key:BlockingDialog.title : Busy
INFORMATION: key:BlockingDialogTimer.delay : 250
INFORMATION: key:copy.Action.shortDescription : Copy the current selection to the clipboard
INFORMATION: key:copy.Action.text : &Copy
INFORMATION: key:cut.Action.shortDescription : Move the current selection to the clipboard
INFORMATION: key:cut.Action.text : Cu&t
INFORMATION: key:delete.Action.shortDescription : Delete current selection
INFORMATION: key:delete.Action.text : &Delete
INFORMATION: key:demoPanel.loadingMessage : demo loading
INFORMATION: key:demos.title : GUI Components
INFORMATION: key:error.noDemosLoaded : SwingLabs Demos was unable to load demos 
INFORMATION: key:error.title : SwingLabs Demos Error
INFORMATION: key:exit.text : Exit
INFORMATION: key:file.text : File
INFORMATION: key:iconPath : resources/images/LoginPaneDemo.png
INFORMATION: key:langLabel.text : select language
INFORMATION: key:launcher.text : Launch Login to DB Screen
INFORMATION: key:lookAndFeel.text : Look and Feel
INFORMATION: key:mainFrame.title : SwingLabs Demos
INFORMATION: key:paste.Action.shortDescription : Paste the contents of the clipboard at the current insertion point
INFORMATION: key:paste.Action.text : &Paste
INFORMATION: key:quit.Action.shortDescription : Exit the application
INFORMATION: key:quit.Action.text : E&xit
INFORMATION: key:select-all.Action.shortDescription : Selects the entire text
INFORMATION: key:select-all.Action.text : Select &All
INFORMATION: key:sourceCodeCheckboxItem.text : Show Source Code
INFORMATION: key:toggleCodeViewerVisible.Action.text : Show Source Code
INFORMATION: key:toggleFontSet.Action.text : Large Font
INFORMATION: key:toggleSelectorVisible.Action.text : Show Demo Selector
INFORMATION: key:view.text : View

 */
        	} catch (Throwable e) {	
        	}
        });
        
//        bind();
    }
    protected void injectResources() {
        createLoginPaneDemo();
        super.injectResources();
    }

    private static final String demoUrl = "jdbc:postgresql://localhost/demo";
    private static final String ad393Url = "jdbc:postgresql://localhost/ad393";
    
    private void createLoginPaneDemo() {
        service = new DBLoginService(DBLoginService.DRIVER, ad393Url);
        loginPane = new JXLoginPane(service);
        LOG.info("banner:"+loginPane.getBanner());
        List<String> servers = new ArrayList<String>();
        servers.add(ad393Url);
        servers.add(demoUrl);
        loginPane.setServers(servers);
        
        loginPane.addPropertyChangeListener("status", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				JXLoginPane.Status status = (JXLoginPane.Status)evt.getNewValue();
				JXLoginPane.Status old = (JXLoginPane.Status)evt.getOldValue();
				LOG.info("new status is "+status + " old:"+old);
				statusLabel.setText(status.toString());
				LoginToDBPaneDemo.this.validate();
			}
        	
        });
        
        // customization:
//        loginPane.setBanner(null); // No banner (customization)
//        loginPane.setBanner(new MoonLoginPaneUI(loginPane).getBanner());

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
        
        statusLabel = new JXLabel(loginPane.getStatus().name());
        statusLabel.setFont(font);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);
        add(loginLauncher, BorderLayout.SOUTH);
        
        JPanel p = new JPanel(new VerticalLayout());
        add(p);
        
//        JLabel langLabel = new JXLabel("select language for Login Screen:", SwingConstants.CENTER);
        JLabel langLabel = new JXLabel();
        langLabel.setName("langLabel"); // dann text aus prop
//        langLabel.setIcon(null) - ??? wie icon per prop iconPath setzen?
        
        localeBox = new JXComboBox<DisplayLocale>();
        localeBox.setModel(createDisplayLocaleList());
        
        langLabel.setFont(font);
        p.add(langLabel);
        
        localeBox.setFont(font);
        localeBox.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));
        localeBox.addActionListener(event -> {
        	Locale selected = ((DisplayLocale)localeBox.getSelectedItem()).getLocale();
        	loginPane.setLocale(selected);
        });
        p.add(localeBox);
        loginPane.setSaveMode(SaveMode.BOTH);
    }
    
    /**
     * wrapper for class Locale
     * <p>
     * class Locale is final, so cannot subclass it
     *
     */
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
    
	@Override
	public void actionPerformed(ActionEvent event) {
		if(statusLabel.getText().equals(Status.SUCCEEDED.toString())) {
			LOG.info("status:SUCCEEDED!!!!");
			return;
		}
		LOG.info("event.Source:"+event.getSource().getClass() + " : "+event.getSource());
		Status status = JXLoginPane.showLoginDialog(LoginToDBPaneDemo.this, loginPane); // returns status
		LOG.info("event:"+event + "\n , status:"+status);
		statusLabel.setText(status.toString()); // statt status.name() // in PropertyChangeListener
		// TODO status:SUCCEEDED - ABER progressPanel ist noch aktiv
		if(status==Status.SUCCEEDED) {
			loginPane.setVisible(false);
			loginLauncher.setText("login "+Status.SUCCEEDED.toString());
			loginLauncher.setEnabled(false);
		}
	}
	
	JFrame frame;
    protected void bind() {
    	loginLauncher.addActionListener(this);
//    	loginLauncher.addActionListener(event -> {
//    		LOG.info("event.Source:"+event.getSource());
//    		// per Frame:
//    		if(frame==null) { // start loginPane in a frame
//        		frame = JXLoginPane.showLoginFrame(loginPane);
//        		LOG.info("nach frame:"+frame);
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////                frame.setJMenuBar(createAndFillMenuBar(panel));
////                loginPane.setSaveMode(SaveMode.BOTH);
//                frame.pack();
//                frame.setVisible(true); 
//    		} else {
//    			LOG.info("***kein zweites mal! *** frame:"+frame);
//    			statusLabel.setText(loginPane.getStatus().name());
//    		}
//    	});

    	// ???:
    	// Status {NOT_STARTED, IN_PROGRESS, FAILED, CANCELLED, SUCCEEDED}
        Bindings.createAutoBinding(UpdateStrategy.READ,
        		loginPane, BeanProperty.create("status"),
                this.statusLabel, BeanProperty.create("text"));
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
