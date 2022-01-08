/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.macosx.MacOSXLookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

/**
 * Provides helper methods to test SwingX components .<br>
 */
public class TestUtilities {

	private static final Logger LOG = Logger.getLogger(TestUtilities.class.getName());

  /**
   * Go through all existing LookAndFeelAddons. This leads all registered
   * {@link org.jdesktop.swingx.plaf.ComponentAddon} to initialize/uninitialize themselves.
   */
  public static void cycleAddons(JComponent component) throws Exception {
    LookAndFeelAddons.setAddon(MacOSXLookAndFeelAddons.class.getName());
    component.updateUI();

    MetalTheme oldTheme = MetalLookAndFeel.getCurrentTheme();
    try {
      MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
      LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class.getName());
      component.updateUI();

      MetalLookAndFeel.setCurrentTheme(new OceanTheme());
      LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class.getName());
      component.updateUI();
    } finally {
      MetalLookAndFeel.setCurrentTheme(oldTheme);
    }

    LookAndFeelAddons.setAddon(MotifLookAndFeelAddons.class.getName());
    component.updateUI();

    LookAndFeelAddons.setAddon(WindowsLookAndFeelAddons.class.getName());
    component.updateUI();

    String property = UIManager.getString("win.xpstyle.name");
    try {
      UIManager.put("win.xpstyle.name", WindowsLookAndFeelAddons.HOMESTEAD_VISUAL_STYLE);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class.getName());
      component.updateUI();

      UIManager.put("win.xpstyle.name",
        WindowsLookAndFeelAddons.SILVER_VISUAL_STYLE);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class.getName());
      component.updateUI();

      UIManager.put("win.xpstyle.name", null);
      LookAndFeelAddons.setAddon(WindowsClassicLookAndFeelAddons.class.getName());
      component.updateUI();

    } finally {
      UIManager.put("win.xpstyle.name", property);
    }
  }

	public static InputStream getResourceAsStream(Class<?> clazz, String resourceName) {
		
		if(clazz==null) return getFileAsStream(resourceName);
		
		InputStream is = clazz.getResourceAsStream(resourceName); // Throws NullPointerException
		LOG.finer("InputStream is:"+is);
		if(is==null) {
			LOG.log(Level.WARNING, "cannot find resource "+clazz.getName() + '#' + resourceName + " try FileAsStream ...");
			// try FileInputStream:
			
			// first PackageName as dir
			String dir = clazz.getPackageName().replace('.', '/')+'/';
			is = getFileAsStream(dir, resourceName);
			if(is!=null) return is;
			
			// this package org.jdesktop.swingx as dir
			dir = TestUtilities.class.getPackageName().replace('.', '/')+'/';
			is = getFileAsStream(dir, resourceName);
			if(is!=null) return is;
			
			// try default eclipse test folder ...
			String src = "src/test/resources/";
			is = getFileAsStream(src+dir, resourceName);
			if(is!=null) return is;

			if(is==null) return getFileAsStream(resourceName);
		}
		return is;
	}

	public static InputStream getFileAsStream(String dir, String resourceName) {
		File path = new File(dir);
		if (!path.exists()) {
			LOG.fine("(package)/path not found:"+path);
			return null;
		}
		return getFileAsStream(dir+resourceName);
	}

	/**
	 * try to load a resource from file with log info
	 * 
	 * @param resourceName fileName
	 * @return (File)InputStream
	 * 
	 * @throws  NullPointerException
     *          If the {@code resourceName} argument is {@code null}
	 */
	public static InputStream getFileAsStream(String resourceName) {
        FileInputStream fis = null;
		try {
	        File file = new File(resourceName); // Throws NullPointerException - If the pathname argument is null
	        if (!file.exists()) {
	        	LOG.log(Level.WARNING, "cannot find resource "+file);
	        } else {
				LOG.info("found:"+file);
	        	fis = new FileInputStream(file);
	        }
		} catch (NullPointerException e) {
			throw e;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

}
