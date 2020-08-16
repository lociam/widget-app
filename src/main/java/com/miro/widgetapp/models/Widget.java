package com.miro.widgetapp.models;

import java.util.Optional;

/**
 * Represents a widget with all its attributes
 */
public class Widget {
    /**
     * Unique identifier of the widget
     */
    private String id;

    /**
     * x top left coordinate of the widget
     */
    private int x;

    /**
     * y top left coordinate of the widget
     */
    private int y;

    /**
     * z index indicating the widget order on screen
     */
    private Optional<Integer> zIndex;

    /**
     * Widget width
     */
    private int width;

    /**
     * Widget height
     */
    private int height;

    /**
     * Creates a new widget with the given parameters
     *
     * @param id
     * @param x
     * @param y
     * @param zIndex
     * @param width
     * @param height
     */
    public Widget(String id, int x, int y, Optional<Integer> zIndex, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the unique id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the id to the given value
     *
     * @param id new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the x coordinate
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Updates the x coordinate to the given value
     *
     * @param x new x value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Updates the y coordinate to the given value
     *
     * @param y new y value
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the z index
     *
     * @return z index
     */
    public Optional<Integer> getZIndex() {
        return zIndex;
    }

    /**
     * Updates the zIndex to the given value
     *
     * @param zIndex
     */
    public void setZIndex(Optional<Integer> zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Returns the widget's width
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Updates the width to the given value
     *
     * @param width new width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the widget's height
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Updates the height to the given value
     *
     * @param height new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Increase by one the value of the zIndex
     */
    public void increaseZIndex() {
        zIndex = zIndex.map(x -> x + 1);
    }

    public Widget clone() {
        return new Widget(
                id,
                x,
                y,
                zIndex,
                width,
                height
        );
    }
}
