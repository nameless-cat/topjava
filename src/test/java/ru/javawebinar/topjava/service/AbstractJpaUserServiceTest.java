package ru.javawebinar.topjava.service;

import net.ttddyy.dsproxy.QueryCountHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.testng.Assert;

/**
 * Created by wwwtm on 03.05.2017.
 */
@Ignore("Abstract class")
public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest
{
    private int expectedQueries;

    public int getExpectedQueries()
    {
        return expectedQueries;
    }

    public void setExpectedQueries(int expectedQueries)
    {
        this.expectedQueries = expectedQueries;
    }

    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        QueryCountHolder.clear();
        setExpectedQueries(-1);
    }

    @After
    public void queryCount()
    {
        if (getExpectedQueries() != -1)
            Assert.assertEquals(QueryCountHolder.get("ProxyDS").getTotal(), getExpectedQueries());
    }
}
