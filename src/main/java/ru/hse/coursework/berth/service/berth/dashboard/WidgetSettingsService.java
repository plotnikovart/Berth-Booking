package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.enums.EnumHelper;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.WidgetSettings;
import ru.hse.coursework.berth.database.entity.WidgetSettingsBerth;
import ru.hse.coursework.berth.database.entity.WidgetSettingsDefault;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.WidgetSettingsBerthRepository;
import ru.hse.coursework.berth.database.repository.WidgetSettingsDefaultRepository;
import ru.hse.coursework.berth.service.berth.dashboard.dto.SettingsDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetSettingsDto;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static one.util.streamex.StreamEx.of;

@Service
@RequiredArgsConstructor
public class WidgetSettingsService {

    private final WidgetSettingsBerthRepository widgetSettingsBerthRepository;
    private final WidgetSettingsDefaultRepository widgetSettingsDefaultRepository;
    private final BerthRepository berthRepository;


    List<WidgetSettingsDto> getSettings(Long berthId) {
        List<WidgetSettingsDto> defaults = getDefaultSettings();
        Map<String, SettingsDto> berths = of(getBerthSettings(berthId))
                .toMap(WidgetSettingsDto::getCode, WidgetSettingsDto::getSettings);

        defaults.forEach(it ->
                // berth settings override defaults
                ofNullable(berths.get(it.getCode()))
                        .ifPresent(it::setSettings)
        );

        return defaults;
    }

    @Transactional
    public void updateSettings(Long berthId, List<WidgetSettingsDto> settings) {
        widgetSettingsBerthRepository.deleteAllByPkBerthId(berthId);
        widgetSettingsBerthRepository.flush();

        List<WidgetSettingsBerth> newWidgetSettings = of(settings)
                .map(it -> {
                    var widgetEnum = ofNullable(EnumHelper.getEnumByIdentifier(it.getCode(), WidgetEnum.class))
                            .orElseThrow(NotFoundException::new);

                    var pk = new WidgetSettingsBerth.PK()
                            .setBerthId(berthId)
                            .setCode(widgetEnum.getIdentifier());

                    return (WidgetSettingsBerth) new WidgetSettingsBerth()
                            .setPk(pk)
                            .setColumnNum(it.getSettings().getColumn())
                            .setRowNum(it.getSettings().getRow())
                            .setIsVisible(it.getSettings().getIsVisible());
                })
                .toList();

        widgetSettingsBerthRepository.saveAll(newWidgetSettings);
        widgetSettingsBerthRepository.flush();
    }


    private List<WidgetSettingsDto> getBerthSettings(Long berthId) {
        List<WidgetSettingsBerth> all = widgetSettingsBerthRepository.findAllByPkBerthId(berthId);
        return of(all)
                .map(it -> toDto(it.getWidgetEnum(), it))
                .toList();
    }

    private List<WidgetSettingsDto> getDefaultSettings() {
        List<WidgetSettingsDefault> all = widgetSettingsDefaultRepository.findAll();
        return of(all)
                .map(it -> toDto(it.getWidgetEnum(), it))
                .toList();
    }

    private WidgetSettingsDto toDto(WidgetEnum widgetEnum, WidgetSettings settings) {
        return new WidgetSettingsDto()
                .setCode(widgetEnum.getIdentifier())
                .setSettings(new SettingsDto()
                        .setColumn(settings.getColumnNum())
                        .setRow(settings.getRowNum())
                        .setIsVisible(settings.getIsVisible())
                );
    }
}
