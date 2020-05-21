package ru.hse.coursework.berth.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.entity.enums.ShipType;
import ru.hse.coursework.berth.database.repository.*;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;
import ru.hse.coursework.berth.service.booking.BookingSearchService;
import ru.hse.coursework.berth.service.booking.dto.BookingSearchReq;
import ru.hse.coursework.berth.service.converters.impl.DictAmenityConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.hse.coursework.berth.util.ConstructHelper.buildBerthPlace;

class BookingSearchServiceTest extends AbstractAmenityTest {

    @Autowired
    ShipRepository shipRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BerthRepository berthRepository;
    @Autowired
    BerthPlaceRepository berthPlaceRepository;
    @Autowired
    BookingSearchService bookingSearchService;
    @Autowired
    DictAmenityConverter amenityConverter;

    protected Berth berth1, berth2, berth3, berth4, berth5Unconfirmed;
    protected BerthPlace place1, place2, place3, place4, place5, place6, place7;
    protected Ship ship1, ship2;

    protected DictAmenityDto amDto1, amDto2, amDto3;

    private final Double LAT = 58.968403;
    private final Double LNG = 27.448905;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        bookingRepository.deleteAll();
        berthRepository.deleteAll();
        shipRepository.deleteAll();

        amDto1 = amenityConverter.toDto(amenity1);
        amDto2 = amenityConverter.toDto(amenity2);
        amDto3 = amenityConverter.toDto(amenity3);

        // ======================================
        OperationContext.accountId(user1Account.getId());

        ship1 = new Ship()
                .setName("1")
                .setType(ShipType.POWER)
                .setLength(1.5)
                .setWidth(1.5)
                .setDraft(1.5)
                .setOwner(user1Account);

        ship2 = new Ship()
                .setName("2")
                .setType(ShipType.POWER)
                .setLength(2.5)
                .setWidth(2.5)
                .setDraft(2.5)
                .setOwner(user1Account);

        shipRepository.saveAll(List.of(ship1, ship2));

        // =====================================
        berth1 = new Berth()    // 3.53 km
                .setName("1")
                .setLat(58.950880)
                .setLng(27.397539)
                .setOwner(user2Account)
                .setIsConfirmed(true)
                .setAmenities(List.of(amenity1, amenity2));

        place1 = buildBerthPlace(berth1, "place1", 10.0, 1.0, 1.0, 1.0);
        place2 = buildBerthPlace(berth1, "place2", 20.0, 2.0, 2.0, 2.0);

        berthRepository.save(berth1);
        berthPlaceRepository.saveAll(List.of(place1, place2));

        // =========================
        berth2 = new Berth()    // 1.56 km
                .setName("2")
                .setLat(58.977930)
                .setLng(27.428904)
                .setOwner(user2Account)
                .setIsConfirmed(true)
                .setAmenities(List.of(amenity1, amenity2, amenity3));

        place3 = buildBerthPlace(berth2, "place3", 20.0, 2.0, 2.0, 2.0);
        place4 = buildBerthPlace(berth2, "place4", 30.0, 3.0, 3.0, 3.0);

        berthRepository.save(berth2);
        berthPlaceRepository.saveAll(List.of(place3, place4));

        // ============================
        berth3 = new Berth()    // 2.55 km
                .setName("3")
                .setLat(58.980555)
                .setLng(27.486817)
                .setOwner(user2Account)
                .setIsConfirmed(true)
                .setAmenities(List.of(amenity1, amenity2));

        place5 = buildBerthPlace(berth3, "place5", 20.0, 2.0, 2.0, 2.0);

        berthRepository.save(berth3);
        berthPlaceRepository.saveAll(List.of(place5));

        // ===========================
        berth4 = new Berth()    // 5.24 km
                .setName("4")
                .setLat(58.960529)
                .setLng(27.539061)
                .setOwner(user2Account)
                .setIsConfirmed(true)
                .setAmenities(List.of(amenity1));

        place6 = buildBerthPlace(berth4, "place6", 30.0, 3.0, 3.0, 3.0);

        berthRepository.save(berth4);
        berthPlaceRepository.saveAll(List.of(place6));

        // ===========================
        berth5Unconfirmed = new Berth()    // 1.56 km
                .setName("2")
                .setLat(58.977930)
                .setLng(27.428904)
                .setOwner(user2Account)
                .setIsConfirmed(false)
                .setAmenities(List.of(amenity1, amenity2, amenity3));

        place7 = buildBerthPlace(berth5Unconfirmed, "place7", 30.0, 3.0, 3.0, 3.0);

        berthRepository.save(berth5Unconfirmed);
        berthPlaceRepository.saveAll(List.of(place7));
    }

    @Test
    @SuppressWarnings("deprecation")
    void test() {
        var booking1 = new Booking()
                .setBerthPlace(place2)
                .setShip(ship1)
                .setStartDate(LocalDate.of(2019, 6, 15))
                .setEndDate(LocalDate.of(2019, 6, 22))
                .setStatus(BookingStatus.PAYED)
                .setRenter(user1Account)
                .setTotalPrice(0.0)
                .setServiceFee(0.0);

        var booking2 = new Booking()
                .setBerthPlace(place4)
                .setShip(ship2)
                .setStartDate(LocalDate.of(2019, 6, 15))
                .setEndDate(LocalDate.of(2019, 6, 20))
                .setStatus(BookingStatus.PAYED)
                .setRenter(user1Account)
                .setTotalPrice(0.0)
                .setServiceFee(0.0);

        var booking3 = new Booking()    // ничего не меняющее бронирование, так как статус не оплачено
                .setBerthPlace(place3)
                .setShip(ship1)
                .setStartDate(LocalDate.of(2000, 6, 15))
                .setEndDate(LocalDate.of(2030, 6, 20))
                .setStatus(BookingStatus.APPROVED)
                .setRenter(user1Account)
                .setTotalPrice(0.0)
                .setServiceFee(0.0);

        bookingRepository.saveAll(List.of(booking1, booking2, booking3));


        // поиск с минимальным набором параметров (неподтвержденный причал не включается в поиск)
        var req = new BookingSearchReq()
                .setLat(LAT)
                .setLng(LNG)
                .setRad(2.7);

        var resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place3, place4, place5));


        // нет 4 места из-за даты бронирования, причал 1 и 4 не попадают из-за радиуса
        req = new BookingSearchReq()
                .setStartDate(LocalDate.of(2019, 6, 20))
                .setEndDate(LocalDate.of(2019, 6, 23))
                .setLat(LAT)
                .setLng(LNG)
                .setRad(2.6)
                .setShipId(ship1.getId())
                .setAmenities(List.of(amDto1, amDto2));

        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place3, place5));

        // нет 5 места из-за удобств, причал 1 и 4 не попадают из-за радиуса
        req = new BookingSearchReq()
                .setStartDate(LocalDate.of(2019, 6, 21))
                .setEndDate(LocalDate.of(2019, 6, 23))
                .setLat(LAT)
                .setLng(LNG)
                .setRad(2.6)
                .setShipId(ship1.getId())
                .setAmenities(List.of(amDto1, amDto2, amDto3));

        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place3, place4));

        // причал 1 и 4 не попадают из-за радиуса
        req = new BookingSearchReq()
                .setStartDate(LocalDate.of(2019, 6, 21))
                .setEndDate(LocalDate.of(2019, 6, 23))
                .setLat(LAT)
                .setLng(LNG)
                .setRad(2.6)
                .setShipId(ship1.getId())
                .setAmenities(null);

        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place3, place4, place5));


        // расширяем радиус поиска, нет 4 и 2 мест из-за дат бронирования, нет place 1 из-за размера лодки
        req = new BookingSearchReq()
                .setStartDate(LocalDate.of(2019, 6, 15))
                .setEndDate(LocalDate.of(2019, 6, 23))
                .setLat(LAT)
                .setLng(LNG)
                .setRad(4.4)
                .setShipId(ship1.getId())
                .setAmenities(null);

        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place3, place5));

        // меняем лодку на еще больше и ничего не находим
        req.setShipId(ship2.getId());
        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp));

        // расширяем радиус поиска для большой лодки
        req.setRad(10.0);
        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place6));

        // добавляем обязательные amenity
        req.setAmenities(List.of(amDto1));
        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp, place6));

        req.setAmenities(List.of(amDto1, amDto2));
        resp = bookingSearchService.searchPlaces(req);
        Assertions.assertTrue(containsOnly(resp));
    }


    private boolean containsOnly(List<BerthDto.Resp.Search> resp, BerthPlace... places) {
        Set<Long> foundPlacesIds = resp.stream()
                .flatMap(it -> it.getPlaces().stream())
                .map(BerthPlaceDto::getId)
                .collect(Collectors.toSet());

        if (foundPlacesIds.size() > places.length) {
            throw new ServiceException("Found places has too many items");
        }

        for (var place : places) {
            if (!foundPlacesIds.contains(place.getId())) {
                throw new ServiceException("Expected place with id = " + place.getId());
            }
        }

        return true;
    }
}