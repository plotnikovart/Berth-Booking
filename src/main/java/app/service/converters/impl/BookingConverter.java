package app.service.converters.impl;

import app.common.DateHelper;
import app.database.entity.BerthPlace;
import app.database.entity.Booking;
import app.database.entity.Ship;
import app.database.repository.BerthPlaceRepository;
import app.database.repository.ShipRepository;
import app.service.converters.AbstractConverter;
import app.web.dto.BerthDto;
import app.web.dto.BookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public BookingDto.Resp convertToDto(BookingDto.Resp dto, Booking e) {
        return (BookingDto.Resp) dto
                .setId(e.getId())
                .setBerth(berthConverter.convertToDto(new BerthDto.Resp(), e.getBerthPlace().getBerth(), false, false))
                .setBerthPlace(berthPlaceConverter.convertToDto(e.getBerthPlace()))
                .setShip(shipConverter.convertToDto(e.getShip()))
                .setOwner(userInfoConverter.convertToDto(e.getOwner()))
                .setRenter(userInfoConverter.convertToDto(e.getRenter()))
                .setStatus(e.getStatus())
                .setStartDate(DateHelper.convertToDate(e.getStartDate()))
                .setEndDate(DateHelper.convertToDate(e.getEndDate()));
    }

    @Override
    public Booking convertToEntity(Booking entity, BookingDto.Req dto) {
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
    public List<BookingDto.Resp> convertToDtos(Collection<Booking> entities) {
        if (!entities.isEmpty()) {
            List<BerthPlace> places = entities.stream().map(Booking::getBerthPlace).collect(Collectors.toList());
            List<Ship> ships = entities.stream().map(Booking::getShip).collect(Collectors.toList());

            shipRepository.loadPhotos(ships);
            berthPlaceRepository.loadBerthsWithPhotos(places);
        }

        return super.convertToDtos(entities);
    }
}
