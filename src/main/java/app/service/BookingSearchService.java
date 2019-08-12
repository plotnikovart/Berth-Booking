package app.service;

import app.common.DateHelper;
import app.database.entity.BerthPlace;
import app.database.entity.Booking;
import app.database.entity.enums.BookingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingSearchService {

    @Transactional(readOnly = true)
    public boolean isReserved(BerthPlace berthPlace, LocalDate startDate, LocalDate endDate) {
        Optional<Booking> opt = berthPlace.getBookingList().stream()
                .filter(b -> b.getStatus() == BookingStatus.APPROVED)
                .filter(b -> DateHelper.isIntersect(startDate, endDate, b.getStartDate(), b.getEndDate()))
                .findAny();

        return opt.isPresent();
    }


}
