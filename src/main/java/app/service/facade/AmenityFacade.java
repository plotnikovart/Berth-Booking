package app.service.facade;

import app.database.entity.DictAmenity;
import app.database.repository.DictAmenityRepository;
import app.service.berth.dto.DictAmenityDto;
import app.service.converters.impl.DictAmenityConverter;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AmenityFacade {

    private final DictAmenityRepository repository;
    private final DictAmenityConverter converter;

    public List<DictAmenityDto> getAll() {
        var amenities = StreamEx.of(repository.findAll())
                .sortedBy(DictAmenity::getKey)
                .toList();

        return converter.toDtos(amenities);
    }
}
