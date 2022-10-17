/*
 * Copyright (c) 2005-2021 Radiance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of the copyright holder nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jdesktop.swingx.image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

// copied from org.pushingpixels.radiance.common.api.RadianceCommonCortex
public class RadianceCC {

    /**
     * Gets a scaled, high-DPI aware image of specified dimensions.
     * <p>
     * Use {@link #drawImageWithScale(Graphics, double, Image, int, int)} or
     * {@link #drawImageWithScale(Graphics, double, Image, int, int, int, int, int, int)}
     * to draw the image obtained with this method. Note that applying an extension of
     * {@link RadianceAbstractFilter} is a "safe" operation
     * as far as preserving the scale-aware configuration. If you are using a custom
     * {@link java.awt.image.BufferedImageOp} that is not a
     * {@link RadianceAbstractFilter}, the resulting image will be
     * a regular {@link BufferedImage} that will not be drawn correctly using one of the
     * <code>drawImageWithScale</code> methods above. In such a case, use
     * {@link #getScaleFactor(Component)} to divide the image width and height for the
     * purposes of drawing.
     *
     * @param width  Width of the target image
     * @param height Width of the target image
     * @return A scaled, high-DPI aware image of specified dimensions.
     */
    public static BufferedImage getBlankScaledImage(double scale, int width, int height) {
        if (scale > 1.0) {
            return JBHiDPIScaledImage.createScaled(scale, width, height, BufferedImage.TYPE_INT_ARGB);
        } else {
            GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice d = e.getDefaultScreenDevice();
            GraphicsConfiguration c = d.getDefaultConfiguration();
            return c.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        }
    }

    public static void drawImageWithScale(Graphics g, double scaleFactor, Image img, int x, int y) {
        if (img instanceof JBHiDPIScaledImage) {
            g.drawImage(img, x, y, (int) (img.getWidth(null) / scaleFactor),
                    (int) (img.getHeight(null) / scaleFactor), null);
        } else {
            g.drawImage(img, x, y, img.getWidth(null), img.getHeight(null), null);
        }
    }

    public static void drawImageWithScale(Graphics g, double scaleFactor, Image img, int x, int y,
            int width, int height, int offsetX, int offsetY) {
        if (img instanceof JBHiDPIScaledImage) {
            g.drawImage(img, x, y, x + width, y + height,
                    x + (int) (offsetX * scaleFactor), y + (int) (offsetY * scaleFactor),
                    x + (int) ((offsetX + width) * scaleFactor),
                    y + (int) ((offsetY + height) * scaleFactor), null);
        } else {
            g.drawImage(img, x, y, x + width, y + height,
                    x + offsetX, y + offsetY,
                    x + offsetX + width, y + offsetY + height, null);
        }
    }

    public static double getScaleFactor(Component component) {
        if ((component == null) || (component.getGraphicsConfiguration() == null)) {
            // TODO - revisit this
            return UIUtil.getScaleFactor();
        }
        AffineTransform transform = component.getGraphicsConfiguration().getDevice()
                .getDefaultConfiguration().getDefaultTransform();
        return Math.max(transform.getScaleX(), transform.getScaleY());
    }

}
