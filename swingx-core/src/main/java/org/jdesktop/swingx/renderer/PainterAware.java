/*
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.renderer;

import javax.swing.Painter;

/*
 * Temporary hook to allow painters in rendering. <p>
 * 
 * NOTE: this will be removed as soon as the painter_work enters main.
 * 
 * @author Jeanette Winzenburg
 */
// was meint kleopatra mit "enters main"?
// offenbar ist PainterAware nicht in java 1.7 angekommen, sondern nur die Klasse javax.swing.Painter<T>
/*

@since 1.7 existiert public interface javax.swing.Painter<T>
mit Methode public void paint(Graphics2D g, T object, int width, int height);
Klassen, die PainterAware implementieren:
- JRendererCheckBox extends JCheckBox
- JRendererLabel extends JLabel           ==> es gibt doch JXLabel mit JXLabel.Renderer
- JXRendererHyperlink extends JXHyperlink
- WrappingIconPanel extends JXPanel

 */
//@Deprecated
public interface PainterAware<T> extends Painter<T> {
	
	/**
	 * @param painter Painter
	 */
    void setPainter(Painter<?> painter);

    /**
     * 
     * @return Painter
     */
    Painter<?> getPainter();
}
