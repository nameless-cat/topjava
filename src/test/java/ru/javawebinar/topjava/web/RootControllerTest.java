package ru.javawebinar.topjava.web;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEALS_WE;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

public class RootControllerTest
        extends AbstractControllerTest
{

    @Test
    public void testUsers() throws Exception
    {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    public void redirectToMealsAfterSelectingUser() throws Exception
    {
        mockMvc.perform(post("/users")
                .param("userId", String.valueOf(USER_ID)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:meals"))
                .andExpect(redirectedUrl("meals"))
                .andDo(print());
    }

    @Test
    public void renderMealsOfAuthorizedUser() throws Exception
    {
        mockMvc.perform(get("/meals"))
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", MEALS_WE))
                .andDo(print());
    }

    @Test
    public void returnIndexPageIfRootRequest() throws Exception
    {
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}