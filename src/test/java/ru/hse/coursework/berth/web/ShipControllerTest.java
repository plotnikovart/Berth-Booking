package ru.hse.coursework.berth.web;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.enums.ShipType;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.service.file.FileStorageService;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;
import ru.hse.coursework.berth.service.ship.dto.*;

import java.time.LocalDate;
import java.util.List;

class ShipControllerTest extends AbstractAccountTest {

    @Autowired
    ShipRepository shipRepository;
    @Autowired
    private ShipController shipController;
    @Autowired
    FileStorageService fileStorageService;

    private FileInfoDto file1;
    private FileInfoDto file2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        shipRepository.deleteAll();

        file1 = fileStorageService.saveFile(new byte[]{1}, "файл1.jpg");
        file2 = fileStorageService.saveFile(new byte[]{2}, "файл2.jpg");
    }

    @Test
    void crud() {
        OperationContext.accountId(moderatorAccount.getId());

        var dimensions = new Dimensions()
                .setWidth(1.0)
                .setLength(2.0)
                .setDraft(3.0);
        var insurance = new Insurance()
                .setFile(file1)
                .setNumber("number 1")
                .setExpire(LocalDate.now())
                .setCompany("company");
        var registration = new Registration()
                .setFile(file1)
                .setExpire(LocalDate.now().plusDays(1))
                .setNumber("number2");
        var model = new Model()
                .setYear(2020)
                .setProducer("producer 1")
                .setModel("model 1");

        var shipDto = new ShipDto()
                .setName("Корабль 1")
                .setType(ShipType.POWER)
                .setPhotos(List.of(file1, file2))
                .setDimensions(dimensions)
                .setModel(model)
                .setInsurance(insurance)
                .setRegistration(registration);

        // CREATE
        ShipDto.Resp expected = (ShipDto.Resp) new ShipDto.Resp()
                .setName(shipDto.getName())
                .setType(shipDto.getType())
                .setPhotos(shipDto.getPhotos())
                .setDimensions(SerializationUtils.clone(shipDto.getDimensions()))
                .setModel(SerializationUtils.clone(shipDto.getModel()))
                .setInsurance(SerializationUtils.clone(shipDto.getInsurance()))
                .setRegistration(SerializationUtils.clone(shipDto.getRegistration()));

        ShipDto.Resp actual = shipController.createShip(shipDto).getData();
        Assertions.assertEquals(expected.setId(actual.getId()), actual);

        Long shipId = actual.getId();

        // UPDATE
        shipDto
                .setType(ShipType.SAIL)
                .setPhotos(null)
                .getInsurance().setCompany("company 2");
        expected
                .setType(shipDto.getType())
                .setInsurance(shipDto.getInsurance());

        actual = shipController.updateShip(shipId, shipDto).getData();
        Assertions.assertEquals(expected, actual);

        // UPDATE ANOTHER USER SHIP
        OperationContext.accountId(userAccount.getId());
        Assertions.assertThrows(AccessException.class, () -> shipController.updateShip(shipId, shipDto));
        OperationContext.accountId(moderatorAccount.getId());

        // GET ALL
        List<ShipDto.Resp> actual2 = shipController.getShips().getData();
        List<ShipDto.Resp> expected2 = List.of(expected);
        Assertions.assertEquals(expected2, actual2);

        // DELETE ANOTHER USER SHIP
        OperationContext.accountId(userAccount.getId());
        Assertions.assertThrows(AccessException.class, () -> shipController.deleteShip(shipId));
        OperationContext.accountId(moderatorAccount.getId());

        // DELETE
        shipController.deleteShip(shipId);
        Assertions.assertEquals(0, shipController.getShips().getData().size());
    }
}