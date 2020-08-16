package com.miro.widgetapp.repositories;

import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;

import java.util.Optional;

public interface IWidgetRepository {
    /**
     * Returns the widget with the given id
     * @param id desired widget id
     * @return the desired widget
     * @throws WidgetNotFoundException if there is no widget with the given id
     */
    Widget get(String id) throws WidgetNotFoundException;

    /**
     * Returns an array with all the widgets sorted by descending zIndex
     * @return
     */
    Widget[] all();

    /**
     * Updates the widget identified by id
     * @param id id of the widget to be updated
     * @param x new x coordinate
     * @param y new y coordinate
     * @param zIndex new zindex
     * @param width new width
     * @param height new height
     * @return the updated widget
     * @throws WidgetNotFoundException if there's no a widget identified by id
     */
    Widget update(
            String id,
            Optional<Integer> x,
            Optional<Integer> y,
            Optional<Integer> zIndex,
            Optional<Integer> width,
            Optional<Integer> height) throws WidgetNotFoundException;

    /**
     * Deletes the widget identified by id
     * @param id
     * @throws WidgetNotFoundException if there's no a widget identified by widget.id
     */
    void delete(String id) throws WidgetNotFoundException;

    /**
     * Inserts the given widget into the DB. If the widget id is null a new id is generated for that widget.
     * @param widget widget to be inserted
     * @return the inserted widget
     * @throws DuplicatedWidgetException if there's already a widget identified by widget.id
     */
    Widget insert(Widget widget) throws DuplicatedWidgetException;
}
