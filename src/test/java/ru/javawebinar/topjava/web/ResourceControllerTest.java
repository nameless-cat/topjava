package ru.javawebinar.topjava.web;

import org.junit.Test;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by wwwtm on 15.05.2017.
 */
public class ResourceControllerTest
        extends AbstractControllerTest
{
    @Test
    public void requestForCssMustReturnCorrectStatusAndContentType() throws Exception
    {

        mockMvc.perform(get("/resources/css/style.css")
                .accept("*/*"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType("text", "css")))
                .andExpect(header().string("Content-Type", "text/css;charset=UTF-8"))
                .andDo(print());
    }
}
