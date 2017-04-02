package com.opinta.temp;

import org.hsqldb.util.DatabaseManagerSwing;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Profile("dev")
public class StartHsqlDbManager {
    @PostConstruct
    public void startDBManager() {
//        DatabaseManagerSwing.main(new String[]{"--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", "sa"});
    }
}
