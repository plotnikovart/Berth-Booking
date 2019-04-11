package app.database.dao;

import app.common.CrudRepositoryWithValidation;
import app.database.model.Ship;
import org.springframework.data.repository.CrudRepository;

public interface ShipDao extends CrudRepositoryWithValidation<Ship, Long> {
}
