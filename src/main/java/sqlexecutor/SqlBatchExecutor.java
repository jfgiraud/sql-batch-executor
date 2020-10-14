package sqlexecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class SqlBatchExecutor {

    private boolean inTransaction;
    private String databaseUrl;

    public SqlBatchExecutor(String databaseUrl, boolean inTransaction) {
        this.inTransaction = inTransaction;
        this.databaseUrl = databaseUrl;
    }

    private Connection open() throws SQLException {
        Connection connection = SqlConnectionManager.getConnection(databaseUrl);
        connection.setAutoCommit(!inTransaction);
        return connection;
    }

    public void update(Job job) throws Exception {
        Connection connection = open();
        try {
            job.execute(connection);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

    public <T> List<T> select(SelectJob<T> job) throws SQLException {
        Connection connection = open();
        try {
            return job.execute(connection);
        } finally {
            connection.close();
        }
    }

    public <T> T select1(SelectJob<T> job) throws SQLException {
        Connection connection = open();
        try {
            List<T> l = job.execute(connection);
            if (l.isEmpty())
                return null;
            return l.get(0);
        } finally {
            connection.close();
        }
    }
}
