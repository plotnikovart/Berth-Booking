package app.service.facade;

import app.database.repository.ConvenienceRepository;
import app.service.converters.impl.ConvenienceConverter;
import app.web.dto.ConvenienceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConvenienceFacade {

    private final ConvenienceRepository repository;
    private final ConvenienceConverter converter;

    public List<ConvenienceDto> getAll() {
        var conveniences = repository.findAll();
        return converter.convertToDtos(conveniences);
    }

}
