package com.miro.widgetapp.exceptions;

public class DuplicatedWidgetException extends Throwable {
    /**
     * Id of the widget
     */
    public String id;

    public DuplicatedWidgetException(String id){
        super("There's a widget already defined with that id.");
        this.id = id;
    }
}
