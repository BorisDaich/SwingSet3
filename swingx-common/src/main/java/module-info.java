module swingx.common {
	exports org.jdesktop.beans;
	exports org.jdesktop.swingx.util;
	
	requires transitive java.desktop;
	requires java.logging;
	requires transitive java.compiler;
	
	requires org.kohsuke.metainf_services;
}