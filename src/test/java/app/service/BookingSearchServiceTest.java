package app.service;

import app.common.DateHelper;
import app.database.entity.Berth;
import app.database.entity.BerthPlace;
import app.database.entity.Booking;
import app.database.entity.Ship;
import app.database.entity.enums.BookingStatus;
import app.database.repository.*;
import app.web.dto.BerthDto;
import app.web.dto.ConvenienceDto;
import app.web.dto.request.BookingSearchRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
class BookingSearchServiceTest extends AbstractConvenienceTest {

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

    protected Berth berth1, berth2, berth3, berth4;
    protected BerthPlace place1, place2, place3, place4, place5, place6;
    protected Ship ship1, ship2;

    protected ConvenienceDto convDto1, convDto2, convDto3;

    private final Double LAT = 58.968403;
    private final Double LNG = 27.448905;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        berthRepository.deleteAll();
        shipRepository.deleteAll();

        convDto1 = conv1.getDto();
        convDto2 = conv2.getDto();
        convDto3 = conv3.getDto();

        // ======================================
        ship1 = new Ship()
                .setName("1")
                .setLength(1.5)
                .setWidth(1.5)
                .setDraft(1.5)
                .setUserInfo(userInfo);

        ship2 = new Ship()
                .setName("2")
                .setLength(2.5)
                .setWidth(2.5)
                .setDraft(2.5)
                .setUserInfo(userInfo);

        shipRepository.saveAll(List.of(ship1, ship2));

        // =====================================
        berth1 = new Berth()
                .setName("1")
                .setLat(58.950880)
                .setLng(27.397539)
                .setUserInfo(userInfo)
                .setStandardPrice(20.0)
                .setConveniences(List.of(conv1, conv2));

        place1 = new BerthPlace()
                .setBerth(berth1)
                .setLength(1.0)
                .setWidth(1.0)
                .setDraft(1.0);

        place2 = new BerthPlace()
                .setBerth(berth1)
                .setLength(2.0)
                .setWidth(2.0)
                .setDraft(2.0);

        berthRepository.save(berth1);
        berthPlaceRepository.saveAll(List.of(place1, place2));

        // =========================
        berth2 = new Berth()
                .setName("2")
                .setLat(58.977930)
                .setLng(27.428904)
                .setUserInfo(userInfo)
                .setStandardPrice(20.0)
                .setConveniences(List.of(conv1, conv2, conv3));

        place3 = new BerthPlace()
                .setBerth(berth2)
                .setLength(2.0)
                .setWidth(2.0)
                .setDraft(2.0);

        place4 = new BerthPlace()
                .setBerth(berth2)
                .setLength(3.0)
                .setWidth(3.0)
                .setDraft(3.0);

        berthRepository.save(berth2);
        berthPlaceRepository.saveAll(List.of(place3, place4));

        // ============================
        berth3 = new Berth()
                .setName("3")
                .setLat(58.980555)
                .setLng(27.486817)
                .setUserInfo(userInfo)
                .setStandardPrice(20.0)
                .setConveniences(List.of(conv2, conv3));

        place5 = new BerthPlace()
                .setBerth(berth3)
                .setLength(2.0)
                .setWidth(2.0)
                .setDraft(2.0);

        berthRepository.save(berth3);
        berthPlaceRepository.saveAll(List.of(place5));

        // ===========================
        berth4 = new Berth()
                .setName("4")
                .setLat(58.960529)
                .setLng(27.539061)
                .setUserInfo(userInfo)
                .setStandardPrice(20.0)
                .setConveniences(List.of(conv1));

        place6 = new BerthPlace()
                .setBerth(berth4)
                .setLength(3.0)
                .setWidth(3.0)
                .setDraft(3.0);

        berthRepository.save(berth4);
        berthPlaceRepository.saveAll(List.of(place6));

        commitAndStartNewTransaction();
    }

    @Test
    void test() {
        var booking1 = new Booking()
                .setBerthPlace(place2)
                .setShip(ship1)
                .setStartDate(LocalDate.of(2019, 6, 15))
                .setEndDate(LocalDate.of(2019, 6, 22))
                .setStatus(BookingStatus.APPROVED)
                .setOwner(userInfo)
                .setRenter(userInfo);

        var booking2 = new Booking()
                .setBerthPlace(place4)
                .setShip(ship2)
                .setStartDate(LocalDate.of(2019, 6, 15))
                .setEndDate(LocalDate.of(2019, 6, 20))
                .setStatus(BookingStatus.APPROVED)
                .setOwner(userInfo)
                .setRenter(userInfo);

        bookingRepository.saveAll(List.of(booking1, booking2));

        var req = new BookingSearchRequest()
                .setStartDate(DateHelper.convertToDate(LocalDate.of(2019, 6, 21)))
                .setEndDate(DateHelper.convertToDate(LocalDate.of(2019, 6, 23)))
                .setLat(LAT)
                .setLng(LNG)
                .setRad(2.6)
                .setShipId(ship1.getId())
                .setConvenienceList(List.of(convDto1, convDto2));

        var actual = bookingSearchService.searchPlaces(req);
        Assertions.assertEquals(2, calcPlaces(actual));         // 3, 4

        req.setConvenienceList(List.of());
        actual = bookingSearchService.searchPlaces(req);
        Assertions.assertEquals(3, calcPlaces(actual));         // 3, 4, 5

        req.setRad(4.0);
        actual = bookingSearchService.searchPlaces(req);
        Assertions.assertEquals(3, calcPlaces(actual));         // 3, 4, 5

        req.setRad(10.0);
        req.setShipId(ship2.getId());
        req.setConvenienceList(List.of(convDto1, convDto2, convDto3));
        actual = bookingSearchService.searchPlaces(req);
        Assertions.assertEquals(1, calcPlaces(actual));         // 4
    }


    private long calcPlaces(List<BerthDto.WithId> berthDto) {
        return berthDto.stream().mapToLong(dto -> dto.getPlaceList().size()).sum();
    }

}