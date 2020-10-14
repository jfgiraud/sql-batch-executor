package sqlexecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SelectJob<T> implements Job<List<T>> {

    private String query;
    private List<Object> parameters;
    private DaoMapper<T> daoMapper;

    public SelectJob(String query, DaoMapper<T> daoMapper) {
        this(query, null, daoMapper);
    }

    public SelectJob(String query, List<Object> parameters, DaoMapper<T> daoMapper) {
        this.query = query;
        this.parameters = parameters;
        this.daoMapper = daoMapper;
    }

    public List<T> execute(Connection connection) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query);
        if (parameters != null && !parameters.isEmpty()) {
            for (int i=0; i<parameters.size(); i++) {
                st.setObject(i+1, parameters.get(i));
            }
        }
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
