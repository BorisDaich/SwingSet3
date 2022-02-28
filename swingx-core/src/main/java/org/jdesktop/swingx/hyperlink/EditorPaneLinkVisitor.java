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
package org.jdesktop.swingx.hyperlink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import org.jdesktop.swingx.JXEditorPane;


/**
 * A ActionListener using a JXEditorPane to "visit" a LinkModel.
 * 
 * adds an internal HyperlinkListener to visit links contained
 * in the document. 
 * 
 * @author Jeanette Winzenburg
 */
public class EditorPaneLinkVisitor implements ActionListener {
    private JXEditorPane editorPane;
    private HyperlinkListener hyperlinkListener;
    private LinkModel internalLink;
    
    /**
     * ctor
     */
    public EditorPaneLinkVisitor() {
        this(null);
    }
    
    /**
     * ctor
     * @param pane JXEditorPane
     */
    public EditorPaneLinkVisitor(JXEditorPane pane) {
        if (pane == null) {
            pane = createDefaultEditorPane();
        }
        this.editorPane = pane;
        pane.addHyperlinkListener(getHyperlinkListener());
    }
    
    /**
     * 
     * @return JXEditorPane
     */
    public JXEditorPane getOutputComponent() {
        return editorPane;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof LinkModel) {
            final LinkModel link = (LinkModel) e.getSource();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    visit(link);

                }
            });
        }
   
    }

    /**
     * TODO (maven-javadoc-plugin 3.3.2 needs a doc here)
     * @param link LinkModel
     */
    public void visit(LinkModel link) {
        try {
            // make sure to reload
            editorPane.getDocument().putProperty(Document.StreamDescriptionProperty, null);
            // JW: editorPane defaults to asynchronous loading
            // no need to explicitly start a thread - really?
            editorPane.setPage(link.getURL());
            link.setVisited(true);
        } catch (IOException e1) {
            editorPane.setText("<html>Error 404: couldn't show " + link.getURL() + " </html>");
        }
    }

    /**
     * 
     * @return JXEditorPane
     */
    protected JXEditorPane createDefaultEditorPane() {
        final JXEditorPane editorPane = new JXEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        return editorPane;
    }

    /**
     * 
     * @return HyperlinkListener
     */
    protected HyperlinkListener getHyperlinkListener() {
        if (hyperlinkListener == null) {
            hyperlinkListener = createHyperlinkListener();
        }
        return hyperlinkListener;
    }

    /**
     * 
     * @return HyperlinkListener
     */
    protected HyperlinkListener createHyperlinkListener() {
        return new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
                    visitInternal(e.getURL());
                }
                
            }
            
        };
    }

    /**
     * 
     * @return LinkModel
     */
    protected LinkModel getInternalLink() {
        if (internalLink == null) {
            internalLink = new LinkModel("internal");
        }
        return internalLink;
    }

    /**
     * TODO (maven-javadoc-plugin 3.3.2 needs a doc here)
     * @param url URL
     */
    protected void visitInternal(URL url) {
        try {
            getInternalLink().setURL(url);
            visit(getInternalLink());
        } catch (Exception e) {
            // todo: error feedback
        }
    }


}
