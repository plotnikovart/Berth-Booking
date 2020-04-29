package app.service.facade;

import app.database.repository.DictAmenityRepository;
import app.service.converters.impl.DictAmenityConverter;
import app.web.dto.DictAmenityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConvenienceFacade {

    private final DictAmenityRepository repository;
    private final DictAmenityConverter converter;

    public List<DictAmenityDto> getAll() {
        var conveniences = repository.findAll();
        return converter.toDtos(conveniences);
    }

}
