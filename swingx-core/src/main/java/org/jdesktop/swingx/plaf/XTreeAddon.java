/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
 */
package org.jdesktop.swingx.plaf;

import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.icon.FileIcon;
import org.jdesktop.swingx.icon.FolderIcon;
import org.jdesktop.swingx.icon.FolderPlusIcon;
import org.jdesktop.swingx.icon.RadianceIcon;

public class XTreeAddon extends AbstractComponentAddon {
   
//    private static final Logger LOG = Logger.getLogger(XTreeAddon.class.getName());

	RadianceIcon openIcon;
	RadianceIcon closedIcon;
	RadianceIcon leafIcon;

    /** Creates a new instance of ErrorPaneAddon */
    public XTreeAddon() {
        super("JXTree");
        openIcon =       FolderIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
        closedIcon = FolderPlusIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
        leafIcon =         FileIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        /*
         * keys are defined in javax.swing.plaf.basic.BasicLookAndFeel:
            "Tree.background", window,
            "Tree.textBackground", table.get("text"),
         ==> replace the value with secondary3 Color
         
            "Tree.openIcon"  , SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeOpen.gif"),
            "Tree.closedIcon", SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeClosed.gif"),
            "Tree.leafIcon"  , SwingUtilities2.makeIcon(getClass(), BasicLookAndFeel.class, "icons/TreeLeaf.gif"),      
         ==> replace with RadianceIcons
         */
        UIManager.getLookAndFeelDefaults().put("Tree.background", MetalLookAndFeel.getCurrentTheme().getControl());
        UIManager.getLookAndFeelDefaults().put("Tree.textBackground", MetalLookAndFeel.getCurrentTheme().getControl());

        UIManager.getLookAndFeelDefaults().put("Tree.openIcon", openIcon);
        UIManager.getLookAndFeelDefaults().put("Tree.closedIcon", closedIcon);
        UIManager.getLookAndFeelDefaults().put("Tree.leafIcon", leafIcon);        
    }

    protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
		/*
		 * keys are defined in javax.swing.plaf.nimbus.NimbusDefaults:
        addColor(d, "Tree.textBackground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "Tree.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
		==> replace the value with "control" Color
		 */
		UIManager.getLookAndFeelDefaults().put("Tree.textBackground", UIManager.getColor("control"));
		UIManager.getLookAndFeelDefaults().put("Tree.background", UIManager.getColor("control"));
		
        UIManager.getLookAndFeelDefaults().put("Tree.openIcon", openIcon);
        UIManager.getLookAndFeelDefaults().put("Tree.closedIcon", closedIcon);
        UIManager.getLookAndFeelDefaults().put("Tree.leafIcon", leafIcon);
    }

}
