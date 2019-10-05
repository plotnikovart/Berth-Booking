package app.web;

import app.common.OperationContext;
import app.database.repository.AbstractAccountTest;
import app.database.repository.ShipRepository;
import app.service.file.ImageKind;
import app.web.dto.ShipDto;
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
class ShipControllerTest extends AbstractAccountTest {

    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    private ShipController shipController;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        mvc = MockMvcBuilders.standaloneSetup(shipController).build();
        OperationContext.setAccountId(userInfo.getAccountId());

        shipRepository.deleteAll();
    }

    @Test
    void crud() throws Exception {
        // POST
        var dto = new ShipDto.Req();
        dto.setName("ship1");
        dto.setLength(12.0);
        dto.setWidth(8.0);
        dto.setDraft(3.0);
        dto.setPhotoList(List.of("photo1"));

        MvcResult result = mvc.perform(post("/api/ships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        var response = mapper.readValue(result.getResponse().getContentAsString(), IdResponse.class);

        var dtoResp = new ShipDto.Resp()
                .setId(Long.valueOf((Integer) response.getId()));
        syncDto(dto, dtoResp);

        commitAndStartNewTransaction();

        // GET
        mvc.perform(get("/api/ships/" + dtoResp.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));

        // PUT
        dto.setName("122");
        dto.setPhotoList(List.of("photo1", "photo2"));
        syncDto(dto, dtoResp);

        mvc.perform(put("/api/ships/" + dtoResp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        // GET
        mvc.perform(get("/api/ships/" + dtoResp.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));

        // DELETE
        mvc.perform(delete("/api/ships/" + dtoResp.getId()))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        // GET ALL
        mvc.perform(get("/api/ships"))
                .andExpect(content().json((mapper.writeValueAsString(List.of()))));
    }

    private void syncDto(ShipDto.Req req, ShipDto.Resp resp) {
        var photoList = req.getPhotoList().stream()
                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.SHIP.name().toLowerCase(), account.getId(), photo))
                .collect(Collectors.toList());

        resp
                .setPhotoList(photoList)
                .setName(req.getName())
                .setLength(req.getLength())
                .setWidth(req.getWidth())
                .setDraft(req.getDraft());
    }
}