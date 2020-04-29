package app.service.converters.impl;

import app.database.entity.DictAmenity;
import app.service.converters.AbstractRespConverter;
import app.web.dto.DictAmenityDto;
import org.springframework.stereotype.Component;

@Component
public class DictAmenityConverter extends AbstractRespConverter<DictAmenity, DictAmenityDto> {

    @Override
    public DictAmenityDto toDto(DictAmenityDto dto, DictAmenity e) {
        return dto
                .setKey(e.getKey())
                .setValue(e.getValue());
    }
}
