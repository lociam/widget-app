package com.miro.widgetapp.controller;

import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionsAdvice {

    @ResponseBody
    @ExceptionHandler(WidgetNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String widgetNotFound(WidgetNotFoundException exception){
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DuplicatedWidgetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String duplicatedWidget(DuplicatedWidgetException exception){
        return exception.getMessage();
    }
}
