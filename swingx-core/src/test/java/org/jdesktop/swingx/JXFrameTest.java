/*
 * Created on 04.10.2010
 *
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.icon.PauseIcon;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JXFrameTest extends InteractiveTestCase {

    private static final Logger LOG = Logger.getLogger(JXFrameTest.class.getName());

	public static void main(String args[]) {
//		setSystemLF(true);
//      Locale.setDefault(new Locale("es"));
		JXFrameTest test = new JXFrameTest();
		try {
			test.runInteractiveTests();
//            test.runInteractiveTests("interactive.*Compare.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

    @Test
    public void testGraphicsConfig() {
        // This test will not work in a headless configuration.
        if(GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run ui test - headless environment");
            return;
        }
        JXFrame compare = new JXFrame();
        GraphicsConfiguration gc = compare.getGraphicsConfiguration();
        LOG.info("GraphicsConfiguration gc:"+gc);
        JXFrame frame = new JXFrame(gc);
        assertEquals(gc, frame.getGraphicsConfiguration());
        assertEquals(compare.getDefaultCloseOperation(), frame.getDefaultCloseOperation());
        assertEquals(compare.getTitle(), frame.getTitle());
    }
    
    /**
     * interactiveMultipleFrames shows control frame (gossip)
     * <p>
     * - the gossip RootFrame is a Subclass of WindowFrame;  
     * 		it has a JMenuBar at top to control the L&F and an empty JXStatusBar at bottom 
     * 		and contains a very simple frame manager to create new WindowFrame's
     * - to keep it simple all WindowFrame's including gossip contains same JXPanel in BorderLayout
     * 		two Buttons at WEST and EAST, and empty CENTER
     * 		the WEST button can be used to create new frames, the action is done by RootFrame's frame manager
     * <p>
     * the pause button stops the frame manager to create new windows
     * <p>
     * closing behavior :
     * - closing gossip RootFrame do EXIT_ON_CLOSE - closes all Windows
     * - closing other WindowFrame closes only this window
     */
    public void interactiveMultipleFrames() {
        WindowFrame gossip = new RootFrame(); // RootFrame contains a simple frame manager
        @SuppressWarnings("unused")
		JXStatusBar statusBar = gossip.getStatusBar(); // just to paint it
//        gossip.pack(); // auto or fix:
        gossip.setSize(600, 200);
    	gossip.setVisible(true);
    }

    @SuppressWarnings("serial")
	public class WindowFrame extends JXFrame {
    	private static int windowCounter = 0; // für windowNo, wird pro ctor hochgezählt
    	private int windowNo;
    	RootFrame rootFrame; // mit FrameManager
    	public RootFrame getRootFrame() {
    		return rootFrame;
    	}
    	
    	private int window_ID;
    	JXPanel jPanel = new JXPanel(new BorderLayout());
    	AbstractAction addFrameAction;
    	/*
    	 * window_ID==-1 is used for RootFrame
    	 */
    	WindowFrame(String title, RootFrame rootFrame, int window_ID, Object object) {
    		super(title
    			, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
    			, window_ID==-1 ? true : false // exitOnClose
    			);
    		windowCounter++;
    		this.windowNo = windowCounter-1;
    		
    		this.rootFrame = rootFrame;
    		this.window_ID = window_ID;
    		
    		// bei RootFrame: setJMenuBar:
    		if(this instanceof RootFrame) {
    			LOG.info("\nthis:"+this);
//    			JMenu jMenu = createPlafMenu(); // aus InteractiveTestCase
//    			JMenuBar jMenuBar = createAndFillMenuBar(null);
    			setJMenuBar(createAndFillMenuBar(null));
    		}
    		
    		addFrameAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	//int frameNumber = rootFrame.frames.size(); - nicht aus frames.size (!), 
                	// denn frame n kann noch existeren, aber frames.size=n sein, wenn ein "ältere" geschlossen wurde
                	int frameNumber = windowCounter;
                	LOG.info("makeFrame #"+frameNumber+" ... rootFrame:"+getRootFrame());
                	WindowFrame frame = getRootFrame().makeFrame(frameNumber, getRootFrame(), 1, null);
                	if(frame!=null) {
                    	frame.pack();
                    	frame.setVisible(true);
                	}
                }
            };
            addFrameAction.putValue(Action.NAME, "addFrame");
        	RadianceIcon play = PlayIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
        	play.setColorFilter(color -> Color.GREEN);
            addFrameAction.putValue(Action.LARGE_ICON_KEY, play);
            JXButton addFrameBtn = new JXButton(addFrameAction);

    		AbstractAction disableFrameMgrAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	LOG.info("disable FrameMgr");
                	if(getRootFrame().enable) {
                    	getRootFrame().enable = false;
                    	// TODO for all frames:
                        //addFrameAction.putValue(Action.LARGE_ICON_KEY, new StopIcon(SizingConstants.ACTION_ICON, Color.RED));
                	}
                }
            };
            disableFrameMgrAction.putValue(Action.NAME, "disableFrameMgr");
        	RadianceIcon pauseIcon = PauseIcon.of(RadianceIcon.LAUNCHER_ICON, RadianceIcon.LAUNCHER_ICON);
        	pauseIcon.setColorFilter(color -> Color.MAGENTA);
            disableFrameMgrAction.putValue(Action.LARGE_ICON_KEY, pauseIcon);
        	JXButton pause = new JXButton(disableFrameMgrAction);

    		JToolBar toolbar = new JToolBar();
    		getRootPaneExt().setToolBar(toolbar);
            AbstractButton addFrameTbBtn = addActionToToolBar(this, addFrameAction);
            LOG.info("Toolbat Button addFrameAction:"+addFrameTbBtn);
            
//    		super.setSize(600, 200); ==> pack()
    		getContentPane().add(jPanel);
    		
        	jPanel.add(addFrameBtn, BorderLayout.WEST);

        	jPanel.add(new JXLabel("empty", SwingConstants.CENTER), BorderLayout.CENTER);
        	
        	jPanel.add(pause, BorderLayout.EAST);

//    		jPanel.add(statusBar, BorderLayout.PAGE_END); // == SOUTH
//    		
//    		addWindowListener(this); // wg. - JFrame.DISPOSE_ON_CLOSE
    	}
    	WindowFrame(String title) { // für RootFrame
    		this(title, null, -1, null);
    	}

    	// aus InteractiveTestCase.createAndFillMenuBar
        public AbstractButton addActionToToolBar(JXFrame frame, Action action) {
            JToolBar toolbar = frame.getRootPaneExt().getToolBar();
            if (toolbar != null) {
                AbstractButton button = toolbar.add(action);
                button.setFocusable(false);
                return button;
            }
            return null;
        }

        /**
         * Creates, fills and returns a JMenuBar. 
         * 
         * @param component the component that was added to the frame.
         * @return a menu bar filled with actions as defined in createAndAddMenus
         * 
         * @see #createAndAddMenus
         */
    	// aus InteractiveTestCase.createAndFillMenuBar
        protected JMenuBar createAndFillMenuBar(JComponent component) {
            JMenuBar bar = new JMenuBar();
            createAndAddMenus(bar, component);
            
            return bar;
        }

        /**
         * Returns the <code>JXFrame</code>'s status bar. Lazily creates and 
         * sets an instance if necessary.
         * @param frame the target frame
         * @return the frame's statusbar
         */
    	// aus InteractiveTestCase.getStatusBar
        static JXStatusBar getStatusBar(JXFrame frame) {
            JXStatusBar statusBar = frame.getRootPaneExt().getStatusBar();
            if (statusBar == null) {
                statusBar = new JXStatusBar();
                frame.setStatusBar(statusBar);
            }
            return statusBar;
        }
        public JXStatusBar getStatusBar() {
        	return getStatusBar(this);
        }
    }
    
    @SuppressWarnings("serial")
    public class RootFrame extends WindowFrame {
    	private static final String TITLE = "Gossip";
    	public RootFrame() {
			super(TITLE);
			super.rootFrame = this;
			// TODO ...
			frames = new ArrayList<JXFrame>();
			frames.add(this);
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			LOG.info(TITLE+" frame ctor. frames#="+frames.size() + " super.rootFrame:"+super.rootFrame);
		}
		// simple frame manager
    	List<JXFrame> frames;
    	boolean enable = true;
    	boolean remove(JXFrame frame) {
    		return frames.remove(frame);
    	}
    	WindowFrame makeFrame(int frameNumber, RootFrame rootFrame, int window_ID, Object object) {
    		if(enable) {
        		WindowFrame frame = new WindowFrame("Frame number " + frameNumber, rootFrame, window_ID, object);
        		frames.add(frame);
        		return frame;
    		}
    		return null;
    	}
    	// ...
    	// <<<
    }
}
