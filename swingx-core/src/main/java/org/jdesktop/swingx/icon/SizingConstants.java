package org.jdesktop.swingx.icon;

import javax.swing.SwingConstants;

/*

https://iconhandbook.co.uk/reference/chart/android/

- Pixel Densities : mdpi (Baseline): 160 dpi ...
- Launcher icons : 48 × 48 (mdpi) aka L
- Action bar, Dialog & Tab icons : 24 × 24 area or 32 × 32 (mdpi) aka M or N
- Small Contextual Icons : 16 × 16 (mdpi) aka S/Small
 */
public interface SizingConstants extends SwingConstants {

    public static final int XS  = 10;
    public static final int  S  = 16; // Small size used with key javax.swing.Action.SMALL_ICON
    public static final int  M  = 24; // Action bar, Dialog & Tab icons
    public static final int  N  = 32; // Normal size used with key javax.swing.Action.LARGE_ICON_KEY
    public static final int  L  = 48; // Launcher icons
    public static final int XL  = 64;
    public static final int XXL =128;

    /**
     * This is typically used with menus such as <code>JMenuItem</code>.
     */
    public static final int  SMALL_ICON  = S;
    /**
     * This is typically used by buttons, such as <code>JButton</code> and <code>JToggleButton</code>.
     */	
    public static final int  BUTTON_ICON  = N;
    
    public static final int  ACTION_ICON  = M;
    public static final int  LAUNCHER_ICON  = L;

}
