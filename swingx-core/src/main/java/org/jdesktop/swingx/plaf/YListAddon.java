package org.jdesktop.swingx.plaf;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JYList;

public class YListAddon extends AbstractComponentAddon {

	public YListAddon() {
		super("JYList");
	}

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JYList.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicYListUI");
        
        /*
         * key "List.background" is defined in javax.swing.plaf.basic.BasicLookAndFeel
         * replace the value with secondary3 Color
         */
        UIManager.getLookAndFeelDefaults().put("List.background", MetalLookAndFeel.getCurrentTheme().getControl());
        Border border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);
        UIManager.getLookAndFeelDefaults().put("List.focusSelectedCellHighlightBorder", border);
        UIManager.getLookAndFeelDefaults().put("List.focusCellHighlightBorder", new BevelBorder(BevelBorder.RAISED));
        
    }

    @Override
	protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
		defaults.add(JYList.uiClassID, "org.jdesktop.swingx.plaf.synth.SynthYListUI");

		/*
		 * key "List.background" is defined in javax.swing.plaf.nimbus.NimbusDefaults
		 * addColor(d, "List.background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0)
		 * replace the value with "control" Color
		 */
		UIManager.getLookAndFeelDefaults().put("List.background", UIManager.getColor("control"));
//		UIManager.getLookAndFeelDefaults().put("List.background", new ColorUIResource(214, 217, 223));

	}

}
