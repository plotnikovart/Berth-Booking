package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.WidgetSettingsBerth;

import java.util.List;

@Repository
public interface WidgetSettingsBerthRepository extends JpaRepository<WidgetSettingsBerth, WidgetSettingsBerth.PK> {

    List<WidgetSettingsBerth> findAllByPkBerthId(Long berthId);

    @Modifying
    void deleteAllByPkBerthId(Long berthId);
}
