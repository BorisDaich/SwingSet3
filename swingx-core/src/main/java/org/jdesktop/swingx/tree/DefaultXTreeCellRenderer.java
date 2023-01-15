/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.tree;

import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.SwingXUtilities;

/**
 * Quick fix for #1061-swingx (which actually is a core issue): 
 * tree icons lost on toggle laf. Updates colors as well -
 * but beware: this is incomplete as some of super's properties are private!
 * 
 * Will not do more because in the longer run (as soon as we've fixed the editor issues) 
 * the JXTree's default renderer will be changed to SwingX DefaultTreeRenderer.
 * 
 * @author Jeanette Winzenburg
 */
@SuppressWarnings("serial")
public class DefaultXTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Logger LOG = Logger.getLogger(DefaultXTreeCellRenderer.class.getName());

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to update icons and colors.
     */
    @Override
    public void updateUI() {
        super.updateUI();
//        LOG.info("------ do updateIcons() + updateColors()");
        updateIcons();
        updateColors();
    }

    /**
     * 
     */
    protected void updateColors() {
    	LOG.fine("Colors:"
    			+"\n Tree.background="+UIManager.getColor("Tree.background")
// Tree.background=DerivedColor(color=255,255,255 parent=nimbusLightBackground offsets=0.0,0.0,0.0,0 pColor=255,255,255
    			+"\n Tree.selectionForeground="+UIManager.getColor("Tree.selectionForeground")
    			+"\n Tree.textForeground="+UIManager.getColor("Tree.textForeground")
    			+"\n Tree.selectionBackground="+UIManager.getColor("Tree.selectionBackground")
// Tree.selectionBackground=DerivedColor(color=57,105,138 parent=nimbusSelectionBackground offsets=0.0,0.0,0.0,0 pColor=57,105,138
    			+"\n Tree.textBackground="+UIManager.getColor("Tree.textBackground")
    			+"\n Tree.selectionBorderColor="+UIManager.getColor("Tree.selectionBorderColor")
    			+"\n"
    			);
        if (SwingXUtilities.isUIInstallable(getTextSelectionColor())) {
            setTextSelectionColor(UIManager.getColor("Tree.selectionForeground"));
        }
        if (SwingXUtilities.isUIInstallable(getTextNonSelectionColor())) {
            setTextNonSelectionColor(UIManager.getColor("Tree.textForeground"));
        }
        if (SwingXUtilities.isUIInstallable(getBackgroundSelectionColor())) {
            setBackgroundSelectionColor(UIManager.getColor("Tree.selectionBackground"));
        }
        if (SwingXUtilities.isUIInstallable(getBackgroundNonSelectionColor())) {
            setBackgroundNonSelectionColor(UIManager.getColor("Tree.textBackground"));
        }
        if (SwingXUtilities.isUIInstallable(getBorderSelectionColor())) {
            setBorderSelectionColor(UIManager.getColor("Tree.selectionBorderColor"));
        }
//        Object value = UIManager.get("Tree.drawsFocusBorderAroundIcon");
//        drawsFocusBorderAroundIcon = (value != null && ((Boolean)value).
//                                      booleanValue());
//        value = UIManager.get("Tree.drawDashedFocusIndicator");
//        drawDashedFocusIndicator = (value != null && ((Boolean)value).
//                                    booleanValue());
    }

    /**
     * 
     */
    protected void updateIcons() {
        if (SwingXUtilities.isUIInstallable(getLeafIcon())) {
        	//  the given property "Tree.leafIcon" should be replacedby the UI's default value
        	LOG.fine("Tree.leafIcon should be replacedby the UI's default value !!!!!!");
            setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
        }
        if (SwingXUtilities.isUIInstallable(getClosedIcon())) {
            setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
        }
        if (SwingXUtilities.isUIInstallable(getOpenIcon())) {
            setOpenIcon(UIManager.getIcon("Tree.openIcon"));
        }

    }

//    public Icon getDefaultLeafIcon() {
//        return DefaultLookup.getIcon(this, ui, "Tree.leafIcon");
//    }

}
