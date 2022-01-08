/*
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.border;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.TestUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MatteBorderExtUnitTest extends junit.framework.TestCase {
	
//	private static final Logger LOG = Logger.getLogger(MatteBorderExtUnitTest.class.getName());

	public MatteBorderExtUnitTest() {
		super("MatteBorderExt unit test");
	}

    // XXX placeholder
    @Test
    public void testDummy() { }

    public static void main(String[] args) {
        try {
	    //	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {

        }

        final TestCase[] testCases = createTestCases();
        if (testCases.length > 0) {
            // Automatically exit after last window is closed.
            testCases[testCases.length - 1].frame.setDefaultCloseOperation(
            	JFrame.EXIT_ON_CLOSE);

            Point location = testCases[0].frame.getLocation();

            for (int i = testCases.length - 1; i >= 0; i--) {
                location.translate(30, 30); // stagger frames
                testCases[i].frame.setTitle("MatteBorderExt Unit Test " + (i +
								      1));
                testCases[i].frame.setLocation(location);
                testCases[i].frame.setVisible(true);
            }
        }
    }

    /**
     * For unit testing only
     * @return test cases to be run in sequence
     */
	private static TestCase[] createTestCases() {
		final TestCase[] testCases = new TestCase[] {

			new TestCase() { // 1
				@Override
				public JComponent define() {
					Class<?> klass = MatteBorderExtUnitTest.class;
					ArrayList<Icon> iconList = new ArrayList<Icon>();
					String dir = "resources/images/";
					try {
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellTopLeft.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellTop.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellTopRight.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellRight.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellBottomRight.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellBottom.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellBottomLeft.gif").readAllBytes()));
						iconList.add(new ImageIcon(TestUtilities.getResourceAsStream(klass, dir+"wellLeft.gif").readAllBytes()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					JPanel panel = new JPanel();
					panel.setBackground(Color.white);
					Icon[] icons = new Icon[iconList.size()];
					icons = iconList.toArray(icons);
					panel.setBorder(new MatteBorderExt(14, 14, 14, 14, icons));
					return panel;
				}
			},

		};
		return testCases;
	}

	private static abstract class TestCase {

		public TestCase() {
			this.frame = wrap(define());
		}

		public abstract JComponent define();

		public JFrame wrap(JComponent component) {
			this.component = component;
			final JFrame frame = new JFrame();
			frame.getContentPane().add(component);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			return frame;
		}

		@SuppressWarnings("unused")
		public JComponent component;
		public final JFrame frame;

	}

}
