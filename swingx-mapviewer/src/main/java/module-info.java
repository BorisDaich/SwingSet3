module swingx.mapviewer {
	exports org.jxmapviewer;
	exports org.jxmapviewer.viewer;
	requires transitive java.desktop;
	requires java.logging;
	requires swingx.painters;
	requires transitive swingx.common;
}