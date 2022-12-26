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

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXList;

/**
 * Addon for <code>JXList</code>.
 * <p>
 * 
 * Install a custom ui to support sorting/filtering in JXList.
 */
public class XListAddon extends AbstractComponentAddon {

    private static final Logger LOG = Logger.getLogger(XListAddon.class.getName());

    public XListAddon() {
        super("JXList");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXList.uiClassID, "org.jdesktop.swingx.plaf.basic.core.BasicXListUI");
        
        UIManager.getLookAndFeelDefaults().put("List.dropLineColor", Color.YELLOW);

        /*
         * key "List.background" is defined in javax.swing.plaf.basic.BasicLookAndFeel
         * replace the value with secondary3 Color
         */
        UIManager.getLookAndFeelDefaults().put("List.background", MetalLookAndFeel.getCurrentTheme().getControl());

        Border border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.RED);
        UIManager.getLookAndFeelDefaults().put("List.focusSelectedCellHighlightBorder", border);
        UIManager.getLookAndFeelDefaults().put("List.focusCellHighlightBorder", BorderFactory.createBevelBorder(BevelBorder.RAISED));
        UIManager.getLookAndFeelDefaults().put("List.cellNoFocusBorder", BorderFactory.createEtchedBorder());
        
        if (isGTK()) {
            replaceListTableBorders(addon, defaults);
        }
    }

    @Override
    protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    	// Adds default key/value pairs addon to the given list defaults
//    	int oldSize = defaults.toArray().length;
//		super.addNimbusDefaults(addon, defaults);
//		LOG.info("oldSize="+oldSize+" : added "+(defaults.toArray().length - oldSize) + " key/value pairs");
//    	List<Object> l = new ArrayList<Object>(Arrays.asList(defaults.toArray())); // defaults.toArray()
//    	l.forEach( e -> {
//    		LOG.info(""+e);
//    	});
        defaults.add(JXList.uiClassID, "org.jdesktop.swingx.plaf.synth.SynthXListUI");

        /*
		 * key "List.background" is defined in javax.swing.plaf.nimbus.NimbusDefaults
		 * addColor(d, "List.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0)
		 * replace the value with "control" Color
		 */
		UIManager.getLookAndFeelDefaults().put("List.background", UIManager.getColor("control"));

		Border border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);
        UIManager.getLookAndFeelDefaults().put("List.focusSelectedCellHighlightBorder", border);       
        UIManager.getLookAndFeelDefaults().put("List.focusCellHighlightBorder", BorderFactory.createBevelBorder(BevelBorder.RAISED));
        UIManager.getLookAndFeelDefaults().put("List.cellNoFocusBorder", BorderFactory.createEtchedBorder());
        
    }
    

    private void replaceListTableBorders(LookAndFeelAddons addon, DefaultsList defaults) {
        replaceBorder(defaults, "List.", "focusCellHighlightBorder");
        replaceBorder(defaults, "List.", "focusSelectedCellHighlightBorder");
        replaceBorder(defaults, "List.", "noFocusBorder");
    }



    /**
     * @param defaults
     * @param componentPrefix
     * @param borderKey
     */
    private void replaceBorder(DefaultsList defaults, String componentPrefix, String borderKey) {
        String key = componentPrefix + borderKey;
        Border border = UIManager.getBorder(componentPrefix + borderKey);
        if (border instanceof AbstractBorder && border instanceof UIResource
                && border.getClass().getName().contains("ListTable")) {
            border = new SafeBorder((AbstractBorder) border);
            // PENDING JW: this is fishy ... adding to lookAndFeelDefaults is taken
            UIManager.getLookAndFeelDefaults().put(key, border);
            // adding to defaults is not
//            defaults.add(key, border);
            
        }
    }

    /**
     * 
     * @return true if the LF is GTK.
     */
    private boolean isGTK() {
        return "GTK".equals(UIManager.getLookAndFeel().getID());
    }

}
