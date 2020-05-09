package ru.hse.coursework.berth.service.converters.impl;

import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.DictAmenity;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.converters.AbstractRespConverter;

@Component
public class DictAmenityConverter extends AbstractRespConverter<DictAmenity, DictAmenityDto> {

    @Override
    public DictAmenityDto toDto(DictAmenityDto dto, DictAmenity e) {
        return dto
                .setKey(e.getKey())
                .setValue(e.getValue());
    }
}
