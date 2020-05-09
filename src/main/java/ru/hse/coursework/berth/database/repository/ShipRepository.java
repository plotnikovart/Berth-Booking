package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Ship;

import java.util.Collection;
import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query("select s from Ship s where s in (?1)")
    @EntityGraph(attributePaths = "photos")
    List<Ship> loadPhotos(Collection<Ship> ships);
}
