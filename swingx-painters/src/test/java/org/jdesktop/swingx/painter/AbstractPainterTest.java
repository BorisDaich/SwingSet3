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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Button;
import java.awt.Graphics2D;
import java.awt.image.BufferedImageOp;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;

/**
 * Test for AbstractPainter
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class AbstractPainterTest {
	
    protected AbstractPainter p;
    protected Graphics2D g;

    @Before
    public void setUp() {
        p = spy(createTestingPainter());
        g = mock(Graphics2D.class);
    }

    protected AbstractPainter createTestingPainter() {
        return new DummyPainter();
    }
    
    @Test
    public void testDefaults() {
        assertThat(p.getFilters().length, CoreMatchers.is(0));
        assertThat(p.getInterpolation(), CoreMatchers.is(AbstractPainter.Interpolation.NearestNeighbor));
        assertThat(p.isAntialiasing(), CoreMatchers.is(true));
        assertThat(p.isCacheable(), CoreMatchers.is(false));
        assertThat(p.isCacheCleared(), CoreMatchers.is(true));
        assertThat(p.isDirty(), CoreMatchers.is(false));
        assertThat(p.isInPaintContext(), CoreMatchers.is(false));
        assertThat(p.isVisible(), CoreMatchers.is(true));
        assertThat(p.shouldUseCache(), CoreMatchers.is(p.isCacheable()));
    }
    
    /**
     * Ensure a {@link NullPointerException} is thrown with a {@code null} graphics object.
     */
    @Test(expected=NullPointerException.class)
    public void testPaintWithNullGraphics() {
        p.paint(null, null, 10, 10);
    }

    /**
     * {@link AbstractPainter} will pass {@code null} objects to
     * {@link AbstractPainter#doPaint(Graphics2D, Object, int, int) doPaint}.
     */
    @Test
    public void testPaintWithNullObject() {
        p.paint(g, null, 10, 10);
        
        if (p.isCacheable()) {
            verify(p).doPaint(any(Graphics2D.class), ArgumentMatchers.isNull(), eq(10), eq(10));
        } else {
            verify(p).doPaint(g, null, 10, 10);
        }
    }
    
    @SuppressWarnings("serial")
	class AnyComponent extends java.awt.Component {
    	
    }
    /**
     * {@link AbstractPainter} will pass any object to
     * {@link AbstractPainter#doPaint(Graphics2D, Object, int, int) doPaint}.
     * EUG: really any object? : or of type java.awt.Component
     */
    @Test
    public void testPaintWithAnyObject() {
    	AnyComponent any = new AnyComponent();
        p.paint(g, any, 10, 10);
        
        if (p.isCacheable()) {
            verify(p).doPaint(any(Graphics2D.class), eq(any), eq(10), eq(10));
        } else {
            verify(p).doPaint(g, any, 10, 10);
        }
        
        java.awt.Button aButton = new Button();

        p.clearCache();
        p.paint(g, aButton, 10, 10);
        
        if (p.isCacheable()) {
            verify(p).doPaint(any(Graphics2D.class), eq(aButton), eq(10), eq(10));
        } else {
            verify(p).doPaint(g, aButton, 10, 10);
        }
    }
    
    /**
     * Ensure that no painting occurs if width and/or height is <= 0.
     */
    @Test
    public void testNoPaintWithNonPositiveDimension() {
        p.paint(g, null, 0, 10);
        p.paint(g, null, 10, 0);
        p.paint(g, null, -1, 10);
        p.paint(g, null, 10, -1);
        p.paint(g, null, 0, 0);
        
        verify(p, never()).doPaint(any(Graphics2D.class), any(Object.class), anyInt(), anyInt());
    }
    
    /**
     * Ensure that visibility settings correctly alter painting behavior.
     */
    @Test
    public void testPaintAndVisibility() {
        p.setVisible(false);
        p.paint(g, null, 10, 10);
        verify(p, never()).doPaint(g, null, 10, 10);
        
        p.setVisible(true);
        testPaintWithNullObject();
    }
    
    /**
     * Ensure that paint orders calls correctly.
     */
    @Test
    public void testInOrderPaintCallsWithoutCaching() {
        when(p.shouldUseCache()).thenReturn(false);
        
        InOrder orderedCalls = inOrder(p);
        p.paint(g, null, 10, 10);
        
        orderedCalls.verify(p).configureGraphics(g);
        orderedCalls.verify(p, times(0)).validate(null);
        orderedCalls.verify(p).doPaint(g, null, 10, 10);
    }
    
    /**
     * Ensure that paint orders calls correctly.
     */
    @Test
    public void testInOrderPaintCallsWithCaching() {
        when(p.shouldUseCache()).thenReturn(true);
        
        InOrder orderedCalls = inOrder(p);
        p.paint(g, null, 10, 10);
        
        orderedCalls.verify(p).configureGraphics(g);
        orderedCalls.verify(p).validate(null);
        //when caching we get a different graphics object
        verify(p).doPaint(any(Graphics2D.class), ArgumentMatchers.isNull(), eq(10), eq(10));
    }

    /**
     * Issue #??-swingx: clearCache has no detectable effect. Test was poorly designed. It has had
     * an effect for a long time, but the member is not bound, so the test was failing erroneously.
     */
    @Test
    public void testClearCacheDetectable() {
        p.setCacheable(true);
        p.paint(g, null, 10, 10);
        
        // sanity
        //when caching we get a different graphics object
        verify(p).doPaint(any(Graphics2D.class), ArgumentMatchers.isNull(), eq(10), eq(10));
        assertThat("clean after paint", false, CoreMatchers.is(p.isDirty()));
        assertThat("cacheable is enabled", true, CoreMatchers.is(p.isCacheable()));
        assertThat("has a cached image", false, CoreMatchers.is(p.isCacheCleared()));
        
        p.clearCache();
        
        assertThat("has a cached image", true, CoreMatchers.is(p.isCacheCleared()));
    }
    
    /**
     * Ensures that setting cacheable makes shouldUseCache return true.
     */
    @Test
    public void testSetCacheableEnablesCache() {
        p.setCacheable(true);
        
        assertThat(p.shouldUseCache(), CoreMatchers.is(true));
    }
    
    /**
     * Ensures that setting filters makes shouldUseCache return true.
     */
    @Test
    public void testFiltersEnableCache() {
        p.setFilters(mock(BufferedImageOp.class));
        
        assertThat(p.shouldUseCache(), CoreMatchers.is(true));
    }

    /**
     * Ensure that shouldUseCache forces the use of the cache.
     */
    @Test
    public void testShouldUseCacheRepaintsWithCachedCopy() {
        when(p.shouldUseCache()).thenReturn(true);
        
        p.paint(g, null, 10, 10);
        
        //when caching we get a different graphics object
        verify(p, times(1)).doPaint(any(Graphics2D.class), ArgumentMatchers.isNull(), eq(10), eq(10));
        
        p.paint(g, null, 10, 10);
        p.paint(g, null, 10, 10);
        p.paint(g, null, 10, 10);
        p.paint(g, null, 10, 10);
        
        //we do not invoke doPaint a subsequent calls
        verify(p, times(1)).doPaint(any(Graphics2D.class), ArgumentMatchers.isNull(), eq(10), eq(10));
    }
}
