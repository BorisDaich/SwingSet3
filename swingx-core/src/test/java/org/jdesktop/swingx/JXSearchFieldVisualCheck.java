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
package org.jdesktop.swingx;

import java.awt.event.InputEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import org.jdesktop.swingx.search.RecentSearches;
import org.junit.Test;

/**
 * Visual tests of JXSearchField issues.
 * @author Karl Schaefer
 * @author EUG https://github.com/homebeaver add tests
 */
public class JXSearchFieldVisualCheck extends InteractiveTestCase {
	
    private static final Logger LOG = Logger.getLogger(JXSearchFieldVisualCheck.class.getName());
    private static final String LONG_PROMPT = "prompt prompt prompt prompt prompt prompt prompt";

	String lastcmd = null;
    @Test
    public void interactiveSearchFieldInstant() {
        JXSearchField search = new JXSearchField(LONG_PROMPT);
        if(search.isInstantSearchMode()) {
            // INSTANT: event is fired, when the user presses enter or changes the search text.
        	// event is delayed about the number of milliseconds specified
            LOG.fine("default SearchMode="+search.getSearchMode() + " InstantSearchDelay="+search.getInstantSearchDelay());
        }
		assertEquals(JXSearchField.SearchMode.INSTANT, search.getSearchMode());
		
		search.addActionListener(actionEvent -> {
			// ? kann ich unterscheiden, ob nur Enter oder search text Änderung
			// offenbar nur wenn ich mir ActionCommand merke:
			LOG.info("actionEvent["+actionEvent.getClass().getSimpleName()+"]:"+actionEvent.paramString()
			+(actionEvent.getActionCommand().equals(lastcmd) ? " ENTER" : " CHANGE of search text")
			);
			lastcmd = actionEvent.getActionCommand();
		});
        LOG.info("test finished - enter a search string for visual check.\n");
        showInFrame(search, "JXSearchField INSTANT Mode testing");
        search.setText("press <[x] to clear ...");
    }

    @Test
    public void interactiveSearchFieldRegular() {
        JXSearchField search = new JXSearchField(LONG_PROMPT);
		assertEquals(JXSearchField.SearchMode.INSTANT, search.getSearchMode());
        LOG.info("default SearchMode="+search.getSearchMode() + " switch to REGULAR ...");
        search.setSearchMode(JXSearchField.SearchMode.REGULAR);
        // In REGULAR search mode, an action event is fired, 
        // when the user presses enter or clicks the find button.
        LOG.fine("SearchMode="+search.getSearchMode() + " isUseSeperatePopupButton="+search.isUseSeperatePopupButton());
		assertFalse(search.isUseSeperatePopupButton());
        LOG.fine("SearchMode="+search.getSearchMode() + " popup menu="+search.getFindPopupMenu());
		assertNull(search.getFindPopupMenu());
		
		search.addActionListener(actionEvent -> {
			// "modifiers=Button1" kommt von deprecated KeyEvent.getKeyModifiersText(modifiers):
			// modifiers & InputEvent.BUTTON1_MASK, daher ModifiersExText
			LOG.info("actionEvent["+actionEvent.getClass().getSimpleName()+"]:"+actionEvent.getActionCommand()
			+", ModifiersExText("+actionEvent.getModifiers()+")="+InputEvent.getModifiersExText(actionEvent.getModifiers())
			+(actionEvent.getModifiers()==0 ? " ENTER" : " find button pushed")
			);
		});
        LOG.info("REGULAR test finished - enter a search string+push find button for visual check.\n");
        showInFrame(search, "JXSearchField REGULAR Mode testing");
    }

    @Test
    public void interactiveSearchFieldRegularWithPopUpButton() {
        JXSearchField search = new JXSearchField(LONG_PROMPT);
		assertEquals(JXSearchField.SearchMode.INSTANT, search.getSearchMode());
        LOG.info("default SearchMode="+search.getSearchMode() + " switch to REGULAR ...");
        search.setSearchMode(JXSearchField.SearchMode.REGULAR);
        // In REGULAR search mode, an action event is fired, 
        // when the user presses enter or clicks the find button.
        LOG.fine("SearchMode="+search.getSearchMode() + " isUseSeperatePopupButton="+search.isUseSeperatePopupButton());
		assertFalse(search.isUseSeperatePopupButton());
		
        JButton popup = search.getPopupButton();
        LOG.info("SearchMode="+search.getSearchMode() + " popup button="+popup);
        
/* void setFindPopupMenu(JPopupMenu findPopupMenu)

Sets the popup menu that will be displayed when the popup button is clicked. 
If a find popup menu is set and isUseSeperatePopupButton() returns false, 
the popup button will be displayed instead of the find button.
                                   -------
 */
        JPopupMenu popupMenu = search.getFindPopupMenu();
        LOG.info("search.isManagingRecentSearches="+search.isManagingRecentSearches() 
        + ", isUseSeperatePopupButton="+search.isUseSeperatePopupButton()
        + ", popupMenu="+popupMenu + "// expected null because not installed"); 
		assertFalse(search.isManagingRecentSearches());
		assertNull(popupMenu);
		search.setFindPopupMenu(new RecentSearches.RecentSearchesPopup(search.getRecentSearches(), search));
		
		search.addActionListener(actionEvent -> {
			LOG.info("actionEvent["+actionEvent.getClass().getSimpleName()+"]:"+actionEvent.getActionCommand()
			+", ModifiersExText("+actionEvent.getModifiers()+")="+InputEvent.getModifiersExText(actionEvent.getModifiers())
			+(actionEvent.getModifiers()==0 ? " ENTER" : " find button pushed")
			);
		});
                
        showInFrame(search, "JXSearchField REGULAR WithPopUpButton testing");
    }
    
    @Test
    public void interactiveRenderingCheck() {
        JXSearchField search = new JXSearchField(LONG_PROMPT);
		assertEquals(JXSearchField.SearchMode.INSTANT, search.getSearchMode());
        LOG.info("default SearchMode="+search.getSearchMode() + " switch to REGULAR and UseSeperatePopupButton ...");
        search.setSearchMode(JXSearchField.SearchMode.REGULAR);
        // In REGULAR search mode, an action event is fired, 
        // when the user presses enter or clicks the find button.
        LOG.fine("SearchMode="+search.getSearchMode() + " isUseSeperatePopupButton="+search.isUseSeperatePopupButton());
		assertFalse(search.isUseSeperatePopupButton());
		
        search.setUseSeperatePopupButton(true);
        JButton popup = search.getPopupButton();
        LOG.info("SearchMode="+search.getSearchMode() + " popup button="+popup);
        
/* void setFindPopupMenu(JPopupMenu findPopupMenu)

Otherwise - isUseSeperatePopupButton() returns true
the popup button will be displayed in addition to the find button.
The find popup menu is managed using NativeSearchFieldSupport to achieve compatibility 
with the native search field support provided by the Mac Look And Feel since Mac OS 10.5.
If a recent searches save key has been set and therefore a recent searches popup menu is installed, 
this method does nothing. 
 */
        JPopupMenu popupMenu = search.getFindPopupMenu();
        LOG.info("search.isManagingRecentSearches="+search.isManagingRecentSearches() 
        + ", isUseSeperatePopupButton="+search.isUseSeperatePopupButton()
        + ", popupMenu="+popupMenu + "// expected null because not installed"); 
		assertFalse(search.isManagingRecentSearches());
		assertNull(popupMenu);
		
		// Now Install a recent searches popup menu as the find popup menu,
        search.setRecentSearchesSaveKey("String recentSearchesSaveKey");
        LOG.info("????? search.isManagingRecentSearches="+search.isManagingRecentSearches());
        popupMenu = new RecentSearches.RecentSearchesPopup(search.getRecentSearches(), search);
        search.setFindPopupMenu(popupMenu);
        LOG.info("popupMenu"
        		+ "="+search.getFindPopupMenu());
        
		search.addActionListener(actionEvent -> {
			LOG.info("actionEvent["+actionEvent.getClass().getSimpleName()+"]:"+actionEvent.getActionCommand()
			+", ModifiersExText("+actionEvent.getModifiers()+")="+InputEvent.getModifiersExText(actionEvent.getModifiers())
			+(actionEvent.getModifiers()==0 ? " ENTER" : " find button pushed")
			);
		});
                
        showInFrame(search, "JXSearchFieldVisualCheck testing");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        JXSearchFieldVisualCheck test = new JXSearchFieldVisualCheck();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("interactiveRenderingCheck");
          } catch (Exception e) {
              System.err.println("exception when executing interactive tests:");
              e.printStackTrace();
          }
    }

    /**
     * do nothing test - keep the testrunner happy.
     */
    public void testDummy() {
    }

}
