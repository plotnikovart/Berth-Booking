package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.ShipPhoto;

@Repository
public interface ShipPhotoRepository extends JpaRepository<ShipPhoto, ShipPhoto.PK> {
}
