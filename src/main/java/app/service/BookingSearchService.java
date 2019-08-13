package app.service;

import app.common.DateHelper;
import app.database.entity.BerthPlace;
import app.database.entity.Booking;
import app.database.entity.enums.BookingStatus;
import app.database.repository.BerthPlaceRepository;
import app.database.repository.BerthSearchRepository;
import app.web.dto.request.PlaceSearchRequest;
import app.web.dto.request.Sorting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingSearchService {

    private final BerthPlaceRepository berthPlaceRepository;
    private final BerthSearchRepository berthSearchRepository;

    @Transactional(readOnly = true)
    public boolean isReserved(BerthPlace berthPlace, LocalDate startDate, LocalDate endDate) {
        Optional<Booking> opt = berthPlace.getBookingList().stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .filter(b -> DateHelper.isIntersect(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .findAny();

        return opt.isPresent();
    }

    @Transactional(readOnly = true)
    public void searchPlaces(PlaceSearchRequest req, Sorting sorting) {
        berthSearchRepository.findByCoordinates(12.0, 12.0, 25000.0);
    }


}
