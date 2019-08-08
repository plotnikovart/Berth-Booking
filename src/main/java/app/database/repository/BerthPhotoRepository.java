package app.database.repository;

import app.database.entity.BerthPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BerthPhotoRepository extends JpaRepository<BerthPhoto, BerthPhoto.PK> {
}
