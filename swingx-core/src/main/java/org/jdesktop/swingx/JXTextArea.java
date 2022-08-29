package org.jdesktop.swingx;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.text.Document;

import org.jdesktop.beans.JavaBean;
import org.jdesktop.swingx.prompt.BuddySupport;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;

/**
 * {@link JTextArea}, with integrated support for prompts.
 * 
 * @see PromptSupport
 * @see BuddySupport
 * @author Peter Weishapl petw@gmx.net
 * 
 */
@JavaBean
public class JXTextArea extends JTextArea {

	private static final long serialVersionUID = -4795426700642723446L;

	public JXTextArea(String text, int rows, int columns) {
        this(null, text, rows, columns);
    }

    public JXTextArea(Document doc) {
        this(doc, null, 0, 0);
    }
    
    public JXTextArea(int rows, int columns) {
        this(null, null, rows, columns);
    }

    public JXTextArea(Document doc, String text, int rows, int columns) {
    	super(doc, text, rows, columns);
    }

	public JXTextArea() {
		this((String)null);
	}

	public JXTextArea(String promptText) {
		this(promptText, null);
	}

	public JXTextArea(String promptText, Color promptForeground) {
		this(promptText, promptForeground, null);
	}

	public JXTextArea(String promptText, Color promptForeground, Color promptBackground) {
		super();
		PromptSupport.init(promptText, promptForeground, promptBackground, this);
	}

	/**
	 * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
	 */
	public FocusBehavior getFocusBehavior() {
		return PromptSupport.getFocusBehavior(this);
	}

	/**
	 * @see PromptSupport#getPrompt(javax.swing.text.JTextComponent)
	 */
	public String getPrompt() {
		return PromptSupport.getPrompt(this);
	}

	/**
	 * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
	 */
	public Color getPromptForeground() {
		return PromptSupport.getForeground(this);
	}

	/**
	 * @see PromptSupport#getForeground(javax.swing.text.JTextComponent)
	 */
	public Color getPromptBackground() {
		return PromptSupport.getBackground(this);
	}

	/**
	 * @see PromptSupport#getFontStyle(javax.swing.text.JTextComponent)
	 */
	public Integer getPromptFontStyle() {
		return PromptSupport.getFontStyle(this);
	}

	/**
	 * @see PromptSupport#getFocusBehavior(javax.swing.text.JTextComponent)
	 */
	public void setFocusBehavior(FocusBehavior focusBehavior) {
		PromptSupport.setFocusBehavior(focusBehavior, this);
	}

	/**
	 * @see PromptSupport#setPrompt(String, javax.swing.text.JTextComponent)
	 */
	public void setPrompt(String labelText) {
		PromptSupport.setPrompt(labelText, this);
	}

	/**
	 * @see PromptSupport#setForeground(Color, javax.swing.text.JTextComponent)
	 */
	public void setPromptForeground(Color promptTextColor) {
		PromptSupport.setForeground(promptTextColor, this);
	}

	/**
	 * @see PromptSupport#setBackground(Color, javax.swing.text.JTextComponent)
	 */
	public void setPromptBackround(Color promptTextColor) {
		PromptSupport.setBackground(promptTextColor, this);
	}

	/**
	 * @see PromptSupport#setFontStyle(Integer, javax.swing.text.JTextComponent)
	 */
	public void setPromptFontStyle(Integer fontStyle) {
		PromptSupport.setFontStyle(fontStyle, this);
	}
}
