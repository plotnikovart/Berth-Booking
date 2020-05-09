package ru.hse.coursework.berth.service.facade;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.DictAmenity;
import ru.hse.coursework.berth.database.repository.DictAmenityRepository;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.converters.impl.DictAmenityConverter;

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
