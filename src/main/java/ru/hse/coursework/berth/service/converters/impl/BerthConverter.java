package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.DictAmenity;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.service.berth.BerthPart;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.service.file.FileInfoService;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static ru.hse.coursework.berth.service.berth.BerthPart.AMENITIES;
import static ru.hse.coursework.berth.service.berth.BerthPart.PLACES;

@Component
@RequiredArgsConstructor
public class BerthConverter extends AbstractConverter<Berth, BerthDto.Resp, BerthDto> {

    private final DictAmenityConverter dictAmenityConverter;
    private final BerthPlaceConverter berthPlaceConverter;
    private final BerthRepository berthRepository;
    private final EntityManager em;
    private final FileInfoService fileInfoService;

    @Override
    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth entity) {
        return toDto(dto, entity, new BerthPart[0]);
    }

    public BerthDto.Resp toDto(Berth entity, BerthPart... include) {
        return toDto(new BerthDto.Resp(), entity, include);
    }

    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth e, BerthPart... include) {
        List<BerthPlaceDto> places = StreamEx.of(include).has(PLACES) ?
                berthPlaceConverter.toDtos(e.getBerthPlaces()) :
                null;

        List<DictAmenityDto> amenities = StreamEx.of(include).has(AMENITIES) ?
                StreamEx.of(dictAmenityConverter.toDtos(e.getAmenities())).sortedBy(DictAmenityDto::getKey).toList() :
                null;

        List<FileInfoDto> photos = StreamEx.of(e.getPhotos()).map(fileInfoService::get).toList();

        return (BerthDto.Resp) dto
                .setId(e.getId())
                .setAvgRating(e.getAvgRating().intValue())
                .setIsConfirmed(e.getIsConfirmed())
                .setPhotos(photos)
                .setPlaces(places)
                .setName(e.getName())
                .setDescription(e.getDescription())
                .setLat(e.getLat())
                .setLng(e.getLng())
                .setPhCode(e.getPhCode())
                .setPhNumber(e.getPhNumber())
                .setSite(e.getSite())
                .setRadio(e.getRadio())
                .setAmenities(amenities);
    }

    @Override
    public Berth toEntity(Berth entity, BerthDto dto) {
        if (dto.getPhotos() != null) {
            List<UUID> photos = dto.getPhotos().stream()
                    .peek(it -> fileInfoService.get(it.getFileId()))
                    .map(FileInfoDto::getFileId)
                    .collect(Collectors.toUnmodifiableList());

            entity.setPhotos(photos);
        }

        if (dto.getAmenities() != null) {
            List<DictAmenity> amenities = dto.getAmenities().stream()
                    .map(it -> em.getReference(DictAmenity.class, it.getKey()))
                    .collect(toList());

            entity.setAmenities(amenities);
        }

        if (dto.getPlaces() != null) {
            List<BerthPlace> oldPlaces = new ArrayList<>(entity.getBerthPlaces());
            Map<Long, BerthPlace> idToPlace = oldPlaces.stream().collect(Collectors.toMap(BerthPlace::getId, Function.identity()));

            List<BerthPlace> newPlaces = dto.getPlaces().stream()
                    .map(placeDto -> {
                        if (placeDto.getId() != null) {
                            var oldPlace = Optional.ofNullable(idToPlace.get(placeDto.getId()))
                                    .orElseThrow(NotFoundException::new);

                            return berthPlaceConverter.toEntity(oldPlace, placeDto);
                        } else {
                            var newPlace = new BerthPlace().setBerth(entity);
                            return berthPlaceConverter.toEntity(newPlace, placeDto);
                        }
                    })
                    .collect(Collectors.toList());

            entity.setBerthPlaces(newPlaces);
        }

        return entity
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setLng(dto.getLng())
                .setLat(dto.getLat())
                .setPhCode(dto.getPhCode())
                .setPhNumber(dto.getPhNumber())
                .setSite(dto.getSite())
                .setRadio(dto.getRadio());
    }

    public List<BerthDto.Resp> toDtos(Collection<Berth> entities, BerthPart... include) {
        if (!entities.isEmpty()) {
            if (StreamEx.of(include).has(AMENITIES)) {
                berthRepository.loadAmenities(entities);
            }

            if (StreamEx.of(include).has(PLACES)) {
                berthRepository.loadPlaces(entities);
            }
        }

        return StreamEx.of(entities).map(it -> toDto(it, include)).toList();
    }
}
