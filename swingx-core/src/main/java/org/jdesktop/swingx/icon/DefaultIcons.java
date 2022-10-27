package org.jdesktop.swingx.icon;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * collects property names for default UI icons defined in javax swing
 * 
 * @author EUG https://github.com/homebeaver
 */
// see https://github.com/homebeaver/SwingSet/issues/23
public class DefaultIcons {

	private static final String HTML = "html";
	/** property name for icons/image-delayed.png */
    public static final String PENDING = HTML+".pendingImage";
	/** property name for icons/image-failed.png */
    // see javax.swing.text.html.ImageView public Icon getNoImageIcon() :
    // UIManager.getLookAndFeelDefaults().get(MISSING) / UIManager.getIcon(MISSING)
    public static final String MISSING = HTML+".missingImage";
    
	/*
	 * icons defined in class javax.swing.plaf.basic.BasicOptionPaneUI
	 * 
	 * Metal:
	 * see protected Icon BasicOptionPaneUI.getIconForType(int messageType)
	 * the images resources are in javax.swing.plaf.metal.icons
	 * resp. in javax.swing.plaf.metal.icons.ocean for ocean theme
	 * make the icons in BasicLookAndFeel#initComponentDefaults 
        // *** FileChooser / FileView value objects

        Object newFolderIcon = SwingUtilities2.makeIcon(getClass(),
                                                        BasicLookAndFeel.class,
                                                        "icons/NewFolder.gif");
...
            "OptionPane.errorIcon", SwingUtilities2.makeIcon(getClass(),
                                                             BasicLookAndFeel.class,
                                                             "icons/Error.gif"),

	 * 
	 * Nimbus: all icons are painted by method protected void doPaint
	 * see class javax.swing.plaf.nimbus.OptionPanePainter
	 * f.i. method private void painterrorIconEnabled(Graphics2D g)
	 */
	private static final String OPTIONPANE = "OptionPane";
	/** property name for messageType 0 , Error.gif */
    public static final String ERROR = OPTIONPANE+".errorIcon";
	/** property name for messageType 1 , Inform.gif */
    public static final String INFORMATION = OPTIONPANE+".informationIcon";
	/** property name for messageType 2 , Warn.gif */
    public static final String WARNING = OPTIONPANE+".warningIcon";
	/** property name for messageType 3 , Question.gif */
    public static final String QUESTION = OPTIONPANE+".questionIcon";
	
	/*
	 * icons defined in class javax.swing.plaf.basic.BasicFileChooserUI 
	 * For nimbus the icons are coded in javax.swing.plaf.nimbus.FileChooserPainter,
	 * f.i. paintfileIconEnabled(Graphics2D)
	 */
	private static final String FILEVIEW = "FileView";
	/** property name for Directory.gif */
    public static final String DIRECTORY = FILEVIEW+".directoryIcon";
	/** property name for File.gif */
    public static final String FILE = FILEVIEW+".fileIcon";
	/** property name for Computer.gif */
    public static final String COMPUTER = FILEVIEW+".computerIcon";
	/** property name for HardDrive.gif */
    public static final String HARDDRIVE = FILEVIEW+".hardDriveIcon";
	/** property name for FloppyDrive.gif */
    public static final String FLOPPYDRIVE = FILEVIEW+".floppyDriveIcon";
    
    private static final String FILECHOOSER = "FileChooser";
	/** property name for NewFolder.gif */
    public static final String NEWFOLDER = FILECHOOSER+".newFolderIcon";
	/** property name for UpFolder.gif */
    public static final String UPFOLDER = FILECHOOSER+".upFolderIcon";
	/** property name for HomeFolder.gif , null in LAF Windows */
    public static final String HOMEFOLDER = FILECHOOSER+".homeFolderIcon";
	/** property name for DetailsView.gif */
    public static final String DETAILVIEW = FILECHOOSER+".detailsViewIcon";
	/** property name for ListView.gif */
    public static final String LISTVIEW = FILECHOOSER+".listViewIcon";
//    public static final String VIEWMENU = FILECHOOSER+".viewMenuIcon"; // null?
    
    private static final String TABLE = "Table";
	/** property name for icon respresenting ascending sort, class SortArrowIcon */
    public static final String ASCENDING = TABLE+".ascendingSortIcon";
	/** property name for icon respresenting descending sort, class SortArrowIcon */
    public static final String DESCENDING = TABLE+".descendingSortIcon";
    
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
    // nicht in BasicTreeUI, aber in BasicLookAndFeel#initComponentDefaults :
	/** property name for TreeOpen.gif */
    public static final String OPEN = TREE+".openIcon";
	/** property name for TreeClosed.gif */
    public static final String CLOSED = TREE+".closedIcon";
	/** property name for TreeLeaf.gif */
    public static final String LEAF = TREE+".leafIcon";
    
//    private static final String INTERNALFRAME = "InternalFrame";
    /* Default frame icons are undefined for Basic: 
    "InternalFrame.maximizeIcon",
       (LazyValue) t -> BasicIconFactory.createEmptyFrameIcon(),
    "InternalFrame.minimizeIcon",
       (LazyValue) t -> BasicIconFactory.createEmptyFrameIcon(),
    "InternalFrame.iconifyIcon",
       (LazyValue) t -> BasicIconFactory.createEmptyFrameIcon(),
    "InternalFrame.closeIcon",
       (LazyValue) t -> BasicIconFactory.createEmptyFrameIcon(),
      ---> in Ocean
*/
//    public static final String MAXIMIZE = INTERNALFRAME+".maximizeIcon";
//    public static final String MINIMIZE = INTERNALFRAME+".minimizeIcon";
	/** property name for JavaCup16.gif - not in Nimbus*/
    public static final String JAVACUP16 = "InternalFrame.icon";
    
    private static final String SLIDER = "Slider";
	/** property name for HorizontalSliderThumbIcon
	 * - in Metal : MetalIconFactory.getHorizontalSliderThumbIcon()
	 * implemented in private static class MetalIconFactory.HorizontalSliderThumbIcon
	 */
    public static final String HORIZONTALTHUMB = SLIDER+".horizontalThumbIcon";
    public static final String VERTICALTHUMB = SLIDER+".verticalThumbIcon";
    
    /**
     * Returns an Icon from the defaults. 
     * If the value for key is not an Icon, null is returned.
     * <p>
     * The result depends on LAF, example:
     * - in WindowsLookAndFeel <code>getIcon(JAVACUP16)</code> returns icon with JavaCup16.gif
     * - in MetalLookAndFeel <code>getIcon(JAVACUP16</code> returns icon computed by InternalFrameDefaultMenuIcon class
     * - in in Nimbus , Motif <code>getIcon(JAVACUP16)</code> returns null
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
