package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.repository.BerthPlaceRepository;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.service.converters.AbstractConverter;
import ru.hse.coursework.berth.web.dto.BookingDto;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingConverter extends AbstractConverter<Booking, BookingDto.Resp, BookingDto.Req> {

    private final EntityManager em;
    private final ShipRepository shipRepository;
    private final BerthPlaceRepository berthPlaceRepository;

    private final UserInfoConverter userInfoConverter;
    private final BerthConverter berthConverter;
    private final BerthPlaceConverter berthPlaceConverter;
    private final ShipConverter shipConverter;

    @Override
    public BookingDto.Resp toDto(BookingDto.Resp dto, Booking e) {
        return (BookingDto.Resp) dto
                .setId(e.getId())
//                .setBerth(berthConverter.toDto(new BerthDto.Resp(), e.getBerthPlace().getBerth(), false, false))
                .setBerthPlace(berthPlaceConverter.toDto(e.getBerthPlace()))
                .setShip(shipConverter.toDto(e.getShip()))
//                .setOwner(userInfoConverter.toDto(e.getOwner()))
//                .setRenter(userInfoConverter.toDto(e.getRenter()))
                .setStatus(e.getStatus())
                .setStartDate(DateHelper.convertToDate(e.getStartDate()))
                .setEndDate(DateHelper.convertToDate(e.getEndDate()));
    }

    @Override
    public Booking toEntity(Booking entity, BookingDto.Req dto) {
        var berthPlace = em.getReference(BerthPlace.class, dto.getBerthPlaceId());
        var ship = em.getReference(Ship.class, dto.getShipId());

        var startDate = DateHelper.convertToLocalDate(dto.getStartDate());
        var endDate = DateHelper.convertToLocalDate(dto.getEndDate());

        return entity
                .setBerthPlace(berthPlace)
                .setShip(ship)
                .setStartDate(startDate)
                .setEndDate(endDate);
    }

    @Override
    public List<BookingDto.Resp> toDtos(Collection<Booking> entities) {
        if (!entities.isEmpty()) {
            List<BerthPlace> places = entities.stream().map(Booking::getBerthPlace).collect(Collectors.toList());
            List<Ship> ships = entities.stream().map(Booking::getShip).collect(Collectors.toList());

            berthPlaceRepository.loadBerthsWithPhotos(places);
        }

        return super.toDtos(entities);
    }
}
