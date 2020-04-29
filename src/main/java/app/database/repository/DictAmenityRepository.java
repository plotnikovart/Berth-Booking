package app.database.repository;

import app.database.entity.DictAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictAmenityRepository extends JpaRepository<DictAmenity, String> {
}
