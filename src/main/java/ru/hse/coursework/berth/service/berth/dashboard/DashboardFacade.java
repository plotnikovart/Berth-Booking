package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.enums.EnumHelper;
import ru.hse.coursework.berth.service.berth.BerthAccessService;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetDataDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetFullDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetSettingsDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static one.util.streamex.StreamEx.of;

@Component
@RequiredArgsConstructor
public class DashboardFacade {

    private final BerthAccessService berthAccessService;
    private final WidgetSettingsService widgetSettingsService;
    private final WidgetDataService widgetDataService;

    @Transactional(readOnly = true)
    public List<WidgetFullDto> getDashboard(Long berthId) {
        berthAccessService.checkAccess(berthId);
        List<WidgetSettingsDto> widgetsSettings = widgetSettingsService.getSettings(berthId);

        List<WidgetEnum> widgets = of(widgetsSettings)
                .filter(it -> it.getSettings().getIsVisible())
                .map(it -> EnumHelper.getEnumByIdentifier(it.getCode(), WidgetEnum.class))
                .filter(Objects::nonNull)
                .toList();

        List<WidgetDataDto> widgetsData = widgetDataService.getWidgetData(widgets, berthId);

        return mapSettingWithData(widgetsSettings, widgetsData);
    }

    @Transactional(readOnly = true)
    public List<WidgetSettingsDto> getSettings(Long berthId) {
        berthAccessService.checkAccess(berthId);
        return widgetSettingsService.getSettings(berthId);
    }

    @Transactional(readOnly = true)
    public List<WidgetSettingsDto> changeSettings(Long berthId, List<WidgetSettingsDto> settings) {
        berthAccessService.checkAccess(berthId);
        widgetSettingsService.updateSettings(berthId, settings);
        return widgetSettingsService.getSettings(berthId);
    }

    public List<String> getWidgetList() {
        return of(WidgetEnum.values()).map(WidgetEnum::getIdentifier).toList();
    }


    private List<WidgetFullDto> mapSettingWithData(List<WidgetSettingsDto> settings, List<WidgetDataDto> data) {
        Map<String, Object> dataMap = of(data).toMap(WidgetDataDto::getCode, it -> it);

        return of(settings)
                .map(it -> new WidgetFullDto()
                        .setCode(it.getCode())
                        .setSettings(it.getSettings())
                        .setData(dataMap.get(it.getCode()))
                )
                .toList();
    }
}
