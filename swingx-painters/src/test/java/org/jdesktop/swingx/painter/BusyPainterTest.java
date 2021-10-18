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

import static org.hamcrest.MatcherAssert.assertThat;

import java.awt.Color;

import org.hamcrest.CoreMatchers;
import org.junit.Test;


/**
 * Test for BusyPainter.
 */
public class BusyPainterTest extends AbstractPainterTest {
    /**
     * {@inheritDoc}
     */
    @Override
    protected BusyPainter createTestingPainter() {
        return new BusyPainter();
    }
    
    @Test
    @Override
    public void testDefaults() {
        super.testDefaults();
        
        BusyPainter bp = (BusyPainter) p;
        assertThat(bp.getBaseColor(), CoreMatchers.is(Color.LIGHT_GRAY));
        assertThat(bp.getDirection(), CoreMatchers.is(BusyPainter.Direction.RIGHT));
        assertThat(bp.getFrame(), CoreMatchers.is(-1));
        assertThat(bp.getHighlightColor(), CoreMatchers.is(Color.BLACK));
        assertThat(bp.getPoints(), CoreMatchers.is(8));
        assertThat(bp.getPointShape(), CoreMatchers.is(BusyPainter.getScaledDefaultPoint(26)));
        assertThat(bp.getTrailLength(), CoreMatchers.is(4));
        assertThat(bp.getTrajectory(), CoreMatchers.is(BusyPainter.getScaledDefaultTrajectory(26)));
        assertThat(bp.isPaintCentered(), CoreMatchers.is(false));
    }
}
