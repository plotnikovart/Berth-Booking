package app.database.repository;

import app.database.entity.Berth;
import app.database.entity.BerthPlace;
import app.database.entity.Booking;
import app.database.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b " +
            "join BerthPlace bp on b.berthPlace = bp " +
            "where bp.berth = ?1 " +
            "order by b.id desc")
    List<Booking> findAllByBerth(Berth berth);

    @Query("select b.berthPlace from Booking b " +
            "where not (b.startDate > ?2 or b.endDate < ?1) " +
            "and b.status = ?3")
    Set<BerthPlace> findPlacesByDatesAndStatus(LocalDate startDate, LocalDate endDate, BookingStatus status);

    default Set<BerthPlace> findApprovedPlacesByDates(LocalDate startDate, LocalDate endDate) {
        return findPlacesByDatesAndStatus(startDate, endDate, BookingStatus.APPROVED);
    }
}
