package app.controller;

import app.common.IdResponse;
import app.common.OperationContext;
import app.database.dao.AbstractUserTest;
import app.database.dao.ShipDao;
import app.database.model.Ship;
import app.database.model.ShipPhoto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@Transactional
class ShipControllerTest extends AbstractUserTest {

    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ShipDao shipDao;
    @Mock
    OperationContext context;

    private ShipController shipController;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        shipController = new ShipController(context, userDao, shipDao);
        mvc = MockMvcBuilders.standaloneSetup(shipController).build();
        Mockito.when(context.getUserLogin()).thenReturn(user.getEmail());

        shipDao.deleteAll();
    }

    @Test
    void getRequest() throws Exception {
        var ship = new Ship();
        ship.setUser(user);
        ship.setName("ship1");
        ship.setLength(12.0);
        ship.setDraft(1.0);
        ship.setPhotos(List.of(new ShipPhoto(ship, 0, "ph1"), new ShipPhoto(ship, 1, "ph2")));
        shipDao.save(ship);

        commitAndStartNewTransaction();
        var expected = List.of(shipController.convertToDTO(ship));

        mvc.perform(get("/api/ships"))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(expected)));

        mvc.perform(get("/api/ships/" + ship.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(expected.get(0))));

        mvc.perform(get("/api/ships/" + (ship.getId() + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crud() throws Exception {
        // POST
        var dto = new ShipController.ShipDTO();
        dto.setName("ship1");
        dto.setLength(12.0);
        dto.setDraft(8.0);
        dto.setFileNames(List.of("photo1"));

        MvcResult result = mvc.perform(post("/api/ships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        var response = mapper.readValue(result.getResponse().getContentAsString(), IdResponse.class);
        dto.setId(Long.valueOf((Integer) response.getId()));
        commitAndStartNewTransaction();

        // GET
        mvc.perform(get("/api/ships/" + dto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dto)));

        // PUT
        dto.setName("122");
        dto.setFileNames(List.of("photo1", "photo2"));
        mvc.perform(put("/api/ships/" + dto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        // GET
        mvc.perform(get("/api/ships/" + dto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(dto)));


        // DELETE
        mvc.perform(delete("/api/ships/" + dto.getId()))
                .andExpect(status().isOk());
        commitAndStartNewTransaction();

        mvc.perform(get("/api/ships/" + dto.getId()))
                .andExpect(status().isNotFound());
    }
}