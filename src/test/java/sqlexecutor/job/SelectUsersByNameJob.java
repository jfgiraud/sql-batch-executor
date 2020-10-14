package sqlexecutor.job;

import sqlexecutor.SelectJob;
import sqlexecutor.bean.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SelectUsersByNameJob extends SelectJob<User> {

    public SelectUsersByNameJob(final String name) {
        super("select first_name, last_name from customer where last_name=?",
                new ArrayList<Object>() {{ add(name); }},
                new UserDaoMapper()
        );
    }

}
