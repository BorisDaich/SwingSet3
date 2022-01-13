package org.jdesktop.swingx.demos.xbutton;

import javax.swing.SwingConstants;

/*

https://iconhandbook.co.uk/reference/chart/android/

- Pixel Densities : mdpi (Baseline): 160 dpi ...
- Launcher icons : 48 × 48 (mdpi) aka L
- Action bar, Dialog & Tab icons : 24 × 24 area in 32 × 32 (mdpi) aka M in N
- Small Contextual Icons : 16 × 16 (mdpi) aka S/Small
 */
public interface SizingConstants extends SwingConstants {

    public static final int XS  = 10;
    public static final int  S  = 16;
    public static final int  M  = 24; // Action bar, Dialog & Tab icons
    public static final int  N  = 32;
    public static final int  L  = 48; // Launcher icons
    public static final int XL  = 64;
    public static final int XXL =128;

    public static final int  LAUNCHER_ICON  = L;
    public static final int  ACTION_ICON  = M;

}
