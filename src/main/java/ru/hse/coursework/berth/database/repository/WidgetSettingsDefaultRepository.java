package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.WidgetSettingsDefault;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;

@Repository
public interface WidgetSettingsDefaultRepository extends JpaRepository<WidgetSettingsDefault, WidgetEnum> {
}
