module swingx.plaf {
	// Definiert Schnittstelle und exportiert sie
	uses org.jdesktop.swingx.plaf.LookAndFeelAddons;
	exports org.jdesktop.swingx.plaf;
	
	requires transitive java.desktop;
	requires java.logging;
	requires metainf.services;
	requires transitive swingx.common;
}