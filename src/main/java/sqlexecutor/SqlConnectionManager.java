package sqlexecutor;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnectionManager {

    public SqlConnectionManager(String driverName) throws ClassNotFoundException {
        Class.forName(driverName);
    }

    public static Connection getConnection(String sqlUrl) throws SQLException {
        if (sqlUrl.startsWith("java:comp/env/")) {
            DataSource ds = null;
            Context ctx = null;
            try {
                ctx = new InitialContext();
                ds = (DataSource) ctx.lookup(sqlUrl);
                if (ds != null) {
                    return ds.getConnection();
                }
                throw new SQLException("getConnection: no connection!");
            } catch (NamingException e) {
                throw new SQLException("getConnection: " + e.getMessage());
            }
        } else {
            return DriverManager.getConnection(sqlUrl);
        }
    }

}
