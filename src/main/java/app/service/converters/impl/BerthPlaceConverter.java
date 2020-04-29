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
                .setName(e.getName())
                .setLength(e.getLength())
                .setDraft(e.getDraft())
                .setWidth(e.getWidth())
                .setPrice(e.getPrice())
                .setX_coord(e.getX_coord())
                .setY_coord(e.getY_coord())
                .setRotate(e.getRotate())
                .setColor(e.getColor());
    }

    @Override
    public BerthPlace toEntity(BerthPlace entity, BerthPlaceDto dto) {
        return entity
                .setName(dto.getName())
                .setLength(dto.getLength())
                .setDraft(dto.getDraft())
                .setWidth(dto.getWidth())
                .setPrice(dto.getPrice())
                .setX_coord(dto.getX_coord())
                .setY_coord(dto.getY_coord())
                .setRotate(dto.getRotate())
                .setColor(dto.getColor());
    }
}
