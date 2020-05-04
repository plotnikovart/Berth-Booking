package app.service.converters.impl;

import app.config.exception.impl.NotFoundException;
import app.database.entity.Berth;
import app.database.entity.BerthPlace;
import app.database.entity.DictAmenity;
import app.database.repository.BerthRepository;
import app.service.berth.dto.BerthDto;
import app.service.converters.AbstractConverter;
import app.service.file.FileInfoService;
import app.service.file.dto.FileInfoDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.DictAmenityDto;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class BerthConverter extends AbstractConverter<Berth, BerthDto.Resp, BerthDto> {

    private final DictAmenityConverter dictAmenityConverter;
    private final BerthPlaceConverter berthPlaceConverter;
    private final BerthRepository berthRepository;
    private final EntityManager em;
    private final FileInfoService fileInfoService;

    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth e, boolean convertPlaces, boolean convertAmenities) {
        List<BerthPlaceDto> places = convertPlaces ? berthPlaceConverter.toDtos(e.getBerthPlaces()) : null;
        List<DictAmenityDto> conveniences = convertAmenities ? dictAmenityConverter.toDtos(e.getAmenities()) : null;
        List<FileInfoDto> photos = StreamEx.of(e.getPhotos()).map(fileInfoService::get).toList();

        return (BerthDto.Resp) dto
                .setId(e.getId())
//                .setRating(e.getRating())
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
                .setAmenities(conveniences);
    }

    @Override
    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth e) {
        return toDto(dto, e, true, true);
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

    @Override
    public List<BerthDto.Resp> toDtos(Collection<Berth> entities) {
        if (!entities.isEmpty()) {
            berthRepository.loadPhotos(entities);
            berthRepository.loadConveniences(entities);
            berthRepository.loadPlaces(entities);
        }

        return super.toDtos(entities);
    }
}
