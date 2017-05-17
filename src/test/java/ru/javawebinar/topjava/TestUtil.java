package ru.javawebinar.topjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.web.json.JacksonObjectMapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class TestUtil {

    public static ResultActions print(ResultActions action) throws UnsupportedEncodingException {
        System.out.println(getContent(action));
        return action;
    }

    public static String getContent(ResultActions action) throws UnsupportedEncodingException {
        return action.andReturn().getResponse().getContentAsString();
    }

    public static final MediaType APPLICATION_JSON_UTF8 =  new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object o) throws JsonProcessingException
    {
        return JacksonObjectMapper.getMapper().writeValueAsBytes(o);
    }

}
