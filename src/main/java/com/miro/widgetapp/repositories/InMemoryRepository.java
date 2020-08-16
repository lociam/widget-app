package com.miro.widgetapp.repositories;

import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;

import java.util.*;

public class InMemoryRepository implements IWidgetRepository {

    private HashMap<String, Widget> widgets;
    private LinkedList<Widget> widgetsByZIndex;

    public InMemoryRepository() {
        widgets = new HashMap<>();
        widgetsByZIndex = new LinkedList<>();
    }

    @Override
    public Widget get(String id) throws WidgetNotFoundException {
        Widget res = widgets.get(id);
        if (res == null) {
            throw new WidgetNotFoundException(id);
        }
        return res;
    }

    @Override
    public Widget[] all(int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = page * pageSize;

        ArrayList<Widget> response = new ArrayList();
        Iterator<Widget> iterator = widgetsByZIndex.descendingIterator();
        int i = 0;
        while(iterator.hasNext()){
            Widget next = iterator.next();
            if( i >= fromIndex && i < toIndex) {
                response.add(next);
            }
            i++;
        }
        return response.toArray(new Widget[response.size()]);
    }

    @Override
    public Widget update(String id, Optional<Integer> x, Optional<Integer> y, Optional<Integer> zIndex, Optional<Integer> width, Optional<Integer> height) throws WidgetNotFoundException {
        Widget oldWidget = widgets.get(id);
        if (oldWidget == null) {
            throw new WidgetNotFoundException(id);
        }
        Widget newWidget = new Widget(
                id,
                x.orElse(oldWidget.getX()),
                y.orElse(oldWidget.getY()),
                Optional.of(zIndex.orElse(oldWidget.getZIndex().get())),
                width.orElse(oldWidget.getWidth()),
                height.orElse(oldWidget.getHeight())
        );
        int index = widgetsByZIndex.indexOf(oldWidget);
        if(newWidget.getZIndex().get() != oldWidget.getZIndex().get()) {
            widgetsByZIndex.remove(index);
            addToZIndex(newWidget);
        } else {
            widgetsByZIndex.set(index, newWidget);
        }

        widgets.put(id, newWidget);
        return newWidget;
    }

    @Override
    public void delete(String id) throws WidgetNotFoundException {
        if (widgets.get(id) == null) {
            throw new WidgetNotFoundException(id);
        }

        widgets.remove(id);
        widgetsByZIndex.removeIf(w -> w.getId().equals(id));
    }

    @Override
    public Widget insert(Widget toInsert) throws DuplicatedWidgetException {
        Widget widget = toInsert.clone();
        if (widget.getId() == null) {
            widget.setId(generateId());
        }

        if (widgets.get(widget.getId()) != null) {
            throw new DuplicatedWidgetException(widget.getId());
        }
        widgets.put(widget.getId(), widget);
        addToZIndex(widget);

        return widget;
    }

    private void addToZIndex(Widget widget) {
        if (widget.getZIndex().isEmpty()) {
            int newIndex = widgetsByZIndex.size() == 0 ? 1 : widgetsByZIndex.getFirst().getZIndex().get() + 1;
            widget.setZIndex(Optional.of(newIndex));
        }
        if (widgetsByZIndex.size() == 0 || widgetsByZIndex.getFirst().getZIndex().get() < widget.getZIndex().get()) {
            widgetsByZIndex.addFirst(widget);
            return;
        }

        Iterator<Widget> iterator = widgetsByZIndex.iterator();
        Widget node = iterator.next();
        int index = 0;
        while (node != null && node.getZIndex().get() >= widget.getZIndex().get()) {
            node.increaseZIndex();
            index++;
            node = iterator.hasNext() ? iterator.next() : null;
        }
        widgetsByZIndex.add(index, widget);
    }

    private String generateId() {
        String newUUID = UUID.randomUUID().toString();
        while (widgets.get(newUUID) != null) {
            newUUID = UUID.randomUUID().toString();
        }
        return newUUID;
    }
}
