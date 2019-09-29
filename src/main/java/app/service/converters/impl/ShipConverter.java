package app.service.converters.impl;

import app.database.entity.Ship;
import app.database.entity.ShipPhoto;
import app.database.repository.ShipRepository;
import app.service.converters.AbstractConverter;
import app.service.file.ImageKind;
import app.web.dto.ShipDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShipConverter extends AbstractConverter<Ship, ShipDto.Resp, ShipDto.Req> {

    private final ShipRepository repository;

    @Override
    public ShipDto.Resp convertToDto(ShipDto.Resp dto, Ship e) {
        var photoList = e.getPhotos().stream()
                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.SHIP.name().toLowerCase(), e.getOwnerId(), photo.getFileName()))
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
    public Ship convertToEntity(Ship entity, ShipDto.Req dto) {
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
    public List<ShipDto.Resp> convertToDtos(Collection<Ship> entities) {
        if (!entities.isEmpty()) {
            repository.loadPhotos(entities);
        }
        return super.convertToDtos(entities);
    }
}
