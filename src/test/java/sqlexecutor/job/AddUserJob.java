package sqlexecutor.job;

import sqlexecutor.Job;
import sqlexecutor.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AddUserJob implements Job<Void> {

    private List<User> users;

    public AddUserJob(User ... users) {
        this.users = Arrays.asList(users);
    }

    public Void execute(Connection connection) throws SQLException {
        PreparedStatement st = connection.prepareStatement("insert into customer (first_name, last_name) values (?, ?)");
        try {
            for (User user : users) {
                addUser(st, user);
            }
        } finally {
            st.close();
        }
        return null;
    }

    private void addUser(PreparedStatement st, User user) throws SQLException {
        st.clearParameters();
        st.setString(1, user.getFirstName());
        st.setString(2, user.getLastName());
        st.executeUpdate();
    }

}
