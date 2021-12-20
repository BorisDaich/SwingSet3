package org.jdesktop.swingx.demos.loginpane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;

public class MoonLoginPaneUI extends BasicLoginPaneUI {

    private static final Logger LOG = Logger.getLogger(MoonLoginPaneUI.class.getName());
    
    public MoonLoginPaneUI(JXLoginPane dlg) {
        super(dlg);
    }

	private static final String FILENAME = "resources/images/moon.jpg";
	private InputStream getFileInputStream(String resourceName) {
        FileInputStream fis = null;
		try {
	        File file = new File(resourceName);
	        if (!file.exists()) {
	    		LOG.log(Level.WARNING, "cannot find resource "+file);
        		String pkg = this.getClass().getPackageName().replace('.', '/')+'/';
        		file = new File(pkg+resourceName);
        		if (!file.exists()) {
            		LOG.info(" >>> try default eclipse output folder ...");
            		// this is default eclipse output folder
            		file = new File("bin/"+pkg+resourceName);
            		if(!file.exists()) {
            			// m2e output folder        			
//                		file = new File("target/classes/"+pkg+FILENAME);
// demos besteht aus zwei projekten: content und swingxset
                		file = new File("content/target/classes/"+pkg+resourceName);
            		}
        		}
	            fis = new FileInputStream(file);
	        }
            fis = new FileInputStream(file); // throws FileNotFoundException
            LOG.info("AbsolutePath:"+file.getAbsolutePath());
		} catch (FileNotFoundException e) {
            LOG.warning("new FileInputStream throws"+e);
		}
		return fis;
	}
    /**
     * the original (super) default 400x60 banner is replaced by part of the moon
     */
    @Override
    public Image getBanner() {
    	try {
            InputStream fis = getFileInputStream(FILENAME);
    		BufferedImage im = ImageIO.read(fis);
    		return im.getSubimage(100, 300, 400, 60);
    	} catch (IOException e) {
    		LOG.log(Level.WARNING, "cannot load resource "+FILENAME);
    	}
    	return super.getBanner();
    }

}
