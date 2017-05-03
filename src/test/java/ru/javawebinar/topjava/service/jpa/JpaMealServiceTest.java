package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractJpaMealServiceTest;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaMealServiceTest extends AbstractJpaMealServiceTest
{
    @Override
    public void testDelete() throws Exception
    {
        super.testDelete();
        setExpectedQueries(2);
    }

    @Override
    public void testDeleteNotFound() throws Exception
    {
        super.testDeleteNotFound();
    }

    @Override
    public void testSave() throws Exception
    {
        super.testSave();
        setExpectedQueries(3);
    }

    @Override
    public void testGet() throws Exception
    {
        super.testGet();
        setExpectedQueries(1);
    }

    @Override
    public void testGetNotFound() throws Exception
    {
        super.testGetNotFound();
    }

    @Override
    public void testUpdate() throws Exception
    {
        super.testUpdate();
        setExpectedQueries(3);
    }

    @Override
    public void testUpdateNotFound() throws Exception
    {
        super.testUpdateNotFound();
    }

    @Override
    public void testGetAll() throws Exception
    {
        super.testGetAll();
        setExpectedQueries(1);
    }

    @Override
    public void testGetBetween() throws Exception
    {
        super.testGetBetween();
        setExpectedQueries(1);
    }
}