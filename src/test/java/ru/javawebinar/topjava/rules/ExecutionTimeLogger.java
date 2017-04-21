package ru.javawebinar.topjava.rules;

import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wwwtm on 20.04.2017.
 */
public class ExecutionTimeLogger extends TestWatcher
{
    private final Logger log;

    private final Map<String, Integer> statistics;

    private long startTime;

    public ExecutionTimeLogger(Logger log, Map<String, Integer> map)
    {
        this.log = log;
        statistics = map;
    }

    @Override
    protected void starting(Description description)
    {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void finished(Description description)
    {

        int evalTime = (int) ((System.currentTimeMillis() - startTime));
        log.debug("Test method {} : evaluation time {} ms",
                description.getMethodName(),
                evalTime);

        statistics.put(description.getMethodName(), evalTime);
    }
}
