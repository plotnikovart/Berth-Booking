package app.database.dao;

import app.database.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipDao extends JpaRepository<Ship, Long> {
}
