/*
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.plaf.synth;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;

/**
 * Replacement of sun.swing.plaf.SynthUI.<p>
 * 
 * Note: this is a temporary emergency measure to make SwingX web-deployable. It is
 * used internally only. Expect problems in future, as custom styles might not be 
 * found: SynthStyleFactory checks against type of sun SynthUI. 
 * 
 * @author Jeanette Winzenburg
 */
/*
EUG : @since 1.7 there is public interface javax.swing.plaf.synth.SynthUI extends SynthConstants
	use it
 */
@Deprecated
public interface SynthUI {
    
    /**
     * Returns the Context for the specified component.
     *
     * @param c Component requesting SynthContext.
     * @return SynthContext describing component.
     */
    public SynthContext getContext(JComponent c);

    /**
     * Paints the border.
     *
     * @param context a component context
     * @param g {@code Graphics} to paint on
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param w width of the border
     * @param h height of the border
     */
    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h);
//	public void paintBorder(SynthContext context, Graphics g, int x, int y, int w, int h);

}
