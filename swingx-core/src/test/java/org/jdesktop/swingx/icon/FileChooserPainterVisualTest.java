package org.jdesktop.swingx.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.plaf.nimbus.test.MyArrowButtonPainter;
import javax.swing.plaf.nimbus.test.MyFileChooserPainter;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

@SuppressWarnings("serial")
public class FileChooserPainterVisualTest extends JXPanel {

	private static final Logger LOG = Logger.getLogger(FileChooserPainterVisualTest.class.getName());
	
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
		JXFrame frame = new JXFrame("FileChooserPainter Visual Test", true); // exitOnClose
		JXPanel demo = new FileChooserPainterVisualTest(); //frame);
        frame = InteractiveTestCase.wrapInFrame(frame, demo);
		frame.setStartPosition(StartPosition.CenterInScreen);

        //Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }

    private FileChooserPainterVisualTest() {
    	super(new BorderLayout());
//    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
//    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        super.add(p, BorderLayout.CENTER);

		PainterIcon fileIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		fileIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.FILEICON_ENABLED));
		JXButton fileIconButton = new JXButton("fileIcon", fileIcon);
		p.add(fileIconButton);
      
		PainterIcon directoryIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		directoryIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.DIRECTORYICON_ENABLED));
		JXButton directoryIconButton = new JXButton("directoryIcon", directoryIcon);
		p.add(directoryIconButton);

		PainterIcon upFolderIcon = new PainterIcon(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
		upFolderIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.UPFOLDERICON_ENABLED));
		JXButton upFolderIconButton = new JXButton("upFolderIcon", upFolderIcon);
		p.add(upFolderIconButton);

		PainterIcon newFolderIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		newFolderIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.NEWFOLDERICON_ENABLED));
		JXButton newFolderIconButton = new JXButton("newFolderIcon", newFolderIcon);
		p.add(newFolderIconButton);

		PainterIcon hardDriveIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		hardDriveIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.HARDDRIVEICON_ENABLED));
		JXButton hardDriveIconButton = new JXButton("hardDriveIcon", hardDriveIcon);
		p.add(hardDriveIconButton);

		PainterIcon floppyDriveIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		floppyDriveIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.FLOPPYDRIVEICON_ENABLED));
		JXButton floppyDriveIconButton = new JXButton("floppyDriveIcon", floppyDriveIcon);
		p.add(floppyDriveIconButton);

		PainterIcon homeFolderIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		homeFolderIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.HOMEFOLDERICON_ENABLED));
		JXButton homeFolderIconButton = new JXButton("homeFolderIcon", homeFolderIcon);
		p.add(homeFolderIconButton);

		PainterIcon detailsViewIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		detailsViewIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.DETAILSVIEWICON_ENABLED));
		JXButton detailsViewIconButton = new JXButton("detailsViewIcon", detailsViewIcon);
		p.add(detailsViewIconButton);

		PainterIcon listViewIcon = new PainterIcon(MyFileChooserPainter.canvasSize());
		listViewIcon.setPainter(MyFileChooserPainter.factory(MyFileChooserPainter.LISTVIEWICON_ENABLED));
		JXButton listViewIconButton = new JXButton("listViewIcon", listViewIcon);
		p.add(listViewIconButton);
    }
    
    private Component createXButton(String propertyName) {
    	Icon icon = DefaultIcons.getIcon(propertyName);  	
    	return new JXButton(propertyName, icon);
    }
    
}
