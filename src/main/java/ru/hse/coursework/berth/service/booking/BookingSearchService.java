package ru.hse.coursework.berth.service.booking;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.database.entity.*;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.*;
import ru.hse.coursework.berth.service.berth.BerthPart;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.booking.dto.BookingSearchReq;
import ru.hse.coursework.berth.service.booking.dto.Sorting;
import ru.hse.coursework.berth.service.converters.impl.BerthConverter;
import ru.hse.coursework.berth.service.converters.impl.BerthPlaceConverter;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingSearchService {

    private final ShipRepository shipRepository;
    private final BerthSearchRepository berthSearchRepository;
    private final BookingRepository bookingRepository;
    private final BerthRepository berthRepository;
    private final DictAmenityRepository dictAmenityRepository;
    private final BerthConverter berthConverter;
    private final BerthPlaceConverter placeConverter;


    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isReserved(BerthPlace berthPlace, LocalDate startDate, LocalDate endDate) {
        Optional<Booking> opt = berthPlace.getBookingList().stream()
                .filter(b -> b.getStatus() == BookingStatus.PAYED)
                .filter(b -> DateHelper.isIntersect(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .findAny();

        return opt.isPresent();
    }

    public boolean isMatch(BerthPlace place, Ship ship) {
        return ship.getLength() <= place.getLength() && ship.getWidth() <= place.getWidth() && ship.getDraft() <= place.getDraft();
    }

    @Transactional(readOnly = true)
    public List<BerthDto.Resp.Search> searchPlaces(BookingSearchReq req) {
        Map<Berth, Double> berthsAndDistance = berthSearchRepository.findByCoordinates(req.getLat(), req.getLng(), req.getRad());

        if (berthsAndDistance.isEmpty()) {
            return List.of();
        }

        Set<DictAmenity> requiredAmenities = extractRequiredAmenities(req);
        Set<BerthPlace> bookedPlaces = extractBookedPlaces(req);
        Ship ship = extractShip(req);

        loadBerthsData(berthsAndDistance.keySet());

        Map<Berth, List<BerthPlace>> filtered = berthsAndDistance.keySet().stream()
                .filter(Berth::getIsConfirmed)
                .filter(berth -> {
                    var amenities = Set.copyOf(berth.getAmenities());
                    return Sets.intersection(amenities, requiredAmenities).size() == requiredAmenities.size();
                })
                .flatMap(berth -> berth.getBerthPlaces().stream())
                .filter(place -> !bookedPlaces.contains(place))
                .filter(place -> ship == null || isMatch(place, ship))
                .collect(Collectors.groupingBy(BerthPlace::getBerth));


        Stream<BerthDto.Resp.Search> result = filtered.entrySet().stream()
                .map(pair -> convertToBerthDtoSearch(pair, berthsAndDistance.get(pair.getKey())));

        if (req.getSorting() == Sorting.DISTANCE) {
            result = result.sorted(Comparator.comparing(BerthDto.Resp.Search::getDistance));
        } else if (req.getSorting() == Sorting.PRICE) {
            result = result.sorted(Comparator.comparing(BerthDto.Resp.Search::getMinPrice));
        } else if (req.getSorting() == Sorting.RATING) {
            result = result.sorted(Comparator.comparing(BerthDto.Resp.Search::getAvgRating));
        }

        return result.collect(Collectors.toList());
    }

    private BerthDto.Resp.Search convertToBerthDtoSearch(Map.Entry<Berth, List<BerthPlace>> pair, Double distance) {
        var minPrice = pair.getValue().stream().mapToDouble(BerthPlace::getPrice).min().orElseThrow();
        var places = placeConverter.toDtos(pair.getValue());

        var berthSearch = (BerthDto.Resp.Search) berthConverter.toDto(new BerthDto.Resp.Search(), pair.getKey(), BerthPart.AMENITIES);
        return (BerthDto.Resp.Search) berthSearch
                .setDistance(distance)
                .setMinPrice(minPrice)
                .setPlaces(places);
    }

    private Set<DictAmenity> extractRequiredAmenities(BookingSearchReq req) {
        if (req.getAmenities() == null || req.getAmenities().isEmpty()) {
            return Set.of();
        }

        List<String> requiredAmIds = req.getAmenities().stream().map(DictAmenityDto::getKey).collect(Collectors.toList());
        return Set.copyOf(dictAmenityRepository.findAllById(requiredAmIds));
    }

    private Set<BerthPlace> extractBookedPlaces(BookingSearchReq req) {
        if (req.getStartDate() == null && req.getEndDate() == null) {
            return Set.of();
        }
        if (req.getStartDate() == null || req.getEndDate() == null) {
            throw new ServiceException("Start and end dates must be specified");
        }
        return bookingRepository.findPayedPlacesByDates(req.getStartDate(), req.getEndDate());
    }

    @Nullable
    private Ship extractShip(BookingSearchReq req) {
        return req.getShipId() != null ? shipRepository.findById(req.getShipId()).orElseThrow(NotFoundException::new) : null;
    }

    private void loadBerthsData(Collection<Berth> berths) {
        if (berths.isEmpty()) {
            return;
        }

        berthRepository.loadPlaces(berths);
        berthRepository.loadAmenities(berths);
    }
}
