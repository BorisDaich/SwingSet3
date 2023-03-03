/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.decorator;

import java.beans.PropertyChangeListener;

import org.jdesktop.swingx.JXList;


/**
 * Test JXList as HighlighterClient.
 * 
 * @author Jeanette Winzenburg
 */
public class ListAsHighlighterClientTest extends AbstractTestHighlighterClient {

	static String[] model = {"1", "2"}; // not empty model
	
    @Override
    protected HighlighterClient createHighlighterClient() {
    	JXList<Object> list = new JXList<>();
    	LOG.config("model.size="+list.getModel().getSize()); // we test with empty list!
        return createHighlighterClient(list);
    }

    private HighlighterClient createHighlighterClient(final JXList<?> list) {
        HighlighterClient client = new HighlighterClient() {

            public void addHighlighter(Highlighter highlighter) {
                list.addHighlighter(highlighter);
            }

            public void addPropertyChangeListener(PropertyChangeListener l) {
                list.addPropertyChangeListener(l);
            }

            public Highlighter[] getHighlighters() {
                return list.getHighlighters();
            }

            public void removeHighlighter(Highlighter highlighter) {
                list.removeHighlighter(highlighter);
            }

            public void removePropertyChangeListener(PropertyChangeListener l) {
                list.removePropertyChangeListener(l);
            }

            public void setHighlighters(Highlighter... highlighters) {
                list.setHighlighters(highlighters);
            }

            public void updateUI() {
                list.updateUI();
            }
            
        };
        return client;
    }
   

}
