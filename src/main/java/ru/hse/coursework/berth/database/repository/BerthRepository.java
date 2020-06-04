package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Berth;

import java.util.Collection;
import java.util.List;

@Repository
public interface BerthRepository extends JpaRepository<Berth, Long>, BerthRepositoryCustom {

    @Query("select b from Berth b where b in (?1)")
    @EntityGraph(attributePaths = "berthPlaces")
    List<Berth> loadPlaces(Collection<Berth> berths);

    @Query("select b from Berth b where b in (?1)")
    @EntityGraph(attributePaths = "amenities")
    List<Berth> loadAmenities(Collection<Berth> berths);

    List<Berth> findAllByOwner(Account owner);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update Berth b set b.avgRating = (select avg(r.rating) from Review r WHERE r.berth = ?1) where b = ?1")
    void updateRating(Berth berth);


    @Query("select count(bp) from BerthPlace bp where bp.berth = ?1")
    Integer countBerthPlaces(Berth berth);
}
