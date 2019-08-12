package app.database.repository;

import app.database.entity.ShipPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipPhotoRepository extends JpaRepository<ShipPhoto, ShipPhoto.PK> {
}
