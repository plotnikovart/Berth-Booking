package app.database.repository;

import app.database.entity.BerthPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BerthPhotoRepository extends JpaRepository<BerthPhoto, BerthPhoto.PK> {
}
