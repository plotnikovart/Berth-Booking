package app.web;

import app.database.repository.AbstractConvenienceTest;
import app.database.repository.BerthRepository;
import app.web.dto.BerthDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.response.IdResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
                (BerthPlaceDto.WithId) new BerthPlaceDto.WithId().setDraft(1.0).setWidth(1.0).setLength(1.0),
                (BerthPlaceDto.WithId) new BerthPlaceDto.WithId().setDraft(1.0).setWidth(1.0).setLength(1.0).setPrice(11.0)
        );

        var convenienceList = List.of(conv1.getDto(), conv2.getDto());

        var dto = (BerthDto.WithId) new BerthDto.WithId()
                .setName("1")
                .setDescription("asada")
                .setStandardPrice(12.5)
                .setLat(12.0)
                .setLng(13.0)
                .setPhotoList(List.of("photo1", "photo2"))
                .setPlaceList(placeList)
                .setConvenienceList(convenienceList);

        // post
        MvcResult actual = mvc.perform(post("/api/berths")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        dto.getPlaceList().get(0).setId(0L);
        dto.getPlaceList().get(1).setId(1L);

        var response = mapper.readValue(actual.getResponse().getContentAsString(), IdResponse.class);
        dto.setId(Long.valueOf((Integer) response.getId()));
        commitAndStartNewTransaction();

        // get
        actual = mvc.perform(get("/api/berths/" + dto.getId()))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(dto, mapper.readValue(actual.getResponse().getContentAsString(), BerthDto.WithId.class));

        // put
        dto.setName("122");
        dto.setPhotoList(List.of("photo1", "photo3"));
        dto.setPlaceList(List.of((BerthPlaceDto.WithId) new BerthPlaceDto.WithId().setDraft(1.0).setWidth(1.0).setLength(1.0)));
        dto.setConvenienceList(List.of(conv1.getDto()));

        mvc.perform(put("/api/berths/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        dto.getPlaceList().get(0).setId(2L);

        // get
        actual = mvc.perform(get("/api/berths/" + dto.getId()))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertEquals(dto, mapper.readValue(actual.getResponse().getContentAsString(), BerthDto.WithId.class));

        // delete
        mvc.perform(delete("/api/berths/" + dto.getId()))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        // GET ALL
        mvc.perform(get("/api/berths"))
                .andExpect(content().json((mapper.writeValueAsString(List.of()))));
    }

    private String rightJson(MvcResult result) throws IOException {
        var dto = mapper.readValue(result.getResponse().getContentAsString(), BerthDto.WithId.class);
        return mapper.writeValueAsString(dto);
    }

}