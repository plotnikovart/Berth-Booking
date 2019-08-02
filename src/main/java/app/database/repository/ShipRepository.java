package app.database.repository;

import app.database.entity.Ship;
import app.database.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @EntityGraph(attributePaths = "photos", type = EntityGraph.EntityGraphType.LOAD)
    List<Ship> findAllByUser(User user);
}
