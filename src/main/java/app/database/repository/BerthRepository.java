package app.database.repository;

import app.database.entity.Account;
import app.database.entity.Berth;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
