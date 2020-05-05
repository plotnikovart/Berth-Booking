package app.service.converters.impl;

import app.config.exception.impl.NotFoundException;
import app.database.entity.Berth;
import app.database.entity.BerthPlace;
import app.database.entity.DictAmenity;
import app.database.repository.BerthRepository;
import app.service.berth.BerthPart;
import app.service.berth.dto.BerthDto;
import app.service.berth.dto.BerthPlaceDto;
import app.service.berth.dto.DictAmenityDto;
import app.service.converters.AbstractConverter;
import app.service.file.FileInfoService;
import app.service.file.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static app.service.berth.BerthPart.AMENITIES;
import static app.service.berth.BerthPart.PLACES;
import static java.util.stream.Collectors.toList;

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
//                .setRating(e.getRating())
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
