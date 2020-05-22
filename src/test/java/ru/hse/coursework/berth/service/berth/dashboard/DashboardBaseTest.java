package ru.hse.coursework.berth.service.berth.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.entity.enums.ShipType;
import ru.hse.coursework.berth.database.repository.AbstractAmenityTest;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.util.ConstructHelper;

import java.util.List;

public abstract class DashboardBaseTest extends AbstractAmenityTest {

    @Autowired
    BerthRepository berthRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ShipRepository shipRepository;

    protected Berth berth1;
    protected BerthPlace berth1Place1, berth1Place2, berth1Place3;

    protected Berth berth2;

    protected Ship ship;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        bookingRepository.deleteAll();
        berthRepository.deleteAll();
        shipRepository.deleteAll();

        OperationContext.accountId(user1Account.getId());

        /////////////////////////////////
        berth1 = new Berth()
                .setIsConfirmed(true)
                .setLng(1.0)
                .setLat(1.0)
                .setOwner(user1Account)
                .setName("berth 1");

        berth1Place1 = ConstructHelper.buildBerthPlace(berth1, "berth1Place1", 10.0, 1.0, 1.0, 1.0);
        berth1Place2 = ConstructHelper.buildBerthPlace(berth1, "berth1Place2", 20.0, 2.0, 2.0, 2.0);
        berth1Place3 = ConstructHelper.buildBerthPlace(berth1, "berth3Place2", 30.0, 3.0, 3.0, 3.0);

        berth1.setBerthPlaces(List.of(berth1Place1, berth1Place2, berth1Place3));
        berth1.setAmenities(List.of(amenity1, amenity2));
        berthRepository.save(berth1);

        ////////////////////////////////
        berth2 = new Berth()
                .setIsConfirmed(true)
                .setLng(2.0)
                .setLat(2.0)
                .setOwner(user1Account)
                .setName("berth 2");

        berthRepository.save(berth2);


        ///////////////////////////////
        ship = new Ship()
                .setName("ship")
                .setOwner(user2Account)
                .setType(ShipType.POWER)
                .setWidth(1.0)
                .setLength(1.0)
                .setDraft(1.0);

        shipRepository.save(ship);
    }
}
