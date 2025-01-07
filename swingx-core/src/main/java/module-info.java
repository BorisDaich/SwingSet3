module swingx.core {
	exports org.jdesktop.swingx;
	
	// Definiert Schnittstelle und exportiert sie
	uses org.jdesktop.swingx.plaf.LookAndFeelAddons;
	exports org.jdesktop.swingx.plaf;
	
	exports org.jdesktop.swingx.plaf.basic;
	exports org.jdesktop.swingx.plaf.basic.core;
	exports org.jdesktop.swingx.plaf.metal;
	exports org.jdesktop.swingx.plaf.misc;
	exports org.jdesktop.swingx.plaf.nimbus;
	exports org.jdesktop.swingx.plaf.synth;
	
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