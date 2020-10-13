package sqlexecutor.job;

import sqlexecutor.DaoMapper;
import sqlexecutor.bean.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoMapper implements DaoMapper<User> {

    public User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        return user;
    }

}
