package app.database.dao;

import app.database.model.ShipPhoto;
import org.springframework.data.repository.CrudRepository;

public interface ShipPhotoDao extends CrudRepository<ShipPhoto, ShipPhoto.ShipPhotoPK> {
}
