package ru.javawebinar.topjava;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wwwtm on 27.04.2017.
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(Profiles.POSTGRES_DB)
@Ignore("Abstract class")
public abstract class ServiceTest
{
    static
    {
        // needed only for java.util.logging (postgres driver)
        SLF4JBridgeHandler.install();
    }

}
