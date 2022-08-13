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
import java.awt.GradientPaint;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.MattePainter;

/**
 * Addon for <code>JXTitledPanel</code>.
 *
 */
public class TitledPanelAddon extends AbstractComponentAddon {

  public TitledPanelAddon() {
    super("JXTitledPanel");
  }

  @Override
  protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    super.addBasicDefaults(addon, defaults);
    
    defaults.add(JXTitledPanel.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicTitledPanelUI");
    defaults.add("JXTitledPanel.titleFont", 
    		UIManagerExt.getSafeFont("Button.font", new FontUIResource("Dialog", Font.PLAIN, 12)));
    defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
    GradientPaint lightGrayToGray = new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, 1, Color.GRAY);
    defaults.add("JXTitledPanel.titlePainter", 
    		new PainterUIResource<JXTitledPanel>(new MattePainter(lightGrayToGray, true)
    				));
    defaults.add("JXTitledPanel.captionInsets", new InsetsUIResource(4, 12, 4, 12));
    defaults.add("JXTitledPanel.rightDecorationInsets", new InsetsUIResource(1,1,1,1));
    defaults.add("JXTitledPanel.leftDecorationInsets", new InsetsUIResource(1,1,1,1));
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    addMetalDefaults(addon, defaults);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    super.addMetalDefaults(addon, defaults);

    if (isPlastic()) { // JGoodies Plastic LaFs
      defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
      // #3179F2 : approx Royal Blue to #C6D3F7 : approx Hawkes Blue
      GradientPaint royalToHawkesBlue = new GradientPaint(0, 0, new Color(49, 121, 242), 0, 1, new Color(198, 211, 247));
      defaults.add("JXTitledPanel.titlePainter", 
    		  new PainterUIResource<JXTitledPanel>(new MattePainter(royalToHawkesBlue, true)
    				  ));
    } else {
      defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
      // Primary3/LEVANDER_BLUE to Primary1/DARK_BLUE_GRAY
      GradientPaint levanderToDarkBlueGray = new GradientPaint(0, 0, MetalLookAndFeel.getPrimaryControl(), 0, 1, MetalLookAndFeel.getPrimaryControlDarkShadow());
      defaults.add("JXTitledPanel.titlePainter", 
    		  new PainterUIResource<JXTitledPanel>(new MattePainter(levanderToDarkBlueGray, true)
    				  ));
    }
  }

  @Override
  protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    super.addWindowsDefaults(addon, defaults);
  
    // JW: hot fix for #291-swingx
    // was tracked down by Neil Weber - the requested colors are not available in 
    // all LFs, so changed to fall-back to something real
    // don't understand why this has blown when trying to toggle to Metal...
    // definitely needs deeper digging 
    // kgs: moved to using getSafeXXX from UIManagerExt
    defaults.add("JXTitledPanel.titleForeground", 
    		UIManagerExt.getSafeColor("InternalFrame.activeTitleForeground", new ColorUIResource(Color.WHITE)));

    // fallBack colors:
    Color royalBlue = new ColorUIResource(49, 121, 242);   // #3179F2 : approx Royal Blue
    Color hawkesBlue = new ColorUIResource(198, 211, 247); // #C6D3F7 : approx Hawkes Blue
    GradientPaint royalToHawkesBlue = new GradientPaint(
    		0, 0, UIManagerExt.getSafeColor("InternalFrame.inactiveTitleBackground", royalBlue), 
    		0, 1, UIManagerExt.getSafeColor("InternalFrame.activeTitleBackground", hawkesBlue));
    
    defaults.add("JXTitledPanel.titlePainter", 
    		new PainterUIResource<JXTitledPanel>(new MattePainter(royalToHawkesBlue, true)
    				));
  
  }
}
