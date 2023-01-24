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
    private int adjustX = 0;
    private int adjustY = 0;

    /**
     * WaypointRenderer with an icon pointing to a target at middle of the bottom border
     * @param wpIcon Waypoint icon to use
     */
    public DefaultWaypointRenderer(Icon wpIcon) {
    	this(wpIcon==null ? 0 : wpIcon.getIconWidth() / 2  // assume adjustX in the middle of icon
    		,wpIcon==null ? 0 : wpIcon.getIconHeight()     // assume adjustY on bottom border
    		, wpIcon);
    }
    /**
     * WaypointRenderer with an icon and hints how to adjust
     * @param adjustx
     * @param adjusty
     * @param wpIcon Waypoint icon to use
     */
    public DefaultWaypointRenderer(int adjustx, int adjusty, Icon wpIcon) {
    	this.adjustX = adjustx;
    	this.adjustY = adjusty;
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
            int x = (int)point.getX() -this.adjustX;
            int y = (int)point.getY() -this.adjustY;
            
            icon.paintIcon(map, g, x, y);
            return;
    	}
        
    	if(iconImg instanceof BufferedImage img) {
            // assume the image points to [img.getWidth() / 2 , img.getHeight()]:
            int x = (int)point.getX() -img.getWidth() / 2;
            int y = (int)point.getY() -img.getHeight();
            
            g.drawImage(img, x, y, null);
    	} else {
        	LOG.warning("iconImg expected to be Icon or BufferedImage but is "+ iconImg.getClass().getName());
    	}
    }
}
