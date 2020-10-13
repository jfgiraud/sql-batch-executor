package sqlexecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobContainer implements Job<Void> {
    private List<Job<Void>> jobs = new ArrayList<Job<Void>>();

    public void add(Job<Void> job) {
        jobs.add(job);
    }

    public Void execute(Connection connection) throws SQLException {
        for (Job job : jobs) {
            job.execute(connection);
        }
        return null;
    }
}
