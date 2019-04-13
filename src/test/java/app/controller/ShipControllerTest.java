package app.controller;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
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
        shipDao.saveAndFlush(ship);

        var expected = List.of(shipController.convertToDTO(ship));
        tpl.execute(status -> {
            try {
                mvc.perform(get("/api/ships")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(mapper.writeValueAsString(expected)));
            } finally {
                return null;
            }
        });

        tpl.execute(status -> {
            try {
                mvc.perform(get("/api/ships/" + ship.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(mapper.writeValueAsString(expected.get(0))));
            } finally {
                return null;
            }
        });


        tpl.execute(status -> {
            try {
                mvc.perform(get("/api/ships/" + (ship.getId() + 1))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            } finally {
                return null;
            }
        });
    }


}