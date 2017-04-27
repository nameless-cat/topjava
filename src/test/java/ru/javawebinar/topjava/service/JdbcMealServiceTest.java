package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by wwwtm on 26.04.2017.
 */
@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest
{
    @BeforeClass
    public static void setUp()
    {
        setLogClass(JdbcMealServiceTest.class);
        clearResult();
    }

    @AfterClass
    public static void result() {
        printResult();
    }
}
