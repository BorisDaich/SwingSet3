package org.jxmapviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Project properties.
 *
 * @author Primoz K.
 */
public enum ProjectProperties {

    /**
     * The only instance of this class
     */
    INSTANCE;

    private static final String PROPERTIES_FILE = "project.properties";

    private static final String PROP_VERSION = "version";
    private static final String PROP_NAME = "name";

    private final Properties props = new Properties();

    private String resolveName(String name) {
        if (!name.startsWith("/")) {
            String baseName = ProjectProperties.class.getPackageName();
            if (!baseName.isEmpty()) {
                int len = baseName.length() + 1 + name.length();
                StringBuilder sb = new StringBuilder(len);
                name = sb.append(baseName.replace('.', '/'))
                    .append('/')
                    .append(name)
                    .toString();
            }
        } else {
            name = name.substring(1);
        }
        return name;
    }
    private ProjectProperties() {
    	Logger.getAnonymousLogger().fine("Loading project properties... "+PROPERTIES_FILE+
    		", resolveName:"+resolveName(PROPERTIES_FILE));

        try {
        	Class<?> type = ProjectProperties.class;
        	InputStream is = type.getResourceAsStream(PROPERTIES_FILE);
        	/*
        	 * getResourceAsStream method returns null when the resource is anon-".class" resource 
        	 * in a package that is not open to the caller's module. 
        	 */
//        	Logger.getAnonymousLogger().info("is:"+is);
            if (is != null) {
                props.load(is);
            } else {
            	Logger.getAnonymousLogger().warning("Project properties file not found. Set default values.");
                props.put(PROP_NAME, "JxMapViewer");
                props.put(PROP_VERSION, "1.0");
            }
        }
        catch (IOException e) {
        	Logger.getAnonymousLogger().warning("Unable to read project properties."+ e);
            props.put(PROP_NAME, "JxMapViewer");
            props.put(PROP_VERSION, "1.0");
        }
    }

    /***************************************************************
     ********************* PROPERTIES GETTERS **********************
     ***************************************************************/

    /**
     * @return Project version.
     */
    public String getVersion() {
        return props.getProperty(PROP_VERSION);
    }

    /**
     * @return Project name.
     */
    public String getName() {
        return props.getProperty(PROP_NAME);
    }

}
