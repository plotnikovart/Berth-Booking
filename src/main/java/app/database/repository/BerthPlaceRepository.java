package app.database.repository;

import app.database.entity.BerthPlace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BerthPlaceRepository extends JpaRepository<BerthPlace, Long> {

    @Query("select bp from BerthPlace bp where bp in (?1)")
    @EntityGraph(attributePaths = "berth.photos")
    List<BerthPlace> loadBerths(Collection<BerthPlace> berths);
}
