package org.jdesktop.swingx.painter;

import java.awt.Component;
import java.awt.Graphics2D;

class DummyLayoutPainter extends AbstractLayoutPainter<Component> {
    @Override
    protected void doPaint(Graphics2D g, Component object, int width, int height) {
    }
}
