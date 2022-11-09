package org.jdesktop.swingx.icon;

import javax.swing.Icon;

/**
 * Extension of the core {@link Icon} interface that adds more capabilities
 * like rotation and point/axis reflection
 * <p>
 * See <a href="https://jdesktop.wordpress.com/2022/11/09/jxicon/">JXIcon documentation</a>.
 * 
 * <p>
 * SizingConstants extends SwingConstants with sizing from XS to XXL
 * 
 * @author EUG https://github.com/homebeaver
 */
public interface JXIcon extends Icon, SizingConstants {

	/**
	 * A hint for point/axis reflection (mirroring) the icon when painting.
	 * <p>
	 * <code>setReflection(true, true)</code> means point reflection
	 * 
	 * @param horizontal will mirror the icon horizontal (X axis)
	 * @param vertical will mirror the icon vertical (Y axis)
	 * 
	 */
	// a default is necessary for icons generated before this feature was active
	default void setReflection(boolean horizontal, boolean vertical) {}
	default void setReflection(boolean pointReflection) {
		setReflection(pointReflection, pointReflection);
	}
	default boolean isReflection() {
		return false;
	}
	
	/**
	 * A hint to rotate the icon when painting
	 * 
	 * @param theta the angle of rotation in radians, zero means no rotation
	 */
	// a default is necessary for icons generated before this feature was active
	default void setRotation(double theta) {}
	default double getRotation() {
		return 0d; // no rotation
	}
	/**
	 * A hint to rotate the icon to a direction.
	 * <p> The icon is aligned to {@code NORTH} per default, 
	 * so rotate direction {@code NORTH_EAST} means rotating 45° right
	 * and {@code WEST} means rotating 90° left or 270° right.
	 * 
	 * @param direction Compass-direction, use {@code SwingConstants} {@code NORTH}, {@code NORTH_EAST} etc
	 * 
	 * @see #setRotation(double)
	 */
	default void setRotation(int direction) {
        if(direction>=NORTH && direction<=NORTH_WEST) {
        	this.setRotation(Math.toRadians(45d*(direction-1)));
        } else {
            setRotation(0d); // no rotation for invalid directions
        }
	}

}
