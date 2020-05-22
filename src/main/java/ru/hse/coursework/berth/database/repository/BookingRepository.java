package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b " +
            "join BerthPlace bp on b.berthPlace = bp " +
            "where bp.berth = ?1 " +
            "order by b.id desc")
    @EntityGraph(attributePaths = {"berthPlace", "ship"})
    List<Booking> findAllByBerthLoadBerthPlaceAndShip(Berth berth);

    @Query("select b.berthPlace from Booking b " +
            "where not (b.startDate > ?2 or b.endDate < ?1) " +
            "and b.status = ?3")
    Set<BerthPlace> findPlacesByDatesAndStatus(LocalDate startDate, LocalDate endDate, BookingStatus status);


    @Query("select b from Booking b where b.renter = ?1")
    @EntityGraph(attributePaths = {"berthPlace.berth.amenities", "ship"})
    List<Booking> findAllByRenterLoadBerthWithAmenitiesAndShip(Account renter);

    default Set<BerthPlace> findPayedPlacesByDates(LocalDate startDate, LocalDate endDate) {
        return findPlacesByDatesAndStatus(startDate, endDate, BookingStatus.PAYED);
    }


    default Integer countBookedPlaces(Berth berth, LocalDate from, LocalDate to) {
        return countBookedPlacesInner(berth, from, to, BookingStatus.PAYED);
    }

    @Query("select count(boo) from Booking boo " +
            "join BerthPlace bp on boo.berthPlace = bp " +
            "where boo.status = ?4 and bp.berth = ?1 " +
            "and not (?3 < boo.startDate or ?2 > boo.endDate)")
    Integer countBookedPlacesInner(Berth berth, LocalDate from, LocalDate to, BookingStatus status);
}
