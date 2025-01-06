module swingx.painters {
	exports org.jdesktop.swingx.painter;
	exports org.jdesktop.swingx.painter.effects;
	
	requires transitive java.desktop;
	requires java.logging;
	
	requires transitive swingx.common;
}