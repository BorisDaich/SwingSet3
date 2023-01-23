/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.jxmapviewer.viewer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import org.jxmapviewer.JXMapViewer;

/**
 * This is a standard waypoint renderer.
 * @author joshy
 * @author EUG https://github.com/homebeaver (use Icon instead of BufferedImage)
 */
public class DefaultWaypointRenderer implements WaypointRenderer<Waypoint> {
	
	private static final Logger LOG = Logger.getLogger(DefaultWaypointRenderer.class.getName());
    
	/**
	 * the type of iconImg is Icon or BufferedImage
	 */
    private Object iconImg = null;

    /**
     * 
     * @param wpIcon Waypoint icon to use
     */
    public DefaultWaypointRenderer(Icon wpIcon) {
    	iconImg = wpIcon;
    }
    /**
     * 
     * @param wpImage Waypoint Image to use
     */
    public DefaultWaypointRenderer(BufferedImage wpImage) {
    	if(wpImage==null) {
    		try {
    			iconImg = ImageIO.read(DefaultWaypointRenderer.class.getResource("images/standard_waypoint.png"));
    		} catch (Exception ex) {
    			// this should not happen:
            	LOG.warning("couldn't read standard_waypoint.png "+ ex);
    		}
    	} else {
    		iconImg = wpImage;
    	}
    }
    /**
     * Uses a default waypoint image
     */
    public DefaultWaypointRenderer() {
    	this((BufferedImage)null);
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint w) {
        if(iconImg == null) return;
        
        Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());
    	if(iconImg instanceof Icon icon) {           
            // unterstellt, dass die Spitze auf [img.getWidth() / 2 , img.getHeight()] zeigt:
            int x = (int)point.getX() -icon.getIconWidth() / 2;
            int y = (int)point.getY() -icon.getIconHeight();
            
            icon.paintIcon(map, g, x, y);
            return;
    	}
        
    	if(iconImg instanceof BufferedImage img) {
            // unterstellt, dass die Spitze auf [img.getWidth() / 2 , img.getHeight()] zeigt:
            int x = (int)point.getX() -img.getWidth() / 2;
            int y = (int)point.getY() -img.getHeight();
            
            g.drawImage(img, x, y, null);
    	} else {
        	LOG.warning("iconImg expected to be Icon or BufferedImage but is "+ iconImg.getClass().getName());
    	}
    }
}
