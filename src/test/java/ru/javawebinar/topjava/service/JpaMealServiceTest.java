package ru.javawebinar.topjava.service;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by wwwtm on 26.04.2017.
 */
@ActiveProfiles(profiles = Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest
{
    @BeforeClass
    public static void setUp()
    {
        setLOG(LoggerFactory.getLogger(JpaMealServiceTest.class));
    }
}
