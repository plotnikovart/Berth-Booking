package ru.hse.coursework.berth.service.booking;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.*;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.*;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.booking.dto.BookingSearchReq;
import ru.hse.coursework.berth.service.booking.dto.Sorting;
import ru.hse.coursework.berth.service.converters.impl.BerthConverter;
import ru.hse.coursework.berth.service.converters.impl.BerthPlaceConverter;

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
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .filter(b -> DateHelper.isIntersect(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .findAny();

        return opt.isPresent();
    }

    public boolean isMatch(BerthPlace place, Ship ship) {
        return ship.getLength() <= place.getLength() && ship.getWidth() <= place.getWidth() && ship.getDraft() <= place.getDraft();
    }

    @Transactional(readOnly = true)
    public List<BerthDto.Resp.Search> searchPlaces(BookingSearchReq req) {
        Ship ship = shipRepository.findById(req.getShipId()).orElseThrow(NotFoundException::new);
        Set<DictAmenity> requiredAmenities = extractAmenities(req);

        Map<Berth, Double> berths = berthSearchRepository.findByCoordinates(req.getLat(), req.getLng(), req.getRad());
        loadData(berths.keySet());

        Set<BerthPlace> bookedPlaces = bookingRepository.findApprovedPlacesByDates(
                DateHelper.convertToLocalDate(req.getStartDate()),
                DateHelper.convertToLocalDate(req.getEndDate()));

        Map<Berth, List<BerthPlace>> filtered = berths.keySet().stream()
                .filter(berth -> {
                    var amenities = Set.copyOf(berth.getAmenities());
                    return Sets.intersection(amenities, requiredAmenities).size() == requiredAmenities.size();
                })
                .flatMap(berth -> berth.getBerthPlaces().stream())
                .filter(place -> !bookedPlaces.contains(place))
                .filter(place -> place.getLength() >= ship.getLength() && place.getWidth() >= ship.getWidth() && place.getDraft() >= ship.getDraft())
                .collect(Collectors.groupingBy(BerthPlace::getBerth));


        Stream<BerthDto.Resp.Search> result = filtered.entrySet().stream()
                .map(pair -> convertToBerthDtoSearch(pair, berths.get(pair.getKey())));

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
        var places = placeConverter.toDtos(pair.getValue());
        var minPrice = 0.0;//pair.getValue().stream().mapToDouble(BerthPlace::getFactPrice).min().orElseThrow();

        var berthSearch = (BerthDto.Resp.Search) berthConverter.toDto(new BerthDto.Resp.Search(), pair.getKey());
        return (BerthDto.Resp.Search) berthSearch
                .setDistance(distance)
                .setMinPrice(minPrice)
                .setPlaces(places);
    }

    private Set<DictAmenity> extractAmenities(BookingSearchReq req) {
        List<String> requiredConvIds = req.getConvenienceList().stream().map(DictAmenityDto::getKey).collect(Collectors.toList());
        return Set.copyOf(dictAmenityRepository.findAllById(requiredConvIds));
    }

    private void loadData(Collection<Berth> berths) {
        if (!berths.isEmpty()) {
            berthRepository.loadPlaces(berths);
            berthRepository.loadAmenities(berths);
        }
    }
}
