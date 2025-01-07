/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.test.PropertyChangeReport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;


@RunWith(JUnit4.class)
public class JXTaskPaneTest extends TestCase {

    private static final Logger LOG = Logger.getLogger(JXTaskPaneTest.class.getName());
    
	@BeforeClass
	public static void install() {
//		System.getProperties().put("swing.addon", "org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons");
//		System.getProperties().get("swing.addon");
		try {
			LOG.config("System.Property \"swing.addon\" = " + System.getProperties().get("swing.addon"));
			String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
			LOG.config("systemLookAndFeelClassName = " + systemLookAndFeelClassName);
//			LOG.info("setLookAndFeel (LAF) to " + systemLookAndFeelClassName);
//			UIManager.setLookAndFeel(systemLookAndFeelClassName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBean() throws Exception {
		PropertyChangeReport report = new PropertyChangeReport();
		JXTaskPane group = new JXTaskPane();
		group.setAnimated(false);
		group.addPropertyChangeListener(report);

		// ANIMATED PROPERTY
		group.setAnimated(true);
		assertTrue(group.isAnimated());
		assertEquals(JXTaskPane.ANIMATED_CHANGED_KEY, report.getLastEvent().getPropertyName());
		assertTrue(report.getLastNewBooleanValue());
		LOG.fine("prop "+JXTaskPane.ANIMATED_CHANGED_KEY + " is true.");

		group.setAnimated(false);
		assertFalse(group.isAnimated());
		assertFalse(report.getLastNewBooleanValue());

		UIManager.put("TaskPane.animate", Boolean.FALSE);
		JXTaskPane anotherGroup = new JXTaskPane();
		assertFalse(anotherGroup.isAnimated());

		UIManager.put("TaskPane.animate", null);
		anotherGroup = new JXTaskPane();
		assertTrue(anotherGroup.isAnimated());

		// TITLE
		group.setTitle("the title");
		assertEquals("the title", group.getTitle());
		assertEquals(JXTaskPane.TITLE_CHANGED_KEY, report.getLastEvent().getPropertyName());
		assertEquals("the title", report.getLastNewValue());

		// ICON
		assertNull(group.getIcon());
		Icon icon = new EmptyIcon();
		group.setIcon(icon);
		assertNotNull(group.getIcon());
		assertEquals(JXTaskPane.ICON_CHANGED_KEY, report.getLastEvent().getPropertyName());
		assertEquals(icon, report.getLastNewValue());
		group.setIcon(null);
		assertEquals(icon, report.getLastOldValue());
		assertNull(report.getLastNewValue());

		// SPECIAL
		assertFalse(group.isSpecial());
		group.setSpecial(true);
		assertTrue(group.isSpecial());
		assertEquals(JXTaskPane.SPECIAL_CHANGED_KEY, report.getLastEvent().getPropertyName());
		assertTrue(report.getLastNewBooleanValue());
		assertFalse(report.getLastOldBooleanValue());

		// SCROLL ON EXPAND
		assertFalse(group.isScrollOnExpand());
		group.setScrollOnExpand(true);
		assertTrue(group.isScrollOnExpand());
		assertEquals(JXTaskPane.SCROLL_ON_EXPAND_CHANGED_KEY, report.getLastEvent().getPropertyName());
		assertTrue(report.getLastNewBooleanValue());
		assertFalse(report.getLastOldBooleanValue());

		// EXPANDED
		assertFalse(group.isCollapsed());
		group.setCollapsed(true);
		assertTrue(group.isCollapsed());
		PropertyChangeEvent event = report.getLastEvent("collapsed");
//    assertEquals("expanded", report.getLastEvent().getPropertyName());
		assertTrue((Boolean) event.getNewValue());
		assertFalse((Boolean) event.getOldValue());
	}

	@Test
	public void testContentPane() {
		JXTaskPane group = new JXTaskPane();
		assertEquals(0, group.getContentPane().getComponentCount());

		// Objects are not added to the taskPane but to its contentPane
		JButton button = new JButton();
		group.add(button);
		assertEquals(group.getContentPane(), button.getParent());
		group.remove(button);
		assertNull(button.getParent());
		assertEquals(0, group.getContentPane().getComponentCount());
		group.add(button);
		group.removeAll();
		assertEquals(0, group.getContentPane().getComponentCount());
		group.add(button);
		group.remove(0);
		assertEquals(0, group.getContentPane().getComponentCount());

		BorderLayout layout = new BorderLayout();
		group.setLayout(layout);
		assertEquals(layout, group.getContentPane().getLayout());
		assertFalse(layout == group.getLayout());
	}

	@Test
	public void testActions() throws Exception {
		JXTaskPane taskPane = new JXTaskPane();
		@SuppressWarnings("serial")
		Action action = new AbstractAction() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
			}
		};
		assertEquals(0, taskPane.getContentPane().getComponentCount());
		Component component = taskPane.add(action);
		assertEquals(taskPane.getContentPane(), component.getParent());
		assertEquals(1, taskPane.getContentPane().getComponentCount());
	}

	@Test
	public void testAddon() throws Exception {
		// move around all addons
		TestUtilities.cycleAddons(new JXTaskPane());
	}

	/* ab v1.7.8:
	 * org.jdesktop.swingx.plaf.misc muss in module swingx.core exportiert werden
	 * org.jdesktop.swingx.plaf.metal dto 
	 */
	@Test
	public void testIssue344() throws Exception {
		new JXTaskPane();
		LookAndFeelAddons.setAddon(new MetalLookAndFeelAddons());
		String uiClass = UIManager.getString(JXTaskPane.uiClassID);
//		UIDefaults uid = UIManager.getLookAndFeelDefaults();
//		uid.forEach((k,v) -> {
//			System.out.println(k + " : "+v);
//		});
		LOG.info("JXTaskPane.uiClassID:"+uiClass + " expected MetalTaskPaneUI or GlossyTaskPaneUI");
		boolean found = "org.jdesktop.swingx.plaf.metal.MetalTaskPaneUI".equals(uiClass)
				|| "org.jdesktop.swingx.plaf.misc.GlossyTaskPaneUI".equals(uiClass);
		assertTrue("Failed to locate UI class for " + JXTaskPane.uiClassID, found);
	}
  
//    
//    /**
//     * Issue #835-swingx: event notification on expanded.
//     * Here: two events fired on setExpanded
//     * @deprecated (pre-0.9.3) remove with {@link JXTaskPane#setExpanded(boolean)}
//     */
//    @Deprecated
//    @Test
//    public void testSingleExpanded() {
//        JXTaskPane pane = new JXTaskPane();
//        PropertyChangeReport report = new PropertyChangeReport();
//        pane.addPropertyChangeListener(report);
//        pane.setExpanded(!pane.isExpanded());
//        TestUtils.assertPropertyChangeEvent(report, "expanded", 
//                !pane.isExpanded(), pane.isExpanded(), false);
//    }
}
