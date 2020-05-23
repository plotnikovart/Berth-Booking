package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetDataDto;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static one.util.streamex.StreamEx.of;

@Slf4j
@Service
@RequiredArgsConstructor
public class WidgetDataService {

    private final WidgetServiceProvider widgetServiceProvider;

    List<WidgetDataDto> getWidgetData(List<WidgetEnum> widgets, Long berthId) {
        final List<WidgetDataDto> result = new CopyOnWriteArrayList<>();

        of(widgets).parallel()
                .forEach(it -> {
                    var widgetData = new WidgetDataDto()
                            .setCode(it.getIdentifier());

                    Optional<WidgetService<Object>> wsOpt = widgetServiceProvider.getWidgetService(it);
                    wsOpt.ifPresent(ws -> {
                        try {
                            Object data = ws.getWidgetData(berthId);
                            widgetData.setData(data);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });

                    result.add(widgetData);
                });

        return result;
    }
}
