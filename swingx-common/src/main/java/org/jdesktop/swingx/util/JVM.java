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
package org.jdesktop.swingx.util;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Deals with the different version of the Java Virtual Machine. <br>
 */
public class JVM {

  /**
   * System Property
   */
  public final static String JAVA_VERSION = "java.version";
  
  public final static int JDK1_0 = 1000;
  public final static int JDK1_1 = 1100;
  public final static int JDK1_2 = 1200;
  public final static int JDK1_3 = 1300;
  public final static int JDK1_4 = 1400;
  public final static int JDK1_5 = 1500;
  public final static int JDK1_6 = 1600;
  public final static int JDK1_6N = 1610;
  public final static int JDK1_7 = 1700;
  public final static int JDK1_8 = 1800;
  public final static int JDK_8  = 8000;
  public final static int JDK_9  = 9000;
  public final static int JDK_10 =10000;
  public final static int JDK_11 =11000;
  public final static int JDK_12 =12000;
  public final static int JDK_13 =13000;
  public final static int JDK_14 =14000;
  public final static int JDK_15 =15000;
  public final static int JDK_16 =16000;
  public final static int JDK_17 =17000;
  public final static int JDK_18 =18000;
  public final static int JDK_19 =19000;

  private static JVM current;
  static {
    current = new JVM();
  }

  /**
   * @return the current JVM object
   */
  public static JVM current() {
    return current;
  }

  private int jdkVersion;

  /**
   * Creates a new JVM data from the <code>java.version</code>
   * System property
   *  
   */
  public JVM() {
    this(System.getProperty(JAVA_VERSION));
  }

  public String toString() {
	  return "jdkVersion="+jdkVersion + " "+JAVA_VERSION+"="+System.getProperty(JAVA_VERSION);
  }

  /**
   * Constructor for the OS object
   * 
   * @param p_JavaVersion String indicating version
   */
  public JVM(String p_JavaVersion) {
		if (p_JavaVersion.startsWith("19")) {
			jdkVersion = JDK_19;
		} else if (p_JavaVersion.startsWith("18")) {
			jdkVersion = JDK_18;
		} else if (p_JavaVersion.startsWith("17")) {
			jdkVersion = JDK_17;
		} else if (p_JavaVersion.startsWith("16")) {
			jdkVersion = JDK_16;
		} else if (p_JavaVersion.startsWith("15")) {
			jdkVersion = JDK_15;
		} else if (p_JavaVersion.startsWith("14")) {
			jdkVersion = JDK_14;
		} else if (p_JavaVersion.startsWith("13")) {
			jdkVersion = JDK_13;
		} else if (p_JavaVersion.startsWith("12")) {
			jdkVersion = JDK_12;
		} else if (p_JavaVersion.startsWith("11")) {
			jdkVersion = JDK_11;
		} else if (p_JavaVersion.startsWith("10")) {
			jdkVersion = JDK_10;
		} else if (p_JavaVersion.startsWith("9")) {
			jdkVersion = JDK_9;
		} else if (p_JavaVersion.startsWith("8")) {
			jdkVersion = JDK_8;
		} else if (p_JavaVersion.startsWith("1.8.")) {
			jdkVersion = JDK1_8;
	} else if (p_JavaVersion.startsWith("1.7.")) {
      jdkVersion = JDK1_7;
    } else if (p_JavaVersion.startsWith("1.6.")) {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel".equals(info.getClassName())) {
              jdkVersion = JDK1_6N;
              break;
          }
      }
      
      jdkVersion = jdkVersion == 0 ? JDK1_6 : jdkVersion;
    } else if (p_JavaVersion.startsWith("1.5.")) {
      jdkVersion = JDK1_5;
    } else if (p_JavaVersion.startsWith("1.4.")) {
      jdkVersion = JDK1_4;
    } else if (p_JavaVersion.startsWith("1.3.")) {
      jdkVersion = JDK1_3;
    } else if (p_JavaVersion.startsWith("1.2.")) {
      jdkVersion = JDK1_2;
    } else if (p_JavaVersion.startsWith("1.1.")) {
      jdkVersion = JDK1_1;
    } else if (p_JavaVersion.startsWith("1.0.")) {
      jdkVersion = JDK1_0;
    } else {
      // unknown version, assume 1.3
      jdkVersion = JDK1_3;
    }
  }

  /**
   * check JDK
   * @param p_Version int JDK Version, i.E. <code>JDK_8</code>
   * @return boolean
   */
  public boolean isOrLater(int p_Version) {
    return jdkVersion >= p_Version;
  }

  /**
   * check JDK
   * @return true when jdk is 1.1
   */
  public boolean isOneDotOne() {
    return jdkVersion == JDK1_1;
  }

  /**
   * check JDK
   * @return true when jdk is 1.2
   */
  public boolean isOneDotTwo() {
    return jdkVersion == JDK1_2;
  }

  /**
   * check JDK
   * @return true when jdk is 1.3
   */
  public boolean isOneDotThree() {
    return jdkVersion == JDK1_3;
  }

  /**
   * check JDK
   * @return true when jdk is 1.4
   */
  public boolean isOneDotFour() {
    return jdkVersion == JDK1_4;
  }

  /**
   * check JDK
   * @return true when jdk is 1.5
   */
  public boolean isOneDotFive() {
    return jdkVersion == JDK1_5;
  }

  /**
   * check JDK
   * @return true when jdk is 1.6
   */
  public boolean isOneDotSix() {
    return jdkVersion == JDK1_6 || isOneDotSixUpdateN();
  }

	/**
	 * Determines if the version of JDK1_6 has Nimbus Look and Feel installed.
	 * 
	 * @return {@code true} if Nimbus is available and the version is 1.6;
	 *         {@code false} otherwise
	 */
	public boolean isOneDotSixUpdateN() {
		return jdkVersion == JDK1_6N;
	}
  
  /**
   * check JDK
   * @return true when jdk is 1.7
   */
  public boolean isOneDotSeven() {
      return jdkVersion == JDK1_7;
  }

}