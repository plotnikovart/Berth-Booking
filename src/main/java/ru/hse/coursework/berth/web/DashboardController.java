package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardFacade;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetFullDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetSettingsDto;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.AllWidgetsModel;
import ru.hse.coursework.berth.web.dto.resp.ListResp;

import java.util.List;

@RestController
@RequestMapping("/api/berths/{berthId}/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardFacade dashboardFacade;

    @GetMapping
    public ListResp<WidgetFullDto> getDashboard(@PathVariable Long berthId) {
        var resp = dashboardFacade.getDashboard(berthId);
        return new ListResp<>(resp);
    }

    @PostMapping("settings")
    public ListResp<WidgetSettingsDto> changeSettings(@PathVariable Long berthId, @RequestBody List<WidgetSettingsDto> body) {
        var resp = dashboardFacade.changeSettings(berthId, body);
        return new ListResp<>(resp);
    }

    @GetMapping("settings")
    public ListResp<WidgetSettingsDto> getSettings(@PathVariable Long berthId) {
        var resp = dashboardFacade.getSettings(berthId);
        return new ListResp<>(resp);
    }

    @GetMapping("widgets")
    public ListResp<String> getWidgetList(@RequestParam Long berthId) {
        var resp = dashboardFacade.getWidgetList();
        return new ListResp<>(resp);
    }

    @GetMapping("model")
    public AllWidgetsModel model() {
        return new AllWidgetsModel();
    }
}
