package app.service.converters.impl;

import app.database.entity.Convenience;
import app.service.converters.AbstractRespConverter;
import app.web.dto.ConvenienceDto;
import org.springframework.stereotype.Component;

@Component
public class ConvenienceConverter extends AbstractRespConverter<Convenience, ConvenienceDto> {

    @Override
    public ConvenienceDto toDto(ConvenienceDto dto, Convenience e) {
        return dto
                .setId(e.getId())
                .setCode(e.getCode())
                .setName(e.getName());
    }
}
