package com.miro.widgetapp.exceptions;

/**
 * Represents a widget not found exception
 */
public class WidgetNotFoundException extends Throwable {
    /**
     * Id of the widget that was not found
     */
    private String id;

    public WidgetNotFoundException(String id){
        super("A widget with id "+id+" was not found.");
        this.id = id;
    }
}
