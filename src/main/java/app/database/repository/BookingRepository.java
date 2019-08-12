package app.database.repository;

import app.database.entity.Berth;
import app.database.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b " +
            "join BerthPlace bp on b.berthPlace = bp " +
            "where bp.berth = ?1 " +
            "order by b.id desc")
    List<Booking> findAllByBerth(Berth berth);
}
