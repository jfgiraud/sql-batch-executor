package sqlexecutor;

import junit.framework.TestCase;
import sqlexecutor.bean.User;
import sqlexecutor.job.AddUserJob;
import sqlexecutor.job.SelectAllUsersJob;
import sqlexecutor.job.SelectUsersByNameJob;

import java.sql.*;
import java.util.List;

public class BatchExecutorTest extends TestCase {

    public static final String TEST_DB_URL = "jdbc:hsqldb:file:src/test/resources/testdb?user=SA&password=password";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Class.forName("org.hsqldb.jdbcDriver");
        Connection sqlConnection = DriverManager.getConnection(TEST_DB_URL);
        createTables(sqlConnection);
    }

    private void createTables(Connection sqlConnection) throws SQLException {
        PreparedStatement st = sqlConnection.prepareStatement("drop table customer if exists");
        st.executeUpdate();
        st = sqlConnection.prepareStatement("create table customer (first_name varchar(30) not null, last_name varchar(30) not null, primary key(first_name, last_name))");
        st.executeUpdate();
    }

    public void testInTransactionOk() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);

        AddUserJob job = new AddUserJob(createUser("john", "doe"), createUser("jane", "doe"));

        sqlExecutor.update(job);

        assertEquals(2, count());
    }

    public void testInTransactionOkInContainer() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);

        JobContainer jobContainer = new JobContainer();
        jobContainer.add(new AddUserJob(createUser("john", "doe")));
        jobContainer.add(new AddUserJob(createUser("jane", "doe")));

        sqlExecutor.update(jobContainer);

        assertEquals(2, count());
    }

    private User createUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public void testInTransactionKo() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);

        try {
            AddUserJob job = new AddUserJob(
                    createUser("john", "wayne"),
                    createUser("john", "wayne")
            );

            sqlExecutor.update(job);

            fail("An exception must be raised");
        } catch (Exception e) {
        }
        assertEquals(0, count());
    }

    public void testSelectAll() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);

        AddUserJob job = new AddUserJob(createUser("john", "doe"), createUser("jane", "doe"));

        sqlExecutor.update(job);

        List<User> result = sqlExecutor.select(new SelectAllUsersJob());

        assertEquals(2, result.size());
    }

    public void testSelect() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);
        AddUserJob job = new AddUserJob(
                createUser("john", "doe"),
                createUser("jane", "doe"),
                createUser("paul", "personne")
        );
        sqlExecutor.update(job);

        List<User> result = sqlExecutor.select(new SelectUsersByNameJob("personne"));

        assertEquals(1, result.size());

    }

    public void testSelect1() throws Exception {
        SqlBatchExecutor sqlExecutor = new SqlBatchExecutor(TEST_DB_URL, true);
        AddUserJob job = new AddUserJob(
                createUser("john", "doe"),
                createUser("jane", "doe"),
                createUser("paul", "personne")
        );
        sqlExecutor.update(job);

        User result = sqlExecutor.select1(new SelectUsersByNameJob("personne"));

        assertEquals("paul", result.getFirstName());
    }

    private int count() throws SQLException {
        Connection sqlConnection = DriverManager.getConnection(TEST_DB_URL);
        PreparedStatement st = sqlConnection.prepareStatement("select count(*) as n from customer");
        ResultSet r = st.executeQuery();
        r.next();
        return r.getInt("n");
    }

}
