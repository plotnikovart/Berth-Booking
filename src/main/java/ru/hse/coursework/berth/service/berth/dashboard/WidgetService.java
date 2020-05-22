package ru.hse.coursework.berth.service.berth.dashboard;

public interface WidgetService<D> {

    D getWidgetData(Long berthId);

    WidgetEnum getWidgetEnum();
}
