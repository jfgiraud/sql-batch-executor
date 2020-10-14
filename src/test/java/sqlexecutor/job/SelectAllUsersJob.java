package sqlexecutor.job;

import sqlexecutor.SelectJob;
import sqlexecutor.bean.User;

public class SelectAllUsersJob extends SelectJob<User> {

    public SelectAllUsersJob() {
        super("select first_name, last_name from customer", new UserDaoMapper());
    }

}
