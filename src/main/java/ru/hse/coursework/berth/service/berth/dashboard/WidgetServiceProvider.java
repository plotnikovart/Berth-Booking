package ru.hse.coursework.berth.service.berth.dashboard;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static one.util.streamex.StreamEx.of;

@Component
public class WidgetServiceProvider {

    private final Map<WidgetEnum, WidgetService<?>> widgetServiceMap;

    public WidgetServiceProvider(List<WidgetService<?>> widgetServices) {
        widgetServiceMap = of(widgetServices).toMap(WidgetService::getWidgetEnum, it -> it);
    }

    @SuppressWarnings("unchecked")
    public <D> Optional<WidgetService<D>> getWidgetService(WidgetEnum widgetEnum) {
        WidgetService<D> ws = (WidgetService<D>) widgetServiceMap.get(widgetEnum);
        return Optional.ofNullable(ws);
    }
}
