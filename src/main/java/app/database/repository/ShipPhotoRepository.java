package app.database.repository;

import app.database.entity.ShipPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipPhotoRepository extends JpaRepository<ShipPhoto, ShipPhoto.PK> {
}
