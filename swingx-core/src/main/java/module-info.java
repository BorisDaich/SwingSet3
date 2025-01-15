module swingx.core {
	exports org.jdesktop.swingx;
	exports org.jdesktop.swingx.auth;
	exports org.jdesktop.swingx.calendar;
	exports org.jdesktop.swingx.color;
	exports org.jdesktop.swingx.combobox;
	exports org.jdesktop.swingx.error;
	exports org.jdesktop.swingx.hyperlink;
	exports org.jdesktop.swingx.icon;
	exports org.jdesktop.swingx.multislider;
	exports org.jdesktop.swingx.prompt;
	exports org.jdesktop.swingx.renderer;
	exports org.jdesktop.swingx.rollover;
	exports org.jdesktop.swingx.search;
	exports org.jdesktop.swingx.sort;
	exports org.jdesktop.swingx.table;
	exports org.jdesktop.swingx.tips;
	exports org.jdesktop.swingx.tree;
	exports org.jdesktop.swingx.treetable;
	
	// Definiert Schnittstellen und exportiert sie
	uses org.jdesktop.swingx.plaf.LookAndFeelAddons;
	uses org.jdesktop.swingx.plaf.MonthViewUI;
	exports org.jdesktop.swingx.plaf;
	uses org.jdesktop.swingx.decorator.Highlighter;
	exports org.jdesktop.swingx.decorator;
	uses org.jdesktop.swingx.plaf.basic.BasicMonthViewUI;
// enum	uses org.jdesktop.swingx.plaf.basic.CalendarState;	
	uses org.jdesktop.swingx.plaf.basic.CalendarRenderingHandler;
// not visible:	uses org.jdesktop.swingx.plaf.basic.BasicCalendarRenderingHandler;
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