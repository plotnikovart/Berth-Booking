package app.service.converters.impl;

import app.common.exception.NotFoundException;
import app.database.entity.Berth;
import app.database.entity.BerthPhoto;
import app.database.entity.BerthPlace;
import app.database.entity.Convenience;
import app.database.repository.BerthRepository;
import app.service.converters.AbstractConverter;
import app.service.file.ImageKind;
import app.web.dto.BerthDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.ConvenienceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class BerthConverter extends AbstractConverter<Berth, BerthDto.Resp, BerthDto.Req> {

    private final ConvenienceConverter convenienceConverter;
    private final BerthPlaceConverter berthPlaceConverter;
    private final BerthRepository berthRepository;
    private final EntityManager em;

    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth e, boolean convertPlaces, boolean convertConveniences) {
        List<String> photoList = e.getPhotos().stream()
                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.BERTH.name().toLowerCase(), e.getOwnerId(), photo.getFileName()))
                .collect(toList());

        List<BerthPlaceDto> places = convertPlaces ? berthPlaceConverter.toDtos(e.getBerthPlaces()) : null;
        List<ConvenienceDto> conveniences = convertConveniences ? convenienceConverter.toDtos(e.getConveniences()) : null;

        return (BerthDto.Resp) dto
                .setId(e.getId())
                .setRating(e.getRating())
                .setPhotoList(photoList)
                .setPlaceList(places)
                .setName(e.getName())
                .setDescription(e.getDescription())
                .setLat(e.getLat())
                .setLng(e.getLng())
                .setStandardPrice(e.getStandardPrice())
                .setConvenienceList(conveniences);
    }

    @Override
    public BerthDto.Resp toDto(BerthDto.Resp dto, Berth e) {
        return toDto(dto, e, true, true);
    }

    @Override
    public Berth toEntity(Berth entity, BerthDto.Req dto) {
        if (dto.getPhotoList() != null) {
            var i = new AtomicInteger();
            var newPhotos = dto.getPhotoList().stream()
                    .map(photoName -> new BerthPhoto(entity, i.getAndIncrement(), photoName))
                    .collect(toList());

            entity.setPhotos(newPhotos);
        }

        if (dto.getConvenienceList() != null) {
            var conveniences = dto.getConvenienceList().stream()
                    .map(conv -> em.getReference(Convenience.class, conv.getId()))
                    .collect(toList());

            entity.setConveniences(conveniences);
        }

        if (dto.getPlaceList() != null) {
            List<BerthPlace> oldPlaces = new ArrayList<>(entity.getBerthPlaces());
            Map<Long, BerthPlace> idToPlace = oldPlaces.stream().collect(Collectors.toMap(BerthPlace::getId, Function.identity()));

            List<BerthPlace> newPlaces = dto.getPlaceList().stream()
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
                .setStandardPrice(dto.getStandardPrice());
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
