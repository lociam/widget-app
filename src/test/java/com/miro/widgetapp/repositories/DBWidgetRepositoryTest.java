package com.miro.widgetapp.repositories;

import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@SpringBootTest(properties = {"widget.repository=db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DBWidgetRepositoryTest {

    @Autowired
    IWidgetRepository repository;

    @Test
    public void insertWidgetWithoutZIndex() {
        Widget widget = new Widget(null, 1, 2, Optional.empty(), 100, 150);
        try {
            Widget inserted = repository.insert(widget);
            Assertions.assertNotNull(inserted.getId(), "An id should be assigned");
            Assertions.assertEquals(1, inserted.getZIndex().get(), "A zIndex should be assigned");

            Widget returned = repository.get(inserted.getId());
            Assertions.assertNotNull(returned);

        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a exception");
        } catch (WidgetNotFoundException e) {
            Assertions.fail("It should not throw a not found exception");
        }
    }

    @Test
    public void getWidgetById() {
        Widget widget = new Widget("test-id", 1, 2, Optional.of(3), 100, 150);
        try {
            repository.insert(widget);
            Widget returned = repository.get("test-id");

            Assertions.assertNotNull(returned, "Returned widget should not be null");
            Assertions.assertEquals(widget.getId(), returned.getId(), "The ids should be equal");
            Assertions.assertEquals(widget.getX(), returned.getX(), "The xs should be equal");
            Assertions.assertEquals(widget.getY(), returned.getY(), "The ys should be equal");
            Assertions.assertEquals(widget.getZIndex(), returned.getZIndex(), "The zs should be equal");
            Assertions.assertEquals(widget.getWidth(), returned.getWidth(), "The widths should be equal");
            Assertions.assertEquals(widget.getHeight(), returned.getHeight(), "The heights should be equal");
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        } catch (WidgetNotFoundException e) {
            Assertions.fail("It should not throw a not found exception");
        }
    }

    @Test
    public void getAllSortedByZIndex() {
        Widget w1 = new Widget("f49157be-ad86-4465-aecc-3053a4fd1111", 10, 10, Optional.of(1), 100, 150);
        Widget w2 = new Widget("f49157be-ad86-4465-aecc-3053a4fd2222", 20, 20, Optional.of(2), 200, 250);
        Widget w3 = new Widget("f49157be-ad86-4465-aecc-3053a4fd3333", 30, 30, Optional.of(4), 300, 350);
        Widget w4 = new Widget("f49157be-ad86-4465-aecc-3053a4fd4444", 40, 40, Optional.empty(), 400, 450);

        Widget w5 = new Widget("f49157be-ad86-4465-aecc-3053a4fd5555", 50, 50, Optional.of(3), 500, 550);

        try {
            repository.insert(w1);
            repository.insert(w2);
            repository.insert(w3);
            repository.insert(w4);

            Widget[] widgets = repository.all(1, 10);
            Assertions.assertEquals(4, widgets.length, "4 widgets should be returned");
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1111", widgets[0].getId());
            Assertions.assertEquals(1, widgets[0].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd2222", widgets[1].getId());
            Assertions.assertEquals(2, widgets[1].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd3333", widgets[2].getId());
            Assertions.assertEquals(4, widgets[2].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd4444", widgets[3].getId());
            Assertions.assertEquals(5, widgets[3].getZIndex().get());

            repository.insert(w5);

            widgets = repository.all(1, 10);
            Assertions.assertEquals(5, widgets.length, "5 widgets should be returned");
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1111", widgets[0].getId());
            Assertions.assertEquals(1, widgets[0].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd2222", widgets[1].getId());
            Assertions.assertEquals(2, widgets[1].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd5555", widgets[2].getId());
            Assertions.assertEquals(3, widgets[2].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd3333", widgets[3].getId());
            Assertions.assertEquals(5, widgets[3].getZIndex().get());

            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd4444", widgets[4].getId());
            Assertions.assertEquals(6, widgets[4].getZIndex().get());

        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        }
    }



    @Test
    public void duplicateInsertException() {
        Widget w1 = new Widget("test-id1", 10, 10, Optional.of(1), 100, 150);

        try {
            repository.insert(w1);
            repository.insert(w1);
            Assertions.fail("It should throw a duplicated exception");
        } catch (DuplicatedWidgetException e) {

        }
    }

    @Test
    public void updateNoZIndexChange() {
        Widget w1 = new Widget("test-id1", 10, 10, Optional.of(1), 100, 150);
        try {
            repository.insert(w1);

            Widget updated = repository.update("test-id1", Optional.empty(), Optional.of(201), Optional.empty(), Optional.empty(), Optional.of(1500));
            Assertions.assertNotNull(updated, "Returned widget should not be null");
            Assertions.assertEquals("test-id1", updated.getId(), "The ids should be equal");
            Assertions.assertEquals(10, updated.getX(), "The xs should be equal");
            Assertions.assertEquals(201, updated.getY(), "The ys should be equal");
            Assertions.assertEquals(1, updated.getZIndex().get(), "The zs should be equal");
            Assertions.assertEquals(100, updated.getWidth(), "The widths should be equal");
            Assertions.assertEquals(1500, updated.getHeight(), "The heights should be equal");
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        } catch (WidgetNotFoundException e) {
            Assertions.fail("It should not throw a not found exception");
        }
    }

    @Test
    public void updateWithZIndexChange() {
        Widget w1 = new Widget("test-id1", 10, 10, Optional.of(1), 100, 150);
        Widget w2 = new Widget("test-id2", 20, 20, Optional.of(2), 200, 250);
        Widget w3 = new Widget("test-id3", 30, 30, Optional.of(3), 300, 350);

        try {
            repository.insert(w1);
            repository.insert(w2);
            repository.insert(w3);

            Widget updated = repository.update("test-id1", Optional.of(101), Optional.of(201), Optional.of(3), Optional.of(1000), Optional.of(1500));
            Assertions.assertNotNull(updated, "Returned widget should not be null");
            Assertions.assertEquals("test-id1", updated.getId(), "The ids should be equal");
            Assertions.assertEquals(101, updated.getX(), "The xs should be equal");
            Assertions.assertEquals(201, updated.getY(), "The ys should be equal");
            Assertions.assertEquals(3, updated.getZIndex().get(), "The zs should be equal");
            Assertions.assertEquals(1000, updated.getWidth(), "The widths should be equal");
            Assertions.assertEquals(1500, updated.getHeight(), "The heights should be equal");

            Widget returned = repository.get("test-id1");
            Assertions.assertNotNull(updated, "Returned widget should not be null");
            Assertions.assertEquals("test-id1", returned.getId(), "The ids should be equal");
            Assertions.assertEquals(101, returned.getX(), "The xs should be equal");
            Assertions.assertEquals(201, returned.getY(), "The ys should be equal");
            Assertions.assertEquals(3, returned.getZIndex().get(), "The zs should be equal");
            Assertions.assertEquals(1000, returned.getWidth(), "The widths should be equal");
            Assertions.assertEquals(1500, returned.getHeight(), "The heights should be equal");

            Widget[] widgets = repository.all(1, 10);
            Assertions.assertEquals(3, widgets.length, "3 widgets should be returned");
            Assertions.assertEquals("test-id2", widgets[0].getId());
            Assertions.assertEquals(2, widgets[0].getZIndex().get());

            Assertions.assertEquals("test-id1", widgets[1].getId());
            Assertions.assertEquals(3, widgets[1].getZIndex().get());

            Assertions.assertEquals("test-id3", widgets[2].getId());
            Assertions.assertEquals(4, widgets[2].getZIndex().get());
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        } catch (WidgetNotFoundException e) {
            Assertions.fail("It should not throw a not found exception");
        }
    }

    @Test
    public void updateNotFound() {
        try {
            repository.update("f49157be-ad86-4465-aecc-3053a4fd1111", Optional.of(101), Optional.of(201), Optional.of(3), Optional.of(1000), Optional.of(1500));
            Assertions.fail("It should throw not found exception");
        } catch (WidgetNotFoundException e) {

        }
    }

    @Test
    public void delete() {
        Widget w1 = new Widget("f49157be-ad86-4465-aecc-3053a4fd1111", 10, 10, Optional.of(1), 100, 150);
        Widget w2 = new Widget("f49157be-ad86-4465-aecc-3053a4fd2222", 20, 20, Optional.of(2), 200, 250);
        Widget w3 = new Widget("f49157be-ad86-4465-aecc-3053a4fd3333", 30, 30, Optional.of(3), 300, 350);

        try {
            repository.insert(w1);
            repository.insert(w2);
            repository.insert(w3);

            repository.delete(w2.getId());
        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        } catch (WidgetNotFoundException e) {
            Assertions.fail("It should not throw a not found exception");
        }

        try {
            repository.get(w2.getId());
            Assertions.fail("It should throw a not found exception");
        } catch (WidgetNotFoundException e) {
        }

        try {
            repository.delete(w2.getId());
            Assertions.fail("It should throw a not found exception");
        } catch (WidgetNotFoundException e) {
        }
    }

    @Test
    public void pagination() {
        Widget[] widgets = new Widget[10];
        for(int i=0; i<widgets.length; i++){
            widgets[i] = new Widget("f49157be-ad86-4465-aecc-3053a4fd111"+i, i, i, Optional.of(i), i, i);
        }

        try {
            for(Widget w: widgets) {
                repository.insert(w);
            }

            Widget[] response = repository.all(1, 3);
            Assertions.assertEquals(3, response.length);
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1110", response[0].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1111", response[1].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1112", response[2].getId());

            response = repository.all(2, 3);
            Assertions.assertEquals(3, response.length);
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1113", response[0].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1114", response[1].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1115", response[2].getId());

            response = repository.all(3, 3);
            Assertions.assertEquals(3, response.length);
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1116", response[0].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1117", response[1].getId());
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1118", response[2].getId());

            response = repository.all(4, 3);
            Assertions.assertEquals(1, response.length);
            Assertions.assertEquals("f49157be-ad86-4465-aecc-3053a4fd1119", response[0].getId());

            response = repository.all(1, 15);
            Assertions.assertEquals(10, response.length);

        } catch (DuplicatedWidgetException e) {
            Assertions.fail("It should not throw a duplicated exception");
        }
    }
}
