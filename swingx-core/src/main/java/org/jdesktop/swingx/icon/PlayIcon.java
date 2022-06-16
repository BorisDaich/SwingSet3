package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Rendering a Play icon (used for Players). Can be filled with any color, default is component foreground (black).
 *
 * @author EUG https://github.com/homebeaver/
 */
public class PlayIcon extends ArrowIcon {

    public PlayIcon() {
    	super();
    	setFilled(true);
    }

    public PlayIcon(int size, Color color) {
    	super(size, color);
    	setFilled(true);
    }

    public PlayIcon(int size) {
    	this(size, null);
    }

    protected PlayIcon(int width, int height) {
    	super(width, height);
    	setFilled(true);
    }

    protected PlayIcon(Dimension size) {
    	this(Double.valueOf(size.getWidth()).intValue(), Double.valueOf(size.getHeight()).intValue());
    	setFilled(true);
    }

}
