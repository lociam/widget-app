package com.miro.widgetapp.repositories;


import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This repository uses SQL to get the widget information from a DB
 */
public class DBWidgetRepository implements IWidgetRepository {
    Logger logger = LoggerFactory.getLogger(DBWidgetRepository.class);

    public DBWidgetRepository(){
        logger.info("Starting DB repository....");
    }

    @Autowired
    private JdbcTemplate jtm;

    @Override
    public Widget get(String id) throws WidgetNotFoundException {
        String sql = "SELECT * FROM widgets WHERE id = '" + id + "'";
        List<Widget> result = jtm.query(sql, new BeanPropertyRowMapper<>(Widget.class));
        if (result.size() == 0) {
            throw new WidgetNotFoundException(id);
        }
        return result.get(0);
    }

    @Override
    public Widget[] all(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM widgets ORDER BY z_index LIMIT " + pageSize + " OFFSET " + offset;
        List<Widget> results = jtm.query(sql, new BeanPropertyRowMapper<>(Widget.class));
        return results.toArray(new Widget[results.size()]);
    }

    @Override
    @Transactional
    public Widget update(String id, Optional<Integer> x, Optional<Integer> y, Optional<Integer> zIndex, Optional<Integer> width, Optional<Integer> height) throws WidgetNotFoundException {
        Widget oldWidget = get(id);
        Widget newWidget = new Widget(
                id,
                x.orElse(oldWidget.getX()),
                y.orElse(oldWidget.getY()),
                Optional.of(zIndex.orElse(oldWidget.getZIndex().get())),
                width.orElse(oldWidget.getWidth()),
                height.orElse(oldWidget.getHeight())
        );
        if (newWidget.getZIndex().get() != oldWidget.getZIndex().get()) {
            jtm.update("UPDATE widgets SET z_index = z_index + 1 WHERE z_index >= " + newWidget.getZIndex().get());
        }

        jtm.update("UPDATE widgets SET " +
                "x=" + newWidget.getX() + "," +
                "y=" + newWidget.getY() + "," +
                "z_index=" + newWidget.getZIndex().get() + "," +
                "width=" + newWidget.getWidth() + "," +
                "height=" + newWidget.getHeight() +
                " WHERE id='" + id + "'"
        );
        return newWidget;
    }

    @Override
    public void delete(String id) throws WidgetNotFoundException {
        String sql = "DELETE FROM widgets WHERE id = '" + id + "'";
        int res = jtm.update(sql);
        if (res == 0) {
            throw new WidgetNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public Widget insert(Widget widget) throws DuplicatedWidgetException {
        String id = widget.getId();
        if (id == null) {
            id = generateId();
        }
        String insertSql;

        if (widget.getZIndex().isPresent()) {
            jtm.update("UPDATE widgets SET z_index = z_index + 1 WHERE z_index >= " + widget.getZIndex().get());

            insertSql = "INSERT INTO widgets (id,x,y,z_index,width,height) VALUES (" +
                    "'" + id + "'," +
                    widget.getX() + "," +
                    widget.getY() + "," +
                    widget.getZIndex().get() + "," +
                    widget.getWidth() + "," +
                    widget.getHeight() +
                    ");";
        } else {
            Integer maxZIndex = jtm.queryForObject("SELECT max(z_index) FROM widgets", Integer.class);

            insertSql = "INSERT INTO widgets (id,x,y,z_index,width,height) VALUES (" +
                    "'" + id + "'," +
                    widget.getX() + "," +
                    widget.getY() + "," +
                    (maxZIndex == null ? 1 : +maxZIndex + 1) + "," +
                    widget.getWidth() + "," +
                    widget.getHeight() +
                    ");";
        }

        try {
            jtm.update(insertSql);
        } catch (Exception e) {
            throw new DuplicatedWidgetException(widget.getId());
        }

        try {
            return get(id);
        } catch (WidgetNotFoundException e) {
            throw new DuplicatedWidgetException(widget.getId());

        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
