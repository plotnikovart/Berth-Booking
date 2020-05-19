package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.service.berth.BerthPart;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.converters.AbstractConverter;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class RenterBookingConverter extends AbstractConverter<Booking, BookingDto.RespRenter, BookingDto.Req> {

    private final EntityManager em;

    private final BerthConverter berthConverter;
    private final ShipConverter shipConverter;

    @Override
    public BookingDto.RespRenter toDto(BookingDto.RespRenter dto, Booking e) {
        return (BookingDto.RespRenter) dto
                .setId(e.getId())
                .setBerth(berthConverter.toDto(e.getBerthPlace().getBerth(), BerthPart.AMENITIES))
                .setBerthPlaceName(e.getBerthPlace().getName())
                .setShip(shipConverter.toDto(e.getShip()))
                .setStatus(e.getStatus())
                .setTotalPrice(e.getTotalPrice())
                .setStartDate(e.getStartDate())
                .setEndDate(e.getEndDate());
    }

    @Override
    public Booking toEntity(Booking entity, BookingDto.Req dto) {
        var berthPlace = em.getReference(BerthPlace.class, dto.getBerthPlaceId());
        var ship = em.getReference(Ship.class, dto.getShipId());

        return entity
                .setBerthPlace(berthPlace)
                .setShip(ship)
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate());
    }
}
