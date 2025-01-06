module swingx.core {
//	exports org.jdesktop.swingx;
//	exports org.jdesktop.*;
	requires transitive java.desktop;
	requires java.logging;
	requires java.prefs;
	requires java.sql;
	requires java.naming;

	requires transitive swingx.common;
	requires swingx.graphics;
	requires swingx.autocomplete;
	requires swingx.action;
	requires swingx.painters;
	
	requires org.kohsuke.metainf_services;

}