package app.database.dao;

import app.database.model.Ship;
import org.springframework.data.repository.CrudRepository;

public interface ShipDao extends CrudRepository<Ship, Long> {
}
