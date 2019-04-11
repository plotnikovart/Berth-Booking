package app.database.dao;

import app.database.model.Ship;
import app.database.model.ShipPhoto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.List;

import static org.junit.Assert.assertEquals;

class ShipDaoTest extends AbstractUserTest {

    @Autowired
    private ShipDao shipDao;

    @BeforeEach
    void setUp() {
        super.setUp();
        shipDao.deleteAll();
    }

    @Test
    void CRUD() {
        var ship = new Ship();
        ship.setUser(user);
        ship.setName("Судно 1");
        ship.setLength(12.0);
        ship.setDraft(4.0);
        ship.setWidth(1.0);

        var photo1 = new ShipPhoto(ship, 1, "asdasdd");
        var photo2 = new ShipPhoto(ship, 2, "assdd");
        ship.setPhotos(List.of(photo1, photo2));

        shipDao.save(ship);
        user.getShips().add(ship);

        tpl.execute(status -> {
            Ship shipFromDb = shipDao.findById(ship.getId()).orElseThrow();
            ReflectionAssert.assertReflectionEquals(ship, shipFromDb);
            return null;
        });

        ship.setPhotos(List.of(photo1));
        shipDao.save(ship);
        tpl.execute(status -> {
            Ship shipFromDb = shipDao.findById(ship.getId()).orElseThrow();
            assertEquals(1, shipFromDb.getPhotos().size());
            return null;
        });
    }
}