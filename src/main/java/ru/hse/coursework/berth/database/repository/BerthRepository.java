package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Berth;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface BerthRepository extends JpaRepository<Berth, Long> {

    @Query("select b from Berth b where b in (?1)")
    @EntityGraph(attributePaths = "berthPlaces")
    List<Berth> loadPlaces(Collection<Berth> berths);

    @Query("select b from Berth b where b in (?1)")
    @EntityGraph(attributePaths = "amenities")
    List<Berth> loadAmenities(Collection<Berth> berths);

    List<Berth> findAllByOwner(Account owner);

    @Query("select b from Berth b where b.changeDate >= ?1")
    List<Berth> findChanged(LocalDateTime since);
}
