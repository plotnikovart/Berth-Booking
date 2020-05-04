package app.web;

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
import app.web.dto.DictAmenityDto;
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

//    @Test
//    void crud() throws Exception {
//        var placeList = List.of(
//                new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0),
//                new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0).setPrice(11.0)
//        );
//
//        var convenienceList = List.of(convConverter.toDto(conv1), convConverter.toDto(conv2));
//
//        var dto = (BerthDto.Req) new BerthDto.Req()
//                .setPhotoList(List.of("photo1", "photo2"))
//                .setValue("1")
//                .setDescription("asada")
//                .setStandardPrice(12.5)
//                .setLat(12.0)
//                .setLng(13.0)
//                .setPlaceList(placeList)
//                .setConvenienceList(convenienceList);
//
//
//        // post
//        MvcResult actual = mvc.perform(post("/api/berths")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        var response = mapper.readValue(actual.getResponse().getContentAsString(), IdResponse.class);
//        var dtoResp = new BerthDto.Resp()
//                .setId(Long.valueOf((Integer) response.getId()));
//        syncDto(dto, dtoResp);
//
//        placeList.get(0).setId(0L);
//        placeList.get(1).setId(1L);
//
//        commitAndStartNewTransaction();
//
//        // get
//        mvc.perform(get("/api/berths/" + dtoResp.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));
//
//        // put
//        dto.setValue("122");
//        dto.setPhotoList(List.of("photo1", "photo3"));
//        dto.setPlaceList(List.of(placeList.get(0).setLength(2.0), new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0).setPrice(1.0)));
//        dto.setConvenienceList(List.of(convConverter.toDto(conv1)));
//
//        mvc.perform(put("/api/berths/" + dtoResp.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//
//        syncDto(dto, dtoResp);
//        dto.getPlaceList().get(1).setId(2L);
//
//        commitAndStartNewTransaction();
//
//        // get
//        mvc.perform(get("/api/berths/" + dtoResp.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));
//
//        // delete
//        mvc.perform(delete("/api/berths/" + dtoResp.getId()))
//                .andExpect(status().isOk());
//        commitAndStartNewTransaction();
//
//        // GET ALL
//        mvc.perform(get("/api/berths"))
//                .andExpect(content().json((mapper.writeValueAsString(List.of()))));
//    }
//
//
//    private void syncDto(BerthDto.Req req, BerthDto.Resp resp) {
//        var photoList = req.getPhotoList().stream()
//                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.BERTH.value().toLowerCase(), account.getId(), photo))
//                .collect(Collectors.toList());
//
//        resp
//                .setPhotoList(photoList)
//                .setValue(req.getValue())
//                .setDescription(req.getDescription())
//                .setStandardPrice(req.getStandardPrice())
//                .setLat(req.getLat())
//                .setLng(req.getLng())
//                .setPlaceList(req.getPlaceList())
//                .setConvenienceList(req.getConvenienceList());
//    }
}