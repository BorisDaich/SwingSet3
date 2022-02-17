package org.jdesktop.swingx.prompt;

import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextUI;

import org.jdesktop.swingx.plaf.TextUIWrapper;

/** TODO doc */
public class BuddySupport {
	/** TODO doc */
	public enum Position {
		/** left */
		LEFT, 
		/** right */
		RIGHT
	};
	/** TODO doc */
	public static final String OUTER_MARGIN = "outerMargin";

	/**
	 * TODO doc
	 * @param c Component
	 * @param textField JTextField
	 */
	public static void addLeft(Component c, JTextField textField) {
		add(c, Position.LEFT, textField);
	}

	/**
	 * TODO doc
	 * @param c Component
	 * @param textField JTextField
	 */
	public static void addRight(Component c, JTextField textField) {
		add(c, Position.RIGHT, textField);
	}

	/**
	 * TODO doc
	 * @param c Component
	 * @param pos Position
	 * @param textField JTextField
	 */
	public static void add(Component c, Position pos, JTextField textField) {
		TextUIWrapper.getDefaultWrapper().install(textField, true);

		List<Component> leftBuddies = buddies(Position.LEFT, textField);
		List<Component> rightBuddies = buddies(Position.RIGHT, textField);

		// ensure buddies are added
		setLeft(textField, leftBuddies);
		setRight(textField, rightBuddies);

		// check if component is already here
		if (isBuddy(c, textField)) {
			throw new IllegalStateException("Component already added.");
		}

		if (Position.LEFT == pos) {
			leftBuddies.add(c);
		} else {
			rightBuddies.add(0, c);
		}

		addToComponentHierarchy(c, pos, textField);
	}
	
	/**
	 * TODO doc
	 * @param width the gap width
	 * @param pos Position
	 * @param textField JTextField
	 */
	public static void addGap(int width, Position pos, JTextField textField) {
		add(createGap(width), pos, textField);
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 * @param rightBuddies list of components
	 */
	public static void setRight(JTextField textField, List<Component> rightBuddies) {
		set(rightBuddies, Position.RIGHT, textField);
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 * @param leftBuddies list of components
	 */
	public static void setLeft(JTextField textField, List<Component> leftBuddies) {
		set(leftBuddies, Position.LEFT, textField);
	}

	/**
	 * TODO doc
	 * @param buddies list
	 * @param pos Position
	 * @param textField JTextField
	 */
	public static void set(List<Component> buddies, Position pos, JTextField textField) {
		textField.putClientProperty(pos, buddies);
	}

	/**
	 * TODO doc
	 * @param c Component
	 * @param pos Position
	 * @param textField JTextField
	 */
	private static void addToComponentHierarchy(Component c, Position pos, JTextField textField) {
		textField.add(c, pos.toString());
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 * @return list of components
	 */
	public static List<Component> getLeft(JTextField textField) {
		return getBuddies(Position.LEFT, textField);
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 * @return list of components
	 */
	public static List<Component> getRight(JTextField textField) {
		return getBuddies(Position.RIGHT, textField);
	}

	/**
	 * TODO doc
	 * @param pos Position
	 * @param textField JTextField
	 * @return list of components
	 */
	public static List<Component> getBuddies(Position pos, JTextField textField) {
		return Collections.unmodifiableList(buddies(pos, textField));
	}

	@SuppressWarnings("unchecked")
	private static List<Component> buddies(Position pos, JTextField textField) {
		List<Component> buddies = (List<Component>) textField.getClientProperty(pos);

		if (buddies != null) {
			return buddies;
		}
		return new ArrayList<Component>();
	}

	/**
	 * check if components are boddies
	 * @param c Component
	 * @param textField JTextField
	 * @return components are boddies
	 */
	public static boolean isBuddy(Component c, JTextField textField) {
		return buddies(Position.LEFT, textField).contains(c) || buddies(Position.RIGHT, textField).contains(c);
	}

	/**
	 * Because {@link BasicTextUI} removes all components when uninstalled and
	 * therefore all buddies are removed when the LnF changes.
	 * 
	 * @param c JComponent
	 * @param textField JTextField
	 */
	public static void remove(JComponent c, JTextField textField) {
		buddies(Position.LEFT, textField).remove(c);
		buddies(Position.RIGHT, textField).remove(c);

		textField.remove(c);
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 */
	public static void removeAll(JTextField textField) {
		List<Component> left = buddies(Position.LEFT, textField);
		for (Component c : left) {
			textField.remove(c);
		}
		left.clear();
		List<Component> right = buddies(Position.RIGHT, textField);
		for (Component c : right) {
			textField.remove(c);
		}
		right.clear();
		
	}

	/**
	 * TODO doc
	 * @param buddyField JTextField
	 * @param margin Insets
	 */
	public static void setOuterMargin(JTextField buddyField, Insets margin) {
		buddyField.putClientProperty(OUTER_MARGIN, margin);
	}

	/**
	 * TODO doc
	 * @param buddyField JTextField
	 * @return Insets
	 */
	public static Insets getOuterMargin(JTextField buddyField) {
		return (Insets) buddyField.getClientProperty(OUTER_MARGIN);
	}

	/**
	 * TODO doc
	 * @param textField JTextField
	 */
	public static void ensureBuddiesAreInComponentHierarchy(JTextField textField) {
		for (Component c : BuddySupport.getLeft(textField)) {
			addToComponentHierarchy(c, Position.LEFT, textField);
		}
		for (Component c : BuddySupport.getRight(textField)) {
			addToComponentHierarchy(c, Position.RIGHT, textField);
		}
	}

	/**
	 * Create a gap to insert between to buddies.
	 * 
	 * @param width the gap width
	 * @return Component
	 */
	public static Component createGap(int width) {
		return Box.createHorizontalStrut(width);
	}
}
