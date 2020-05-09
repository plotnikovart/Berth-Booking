package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Ship;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    List<Ship> findAllByOwner(Account owner);
}
