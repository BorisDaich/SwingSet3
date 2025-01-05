package org.jdesktop.swingx.plaf;

import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JComponent;

/**
 * a UI Property Change Handler
 */
public abstract class AbstractUIChangeHandler implements PropertyChangeListener {
	
	// prevent double installation.
	private final Map<JComponent, Boolean> installed = new WeakHashMap<JComponent, Boolean>();

	/**
	 * install component
	 * @param c component to install
	 */
	public void install(JComponent c) {
		synchronized (installed) {
			if (!isInstalled(c)) {
				c.addPropertyChangeListener("UI", this);
				installed.put(c, Boolean.TRUE);
			}
		}
	}

	/**
	 * check if component is installed
	 * @param c the component to check
	 * @return true it c is installed
	 */
	public boolean isInstalled(JComponent c) {
		synchronized (installed) {
			return installed.containsKey(c);
		}
	}

	/**
	 * uninstall component
	 * @param c the component to remove
	 */
	public void uninstall(JComponent c) {
		synchronized (installed) {
			c.removePropertyChangeListener("UI", this);
			installed.remove(c);
		}
	}
}