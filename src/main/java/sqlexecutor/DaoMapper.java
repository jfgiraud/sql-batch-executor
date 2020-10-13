package sqlexecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DaoMapper<T> {

    T map(ResultSet rs) throws SQLException;

}
