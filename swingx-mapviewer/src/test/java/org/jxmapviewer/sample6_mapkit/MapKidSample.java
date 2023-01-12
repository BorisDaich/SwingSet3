package org.jxmapviewer.sample6_mapkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapKit.DefaultProviders;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 * Main with JXMapKit
 * @author EUG https://github.com/homebeaver (integrate to SwingSet3)
 */
public class MapKidSample {
	
    /**
     * @param args the program args (ignored)
     */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JXMapKit kit = new JXMapKit();
				kit.setDefaultProvider(DefaultProviders.OpenStreetMaps);

				TileFactoryInfo info = new OSMTileFactoryInfo();
				TileFactory tf = new DefaultTileFactory(info);
				kit.setTileFactory(tf);
				kit.setZoom(14);
				kit.setAddressLocation(new GeoPosition(51.5, 0)); // London
				kit.getMainMap().setDrawTileBorders(true);
				kit.getMainMap().setRestrictOutsidePanning(true);
				kit.getMainMap().setHorizontalWrapped(false);

				((DefaultTileFactory) kit.getMainMap().getTileFactory()).setThreadPoolSize(8);
				JFrame frame = new JFrame("JXMapKit test");
				frame.add(kit);
				frame.pack();
				frame.setSize(500, 300);
				frame.setVisible(true);
			}
		});
	}
}