package org.jdesktop.beans;

/**
 * Used with the setEnumerationValues method to specify enumerated values for
 * properties
 */
public final class EnumerationValue {
	
    private String name;
    private Object value;
    private String javaInitializationString;
    
    /**
     * ctor
     * @param name String
     * @param value Object
     * @param javaInitString String
     */
    public EnumerationValue(String name, Object value, String javaInitString) {
        this.name = name;
        this.value = value;
        this.javaInitializationString = javaInitString;
    }
    
    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * 
     * @return value object 
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * 
     * @return javaInitializationString
     */
    public String getJavaInitializationString() {
        return javaInitializationString;
    }
}