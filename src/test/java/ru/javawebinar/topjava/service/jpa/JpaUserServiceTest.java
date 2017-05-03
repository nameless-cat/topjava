package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractJpaUserServiceTest;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaUserServiceTest extends AbstractJpaUserServiceTest
{
    @Override
    public void testSave() throws Exception
    {
        super.testSave();
        setExpectedQueries(5);
    }

    @Override
    public void testDelete() throws Exception
    {
        super.testDelete();
        setExpectedQueries(3);
    }

    @Override
    public void testGet() throws Exception
    {
        super.testGet();
        setExpectedQueries(2);
    }


    @Override
    public void testGetByEmail() throws Exception
    {
        super.testGetByEmail();
        setExpectedQueries(1);
    }

    @Override
    public void testGetAll() throws Exception
    {
        super.testGetAll();
        setExpectedQueries(2);
    }

    @Override
    public void testUpdate() throws Exception
    {
        super.testUpdate();
        setExpectedQueries(5);
    }
}