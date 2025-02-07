package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.swingx.prompt.BuddySupport.Position;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;

/**
 * {@link JFormattedTextField}, with integrated support for prompts and buddies.
 * 
 * @see PromptSupport
 * @see BuddySupport
 * @author Peter Weishapl petw@gmx.net
 * @author EUG https://github.com/homebeaver
 */
public class JXFormattedTextField extends JFormattedTextField {
	
	private static final long serialVersionUID = -6438553038504842348L;

	public JXFormattedTextField(Object value) {
		super(value);
	}

	public JXFormattedTextField(java.text.Format format) {
		super(format);
	}

    public JXFormattedTextField(AbstractFormatter formatter) {
    	super(formatter);
    }

	public JXFormattedTextField() {
		this((String)null);
	}

	public JXFormattedTextField(String promptText) {
		this(promptText, null);
	}

	public JXFormattedTextField(String promptText, Color promptForeground) {
		this(promptText, promptForeground, null);
	}
	
	/**
	 * ctor
	 * @param promptText String
	 * @param promptForeground Color
	 * @param promptBackground Color
	 */
	public JXFormattedTextField(String promptText, Color promptForeground, Color promptBackground) {
		super();
		PromptSupport.init(promptText, promptForeground, promptBackground, this);
	}

	// is implemented in JTextComponent, which is super.super.super
	// JFormattedTextField extends JTextField extends JTextComponent extends JComponent
	/**
	 *  {@inheritDoc} <p>
	 *  Overrides the JTextComponent method to paint an appropriate 
	 *  visual representation in Nimbus. 
	 *  In Nimbus LaF not editable (aka read only) fields have background 
	 *  same to background of editable fields.
	 */
	@Override
	public void setEditable(boolean b) {
		super.setEditable(b);
		if(AbstractComponentAddon.isNimbus()) {
			// RO => disable ; RW => unverändert
			if (!b) setEnabled(b);
		}
	}
    /**
     * {@inheritDoc} <p>
     * Overrides the JComponent method to restore "enabled" property in nimbus
     * @see #setEditable(boolean)
     */
	@Override
	public void setEnabled(boolean b) {
		if(b) {
			super.setEnabled(AbstractComponentAddon.isNimbus() ? isEditable() : b);
		} else {
			super.setEnabled(b);
		}		
	}
    /**
     * {@inheritDoc} <p>
     * Overrides the awt.Component method to restore "enabled" property in nimbus
     * @see #setEditable(boolean)
     */
	@Override
    public boolean isEnabled() {
    	boolean enabled = super.isEnabled();
        return AbstractComponentAddon.isNimbus() ? super.isEditable() : enabled;
    }
	
	/**
	 * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
	 * @return FocusBehavior
	 */
	public FocusBehavior getFocusBehavior() {
		return PromptSupport.getFocusBehavior(this);
	}

	/**
	 * @see PromptSupport#getPrompt(javax.swing.text.JTextComponent)
	 * @return Prompt
	 */
	public String getPrompt() {
		return PromptSupport.getPrompt(this);
	}

	/**
	 * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
	 * @return Color
	 */
	public Color getPromptForeground() {
		return PromptSupport.getForeground(this);
	}
	
	/**
	 * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
	 * @return Color
	 */
	public Color getPromptBackground() {
		return PromptSupport.getBackground(this);
	}

	/**
	 * @see PromptSupport#getFontStyle(javax.swing.text.JTextComponent)
	 * @return FontStyle
	 */
	public Integer getPromptFontStyle() {
		return PromptSupport.getFontStyle(this);
	}

	/**
	 * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
	 * @param focusBehavior FocusBehavior
	 */
	public void setFocusBehavior(FocusBehavior focusBehavior) {
		PromptSupport.setFocusBehavior(focusBehavior, this);
	}

	/**
	 * @see PromptSupport#setPrompt(String, javax.swing.text.JTextComponent)
	 * @param labelText String
	 */
	public void setPrompt(String labelText) {
		PromptSupport.setPrompt(labelText, this);
	}

	/**
	 * @see PromptSupport#setForeground(Color, javax.swing.text.JTextComponent)
	 * 
	 * @param promptTextColor Color
	 */
	public void setPromptForeground(Color promptTextColor) {
		PromptSupport.setForeground(promptTextColor, this);
	}
	
	/**
	 * @see PromptSupport#setBackground(Color, javax.swing.text.JTextComponent)
	 * 
	 * @param promptTextColor Color
	 */
	public void setPromptBackground(Color promptTextColor) {
		PromptSupport.setBackground(promptTextColor, this);
	}

	/**
	 * @see PromptSupport#setFontStyle(Integer, javax.swing.text.JTextComponent)
	 * 
	 * @param fontStyle Integer
	 */
	public void setPromptFontStyle(Integer fontStyle) {
		PromptSupport.setFontStyle(fontStyle, this);
	}
	
	/**
	 * @see BuddySupport#setOuterMargin(JTextField, Insets)
	 * @param margin Insets
	 */
	public void setOuterMargin(Insets margin) {
		BuddySupport.setOuterMargin(this, margin);
	}

	/**
	 * @see BuddySupport#getOuterMargin(JTextField)
	 * @return Insets
	 */
	public Insets getOuterMargin() {
		return BuddySupport.getOuterMargin(this);
	}

	/**
	 * @see BuddySupport#add(Component, Position, JTextField)
	 * @param buddy Component
	 * @param pos Position
	 */
	public void addBuddy(Component buddy, Position pos) {
		BuddySupport.add(buddy, pos, this);
	}

	/**
	 * @see BuddySupport#addGap(int, Position, JTextField)
	 * @param width int
	 * @param pos Position
	 */
	public void addGap(int width, Position pos) {
		BuddySupport.addGap(width, pos, this);
	}

	/**
	 * @see BuddySupport#getBuddies(Position, JTextField)
	 * @param pos Position
	 * @return list of Components
	 */
	public List<Component> getBuddies(Position pos) {
		return BuddySupport.getBuddies(pos, this);
	}

	/**
	 * @see BuddySupport#removeAll(JTextField)
	 */
	public void removeAllBuddies() {
		BuddySupport.removeAll(this);
	}
}
