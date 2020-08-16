package com.miro.widgetapp.controller;


import com.miro.widgetapp.exceptions.DuplicatedWidgetException;
import com.miro.widgetapp.exceptions.WidgetNotFoundException;
import com.miro.widgetapp.models.Widget;
import com.miro.widgetapp.repositories.IWidgetRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Widget Controller", produces = "Widget REST api")
public class WidgetController {

    private final IWidgetRepository widgetRepository;

    public WidgetController(IWidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @GetMapping("/widget/{id}")
    @ApiOperation("Search for a widget by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget found"),
            @ApiResponse(code = 404, message = "Widget was not found")
    })
    public Widget get(@PathVariable String id) throws WidgetNotFoundException {
        return widgetRepository.get(id);
    }

    @GetMapping("/widget")
    @ApiOperation("Return a list of all the widgets sorted by zindex")
    @ApiResponse(code = 200, message = "List of widgets")
    public Widget[] all() {
        return widgetRepository.all();
    }

    @PostMapping("/widget")
    @ApiOperation("Insert a new widget")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget was inserted"),
            @ApiResponse(code = 400, message = "Duplicated widget")
    })
    public Widget insert(@RequestBody InputWidgetRequest widget) throws DuplicatedWidgetException {
        Widget toInsert = new Widget(
                null,
                widget.getX().orElse(0),
                widget.getY().orElse(0),
                widget.getzIndex(),
                widget.getWidth().orElse(0),
                widget.getHeight().orElse(0)
        );
        return widgetRepository.insert(toInsert);
    }

    @PutMapping("/widget/{id}")
    @ApiOperation("Update an existing widget")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget updated"),
            @ApiResponse(code = 404, message = "Widget was not found")
    })
    public Widget update(@PathVariable String id, @RequestBody InputWidgetRequest widget)
            throws WidgetNotFoundException {
        return widgetRepository.update(
                id,
                widget.getX(),
                widget.getY(),
                widget.getzIndex(),
                widget.getWidth(),
                widget.getHeight()
        );
    }

    @DeleteMapping("/widget/{id}")
    @ApiOperation("Delete a widget by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Widget deleted"),
            @ApiResponse(code = 404, message = "Widget was not found")
    })
    public void delete(@PathVariable String id) throws WidgetNotFoundException {
        widgetRepository.delete(id);
    }
}
