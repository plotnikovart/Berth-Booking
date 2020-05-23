package ru.hse.coursework.berth.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.repository.WidgetSettingsBerthRepository;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.dto.SettingsDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetFullDto;
import ru.hse.coursework.berth.service.berth.dashboard.dto.WidgetSettingsDto;

import java.util.List;

class DashboardControllerTest extends DashboardBaseTest {

    @Autowired
    DashboardController dashboardController;
    @Autowired
    WidgetSettingsBerthRepository widgetSettingsBerthRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        widgetSettingsBerthRepository.deleteAll();
    }

    @Test
    void test() {
        OperationContext.accountId(user1Account.getId());

        // test defaults
        List<WidgetSettingsDto> defaultSettings = dashboardController.getSettings(berth1.getId()).getData();
        checkAllWidgetsContains(defaultSettings);

        List<WidgetFullDto> dashboard = dashboardController.getDashboard(berth1.getId()).getData();
        Assertions.assertEquals(countRequiredWidgets(defaultSettings), countData(dashboard));

        // append 2 user widget setting
        var userSetting1 = new WidgetSettingsDto()
                .setCode(WidgetEnum.LOCATION.getIdentifier())
                .setSettings(new SettingsDto()
                        .setIsVisible(true)
                        .setRow(20000)
                        .setColumn(20000)
                );

        var userSetting2 = new WidgetSettingsDto()
                .setCode(WidgetEnum.RATING_TREND.getIdentifier())
                .setSettings(new SettingsDto()
                        .setIsVisible(true)
                        .setRow(20000)
                        .setColumn(20000)
                );

        var userSettings = List.of(userSetting1, userSetting2);

        defaultSettings = dashboardController.changeSettings(berth1.getId(), userSettings).getData();
        checkAllWidgetsContains(defaultSettings);
        Assertions.assertTrue(defaultSettings.containsAll(userSettings));

        defaultSettings = dashboardController.getSettings(berth1.getId()).getData();
        checkAllWidgetsContains(defaultSettings);
        Assertions.assertTrue(defaultSettings.containsAll(userSettings));


        dashboard = dashboardController.getDashboard(berth1.getId()).getData();
        Assertions.assertEquals(countRequiredWidgets(defaultSettings), countData(dashboard));

        // remove 1 user setting
        userSettings = List.of(userSetting1);
        defaultSettings = dashboardController.changeSettings(berth1.getId(), userSettings).getData();
        checkAllWidgetsContains(defaultSettings);
        Assertions.assertTrue(defaultSettings.contains(userSetting1));
        Assertions.assertFalse(defaultSettings.contains(userSetting2));
    }

    private long countData(List<WidgetFullDto> dashboard) {
        return dashboard.stream().filter(it -> it.getData() != null).count();
    }

    private long countRequiredWidgets(List<WidgetSettingsDto> settings) {
        return settings.stream().filter(it -> it.getSettings().getIsVisible()).count();
    }

    private void checkAllWidgetsContains(List<WidgetSettingsDto> settings) {
        Assertions.assertEquals(WidgetEnum.values().length, settings.size());
        Assertions.assertEquals(WidgetEnum.values().length, settings.stream().map(WidgetSettingsDto::getCode).distinct().count());
    }
}