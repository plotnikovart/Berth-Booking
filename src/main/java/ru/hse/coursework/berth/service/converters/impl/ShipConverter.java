package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.entity.ShipPhoto;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.web.dto.ShipDto;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShipConverter extends AbstractConverter<Ship, ShipDto.Resp, ShipDto.Req> {

    private final ShipRepository repository;

    @Override
    public ShipDto.Resp toDto(ShipDto.Resp dto, Ship e) {
        var photoList = e.getPhotos().stream()
                .map(photo -> "")//MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.SHIP.value().toLowerCase(), e.getOwnerId(), photo.getFileName()))
                .collect(Collectors.toList());

        return (ShipDto.Resp) dto
                .setId(e.getId())
                .setPhotoList(photoList)
                .setName(e.getName())
                .setDraft(e.getDraft())
                .setWidth(e.getWidth())
                .setLength(e.getLength());
    }

    @Override
    public Ship toEntity(Ship entity, ShipDto.Req dto) {
        if (dto.getPhotoList() != null) {
            var i = new AtomicInteger(0);
            var newPhotos = dto.getPhotoList().stream()
                    .map(fileName -> new ShipPhoto(entity, i.getAndIncrement(), fileName))
                    .collect(Collectors.toList());

            entity.setPhotos(newPhotos);
        }

        return entity
                .setName(dto.getName())
                .setDraft(dto.getDraft())
                .setLength(dto.getLength())
                .setWidth(dto.getWidth());
    }

    @Override
    public List<ShipDto.Resp> toDtos(Collection<Ship> entities) {
        if (!entities.isEmpty()) {
            repository.loadPhotos(entities);
        }
        return super.toDtos(entities);
    }
}
