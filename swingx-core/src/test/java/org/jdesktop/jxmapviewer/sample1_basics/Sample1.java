package org.jdesktop.jxmapviewer.sample1_basics;

import javax.swing.JFrame;

import org.jdesktop.jxmapviewer.JXMapViewer;
import org.jdesktop.jxmapviewer.OSMTileFactoryInfo;
import org.jdesktop.jxmapviewer.viewer.DefaultTileFactory;
import org.jdesktop.jxmapviewer.viewer.GeoPosition;
import org.jdesktop.jxmapviewer.viewer.TileFactoryInfo;

/**
 * A simple sample application that shows
 * a OSM map of Europe
 * @author Martin Steiger
 */
public class Sample1
{
    /**
     * @param args the program args (ignored)
     */
    public static void main(String[] args)
    {
        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 1");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
