package app.service.converters.impl;

import app.database.entity.BerthPlace;
import app.service.converters.AbstractSingleConverter;
import app.web.dto.BerthPlaceDto;
import org.springframework.stereotype.Component;

@Component
public class BerthPlaceConverter extends AbstractSingleConverter<BerthPlace, BerthPlaceDto> {

    @Override
    public BerthPlaceDto toDto(BerthPlaceDto dto, BerthPlace e) {
        return dto
                .setId(e.getId())
                .setLength(e.getLength())
                .setDraft(e.getDraft())
                .setWidth(e.getWidth())
                .setPrice(e.getPrice());
    }

    @Override
    public BerthPlace toEntity(BerthPlace entity, BerthPlaceDto dto) {
        return entity
                .setLength(dto.getLength())
                .setDraft(dto.getDraft())
                .setWidth(dto.getWidth())
                .setPrice(dto.getPrice());
    }
}
