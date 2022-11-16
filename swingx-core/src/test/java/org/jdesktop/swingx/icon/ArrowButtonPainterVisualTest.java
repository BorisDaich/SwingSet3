package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.nimbus.test.MyArrowButtonPainter;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

public class ArrowButtonPainterVisualTest extends JXPanel {

	private static final Logger LOG = Logger.getLogger(ArrowButtonPainterVisualTest.class.getName());
	
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater( () -> {
//        	InteractiveTestCase.setLAF("Nimbus");
        	createAndShowGUI();
        });
    }

    /**
     * Create the GUI and show it.  
     * For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
		JXFrame frame = new JXFrame("ArrowButtonPainter Visual Test", true); // exitOnClose
		JXPanel demo = new ArrowButtonPainterVisualTest(); //frame);
        frame = InteractiveTestCase.wrapInFrame(frame, demo);
		frame.setStartPosition(StartPosition.CenterInScreen);

        //Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }

    private static Dimension HGAP15 = new Dimension(15,1);
    private static Dimension VGAP15 = new Dimension(1,15);
    private static Dimension HGAP30 = new Dimension(30,1);
    private static Dimension VGAP30 = new Dimension(1,30);
    
    private PainterIcon fgEnabledIcon;
    private PainterIcon fgDisabledIcon;

    private ArrowButtonPainterVisualTest() {
    	super(new BorderLayout());
//    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
//    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

        JButton jButtonNORTH = new BasicArrowButton(SwingConstants.NORTH);
        JButton jButtonSOUTH = new BasicArrowButton(SwingConstants.SOUTH);
        JButton jButtonWEST  = new BasicArrowButton(SwingConstants.WEST);
        JButton jButtonEAST  = new BasicArrowButton(SwingConstants.EAST);
        super.add(jButtonNORTH, BorderLayout.NORTH);
        super.add(jButtonSOUTH, BorderLayout.SOUTH);
        super.add(jButtonWEST , BorderLayout.WEST);
        super.add(jButtonEAST , BorderLayout.EAST);

        jButtonNORTH.addActionListener( ae -> {
        	LOG.info(""+ae);
            // the default is WEST, this rotates 90° right, the result is NORTH :
            fgEnabledIcon.setRotation(JXIcon.EAST); 
            fgDisabledIcon.setRotation(JXIcon.EAST);
            ArrowButtonPainterVisualTest.this.updateUI();
        });
        jButtonEAST.addActionListener( ae -> {
            // the default is WEST, this rotates 180° right, the result is EAST
            fgEnabledIcon.setRotation(JXIcon.SOUTH); 
            fgDisabledIcon.setRotation(JXIcon.SOUTH);
            ArrowButtonPainterVisualTest.this.updateUI();
        });
        jButtonSOUTH.addActionListener( ae -> {
            fgEnabledIcon.setRotation(JXIcon.WEST); 
            fgDisabledIcon.setRotation(JXIcon.WEST);
            ArrowButtonPainterVisualTest.this.updateUI();
        });
        jButtonWEST.addActionListener( ae -> {
            fgEnabledIcon.setRotation(JXIcon.NORTH); 
            fgDisabledIcon.setRotation(JXIcon.NORTH);
            ArrowButtonPainterVisualTest.this.updateUI();
        });

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        super.add(p, BorderLayout.CENTER);

        Painter<? extends Component> fgEnabled = MyArrowButtonPainter.factory(MyArrowButtonPainter.FOREGROUND_ENABLED);
        fgEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
        fgEnabledIcon.setPainter(fgEnabled);
        JXButton xButtonEnabled = new JXButton("xButtonEnabled", fgEnabledIcon);
        p.add(xButtonEnabled);
        
        fgDisabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
        fgDisabledIcon.setPainter(MyArrowButtonPainter.factory(MyArrowButtonPainter.FOREGROUND_DISABLED));
        JXButton xButtonDisabled = new JXButton("xButtonDisabled", fgDisabledIcon);
        p.add(xButtonDisabled);
        
//        p.add(createXButton(DefaultIcons.ERROR));
////         icon ist bei allen LAFs gleich:
//        PainterIcon errorEnabledIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
//        errorEnabledIcon.setPainter(MyOptionPanePainter.factory(MyOptionPanePainter.ERRORICON_ENABLED));
//        JXButton xButtonErrorEnabled = new JXButton("xButtonErrorEnabled", errorEnabledIcon);
//        p.add(xButtonErrorEnabled);
    }
    
    private Component createXButton(String propertyName) {
    	Icon icon = DefaultIcons.getIcon(propertyName);  	
    	return new JXButton(propertyName, icon);
    }
    
}
