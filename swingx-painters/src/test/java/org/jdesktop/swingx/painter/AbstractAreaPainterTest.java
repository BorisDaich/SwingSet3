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
package org.jdesktop.swingx.painter;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.Color;
import java.awt.Paint;

import org.hamcrest.CoreMatchers;
import org.jdesktop.swingx.painter.effects.AreaEffect;
import org.junit.Test;

/**
 * Test for AbstractAreaPainter.
 */
@SuppressWarnings({"rawtypes"})
public class AbstractAreaPainterTest extends AbstractLayoutPainterTest {
	
    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractAreaPainter createTestingPainter() {
        return new DummyAreaPainter();
    }
    
    @Test
    @Override
    public void testDefaults() {
        super.testDefaults();
        
        AbstractAreaPainter aap = (AbstractAreaPainter) p;
        assertThat(aap.getAreaEffects(), CoreMatchers.is(new AreaEffect[0]));
        assertThat(aap.getBorderPaint(), CoreMatchers.is(nullValue()));
        assertThat(aap.getBorderWidth(), CoreMatchers.is(0f));
        assertThat(aap.getFillPaint(), CoreMatchers.<Paint>is(Color.RED));
        assertThat(aap.getStyle(), CoreMatchers.is(AbstractAreaPainter.Style.BOTH));
    }
}
