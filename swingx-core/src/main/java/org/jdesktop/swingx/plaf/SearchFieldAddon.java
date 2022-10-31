package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;

import org.jdesktop.swingx.JXSearchField.LayoutStyle;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.DeleteIcon;
import org.jdesktop.swingx.icon.DeletedIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SearchIcon;

public class SearchFieldAddon extends AbstractComponentAddon {
	
    public static final String SEARCH_FIELD_SOURCE = "searchField";
    public static final String BUTTON_SOURCE = "button";

    public SearchFieldAddon() {
        super("JXSearchField");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
		super.addBasicDefaults(addon, defaults);
		
		defaults.add("SearchField.layoutStyle", LayoutStyle.MAC);

/* SwingLabs origin (some gif files not exist):
		defaults.add("SearchField.icon",              getIcon("basic/resources/search.gif")); // Lupe / magnifying glass
		defaults.add("SearchField.rolloverIcon",      getIcon("basic/resources/search_rollover.gif"));
		defaults.add("SearchField.pressedIcon",       getIcon("basic/resources/search.gif"));
		defaults.add("SearchField.popupIcon",         getIcon("basic/resources/search_popup.gif"));
		defaults.add("SearchField.popupRolloverIcon", getIcon("basic/resources/search_popup_rollover.gif"));
		defaults.add("SearchField.clearIcon",         getIcon("basic/resources/clear.gif"));
		defaults.add("SearchField.clearRolloverIcon", getIcon("basic/resources/clear_rollover.gif"));
		defaults.add("SearchField.clearPressedIcon",  getIcon("basic/resources/clear_pressed.gif"));
 */
		// Icons from svg:
		RadianceIcon searchIcon = SearchIcon.of(RadianceIcon.ACTION_ICON,   RadianceIcon.ACTION_ICON);
		RadianceIcon searchIconRollover = SearchIcon.of(RadianceIcon.ACTION_ICON,RadianceIcon.ACTION_ICON);
		searchIconRollover.setReflection(false, true); // vertical mirror
		RadianceIcon searchIconpPessed = SearchIcon.of(RadianceIcon.ACTION_ICON,RadianceIcon.ACTION_ICON);
		searchIconpPessed.setReflection(false, true); // vertical mirror
		searchIconpPessed.setColorFilter(color -> Color.RED);
		
		RadianceIcon deleteIcon = DeleteIcon.of(RadianceIcon.ACTION_ICON,   RadianceIcon.ACTION_ICON);
		RadianceIcon deleteIconRollover = DeleteIcon.of(RadianceIcon.ACTION_ICON,   RadianceIcon.ACTION_ICON);
		deleteIconRollover.setColorFilter(color -> Color.RED);
		RadianceIcon deletedIcon = DeletedIcon.of(RadianceIcon.ACTION_ICON,   RadianceIcon.ACTION_ICON);
		deletedIcon.setColorFilter(color -> Color.RED);
		
		RadianceIcon popupIcon = ChevronIcon.of(RadianceIcon.XS,RadianceIcon.XS);		
		RadianceIcon popupIconRollover = ChevronIcon.of(RadianceIcon.XS,RadianceIcon.XS);
		popupIconRollover.setRotation(RadianceIcon.SOUTH);
		
		defaults.add("SearchField.icon",         new IconUIResource(searchIcon));
		defaults.add("SearchField.rolloverIcon", new IconUIResource(searchIconRollover));
		defaults.add("SearchField.pressedIcon",  new IconUIResource(searchIconpPessed));
		
//		defaults.add("SearchField.popupIcon",         getIcon("basic/resources/search_popup.gif"));
//		defaults.add("SearchField.popupRolloverIcon", getIcon("basic/resources/search.gif"));
		// override:
		defaults.add("SearchField.popupIcon",         new IconUIResource(popupIcon));
		defaults.add("SearchField.popupRolloverIcon", new IconUIResource(popupIconRollover));
		
		defaults.add("SearchField.clearIcon",         new IconUIResource(deleteIcon));
		defaults.add("SearchField.clearRolloverIcon", new IconUIResource(deleteIconRollover));
		defaults.add("SearchField.clearPressedIcon",  new IconUIResource(deletedIcon));
		
		defaults.add("SearchField.buttonMargin", new InsetsUIResource(1, 1, 1, 1));
		defaults.add("SearchField.popupSource", BUTTON_SOURCE);
		
		//webstart fix
		UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.SearchField");
//		UIManager.getDefaults().addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.SearchField");
	}

    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addMetalDefaults(addon, defaults);
        
        defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 1, 1));
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addWindowsDefaults(addon, defaults);
        
        defaults.add("SearchField.promptFontStyle", Font.ITALIC);
        defaults.add("SearchField.layoutStyle", LayoutStyle.VISTA);
//        defaults.add("SearchField.icon", getIcon("windows/resources/search.gif"));
//        defaults.add("SearchField.rolloverIcon", getIcon("windows/resources/search_rollover.gif"));
//        defaults.add("SearchField.pressedIcon", getIcon("windows/resources/search_pressed.gif"));
//        defaults.add("SearchField.popupIcon", getIcon("windows/resources/search_popup.gif"));
//        defaults.add("SearchField.popupRolloverIcon", getIcon("windows/resources/search_popup_rollover.gif"));
//        defaults.add("SearchField.popupPressedIcon", getIcon("windows/resources/search_popup_pressed.gif"));
//        defaults.add("SearchField.clearIcon", getIcon("windows/resources/clear.gif"));
//        defaults.add("SearchField.clearRolloverIcon", getIcon("windows/resources/clear_rollover.gif"));
//        defaults.add("SearchField.clearPressedIcon", getIcon("windows/resources/clear_pressed.gif"));
        defaults.add("SearchField.useSeperatePopupButton", Boolean.TRUE);
        defaults.add("SearchField.popupOffset", -1);

        // Do it like 'Windows Media Player' in XP:
        // Replace the border line with the search button line on rollover.
        // But not in classic mode!
        if (UIManager.getLookAndFeel().getClass().getName().indexOf("Classic") == -1) {
            defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, -1, 0, -1));
        } else {
            defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 0, 0));
        }
    }

    @Override
    protected void addMotifDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addMotifDefaults(addon, defaults);
        
//        defaults.add("SearchField.icon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.rolloverIcon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.pressedIcon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.popupIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.popupRolloverIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.popupPressedIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.clearIcon", getIcon("macosx/resources/clear.png"));
//        defaults.add("SearchField.clearRolloverIcon", getIcon("macosx/resources/clear_rollover.png"));
//        defaults.add("SearchField.clearPressedIcon", getIcon("macosx/resources/clear_pressed.png"));
    }

    @Override
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addMacDefaults(addon, defaults);
        
//        defaults.add("SearchField.icon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.rolloverIcon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.pressedIcon", getIcon("macosx/resources/search.png"));
//        defaults.add("SearchField.popupIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.popupRolloverIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.popupPressedIcon", getIcon("macosx/resources/search_popup.png"));
//        defaults.add("SearchField.clearIcon", getIcon("macosx/resources/clear.png"));
//        defaults.add("SearchField.clearRolloverIcon", getIcon("macosx/resources/clear_rollover.png"));
//        defaults.add("SearchField.clearPressedIcon", getIcon("macosx/resources/clear_pressed.png"));
        defaults.add("SearchField.buttonMargin", new InsetsUIResource(0, 0, 0, 0));
        defaults.add("SearchField.popupSource", SEARCH_FIELD_SOURCE);
    }

    // Workaround: Only return true, when the current LnF is Windows or PlasticXP.
    @Override
    protected boolean isWindows(LookAndFeelAddons addon) {
        return super.isWindows(addon)
                || UIManager.getLookAndFeel().getClass().getName().indexOf("Windows") != -1
                || UIManager.getLookAndFeel().getClass().getName().indexOf("PlasticXP") != -1;
    }

//    private IconUIResource getIcon(String resourceName) {
//        URL url = getClass().getResource(resourceName);
//        if (url == null) {
//            return null;
//        } else {
//            return new IconUIResource(new ImageIcon(url));
//        }
//    }
}
