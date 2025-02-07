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
package org.jdesktop.swingx.plaf;

import javax.swing.UIManager;

import org.jdesktop.swingx.plaf.linux.LinuxLookAndFeelAddons;
import org.jdesktop.swingx.plaf.macosx.MacOSXLookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons;
import org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

/**
 * Ease the work of creating an addon for a component.
 * 
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 * @author Karl Schaefer
 */
public abstract class AbstractComponentAddon implements ComponentAddon {

	/**
	 * the name of this addon
	 */
	private String name;

    /**
     * Ctor
     * @param name of the addon
     */
    protected AbstractComponentAddon(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * initialize addon
     * @param addon LookAndFeelAddons
     */
    @Override
    public void initialize(LookAndFeelAddons addon) {
        addon.loadDefaults(getDefaults(addon));
    }

    /**
     * {@inheritDoc}
     * uninitialize addon
     * @param addon LookAndFeelAddons
     */
    @Override
    public void uninitialize(LookAndFeelAddons addon) {
        // commented after Issue 446. Maybe addon should keep track of its
        // added defaults to correctly remove them on uninitialize
        // addon.unloadDefaults(getDefaults(addon));
    }

    /**
     * Adds default key/value pairs to the given list.
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addMotifDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Default implementation calls
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     * 
     * @param addon LookAndFeelAddons
     * @param defaults the DefaultsList to add
     */
    protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
    }

    /**
     * Gets the defaults for the given addon.
     * 
     * Based on the addon, it calls
     * {@link #addMacDefaults(LookAndFeelAddons, DefaultsList)} if isMac() or
     * {@link #addMetalDefaults(LookAndFeelAddons, DefaultsList)} if isMetal()
     * or {@link #addMotifDefaults(LookAndFeelAddons, DefaultsList)} if
     * isMotif() or {@link #addWindowsDefaults(LookAndFeelAddons, DefaultsList)}
     * if isWindows() or
     * {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)} if none of the
     * above was called.
     * 
     * @param addon
     * @return an array of key/value pairs. For example:
     * 
     *         <pre>
     * Object[] uiDefaults = { &quot;Font&quot;, new Font(&quot;Dialog&quot;, Font.BOLD, 12), &quot;Color&quot;,
     *         Color.red, &quot;five&quot;, new Integer(5) };
     * </pre>
     */
    private Object[] getDefaults(LookAndFeelAddons addon) {
        DefaultsList defaults = new DefaultsList();
        
        if (isWindows(addon)) {
            addWindowsDefaults(addon, defaults);
        } else if (isMetal(addon)) {
            addMetalDefaults(addon, defaults);
        } else if (isMac(addon)) {
            addMacDefaults(addon, defaults);
        } else if (isMotif(addon)) {
            addMotifDefaults(addon, defaults);
            // PENDING JW: the separation line here looks fishy
            // what about Nimbus on Linux systems?
        } else if (isLinux(addon)) {
            addLinuxDefaults(addon, defaults);
        } else if (isNimbus(addon)) {
            addNimbusDefaults(addon, defaults);
        } else {
            // at least add basic defaults
            addBasicDefaults(addon, defaults);
        }
        return defaults.toArray();
    }

    //
    // Helper methods to make ComponentAddon developer life easier
    //

    /**
     * check if current look and feel is Windows
     * @param addon LookAndFeelAddons
     * @return true if the addon is the Windows addon or its subclasses
     */
    protected boolean isWindows(LookAndFeelAddons addon) {
        return addon instanceof WindowsLookAndFeelAddons;
    }

    /**
     * check if current look and feel is Metal
     * @param addon LookAndFeelAddons
     * @return true if the addon is the Metal addon or its subclasses
     */
    protected boolean isMetal(LookAndFeelAddons addon) {
        return addon instanceof MetalLookAndFeelAddons;
    }

    /**
     * check if current look and feel is Mac OS X
     * @param addon LookAndFeelAddons
     * @return true if the addon is the Mac OS X addon or its subclasses
     */
    protected boolean isMac(LookAndFeelAddons addon) {
        return addon instanceof MacOSXLookAndFeelAddons;
    }

    /**
     * check if current look and feel is Motif
     * @param addon LookAndFeelAddons
     * @return true if the addon is the Motif addon or its subclasses
     */
    protected boolean isMotif(LookAndFeelAddons addon) {
        return addon instanceof MotifLookAndFeelAddons;
    }

    /**
     * check if current look and feel is Linux
     * @param addon LookAndFeelAddons
     * @return true if the current look and feel is Linux
     */
    protected boolean isLinux(LookAndFeelAddons addon) {
        return addon instanceof LinuxLookAndFeelAddons;
    }

    /**
     * check if current look and feel is Nimbus
     * @param addon LookAndFeelAddons
     * @return true if the current look and feel is Nimbus
     */
    protected boolean isNimbus(LookAndFeelAddons addon) {
        return addon instanceof NimbusLookAndFeelAddons;
    }

    /**
     * @return true if the current look and feel is one of JGoodies Plastic LaFs
     */
    protected boolean isPlastic() {
        return UIManager.getLookAndFeel().getClass().getName().contains("Plastic");
    }

    /**
     * @return true if the current look and feel is Synth LaF
     */
    protected boolean isSynth() {
        return UIManager.getLookAndFeel().getClass().getName().contains("ynth");
    }

    /**
     * @return true if the current look and feel is Nimbus LaF
     */ 
    public static boolean isNimbus() {
        return UIManager.getLookAndFeel().getClass().getName().contains("Nimbus");
    }

}
