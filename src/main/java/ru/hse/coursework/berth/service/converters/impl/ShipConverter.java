package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.service.file.FileInfoService;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;
import ru.hse.coursework.berth.web.dto.ShipDto;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShipConverter extends AbstractConverter<Ship, ShipDto.Resp, ShipDto> {

    private final FileInfoService fileInfoService;

    @Override
    public ShipDto.Resp toDto(ShipDto.Resp dto, Ship e) {
        List<FileInfoDto> photos = StreamEx.of(e.getPhotos())
                .map(fileInfoService::get)
                .toList();

        var dimensions = new ShipDto.Dimensions()
                .setDraft(e.getDraft())
                .setLength(e.getLength())
                .setWidth(e.getWidth());

        var model = new ShipDto.Model()
                .setModel(e.getModel())
                .setProducer(e.getProducer())
                .setYear(e.getYear());

        var registration = new ShipDto.Registration()
                .setNumber(e.getRegistrationNumber())
                .setExpire(e.getRegistrationExpire())
                .setFile(fileInfoService.get(e.getRegistrationFile()));

        var insurance = new ShipDto.Insurance()
                .setCompany(e.getInsuranceCompany())
                .setExpire(e.getInsuranceExpire())
                .setNumber(e.getInsuranceNumber())
                .setFile(fileInfoService.get(e.getInsuranceFile()));


        return (ShipDto.Resp) dto
                .setId(e.getId())
                .setName(e.getName())
                .setType(e.getType())
                .setPhotos(photos)
                .setDimensions(dimensions)
                .setModel(model)
                .setRegistration(registration)
                .setInsurance(insurance);
    }

    @Override
    public Ship toEntity(Ship entity, ShipDto dto) {
        if (dto.getPhotos() != null) {
            List<UUID> photosIds = StreamEx.of(dto.getPhotos())
                    .peek(it -> fileInfoService.get(it.getFileId()))
                    .map(FileInfoDto::getFileId)
                    .toList();

            entity.setPhotos(photosIds);
        }

        return entity
                .setName(dto.getName())
                .setType(dto.getType())

                .setDraft(dto.getDimensions().getDraft())
                .setWidth(dto.getDimensions().getWidth())
                .setLength(dto.getDimensions().getLength())

                .setProducer(dto.getModel().getProducer())
                .setModel(dto.getModel().getModel())
                .setYear(dto.getModel().getYear())

                .setRegistrationNumber(dto.getRegistration().getNumber())
                .setRegistrationExpire(dto.getRegistration().getExpire())
                .setRegistrationFile(dto.getRegistration().getFile().getFileId())

                .setInsuranceCompany(dto.getInsurance().getCompany())
                .setInsuranceNumber(dto.getInsurance().getNumber())
                .setInsuranceExpire(dto.getInsurance().getExpire())
                .setInsuranceFile(dto.getInsurance().getFile().getFileId());
    }
}
