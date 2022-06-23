package org.jdesktop.swingx.icon;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * collects property names for default UI icons defined in javax swing
 * 
 * @author EUG https://github.com/homebeaver
 * @see https://github.com/homebeaver/SwingSet/issues/23
 */
public class DefaultIcons {

	/*
	 * icons defined in class javax.swing.plaf.basic.BasicOptionPaneUI
	 * 
	 * Metal:
	 * see protected Icon BasicOptionPaneUI.getIconForType(int messageType)
	 * the images resources are in javax.swing.plaf.metal.icons
	 * resp. in javax.swing.plaf.metal.icons.ocean for ocean theme
	 * 
	 * Nimbus: all icons are painted by method protected void doPaint
	 * see class javax.swing.plaf.nimbus.OptionPanePainter
	 * f.i. method private void painterrorIconEnabled(Graphics2D g)
	 */
	private static final String OPTIONPANE = "OptionPane";
	/** property name for messageType 0 */
    public static final String ERROR = OPTIONPANE+".errorIcon";
	/** property name for messageType 1 */
    public static final String INFORMATION = OPTIONPANE+".informationIcon";
	/** property name for messageType 2 */
    public static final String WARNING = OPTIONPANE+".warningIcon";
	/** property name for messageType 3 */
    public static final String QUESTION = OPTIONPANE+".questionIcon";
	
	/*
	 * icons defined in class javax.swing.plaf.basic.BasicFileChooserUI 
	 * For nimbus the icons are coded in javax.swing.plaf.nimbus.FileChooserPainter,
	 * f.i. paintfileIconEnabled(Graphics2D)
	 */
	private static final String FILEVIEW = "FileView";
    public static final String DIRECTORY = FILEVIEW+".directoryIcon";
    public static final String FILE = FILEVIEW+".fileIcon";
    public static final String COMPUTER = FILEVIEW+".computerIcon";
    public static final String HARDDRIVE = FILEVIEW+".hardDriveIcon";
    public static final String FLOPPYDRIVE = FILEVIEW+".floppyDriveIcon";
    
    private static final String FILECHOOSER = "FileChooser";
    public static final String NEWFOLDER = FILECHOOSER+".newFolderIcon";
    public static final String UPFOLDER = FILECHOOSER+".upFolderIcon";
    public static final String HOMEFOLDER = FILECHOOSER+".homeFolderIcon";
    public static final String DETAILVIEW = FILECHOOSER+".detailsViewIcon";
    public static final String LISTVIEW = FILECHOOSER+".listViewIcon";
    public static final String VIEWMENU = FILECHOOSER+".viewMenuIcon"; // nix?

    /* nimbus:
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys) {
        //populate componentColors array with colors calculated in getExtendedCacheKeys call
        componentColors = extendedCacheKeys;
        //generate this entire method. Each state/bg/fg/border combo that has
        //been painted gets its own KEY and paint method.
        switch(state) {
            case LEAFICON_ENABLED: paintleafIconEnabled(g); break;
            case CLOSEDICON_ENABLED: paintclosedIconEnabled(g); break;
            case OPENICON_ENABLED: paintopenIconEnabled(g); break;
            case COLLAPSEDICON_ENABLED: paintcollapsedIconEnabled(g); break;
            case COLLAPSEDICON_ENABLED_SELECTED: paintcollapsedIconEnabledAndSelected(g); break;
            case EXPANDEDICON_ENABLED: paintexpandedIconEnabled(g); break;
            case EXPANDEDICON_ENABLED_SELECTED: paintexpandedIconEnabledAndSelected(g); break;

        }

     */
    private static final String TREE = "Tree";
    public static final String COLLAPSED = TREE+".collapsedIcon";
    public static final String EXPANDED = TREE+".expandedIcon";
    // nicht in BasicTreeUI :
    public static final String OPEN = TREE+".openIcon"; // Folder
    public static final String CLOSED = TREE+".closedIcon"; // closed Folder==Folder
    public static final String LEAF = TREE+".leafIcon"; // Sheet
    
    /**
     * Returns an Icon from the defaults. 
     * If the value for key is not an Icon, null is returned.
     * 
     * @param key an Object aka UI name specifying the icon
     * @return Icon the UI icon for this key
     */
    public static Icon getIcon(String key) {
    	return UIManager.getIcon(key);
    }

    // prevent instantiation
    private DefaultIcons() {  	
    }
}
