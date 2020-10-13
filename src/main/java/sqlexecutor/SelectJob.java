package sqlexecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class SelectJob<T> implements Job<List<T>> {

    private String query;
    private DaoMapper<T> daoMapper;

    public SelectJob(String query, DaoMapper<T> daoMapper) {
        this.query = query;
        this.daoMapper = daoMapper;
    }

    public List<T> execute(Connection connection) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query);
        try {
            ResultSet rs = st.executeQuery();
            List<T> result = new ArrayList<T>();
            while (rs.next()) {
                result.add(daoMapper.map(rs));
            }
            return result;
        } finally {
            st.close();
        }
    }

}
