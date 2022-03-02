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
package org.jdesktop.swingx.auth;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;

import org.jdesktop.beans.JavaBean;

/**
 * A login service for connecting to SQL based databases via JDBC
 *
 * @author rbair
 */
@JavaBean
public class JDBCLoginService extends LoginService {
	
    private static final Logger LOG = Logger.getLogger(JDBCLoginService.class.getName());
    
    /**
     * The connection to the database
     */
    private Connection conn;
    /**
     * If used, defines the JNDI context from which to get a connection to
     * the data base
     */
    private String jndiContext;
    /**
     * When using the DriverManager to connect to the database, this specifies
     * any additional properties to use when connecting.
     */
    private Properties properties;
    
    /**
     * Create a new JDBCLoginService and initializes it to connect to a database using the given params.
     * @param driver DB driver class name
     * @param url DB url
     */
    public JDBCLoginService(String driver, String url) {
        super(url);
        loadDriver(driver);
        this.setUrl(url);
    }
    
    /**
     * Create a new JDBCLoginService and initializes it to connect to a database using the given params.
     * @param driver DB driver class name
     * @param url DB url
     * @param props Properties
     */
    public JDBCLoginService(String driver, String url, Properties props) {
        super(url);
        loadDriver(driver);
        this.setUrl(url);
        this.setProperties(props);
    }
    
    /*
    
interface java.sql.Driver:
    Connection connect(String url, java.util.Properties info) throws SQLException;
    boolean acceptsURL(String url) throws SQLException;
    DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException;
    int getMajorVersion();
    int getMinorVersion();
    boolean jdbcCompliant();
    public Logger getParentLogger() throws SQLFeatureNotSupportedException;

Example implementation
org.postgresql.Driver: is NOT jdbcCompliant
  @Override
  public @Nullable Connection connect(String url, @Nullable Properties info) throws SQLException {
  @Override
  public boolean acceptsURL(String url) {
  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {

     */
    private void loadDriver(String driverName) {
        try {
			Class<?> driverType = Class.forName(driverName);
			Driver driver = (Driver)driverType.getDeclaredConstructor().newInstance();
			LOG.info(driverName + " Version "+driver.getMajorVersion()+"."+driver.getMinorVersion()
				+ (driver.jdbcCompliant() ? " - jdbcCompliant" : "")
				);
			DriverManager.registerDriver(driver); // Throws:SQLException - if a database access error occurs
        } catch (Exception e) {
            LOG.log(Level.WARNING, "The driver passed to the " +
                    "JDBCLoginService constructor could not be loaded. " +
                    "This may be due to the driver not being on the classpath", e);
        }
    }

    /**
     * Create a new JDBCLoginService and initializes it to connect to a database using the given params.
     * @param jndiContext String
     */
    public JDBCLoginService(String jndiContext) {
        super(jndiContext);
        this.jndiContext = jndiContext;
    }
    
    /**
     * Default JavaBean constructor
     */
    public JDBCLoginService() {
        super();
    }
    
    /**
     * @return the JDBC connection url
     */
    public String getUrl() {
        return getServer();
    }

    /**
     * @param url set the JDBC connection url
     */
    public void setUrl(String url) {
        String old = getUrl();
        setServer(url);
        firePropertyChange("url", old, getUrl());
    }

    /**
     * @return JDBC connection properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties miscellaneous JDBC properties to use when connecting
     *        to the database via the JDBC driver
     */
    public void setProperties(Properties properties) {
        Properties old = getProperties();
        this.properties = properties;
        firePropertyChange("properties", old, getProperties());
    }
    
    /**
     * 
     * @return Connection
     */
    public Connection getConnection() {
        return conn;
    }
    
    /**
     * 
     * @param conn Connection
     */
    public void setConnection(Connection conn) {
        Connection old = getConnection();
        this.conn = conn;
        firePropertyChange("connection", old, getConnection());
    }
    
    /**
     * Attempts to get a JDBC Connection from a JNDI javax.sql.DataSource, using
     * that connection for interacting with the database.
     * @throws Exception
     */
    private void connectByJNDI(String userName, char[] password) throws Exception {
        InitialContext ctx = new InitialContext();
        javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(jndiContext);
        conn = ds.getConnection(userName, new String(password));
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
    }
    
    /**
     * Attempts to get a JDBC Connection from a DriverManager. If properties
     * is not null, it tries to connect with those properties. If that fails,
     * it then attempts to connect with a user name and password. If that fails,
     * it attempts to connect without any credentials at all.
     * <p>
     * If, on the other hand, properties is null, it first attempts to connect
     * with a username and password. Failing that, it tries to connect without
     * any credentials at all.
     * @throws Exception
     */
    private void connectByDriverManager(String userName, char[] password) throws Exception {
    	String url = getUrl();
        if (getProperties() != null) {
            try {
            	Driver driver = DriverManager.getDriver(url);
            	LOG.info("driver.acceptsURL("+url+"):"+driver.acceptsURL(url) + " NOT used: userName="+userName);
                conn = DriverManager.getConnection(url, getProperties());
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } catch (Exception e) {
                try {
                    conn = DriverManager.getConnection(url, userName, new String(password));
                    conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                } catch (Exception ex) {
                    conn = DriverManager.getConnection(url);
                    conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                }
            }
        } else {
            try {
            	Driver driver = DriverManager.getDriver(url);
            	LOG.info("driver.acceptsURL("+url+"):"+driver.acceptsURL(url));
                conn = DriverManager.getConnection(url, userName, new String(password));
            } catch (SQLException e) {
            	String msg = e.getMessage();
            	int code = e.getErrorCode();
            	String state = e.getSQLState();
            	String lmsg = e.getLocalizedMessage();
                LOG.warning("code="+code + ",state=" + state + ", "+msg);
                LOG.log(Level.WARNING, "Connection with properties failed. " + lmsg);
                throw e;
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Connection with properties failed. " +
                                "Trying to connect without.", e);
                //try to connect without using the userName and password
                conn = DriverManager.getConnection(url);
            }
        }
    }

    /**
     * @param name user name
     * @param password user password
     * @param server Must be either a valid JDBC URL for the type of JDBC driver you are using,
     * or must be a valid JNDIContext from which to get the database connection
     */
    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {
        //try to form a connection. If it works, conn will not be null
        //if the jndiContext is not null, then try to get the DataSource to use
        //from jndi
        if (jndiContext != null) {
            try {
                connectByJNDI(name, password);
            } catch (Exception e) {
                try {
                    connectByDriverManager(name, password);
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Login failed", ex);
                    //login failed
                    return false;
                }
            }
        } else {
            try {
                connectByDriverManager(name, password);
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "", ex);
                return false;
            }
        }
        return true;
    }
}
