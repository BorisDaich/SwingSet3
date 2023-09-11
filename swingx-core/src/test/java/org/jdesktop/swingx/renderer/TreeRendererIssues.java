/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.test.ComponentTreeTableModel;
import org.jdesktop.swingx.test.XTestUtils;

/**
 * Known/open issues with tree renderer.
 * 
 * @author Jeanette Winzenburg
 */
public class TreeRendererIssues extends InteractiveTestCase {
    
    private static final Logger LOG = Logger.getLogger(TreeRendererIssues.class.getName());

    public static void main(String[] args) {
        TreeRendererIssues test = new TreeRendererIssues();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JTree tree;
    private DefaultTreeCellRenderer coreTreeRenderer;
    private DefaultTreeRenderer xTreeRenderer;

    // flag used in setup to explicitly choose LF
    private boolean defaultToSystemLF;

    @Override
    protected void setUp() throws Exception {
//        setSystemLF(true);
        LOG.info("LF: " + UIManager.getLookAndFeel());
        LookAndFeel laf = UIManager.getLookAndFeel();
        if(laf instanceof MetalLookAndFeel metal) {
        	LOG.info("Theme: " + MetalLookAndFeel.getCurrentTheme());
        }
        UIManager.put("Tree.drawsFocusBorderAroundIcon", Boolean.TRUE);
        // make sure we have the same default for each test
        defaultToSystemLF = false;
        setSystemLF(defaultToSystemLF);
        tree = new JTree();
        coreTreeRenderer = new DefaultTreeCellRenderer();
        xTreeRenderer = new DefaultTreeRenderer();
    }
    

    public void interactiveTransparentRenderer() throws IOException {
        final JXTree tree = new JXTree(new ComponentTreeTableModel(new JXFrame()));
        tree.setEditable(true);
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof Component) {
                    return ((Component) value).getName();
                }
                return " - no component - ";
            }};
        DefaultTreeRenderer renderer = new DefaultTreeRenderer(sv);
        tree.setCellRenderer(renderer);
        tree.setForeground(Color.WHITE);
        JXPanel panel = new JXPanel(new BorderLayout());
        ImagePainter imagePainter = new ImagePainter(XTestUtils.loadDefaultImage()); 
        imagePainter.setFillHorizontal(true);
        imagePainter.setFillVertical(true);
        panel.setBackgroundPainter(imagePainter);
        panel.add(new JScrollPane(tree));
        
        JXFrame frame = wrapInFrame(panel, "renderer");
        WrappingProvider provider = (WrappingProvider) renderer.getComponentProvider();
        provider.getDelegate().getRendererComponent(null).setOpaque(false);
        tree.setOpaque(false);
        ((JComponent) tree.getParent()).setOpaque(false);
        ((JComponent) tree.getParent().getParent()).setOpaque(false);
        Action edit = new AbstractActionExt("edit") {

            public void actionPerformed(ActionEvent e) {
                if (tree.isSelectionEmpty()) return;
                TreePath path = tree.getSelectionPath();
                Component comp = (Component) path.getLastPathComponent();
                String oldName = comp.getName();
                if (oldName == null) {
                    oldName = "none";
                }
                String changed = oldName.length() > 60 ? oldName.substring(0, 20) :
                    oldName + "+++++++++++++++++++++++++++++++++++++++++++++";
                tree.getModel().valueForPathChanged(path, changed);
            }
            
        };
        addAction(frame, edit);
        show(frame);
    }
    
    /**
     * Sanity: icons updated on LF change.
     */
    public void interactiveTreeIconsUpdateUI() {
        JXTree tree = new JXTree();
        DefaultTreeRenderer renderer = new DefaultTreeRenderer();
        tree.setCellRenderer(renderer);
        WrappingIconPanel before = (WrappingIconPanel) renderer.getTreeCellRendererComponent(tree, "", false, false, true, -1, false);
        Icon leaf = before.getIcon();
        LOG.info("Icon leaf "+leaf);
        assertNotNull("sanity", leaf);
        assertEquals("sanity", UIManager.getIcon("Tree.leafIcon"), leaf);
        
        String lf = UIManager.getLookAndFeel().getName();
        setSystemLF(!defaultToSystemLF);
        if (lf.equals(UIManager.getLookAndFeel().getName())) {
            LOG.info("cannot run test - equal LF" + lf);
            return;
        }
        LOG.info("LF: " + UIManager.getLookAndFeel() + " was "+lf);
        
        // XXX icon in Metal and Windows Look and Feel are equal!!
        
        SwingUtilities.updateComponentTreeUI(tree);
        WrappingIconPanel after = (WrappingIconPanel) renderer.getTreeCellRendererComponent(tree, "", false, false, true, -1, false);
        Icon leafAfter = after.getIcon();
        assertNotNull("sanity", leafAfter);
        LOG.info("Icon leafAfter "+leafAfter);
        assertFalse("sanity", leaf.equals(leafAfter));
        assertEquals("icon must be updated", UIManager.getIcon("Tree.leafIcon"), leafAfter);
    }
    
    /**
     * base interaction with tree: renderer uses list's unselected  colors
     * 
     * currently, this test fails because the assumptions are wrong! Core
     * renderer behaves slightly unexpected.
     * 
     *
     */
    public void testTreeRendererExtColors() {
        // prepare standard
        Component coreComponent = coreTreeRenderer.getTreeCellRendererComponent(tree, null,
                false, false, false, 0, false);
        // sanity: known standard behaviour
        assertNull(coreComponent.getBackground());
//        assertNull(coreComponent.getForeground());
        assertNull(tree.getForeground());
        Color uiForeground = UIManager.getColor("Tree.textForeground");
        assertEquals(uiForeground, coreComponent.getForeground());
/*

Caused by: junit.framework.AssertionFailedError: expected:<javax.swing.plaf.ColorUIResource[r=0,g=0,b=0]> but was:<sun.swing.PrintColorUIResource[r=51,g=51,b=51]>

 */
        // prepare extended
        Component xComponent = xTreeRenderer.getTreeCellRendererComponent(tree, null,
                false, false, false, 0, false);
        // assert behaviour same as standard TODO
//        assertEquals(coreComponent.getBackground(), xComponent.getBackground());
    }

    /**
     * base interaction with tree: renderer uses list's unselected custom colors.
     * 
     * currently, this test fails because the assumptions are wrong! 
     * Core renderer behaves slightly unexpected.
     * 
     */
    public void testTreeRendererExtTreeColors() {
        Color background = Color.MAGENTA; // Color[r=255,g=0,b=255]
        Color foreground = Color.YELLOW;
        tree.setBackground(background);
        tree.setForeground(foreground);
        coreTreeRenderer.setBackgroundNonSelectionColor(background);
        coreTreeRenderer.setTextNonSelectionColor(foreground);
        // prepare standard
        Component coreComponent = coreTreeRenderer.getTreeCellRendererComponent(tree, 
        		null, false, false, false, 0, false);
        // sanity: known standard behaviour
        // background is manually painted
        LOG.info("-------------:"+coreComponent);
        LOG.info("-------------:"+coreComponent.getBackground());
        // TODO
//        assertEquals(background, coreComponent.getBackground());
/*

Caused by: junit.framework.AssertionFailedError: expected:<java.awt.Color[r=255,g=0,b=255]> but was:<null>

junit.framework.AssertionFailedError: expected:<java.awt.Color[r=255,g=0,b=255]> but was:<null>
	at junit.framework.Assert.fail(Assert.java:57)
	at junit.framework.Assert.failNotEquals(Assert.java:329)
	at junit.framework.Assert.assertEquals(Assert.java:78)
	at junit.framework.Assert.assertEquals(Assert.java:86)
	at junit.framework.TestCase.assertEquals(TestCase.java:246)
	at org.jdesktop.swingx.renderer.TreeRendererIssues.testTreeRendererExtTreeColors(TreeRendererIssues.java:224)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at junit.framework.TestCase.runTest(TestCase.java:177)
	at junit.framework.TestCase.runBare(TestCase.java:142)
	at junit.framework.TestResult$1.protect(TestResult.java:122)
	at junit.framework.TestResult.runProtected(TestResult.java:142)
	at junit.framework.TestResult.run(TestResult.java:125)
	at junit.framework.TestCase.run(TestCase.java:130)
	at junit.framework.TestSuite.runTest(TestSuite.java:241)
	at junit.framework.TestSuite.run(TestSuite.java:236)
	at org.junit.internal.runners.JUnit38ClassRunner.run(JUnit38ClassRunner.java:90)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:93)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:40)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:529)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:756)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:452)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)

 */
        assertEquals(tree.getForeground(), coreComponent.getForeground());
        // prepare extended
        Component xComponent = xTreeRenderer.getTreeCellRendererComponent(tree,
                null, false, false, false, 0, false);
        // assert behaviour same as standard
        assertEquals(background, xComponent.getBackground());
        assertEquals(foreground, xComponent.getForeground());
    }

}
