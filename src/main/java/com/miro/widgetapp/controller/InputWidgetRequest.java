package com.miro.widgetapp.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Represents the required schema for an insert / put widget request
 */
public class InputWidgetRequest {
    @JsonProperty("x")
    private Integer x;
    @JsonProperty("y")
    private Integer y;
    @JsonProperty("zIndex")
    private Integer zIndex;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;

    public Optional<Integer> getX() {
        return Optional.ofNullable(x);
    }

    public Optional<Integer> getY() {
        return Optional.ofNullable(y);
    }

    public Optional<Integer> getzIndex() {
        return Optional.ofNullable(zIndex);
    }

    public Optional<Integer> getWidth() {
        return Optional.ofNullable(width);
    }

    public Optional<Integer> getHeight() {
        return Optional.ofNullable(height);
    }

    public InputWidgetRequest(Integer x, Integer y, Integer zIndex, Integer width, Integer height) {
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
        this.width = width;
        this.height = height;
    }
}
