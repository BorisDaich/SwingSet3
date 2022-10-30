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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.LookAndFeel;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
import org.jdesktop.swingx.plaf.windows.WindowsTipOfTheDayUI;

/**
 * Addon for <code>JXTipOfTheDay</code>.<br>
 * 
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public class TipOfTheDayAddon extends AbstractComponentAddon {

  public TipOfTheDayAddon() {
    super("JXTipOfTheDay");
  }

  // either fontkey/"TextPane.font" or "Label.font" or defaultFont Font.DIALOG
  private FontUIResource getFontUIResource(String fontkey) {
      Font defaultFont = new Font(Font.DIALOG, Font.PLAIN, 12);
      Font font = UIManagerExt.getSafeFont(fontkey, defaultFont);
      font = font.deriveFont(font.getStyle(), 13f);
      return new FontUIResource(font);
  }
  
  @Override
  protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
      super.addBasicDefaults(addon, defaults);
      
      defaults.add(JXTipOfTheDay.uiClassID, BasicTipOfTheDayUI.class.getName());
      defaults.add("TipOfTheDay.font", getFontUIResource("TextPane.font"));
      defaults.add("TipOfTheDay.tipFont", getFontUIResource("Label.font"));
      // TODO getSafeColor, aber was ist Colorkey?
      defaults.add("TipOfTheDay.background", new ColorUIResource(Color.WHITE)); //  LIGHT_GRAY statt .WHITE
      
      // replaced with Radience Icon
      defaults.add("TipOfTheDay.icon",
              LookAndFeel.makeIcon(BasicTipOfTheDayUI.class, "resources/TipOfTheDay24.gif"));
      
//      Color bittersweet = new Color(0xFF6666);
      // TODO getSafeColor, aber was ist der Colorkey?
      Color grey = new Color(117, 117, 117); // #757575 Grey <> #808080 Color.GRAY
      defaults.add("TipOfTheDay.border", new BorderUIResource(BorderFactory.createLineBorder(grey)));
      
      UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.TipOfTheDay");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    super.addWindowsDefaults(addon, defaults);

    defaults.add(JXTipOfTheDay.uiClassID, WindowsTipOfTheDayUI.class.getName());
    defaults.add("TipOfTheDay.background", new ColorUIResource(Color.GRAY)); // #808080
    defaults.add("TipOfTheDay.font", getFontUIResource("Label.font"));
    defaults.add("TipOfTheDay.icon",
            LookAndFeel.makeIcon(WindowsTipOfTheDayUI.class, "resources/tipoftheday.png"));
    defaults.add("TipOfTheDay.border", new BorderUIResource(new WindowsTipOfTheDayUI.TipAreaBorder()));

    UIManagerExt.addResourceBundle(
        "org.jdesktop.swingx.plaf.windows.resources.TipOfTheDay");
  }

}
