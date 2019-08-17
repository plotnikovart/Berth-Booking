package app.service;

import app.common.DateHelper;
import app.common.exception.NotFoundException;
import app.database.entity.*;
import app.database.entity.enums.BookingStatus;
import app.database.repository.*;
import app.web.dto.BerthDto;
import app.web.dto.ConvenienceDto;
import app.web.dto.request.BookingSearchRequest;
import app.web.dto.request.Sorting;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ConvenienceRepository convenienceRepository;


    @Transactional(readOnly = true)
    public boolean isReserved(BerthPlace berthPlace, LocalDate startDate, LocalDate endDate) {
        Optional<Booking> opt = berthPlace.getBookingList().stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .filter(b -> DateHelper.isIntersect(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .findAny();

        return opt.isPresent();
    }

    @Transactional(readOnly = true)
    public List<BerthDto.WithId> searchPlaces(BookingSearchRequest req) {
        Ship ship = shipRepository.findById(req.getShipId()).orElseThrow(NotFoundException::new);
        Set<Convenience> requiredConv = extractConveniences(req);

        Map<Berth, Double> berths = berthSearchRepository.findByCoordinates(req.getLat(), req.getLng(), req.getRad());
        loadData(berths.keySet());

        Set<BerthPlace> bookedPlaces = bookingRepository.findApprovedPlacesByDates(
                DateHelper.convertToLocalDate(req.getStartDate()),
                DateHelper.convertToLocalDate(req.getEndDate()));

        Map<Berth, List<BerthPlace>> filtered = berths.keySet().stream()
                .filter(berth -> {
                    var conv = Set.copyOf(berth.getConveniences());
                    return Sets.intersection(conv, requiredConv).size() == requiredConv.size();
                })
                .flatMap(berth -> berth.getBerthPlaces().stream())
                .filter(place -> !bookedPlaces.contains(place))
                .filter(place -> place.getLength() >= ship.getLength() && place.getWidth() >= ship.getWidth() && place.getDraft() >= ship.getDraft())
                .collect(Collectors.groupingBy(BerthPlace::getBerth));


        Stream<BerthDto.WithId> result = filtered.entrySet().stream()
                .map(pair -> convertToBerthDto(pair, berths.get(pair.getKey())));

        if (req.getSorting() == Sorting.DISTANCE) {
            result = result.sorted(Comparator.comparing(BerthDto::getDistance));
        } else if (req.getSorting() == Sorting.PRICE) {
            result = result.sorted(Comparator.comparing(BerthDto::getMinPrice));
        } else if (req.getSorting() == Sorting.RATING) {
            result = result.sorted(Comparator.comparing(BerthDto::getRating));
        }

        return result.collect(Collectors.toList());
    }

    private BerthDto.WithId convertToBerthDto(Map.Entry<Berth, List<BerthPlace>> pair, Double distance) {
        var places = pair.getValue().stream().map(BerthPlace::getDto).collect(Collectors.toList());
        var conveniences = pair.getKey().getConveniences().stream().map(Convenience::getDto).collect(Collectors.toList());

        double minPrice = pair.getValue().stream().mapToDouble(BerthPlace::getFactPrice).min().orElseThrow();

        return (BerthDto.WithId) pair.getKey().getDto()
                .setDistance(distance)
                .setMinPrice(minPrice)
                .setPlaceList(places)
                .setConvenienceList(conveniences);
    }

    private Set<Convenience> extractConveniences(BookingSearchRequest req) {
        List<Integer> requiredConvIds = req.getConvenienceList().stream().map(ConvenienceDto::getId).collect(Collectors.toList());
        return Set.copyOf(convenienceRepository.findAllById(requiredConvIds));
    }

    private void loadData(Collection<Berth> berths) {
        if (!berths.isEmpty()) {
            berthRepository.loadPlaces(berths);
            berthRepository.loadConveniences(berths);
            berthRepository.loadPhotos(berths);
        }
    }
}
