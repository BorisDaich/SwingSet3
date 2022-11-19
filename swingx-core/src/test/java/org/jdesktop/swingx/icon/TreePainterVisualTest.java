package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.nimbus.test.MyTreeCellEditorPainter;
import javax.swing.plaf.nimbus.test.MyTreeCellPainter;
import javax.swing.plaf.nimbus.test.MyTreePainter;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

@SuppressWarnings("serial")
public class TreePainterVisualTest extends JXPanel {

	private static final Logger LOG = Logger.getLogger(TreePainterVisualTest.class.getName());
	
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater( () -> {
        	InteractiveTestCase.setLAF("Nimbus");
        	createAndShowGUI();
        });
    }

    /**
     * Create the GUI and show it.  
     * For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
		JXFrame frame = new JXFrame("TreePainter Visual Test", true); // exitOnClose
		JXPanel demo = new TreePainterVisualTest(); //frame);
        frame = InteractiveTestCase.wrapInFrame(frame, demo);
		frame.setStartPosition(StartPosition.CenterInScreen);

        //Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }

//    private static Dimension HGAP15 = new Dimension(15,1);
//    private static Dimension VGAP15 = new Dimension(1,15);
//    private static Dimension HGAP30 = new Dimension(30,1);
//    private static Dimension VGAP30 = new Dimension(1,30);
//    
//    private PainterIcon fgEnabledIcon;
//    private PainterIcon fgDisabledIcon;

    private TreePainterVisualTest() {
    	super(new BorderLayout());
//    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
//    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

//        JButton jButtonNORTH = new BasicArrowButton(SwingConstants.NORTH);
//        JButton jButtonSOUTH = new BasicArrowButton(SwingConstants.SOUTH);
//        JButton jButtonWEST  = new BasicArrowButton(SwingConstants.WEST);
//        JButton jButtonEAST  = new BasicArrowButton(SwingConstants.EAST);
//        super.add(jButtonNORTH, BorderLayout.NORTH);
//        super.add(jButtonSOUTH, BorderLayout.SOUTH);
//        super.add(jButtonWEST , BorderLayout.WEST);
//        super.add(jButtonEAST , BorderLayout.EAST);
//
//        jButtonNORTH.addActionListener( ae -> {
//        	LOG.info(""+ae);
//            // the default is WEST, this rotates 90° right, the result is NORTH :
//            fgEnabledIcon.setRotation(JXIcon.EAST); 
//            fgDisabledIcon.setRotation(JXIcon.EAST);
//            TreePainterVisualTest.this.updateUI();
//        });
//        jButtonEAST.addActionListener( ae -> {
//            // the default is WEST, this rotates 180° right, the result is EAST
//            fgEnabledIcon.setRotation(JXIcon.SOUTH); 
//            fgDisabledIcon.setRotation(JXIcon.SOUTH);
//            TreePainterVisualTest.this.updateUI();
//        });
//        jButtonSOUTH.addActionListener( ae -> {
//            fgEnabledIcon.setRotation(JXIcon.WEST); 
//            fgDisabledIcon.setRotation(JXIcon.WEST);
//            TreePainterVisualTest.this.updateUI();
//        });
//        jButtonWEST.addActionListener( ae -> {
//            fgEnabledIcon.setRotation(JXIcon.NORTH); 
//            fgDisabledIcon.setRotation(JXIcon.NORTH);
//            TreePainterVisualTest.this.updateUI();
//        });

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        super.add(p, BorderLayout.CENTER);

		PainterIcon treeLeafEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeLeafEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.LEAFICON_ENABLED));
		JXButton treeLeafEnabledButton = new JXButton("treeLeafEnabled", treeLeafEnabledIcon);
		p.add(treeLeafEnabledButton);
      
		PainterIcon treeClosedEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeClosedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.CLOSEDICON_ENABLED));
		JXButton treeClosedEnabledButton = new JXButton("treeClosedEnabled", treeClosedEnabledIcon);
		p.add(treeClosedEnabledButton);

		PainterIcon treeOpenEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		treeOpenEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.OPENICON_ENABLED));
		JXButton treeOpenEnabledButton = new JXButton("treeOpenEnabled", treeOpenEnabledIcon);
		p.add(treeOpenEnabledButton);

		PainterIcon treeCollapsedEnabledIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeCollapsedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.COLLAPSEDICON_ENABLED));
		JXButton treeCollapsedEnabledButton = new JXButton("treeCollapsedEnabled", treeCollapsedEnabledIcon);
		p.add(treeCollapsedEnabledButton);

		PainterIcon treeCollapsedEnabledSelectedIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeCollapsedEnabledSelectedIcon.setPainter(MyTreePainter.factory(MyTreePainter.COLLAPSEDICON_ENABLED_SELECTED));
		JXButton treeCollapsedEnabledSelectedButton = new JXButton("treeCollapsedEnabledSelected", treeCollapsedEnabledSelectedIcon);
		p.add(treeCollapsedEnabledSelectedButton);

		PainterIcon treeExpandedEnabledIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeExpandedEnabledIcon.setPainter(MyTreePainter.factory(MyTreePainter.EXPANDEDICON_ENABLED));
		JXButton treeExpandedEnabledButton = new JXButton("treeExpandedEnabled", treeExpandedEnabledIcon);
		p.add(treeExpandedEnabledButton);

		PainterIcon treeExpandedEnabledSelectedIcon = new PainterIcon(MyTreePainter.canvasSize());
		treeExpandedEnabledSelectedIcon.setPainter(MyTreePainter.factory(MyTreePainter.EXPANDEDICON_ENABLED_SELECTED));
		JXButton treeExpandedEnabledSelectedButton = new JXButton("treeExpandedEnabledSelected", treeExpandedEnabledSelectedIcon);
		p.add(treeExpandedEnabledSelectedButton);

		PainterIcon treeCellBgEnabledFocusedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgEnabledFocusedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_ENABLED_FOCUSED));
		JXButton treeCellBgEnabledFocusedButton = new JXButton("treeCellBgEnabledFocused", treeCellBgEnabledFocusedIcon);
		p.add(treeCellBgEnabledFocusedButton);

		PainterIcon treeCellBgEnabledSelectedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgEnabledSelectedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_ENABLED_SELECTED));
		JXButton treeCellBgEnabledSelectedButton = new JXButton("treeCellBgEnabledSelected", treeCellBgEnabledSelectedIcon);
		p.add(treeCellBgEnabledSelectedButton);

		PainterIcon treeCellBgSelectedFocusedIcon = new PainterIcon(MyTreeCellPainter.canvasSize());
		treeCellBgSelectedFocusedIcon.setPainter(MyTreeCellPainter.factory(MyTreeCellPainter.BACKGROUND_SELECTED_FOCUSED));
		JXButton treeCellBgSelectedFocusedButton = new JXButton("treeCellBgSelectedFocused", treeCellBgSelectedFocusedIcon);
		p.add(treeCellBgSelectedFocusedButton);

		PainterIcon treeCellEditorBgEnabledIcon = new PainterIcon(MyTreeCellEditorPainter.canvasSize());
		treeCellEditorBgEnabledIcon.setPainter(MyTreeCellEditorPainter.factory(MyTreeCellEditorPainter.BACKGROUND_ENABLED));
		JXButton treeCellEditorBgEnabledButton = new JXButton("treeCellEditorBgEnabled", treeCellEditorBgEnabledIcon);
		p.add(treeCellEditorBgEnabledButton);

		PainterIcon treeCellEditorBgEnabledFocusedIcon = new PainterIcon(MyTreeCellEditorPainter.canvasSize());
		treeCellEditorBgEnabledFocusedIcon.setPainter(MyTreeCellEditorPainter.factory(MyTreeCellEditorPainter.BACKGROUND_ENABLED_FOCUSED));
		JXButton treeCellEditorBgEnabledFocusedButton = new JXButton("treeCellEditorBgEnabledFocused", treeCellEditorBgEnabledFocusedIcon);
		p.add(treeCellEditorBgEnabledFocusedButton);
    }
    
    private Component createXButton(String propertyName) {
    	Icon icon = DefaultIcons.getIcon(propertyName);  	
    	return new JXButton(propertyName, icon);
    }
    
}
