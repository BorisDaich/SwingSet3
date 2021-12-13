package org.jdesktop.swingx.demos.loginpane;

import java.util.logging.Logger;

import org.jdesktop.swingx.auth.JDBCLoginService;

/**
 * A {@code LoginService} that implements a login to a database (postgresql).
 * 
 * TODO: add another db driver
 * - implement the getUserRoles()
 * 
 * @author EUGen hb https://github.com/homebeaver
 */
public class DBLoginService extends JDBCLoginService {
	
    private Logger LOG = Logger.getLogger(DBLoginService.class.getName());
    public static final String DRIVER = "org.postgresql.Driver";

    /**
     * Constructs the default service.
     */
    /*
     * in super:
    public JDBCLoginService(String jndiContext) // Java Naming and Directory Interface
    public JDBCLoginService(String driver, String url, Properties props) 
    public JDBCLoginService(String driver, String url) {
     */
    public DBLoginService(String driver, String url) {
        super(driver, url);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {
    	LOG.info(name + " AT " + server);
    	boolean ret = super.authenticate(name, password, server);
    	if(ret) {
        	LOG.info(name + " conn=" + super.getConnection());
        	// Properties getClientInfo() throws SQLException;
        	// String getSchema() throws SQLException;
    	} else {
        	LOG.info(name + " ret=" + ret);
    	}
    	return ret;
    }

}
