package ru.hse.coursework.berth.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.database.entity.DictAmenity;

import java.util.List;

public class AbstractAmenityTest extends AbstractAccountTest {

    protected DictAmenity amenity1;
    protected DictAmenity amenity2;
    protected DictAmenity amenity3;

    @Autowired
    DictAmenityRepository amenityRepository;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        amenityRepository.deleteAll();

        amenity1 = new DictAmenity()
                .setKey("wifi")
                .setValue("Вай фай");

        amenity2 = new DictAmenity()
                .setKey("gas")
                .setValue("Заправка");

        amenity3 = new DictAmenity()
                .setKey("grocery_store")
                .setValue("Магазин");

        amenityRepository.saveAll(List.of(amenity1, amenity2, amenity3));
    }
}
