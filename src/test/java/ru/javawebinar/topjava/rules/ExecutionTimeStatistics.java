package ru.javawebinar.topjava.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Map;

/**
 * Created by wwwtm on 20.04.2017.
 */
public class ExecutionTimeStatistics
        implements TestRule
{
    public Statement apply(Statement base, Description description)
    {
        return statement(base);
    }

    private Statement statement(final Statement base)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                before();
                try
                {
                    base.evaluate();
                } finally
                {
                    after();
                }
            }
        };
    }

    protected void before() throws Throwable
    {
        // do nothing
    }

    protected void after()
    {
    }
}
