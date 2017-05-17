package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.dto.ValidationErrorsDTO;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by wwwtm on 15.05.2017.
 */
public class MealRestControllerTest
        extends AbstractControllerTest
{
    private final String BASE_URL = "/rest/meals";

    @Test
    public void deletingMealMustReturnActualMealsList() throws Exception
    {
        mockMvc.perform(delete(BASE_URL + "/" + MEAL1_ID))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(BASE_URL))
                .andDo(print());
    }

    @Test
    public void deletingMealWithWrongIdMustRender404() throws Exception
    {
        ValidationErrorsDTO validationError = new ValidationErrorsDTO("contextId", "Not found entity with id=" + ADMIN_MEAL_ID);

        mockMvc.perform(delete(BASE_URL + "/" + ADMIN_MEAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ModelMatcher.of(ValidationErrorsDTO.class).contentMatcher(validationError))
                .andDo(print());
    }

    @Test
    public void updatingMealMustRenderUpdatedMealList() throws Exception
    {
        Meal updated = MealTestData.getUpdated();
        mockMvc.perform(put(BASE_URL + "/" + String.valueOf(updated.getId()))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updated)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(BASE_URL))
                .andDo(print());
    }

    @Test
    public void updatingWrongMealMustRender404() throws Exception
    {
        ValidationErrorsDTO validationErrors = new ValidationErrorsDTO()
                .addField("contextId", "Not found entity with id=" + ADMIN_MEAL_ID);

        mockMvc.perform(put(BASE_URL + "/" + String.valueOf(ADMIN_MEAL_ID))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ADMIN_MEAL1)))
                .andExpect(status().isNotFound())
                .andExpect(ModelMatcher.of(ValidationErrorsDTO.class).contentMatcher(validationErrors))
                .andDo(print());
    }

    @Test
    public void updatingMealWithInvalidFieldsMustReturnListOfThatFields() throws Exception
    {
        Meal updated = MealTestData.getUpdated();
        updated.setDescription("");
        updated.setCalories(9);
        updated.setDateTime(null);

        ValidationErrorsDTO validationErrors = new ValidationErrorsDTO()
                .addField("description", "may not be empty")
                .addField("calories", "must be between 10 and 5000")
                .addField("dateTime", "may not be null");

        mockMvc.perform(put(BASE_URL + "/" + MEAL1_ID)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updated)))
                .andExpect(status().isBadRequest())
                .andExpect(ModelMatcher.of(ValidationErrorsDTO.class).contentMatcher(validationErrors))
                .andDo(print());
    }

    @Test
    public void updatingMealWithInconsistentIdMustReturnStatusCode400() throws Exception
    {
        Meal updated = MealTestData.getUpdated();
        updated.setId(25);

        mockMvc.perform(put(BASE_URL + "/" + MEAL1_ID)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updated)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void createMealMustRenderUpdatedMealList() throws Exception
    {
        Meal created = MealTestData.getCreated();

        mockMvc.perform(post(BASE_URL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(created)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(BASE_URL))
                .andDo(print());
    }

    @Test
    public void creatingMealWithInvalidFieldsMustReturnListOfThatFields() throws Exception
    {
        Meal created = MealTestData.getCreated();
        created.setDescription("");
        created.setCalories(9);
        created.setDateTime(null);

        ValidationErrorsDTO validationErrors = new ValidationErrorsDTO()
                .addField("description", "may not be empty")
                .addField("calories", "must be between 10 and 5000")
                .addField("dateTime", "may not be null");

        mockMvc.perform(post(BASE_URL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(created)))
                .andExpect(status().isBadRequest())
                .andExpect(ModelMatcher.of(ValidationErrorsDTO.class).contentMatcher(validationErrors))
                .andDo(print());
    }

    @Test
    public void creatingMealWithNonNullIdMustReturnStatusCode400() throws Exception
    {
        Meal updated = MealTestData.getUpdated();

        mockMvc.perform(post(BASE_URL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updated)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void getMealForCreateMustReturnNewMeal() throws Exception
    {
        mockMvc.perform(get(BASE_URL + "/create"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.calories", is(1000)))
                .andDo(print());
    }

    @Test
    public void getMealForUpdateMustReturnExistedMeal() throws Exception
    {
        mockMvc.perform(get(BASE_URL + "/update/" + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(ModelMatcher.of(Meal.class).contentMatcher(MEAL1))
                .andDo(print());
    }

    @Test
    public void getMealWithWrongIdForUpdateMustReturnStatusCode404() throws Exception
    {
        mockMvc.perform(get(BASE_URL + "/update/1235"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void getMealWithIllegalIdForUpdateMustReturnStatusCode400() throws Exception
    {
        mockMvc.perform(get(BASE_URL + "/update/cReaTe"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void getListUserMealsMustRenderThatList() throws Exception
    {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(ModelMatcher.of(MealWithExceed.class).contentListMatcher(MEALS_WE))
                .andDo(print());
    }

    @Test
    public void getFilterRequestMustRenderFilteredMeals() throws Exception
    {
        mockMvc.perform(get(BASE_URL)
                .param("endDateTime", "2015-05-30T14:00:30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(ModelMatcher.of(MealWithExceed.class).contentListMatcher(Arrays.asList(MEAL_WE2, MEAL_WE1)))
                .andDo(print());
    }

    @Test
    public void getFilterWithIllegalDateParamMustRenderBadRequest() throws Exception
    {
        mockMvc.perform(get(BASE_URL)
                .param("endDateTime", "123"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
