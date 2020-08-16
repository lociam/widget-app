package com.miro.widgetapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widgetapp.controller.InputWidgetRequest;
import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;
import com.miro.widgetapp.repositories.IWidgetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WidgetApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IWidgetRepository widgetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getNotFound() throws Exception {
        mockMvc.perform(get("/widget/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSuccessful() throws Exception {
        Widget w1 = new Widget("id1", 10, 10, Optional.of(1), 100, 150);

        try {
            widgetRepository.insert(w1);
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("Should not throw exception");
        }

        mockMvc.perform(get("/widget/id1"))
                .andExpect(status().isOk())
                .andDo(r -> {
                    Widget response = objectMapper.readValue(r.getResponse().getContentAsString(), Widget.class);
                    Assertions.assertNotNull(response);
                    Assertions.assertNotNull(response.getId());
                    Assertions.assertEquals(10, response.getX());
                    Assertions.assertEquals(10, response.getY());
                    Assertions.assertEquals(1, response.getZIndex().get());
                    Assertions.assertEquals(100, response.getWidth());
                    Assertions.assertEquals(150, response.getHeight());
                });
    }

    @Test
    public void insertWidget() throws Exception {
        InputWidgetRequest input = new InputWidgetRequest(10, 20, 2, 100, 150);

        mockMvc.perform(post("/widget")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    Widget response = objectMapper.readValue(r.getResponse().getContentAsString(), Widget.class);
                    Assertions.assertNotNull(response);
                    Assertions.assertNotNull(response.getId());
                    Assertions.assertEquals(10, response.getX());
                    Assertions.assertEquals(20, response.getY());
                    Assertions.assertEquals(2, response.getZIndex().get());
                    Assertions.assertEquals(100, response.getWidth());
                    Assertions.assertEquals(150, response.getHeight());

                    try {
                        Widget stored = widgetRepository.get(response.getId());
                        Assertions.assertNotNull(stored);
                        Assertions.assertEquals(10, stored.getX());
                        Assertions.assertEquals(20, stored.getY());
                        Assertions.assertEquals(2, stored.getZIndex().get());
                        Assertions.assertEquals(100, stored.getWidth());
                        Assertions.assertEquals(150, stored.getHeight());
                    } catch (WidgetNotFoundException e) {
                        Assertions.fail("Should not throw exception");
                    }
                });
    }

    @Test
    public void updateNotFound() throws Exception {
        InputWidgetRequest input = new InputWidgetRequest(10, 20, 2, 100, 150);

        mockMvc.perform(put("/widget/update-test")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void updateSuccessful() throws Exception {
        Widget w1 = new Widget("id-update", 10, 10, Optional.of(1), 100, 150);
        try {
            widgetRepository.insert(w1);
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("Should not throw exception");
        }

        InputWidgetRequest input = new InputWidgetRequest(30, 20, 3, 200, 350);

        mockMvc.perform(put("/widget/id-update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input))
        ).andExpect(status().isOk())
                .andDo(r -> {
                    Widget response = objectMapper.readValue(r.getResponse().getContentAsString(), Widget.class);
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals("id-update", response.getId());
                    Assertions.assertEquals(30, response.getX());
                    Assertions.assertEquals(20, response.getY());
                    Assertions.assertEquals(3, response.getZIndex().get());
                    Assertions.assertEquals(200, response.getWidth());
                    Assertions.assertEquals(350, response.getHeight());

                    try {
                        Widget stored = widgetRepository.get(response.getId());
                        Assertions.assertNotNull(stored);
                        Assertions.assertEquals(30, stored.getX());
                        Assertions.assertEquals(20, stored.getY());
                        Assertions.assertEquals(3, stored.getZIndex().get());
                        Assertions.assertEquals(200, stored.getWidth());
                        Assertions.assertEquals(350, stored.getHeight());
                    } catch (WidgetNotFoundException e) {
                        Assertions.fail("Should not throw exception");
                    }
                });
        ;
    }

    @Test
    public void getAll() throws Exception {
        Widget w1 = new Widget("all-1", 10, 10, Optional.of(1), 100, 150);
        Widget w2 = new Widget("all-2", 20, 20, Optional.of(2), 200, 250);
        Widget w3 = new Widget("all-3", 30, 30, Optional.of(3), 300, 350);

        try {
            widgetRepository.insert(w1);
            widgetRepository.insert(w2);
            widgetRepository.insert(w3);
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("Should not throw exception");
        }

        mockMvc.perform(get("/widget")
        ).andExpect(status().isOk())
                .andDo(r -> {
                    Widget[] response = objectMapper.readValue(r.getResponse().getContentAsString(), Widget[].class);
                    Assertions.assertEquals(3, response.length);
                    Assertions.assertEquals("all-1", response[0].getId());
                    Assertions.assertEquals("all-2", response[1].getId());
                    Assertions.assertEquals("all-3", response[2].getId());
                });
    }

    @Test
    public void deleteSuccessful() throws Exception {
        Widget w1 = new Widget("all-1", 10, 10, Optional.of(1), 100, 150);
        Widget w2 = new Widget("all-2", 20, 20, Optional.of(2), 200, 250);
        Widget w3 = new Widget("all-3", 30, 30, Optional.of(3), 300, 350);

        try {
            widgetRepository.insert(w1);
            widgetRepository.insert(w2);
            widgetRepository.insert(w3);
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("Should not throw exception");
        }

        mockMvc.perform(delete("/widget/all-1")
        ).andExpect(status().isOk());

        try {
            widgetRepository.get("all-1");
            Assertions.fail("It should throw exception.");
        } catch (WidgetNotFoundException e) {

        }
    }

    @Test
    public void deleteNotFound() throws Exception {
        mockMvc.perform(delete("/widget/all-1")
        ).andExpect(status().isNotFound());
    }
}
