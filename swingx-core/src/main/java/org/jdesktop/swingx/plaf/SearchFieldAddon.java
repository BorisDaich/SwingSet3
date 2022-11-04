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

/**
 * use from svg generated {@link RadianceIcon}s,
 * 
 * 
 * see <A href="https://github.com/homebeaver/SwingSet/issues/38">Examples with screenshots</A>
 * 
 * @author homeb
 *
 */
public class SearchFieldAddon extends AbstractComponentAddon {
	
    public static final String SEARCH_FIELD_SOURCE = "searchField";
    public static final String BUTTON_SOURCE = "button";
    private static final int ICON_SIZE = RadianceIcon.SMALL_ICON;

    public SearchFieldAddon() {
        super("JXSearchField");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
		super.addBasicDefaults(addon, defaults);
		
		defaults.add("SearchField.layoutStyle", LayoutStyle.MAC);

		// Icons from svg:
		RadianceIcon searchIcon = SearchIcon.of(ICON_SIZE, ICON_SIZE);
		RadianceIcon searchIconRollover = SearchIcon.of(ICON_SIZE, ICON_SIZE);
		searchIconRollover.setReflection(false, true); // vertical mirror
		RadianceIcon searchIconpPessed = SearchIcon.of(ICON_SIZE, ICON_SIZE);
		searchIconpPessed.setReflection(false, true); // vertical mirror
		searchIconpPessed.setColorFilter(color -> Color.RED);
		
		RadianceIcon deleteIcon = DeleteIcon.of(ICON_SIZE, ICON_SIZE);
		RadianceIcon deleteIconRollover = DeleteIcon.of(ICON_SIZE, ICON_SIZE);
		deleteIconRollover.setColorFilter(color -> Color.RED);
		RadianceIcon deletedIcon = DeletedIcon.of(ICON_SIZE, ICON_SIZE);
		deletedIcon.setColorFilter(color -> Color.RED);
		
		RadianceIcon popupIcon = ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);		
		RadianceIcon popupIconRollover = ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
		popupIconRollover.setRotation(RadianceIcon.SOUTH);
		
		defaults.add("SearchField.icon",         new IconUIResource(searchIcon));
		defaults.add("SearchField.rolloverIcon", new IconUIResource(searchIconRollover));
		defaults.add("SearchField.pressedIcon",  new IconUIResource(searchIconpPessed));
		
		defaults.add("SearchField.popupIcon",         new IconUIResource(popupIcon));
		defaults.add("SearchField.popupRolloverIcon", new IconUIResource(popupIconRollover));
		
		defaults.add("SearchField.clearIcon",         new IconUIResource(deleteIcon));
		defaults.add("SearchField.clearRolloverIcon", new IconUIResource(deleteIconRollover));
		defaults.add("SearchField.clearPressedIcon",  new IconUIResource(deletedIcon));
		
		defaults.add("SearchField.buttonMargin", new InsetsUIResource(1, 1, 1, 1));
		defaults.add("SearchField.popupSource", BUTTON_SOURCE);
		
		//webstart fix
		UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.SearchField");
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
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addMacDefaults(addon, defaults);
        
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

}
