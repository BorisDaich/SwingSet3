module swingx.common {
	exports org.jdesktop.beans;
	exports org.jdesktop.swingx.util;
	requires transitive java.desktop;
	requires java.logging;
	requires java.compiler;
	requires metainf.services;
}