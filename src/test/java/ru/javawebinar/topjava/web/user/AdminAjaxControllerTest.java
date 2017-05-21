package ru.javawebinar.topjava.web.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by wwwtm on 21.05.2017.
 */
public class AdminAjaxControllerTest
        extends AbstractControllerTest
{
    private static final String BASE_URL = AdminAjaxController.BASE_URL;

    @Autowired
    private UserService service;

    @Test
    public void putEnableDisableFlagMustChangeUserActiveStatus() throws Exception
    {
        mockMvc.perform(put(BASE_URL)
                .param("enabled", "false")
                .param("id", String.valueOf(USER_ID)))
                .andDo(print())
                .andExpect(status().isOk());

        User user = service.get(USER_ID);

        assertEquals(false, user.isEnabled());
    }

}