package sqlexecutor.job;

import sqlexecutor.SelectJob;
import sqlexecutor.bean.User;

public class SelectUsersJob extends SelectJob<User> {

    public SelectUsersJob() {
        super("select first_name, last_name from customer", new UserDaoMapper());
    }

}
