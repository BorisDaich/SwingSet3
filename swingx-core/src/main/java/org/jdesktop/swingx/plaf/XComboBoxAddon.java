package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.RadianceIcon;

public class XComboBoxAddon extends AbstractComponentAddon {

    private static final Logger LOG = Logger.getLogger(XComboBoxAddon.class.getName());
    private static final int ICON_SIZE = RadianceIcon.XS;

	public XComboBoxAddon() {
		super("JXComboBox");
	}

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add(JXComboBox.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicXComboBoxUI");
        
		LOG.info("\n get background "+UIManager.getLookAndFeelDefaults().get("ComboBox.background")
				+"\n get foreground "+UIManager.getLookAndFeelDefaults().get("ComboBox.foreground")
				);
        /*
         * key "ComboBox.background" is defined in javax.swing.plaf.basic.BasicLookAndFeel
         * replace the value with secondary3 Color
         */
        UIManager.getLookAndFeelDefaults().put("ComboBox.background", MetalLookAndFeel.getCurrentTheme().getControl());
// TEST UIManager.getLookAndFeelDefaults().put("ComboBox.background", Color.red);

//        Border border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);
//        UIManager.getLookAndFeelDefaults().put("ComboBox.border", border);
//        UIManager.getLookAndFeelDefaults().put("ComboBox.border", BorderFactory.createBevelBorder(BevelBorder.RAISED));
        UIManager.getLookAndFeelDefaults().put("ComboBox.border", BorderFactory.createEtchedBorder());
//        UIManager.getLookAndFeelDefaults().put("ComboBox.padding", new java.awt.Insets(5,10,0,15));
        
        /*
         * for a BLACK & WHITE BasicArrowButton use buttonBackground=WHITE and buttonDarkShadow=BLACK
         */
//        UIManager.getLookAndFeelDefaults().put("ComboBox.buttonBackground", Color.WHITE);      
//        UIManager.getLookAndFeelDefaults().put("ComboBox.buttonShadow", Color.red); // button border when pressed    
//        UIManager.getLookAndFeelDefaults().put("ComboBox.buttonDarkShadow", Color.BLACK); // Color of arrow     
//        UIManager.getLookAndFeelDefaults().put("ComboBox.buttonHighlight", Color.YELLOW); // botton left&top border
        
		// Icons from svg:
//		RadianceIcon icon = ChevronIcon.of(ICON_SIZE, ICON_SIZE);
//		//icon.setRotation(RadianceIcon.SOUTH);
//		icon.setReflection(true); // for ChevronIcon same effect as icon.setRotation(RadianceIcon.SOUTH)
//		defaults.add("ComboBox.icon",         new IconUIResource(icon));
//		defaults.add("ComboBox.isShowingPopupIcon",  new IconUIResource(ChevronIcon.of(ICON_SIZE, ICON_SIZE)));
    }

    @Override
	protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
		defaults.add(JXComboBox.uiClassID, "org.jdesktop.swingx.plaf.synth.SynthXComboBoxUI");
		LOG.info("added key "+JXComboBox.uiClassID);

		/*
		 * key "ComboBox.background" is defined in javax.swing.plaf.nimbus.NimbusDefaults
		 * addColor(d, "ComboBox:\"ComboBox.listRenderer\".background", "nimbusLightBackground", 0.0f, 0.0f, 0.0f, 0)
		 * replace the value with "control" Color
		 */
		LOG.info("get background "+UIManager.getLookAndFeelDefaults().get("ComboBox:\"ComboBox.listRenderer\".background"));
		UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".background", UIManager.getColor("control"));
//		UIManager.getLookAndFeelDefaults().put("ComboBox:\"ComboBox.listRenderer\".selectionBackground", UIManager.getColor("nimbusSelectionBackground"));
		UIManager.getLookAndFeelDefaults().put("ComboBox.selectionBackground", UIManager.getColor("nimbusSelectionBackground"));
		UIManager.getLookAndFeelDefaults().put("ComboBox.selectionForeground", UIManager.getColor("nimbusSelectedText"));

//        UIManager.getLookAndFeelDefaults().put("ComboBox.border", new BorderUIResource.EtchedBorderUIResource(EtchedBorder.RAISED));
		defaults.add("ComboBox.border", new BorderUIResource.EtchedBorderUIResource());
//		Border border = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.red);
//        UIManager.getLookAndFeelDefaults().put("ComboBox.border", border);       
	}

}
