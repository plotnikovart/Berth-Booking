package app.web;

import app.database.repository.AbstractConvenienceTest;
import app.database.repository.BerthRepository;
import app.service.file.ImageKind;
import app.web.dto.BerthDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.response.IdResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class BerthControllerTest extends AbstractConvenienceTest {

    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    BerthRepository berthRepository;
    @Autowired
    private BerthController shipController;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        mvc = MockMvcBuilders.standaloneSetup(shipController).build();
        berthRepository.deleteAll();
        commitAndStartNewTransaction();
    }

    @Test
    void crud() throws Exception {
        var placeList = List.of(
                new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0),
                new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0).setPrice(11.0)
        );

        var convenienceList = List.of(convConverter.toDto(conv1), convConverter.toDto(conv2));

        var dto = (BerthDto.Req) new BerthDto.Req()
                .setPhotoList(List.of("photo1", "photo2"))
                .setName("1")
                .setDescription("asada")
                .setStandardPrice(12.5)
                .setLat(12.0)
                .setLng(13.0)
                .setPlaceList(placeList)
                .setConvenienceList(convenienceList);


        // post
        MvcResult actual = mvc.perform(post("/api/berths")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        var response = mapper.readValue(actual.getResponse().getContentAsString(), IdResponse.class);
        var dtoResp = new BerthDto.Resp()
                .setId(Long.valueOf((Integer) response.getId()));
        syncDto(dto, dtoResp);

        placeList.get(0).setId(0L);
        placeList.get(1).setId(1L);

        commitAndStartNewTransaction();

        // get
        mvc.perform(get("/api/berths/" + dtoResp.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));

        // put
        dto.setName("122");
        dto.setPhotoList(List.of("photo1", "photo3"));
        dto.setPlaceList(List.of(placeList.get(0).setLength(2.0), new BerthPlaceDto().setDraft(1.0).setWidth(1.0).setLength(1.0).setPrice(1.0)));
        dto.setConvenienceList(List.of(convConverter.toDto(conv1)));

        mvc.perform(put("/api/berths/" + dtoResp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        syncDto(dto, dtoResp);
        dto.getPlaceList().get(1).setId(2L);

        commitAndStartNewTransaction();

        // get
        mvc.perform(get("/api/berths/" + dtoResp.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));

        // delete
        mvc.perform(delete("/api/berths/" + dtoResp.getId()))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        // GET ALL
        mvc.perform(get("/api/berths"))
                .andExpect(content().json((mapper.writeValueAsString(List.of()))));
    }


    private void syncDto(BerthDto.Req req, BerthDto.Resp resp) {
        var photoList = req.getPhotoList().stream()
                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.BERTH.name().toLowerCase(), account.getId(), photo))
                .collect(Collectors.toList());

        resp
                .setPhotoList(photoList)
                .setName(req.getName())
                .setDescription(req.getDescription())
                .setStandardPrice(req.getStandardPrice())
                .setLat(req.getLat())
                .setLng(req.getLng())
                .setPlaceList(req.getPlaceList())
                .setConvenienceList(req.getConvenienceList());
    }

}