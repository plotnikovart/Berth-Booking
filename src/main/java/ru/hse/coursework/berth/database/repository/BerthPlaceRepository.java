package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.BerthPlace;

import java.util.Collection;
import java.util.List;

@Repository
public interface BerthPlaceRepository extends JpaRepository<BerthPlace, Long> {

    @Query("select bp from BerthPlace bp where bp in (?1)")
    @EntityGraph(attributePaths = "berth.photos")
    List<BerthPlace> loadBerthsWithPhotos(Collection<BerthPlace> berths);
}
