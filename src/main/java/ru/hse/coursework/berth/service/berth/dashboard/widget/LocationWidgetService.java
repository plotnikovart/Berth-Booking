package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BerthSearchRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.LocationDto;
import ru.hse.coursework.berth.service.file.FileInfoService;

import java.util.List;
import java.util.UUID;

import static one.util.streamex.StreamEx.of;

@Service
@RequiredArgsConstructor
public class LocationWidgetService implements WidgetService<LocationDto> {

    private static final Double DEFAULT_RADIUS = 20.0;

    private final FileInfoService fileInfoService;
    private final BerthRepository berthRepository;
    private final BerthSearchRepository berthSearchRepository;

    @Override
    public LocationDto getWidgetData(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        List<Berth> neighbours = of(berthSearchRepository.findByCoordinates(berth.getLat(), berth.getLng(), DEFAULT_RADIUS).keySet())
                .filter(it -> !berthId.equals(it.getId()))
                .toList();

        return new LocationDto()
                .setMe(toItemDto(berth))
                .setRadius(DEFAULT_RADIUS)
                .setNeighbours(of(neighbours).map(this::toItemDto).toList());
    }

    private LocationDto.Item toItemDto(Berth berth) {
        UUID firstPhoto = berth.getPhotos().isEmpty() ? null : berth.getPhotos().get(0);
        return new LocationDto.Item()
                .setId(berth.getId())
                .setLat(berth.getLat())
                .setLng(berth.getLng())
                .setTitle(berth.getName())
                .setPhoto(fileInfoService.get(firstPhoto))
                .setRating(berth.getAvgRating().intValue());
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.LOCATION;
    }
}
