package app.web;

import app.config.exception.impl.NotFoundException;
import app.config.security.OperationContext;
import app.database.entity.enums.BerthApplicationStatus;
import app.database.repository.AbstractAmenityTest;
import app.database.repository.BerthApplicationRepository;
import app.database.repository.BerthRepository;
import app.service.berth.dto.BerthApplicationDto;
import app.service.berth.dto.BerthDto;
import app.service.file.FileStorageService;
import app.service.file.dto.FileInfoDto;
import app.util.CompareHelper;
import app.web.dto.BerthPlaceDto;
import app.web.dto.DictAmenityDto;
import one.util.streamex.StreamEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class BerthControllerTest extends AbstractAmenityTest {

    @Autowired
    BerthRepository berthRepository;
    @Autowired
    BerthApplicationRepository berthApplicationRepository;
    @Autowired
    BerthController berthController;
    @Autowired
    FileStorageService fileStorageService;

    private FileInfoDto file1;
    private FileInfoDto file2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        berthApplicationRepository.deleteAll();
        berthRepository.deleteAll();

        file1 = fileStorageService.saveFile(new byte[]{1}, "файл1.jpg");
        file2 = fileStorageService.saveFile(new byte[]{2}, "файл2.jpg");

        OperationContext.accountId(userAccount.getId());
    }

    @Test
    void application() {
        // create
        var berthDto = new BerthDto()
                .setName("Причал")
                .setDescription("Описание")
                .setLat(0.0)
                .setLng(0.0)
                .setAmenities(List.of(DictAmenityDto.of(amenity1)))
                .setPhotos(List.of(file1, file2));

        var applicationDto = (BerthApplicationDto.Req) new BerthApplicationDto.Req()
                .setBerth(berthDto)
                .setAttachments(List.of(file1, file2))
                .setDescription("Описание заявки");

        var actual = berthController.createApplication(applicationDto).getData();

        var expected = (BerthApplicationDto.Resp) new BerthApplicationDto.Resp()
                .setStatus(BerthApplicationStatus.NEW)
                .setTitle(berthDto.getName())
                .setDescription(applicationDto.getDescription())
                .setAttachments(applicationDto.getAttachments());

        Assertions.assertTrue(CompareHelper.areEqual(expected, actual));

        var allApplications = berthController.getApplications().getData();
        Assertions.assertEquals(1, allApplications.size());
    }

    @Test
    void crud() throws Exception {
        var places = List.of(
                new BerthPlaceDto()
                        .setName("Место 1")
                        .setDraft(1.0)
                        .setWidth(1.0)
                        .setLength(1.0)
                        .setPrice(11.0)
                        .setXCoord(0.0)
                        .setYCoord(0.0)
                        .setRotate(0.0)
                        .setColor("color"),
                new BerthPlaceDto()
                        .setName("Место 2")
                        .setDraft(1.0)
                        .setWidth(1.0)
                        .setLength(1.0)
                        .setPrice(11.0)
                        .setXCoord(0.0)
                        .setYCoord(0.0)
                        .setRotate(0.0)
                        .setColor("color")
        );

        var amenities = List.of(
                DictAmenityDto.of(amenity1),
                DictAmenityDto.of(amenity2)
        );

        var berthDto = new BerthDto()
                .setName("Причал")
                .setDescription("Описание")
                .setLat(0.0)
                .setLng(0.0)
                .setRadio("radio")
                .setSite("site")
                .setPhCode("ru")
                .setPhNumber("88888888")
                .setAmenities(amenities)
                .setPlaces(places)
                .setPhotos(List.of(file1, file2));

        var applicationDto = new BerthApplicationDto.Req()
                .setBerth(berthDto);

        var berthId = berthController.createApplication(applicationDto).getData().getBerthId();

        // GET ONE
        var expected = (BerthDto.Resp) new BerthDto.Resp()
                .setIsConfirmed(false)
                .setName(berthDto.getName())
                .setDescription(berthDto.getDescription())
                .setLat(berthDto.getLat())
                .setLng(berthDto.getLng())
                .setRadio(berthDto.getRadio())
                .setSite(berthDto.getSite())
                .setPhCode(berthDto.getPhCode())
                .setPhNumber(berthDto.getPhNumber())
                .setAmenities(StreamEx.of(berthDto.getAmenities()).sortedBy(DictAmenityDto::getKey).toList())
                .setPlaces(berthDto.getPlaces())
                .setPhotos(berthDto.getPhotos());

        var actual = berthController.getBerth(berthId, null).getData();
        Assertions.assertFalse(CompareHelper.areEqual(expected, actual));

        actual = berthController.getBerth(berthId, List.of("places")).getData();
        Assertions.assertFalse(CompareHelper.areEqual(expected, actual));

        actual = berthController.getBerth(berthId, List.of("places", "amenities")).getData();
        Assertions.assertTrue(CompareHelper.areEqual(expected, actual));

        // GET ALL
        var expected2 = List.of(expected);

        var actual2 = berthController.getAllBerths(null).getData();
        Assertions.assertFalse(CompareHelper.areEqualBerths(expected2, actual2));

        actual2 = berthController.getAllBerths(List.of("places")).getData();
        Assertions.assertFalse(CompareHelper.areEqualBerths(expected2, actual2));

        actual2 = berthController.getAllBerths(List.of("places", "amenities")).getData();
        Assertions.assertTrue(CompareHelper.areEqualBerths(expected2, actual2));

        // GET PLACES
        var pl = berthController.getBerthPlaces(berthId).getData();
        Assertions.assertTrue(CompareHelper.areEqualPlaces(expected.getPlaces(), pl));

        // GET AMENITIES
        var am = berthController.getBerthAmenities(berthId).getData();
        Assertions.assertEquals(expected.getAmenities(), am);


        // UPDATE
        berthDto
                .setPlaces(List.of())
                .setSite(null);

        var expected3 = (BerthDto.Resp) expected
                .setPlaces(berthDto.getPlaces())
                .setSite(berthDto.getSite());

        var actual3 = berthController.updateBerth(berthDto, berthId).getData();
        Assertions.assertTrue(CompareHelper.areEqual(expected3, actual3));


        // DELETE
        berthController.deleteBerth(berthId);
        Assertions.assertThrows(NotFoundException.class, () -> berthController.getBerth(berthId, null));
        Assertions.assertEquals(0, berthController.getAllBerths(null).getData().size());
        Assertions.assertThrows(NotFoundException.class, () -> berthController.getBerthPlaces(berthId).getData().size());
        Assertions.assertThrows(NotFoundException.class, () -> berthController.getBerthAmenities(berthId).getData().size());
    }
}