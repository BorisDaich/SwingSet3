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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import org.junit.Test;
import org.junit.Test;
import org.jdesktop.swingx.search.NativeSearchFieldSupport;
import org.jdesktop.swingx.search.RecentSearches;

/**
 * Visual tests of JXSearchField issues.
 * @author Karl Schaefer
 *
 */
public class JXSearchFieldVisualCheck extends InteractiveTestCase {
	
    private static final Logger LOG = Logger.getLogger(JXSearchFieldVisualCheck.class.getName());
    private static final String LONG_PROMPT = "prompt prompt prompt prompt prompt prompt prompt";
/*
    class JXSearchFieldTester extends JXSearchField {
    	public JXSearchFieldTester(String prompt) {
    		super(prompt);
    		ActionListener al = getFindAction();
//    		public final ActionListener getCancelAction() {
//			LOG.info("ActionListener findAction:"+al);
    		super.addActionListener(actionEvent -> {
    			LOG.info("actionEvent.Class:"+actionEvent.getClass());
    			JXSearchField f = (JXSearchField)actionEvent.getSource();
    			LOG.info("actionEvent Action:"+f.getAction()+" Text:"+f.getText()+ " CaretPosition="+f.getCaretPosition());
    			// SHORT_DESCRIPTION
    		});
    	}
//    	public void addActionListener(ActionListener l) {
//    		
//    	}
//    	public final ActionListener getFindAction() {
//    		addActionListener(new ActionListener()
//    	}

    }
*/    
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
//			JXSearchField f = (JXSearchField)actionEvent.getSource();
//			LOG.info("prop NativeSearchFieldSupport.FIND_ACTION_PROPERTY="
//					+f.getClientProperty(NativeSearchFieldSupport.FIND_ACTION_PROPERTY)
//					);
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
			LOG.info("actionEvent["+actionEvent.getClass().getSimpleName()+"]:"+actionEvent.paramString()
			+", ModifiersExText("+actionEvent.getModifiers()+")="+InputEvent.getModifiersExText(actionEvent.getModifiers())
			+(actionEvent.getModifiers()==0 ? " ENTER" : " find button pushed")
			);
//			lastcmd = actionEvent.getActionCommand();
//			JXSearchField f = (JXSearchField)actionEvent.getSource();
//			LOG.info("prop NativeSearchFieldSupport.FIND_ACTION_PROPERTY="
//					+f.getClientProperty(NativeSearchFieldSupport.FIND_ACTION_PROPERTY)
//					);
		});
        LOG.info("REGULAR test finished - enter a search string+push find button for visual check.\n");
        showInFrame(search, "JXSearchField REGULAR Mode testing");
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
Sets the popup menu that will be displayed when the popup button is clicked. 
If a find popup menu is set and isUseSeperatePopupButton() returns false, 
the popup button will be displayed instead of the find button. 
Otherwise the popup button will be displayed in addition to the find button.
The find popup menu is managed using NativeSearchFieldSupport to achieve compatibility 
with the native search field support provided by the Mac Look And Feel since Mac OS 10.5.
If a recent searches save key has been set and therefore a recent searches popup menu is installed, 
this method does nothing. 
You mustfirst remove the recent searches save key, 
by calling setRecentSearchesSaveKey(String) with a null parameter.
Parameters:findPopupMenu the popup menu, which will be displayed when the popup button is clicked        
 */
        JPopupMenu popupMenu = search.getFindPopupMenu();
        LOG.info("popupMenu="+popupMenu + "// expected null"); 
//        popupMenu = createPopupMenu(search, popup.getIcon());
//        search.setFindPopupMenu(popupMenu);
// public RecentSearchesPopup(RecentSearches recentSearches, JTextField searchField)
        search.setRecentSearchesSaveKey("String recentSearchesSaveKey");
        popupMenu = new RecentSearches.RecentSearchesPopup(search.getRecentSearches(), search);
        LOG.info("popupMenu"
        		+ "="+search.getFindPopupMenu());
        
        
        showInFrame(search, "JXSearchFieldVisualCheck testing");
//        showMessageDialog(search, popup);
    }
    
    // <snip> PopupMenu
    /**
     * a small popup menu, activated via keyboard SHIFT_DOWN+F10
     * shows items with InstalledLookAndFeels
     * and a class ActivatePopupMenuAction with ActionEvent on SHIFT_DOWN+F10
     * 
     * @param comp JComponent, typically JXPanel where to show the popup
     * @return JPopupMenu
     */
    public JPopupMenu createPopupMenu(JComponent comp, Icon icon) {
        JPopupMenu popupMenu = new JPopupMenu("JPopupMenu Laf demo");

        JMenuItem mi = null; // JMenuItem extends AbstractButton / ctor public JMenuItem(String text, Icon icon)
        mi = new JMenuItem("popupIcon", UIManager.getIcon("SearchField.popupIcon"));
        popupMenu.add(mi);
        mi = new JMenuItem("popupRolloverIcon", UIManager.getIcon("SearchField.popupRolloverIcon"));
        popupMenu.add(mi);
        mi = new JMenuItem("popup button Icon", icon);
        popupMenu.add(mi);

        InputMap map = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK), "postMenuAction");
        comp.getActionMap().put("postMenuAction", new ActivatePopupMenuAction(comp, popupMenu));

        return popupMenu;
    }

    class ActivatePopupMenuAction extends AbstractAction {
//		private static final long serialVersionUID = 3925663480989462160L;
		Component invoker;
        JPopupMenu popup;
        
        protected ActivatePopupMenuAction(Component comp, JPopupMenu popupMenu) {
            super("ActivatePopupMenu"); // the name for the action
            this.invoker = comp;
            this.popup = popupMenu;
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
        	LOG.fine("event:"+e);
            Dimension invokerSize = invoker.getSize();
            Dimension popupSize = popup.getPreferredSize();
            popup.show(invoker, 
            		(invokerSize.width - popupSize.width) / 2,
            		(invokerSize.height - popupSize.height) / 2
            	);
        }
    }
    // </snip> PopupMenu

    private void showOptionDialog(Component parentComponent, JButton popup) {
    	
    }
    private void showMessageDialog(Component parentComponent, JButton popup) {
        JOptionPane.showMessageDialog
    	( parentComponent
        , "<html>" + getImgSrc() + "<br><center>" + getBundleString("messagetext") + "</center><br></html>"
        , "title" // getUIString(OPTIONPANE_MESSAGE)
        , JOptionPane.INFORMATION_MESSAGE
//        , getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON
        , UIManager.getIcon("SearchField.popupIcon")
        );
    }
    
    private String getBundleString(String string) {
		// TODO Auto-generated method stub
		return string;
	}

	private String getImgSrc() {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO getPopupButton
/*
    private JButton createMessageDialogButton() {
        Icon icon = getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("messagebutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            JOptionPane.showMessageDialog
        	( ErrorPaneDemo.this
            , "<html>" + getImgSrc() + "<br><center>" + getBundleString("messagetext") + "</center><br></html>"
            , getUIString(OPTIONPANE_MESSAGE)
            , JOptionPane.INFORMATION_MESSAGE
            , getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
        );
		});
    	return b;
    }
 */
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
