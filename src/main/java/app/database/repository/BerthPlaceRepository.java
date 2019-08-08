package app.database.repository;

import app.database.entity.BerthPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BerthPlaceRepository extends JpaRepository<BerthPlace, Long> {
}
