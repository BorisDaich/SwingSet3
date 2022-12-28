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

public class XTreeAddon extends AbstractComponentAddon {
   
//    private static final Logger LOG = Logger.getLogger(XTreeAddon.class.getName());

    /** Creates a new instance of ErrorPaneAddon */
    public XTreeAddon() {
        super("JXTree");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
//    	defaults.add(JXTree.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicXTreeUI");
//    	LOG.info("?????????????????added key "+JXTree.uiClassID);
            
        /*
         * keys are defined in javax.swing.plaf.basic.BasicLookAndFeel:
            "Tree.background", window,
            "Tree.textBackground", table.get("text"),
         * replace the value with secondary3 Color
         */
        UIManager.getLookAndFeelDefaults().put("Tree.background", MetalLookAndFeel.getCurrentTheme().getControl());
        UIManager.getLookAndFeelDefaults().put("Tree.textBackground", MetalLookAndFeel.getCurrentTheme().getControl());
        
    }

    protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
//    	defaults.add(JXTree.uiClassID, "org.jdesktop.swingx.plaf.basic.SynthXTreeUI");
//    	LOG.info("!!!!!!!!!!!!!!!added key "+JXTree.uiClassID);

		/*
		 * keys are defined in javax.swing.plaf.nimbus.NimbusDefaults:
        addColor(d, "Tree.textBackground", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
        addColor(d, "Tree.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0);
		 * replace the value with "control" Color
		 */
		UIManager.getLookAndFeelDefaults().put("Tree.textBackground", UIManager.getColor("control"));
		UIManager.getLookAndFeelDefaults().put("Tree.background", UIManager.getColor("control"));

    }

}
