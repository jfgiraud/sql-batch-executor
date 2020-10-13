package sqlexecutor;

import java.sql.Connection;
import java.sql.SQLException;

public interface Job<T> {

    T execute(Connection connection) throws SQLException;

}
