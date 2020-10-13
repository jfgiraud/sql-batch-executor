package sqlexecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class BatchExecutor {

    private boolean inTransaction;
    private String databaseUrl;

    public BatchExecutor(String databaseUrl, boolean inTransaction) {
        this.inTransaction = inTransaction;
        this.databaseUrl = databaseUrl;
    }

    private Connection open() throws SQLException {
        Connection connection = SqlConnectionManager.getConnection(databaseUrl);
        connection.setAutoCommit(!inTransaction);
        return connection;
    }

    public void executeUpdate(Job job) throws Exception {
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

    public <T> List<T> executeQuery(Job<List<T>> job) throws SQLException {
        Connection connection = open();
        try {
            return job.execute(connection);
        } finally {
            connection.close();
        }
    }
}
