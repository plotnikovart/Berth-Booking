package ru.hse.coursework.berth.service.converters.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.converters.AbstractRespConverter;

@Component
@RequiredArgsConstructor
public class OwnerBookingConverter extends AbstractRespConverter<Booking, BookingDto.RespOwner> {

    private final BerthPlaceConverter berthPlaceConverter;
    private final ShipConverter shipConverter;

    @Override
    public BookingDto.RespOwner toDto(BookingDto.RespOwner dto, Booking entity) {
        return (BookingDto.RespOwner) dto
                .setId(entity.getId())
                .setBerthPlace(berthPlaceConverter.toDto(entity.getBerthPlace()))
                .setShip(shipConverter.toDto(entity.getShip()))
                .setStatus(entity.getStatus())
                .setTotalPrice(entity.getTotalPrice())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate());
    }
}
