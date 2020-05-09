package ru.hse.coursework.berth.web;

//@Transactional
//class ShipControllerTest extends AbstractAccountTest {
//
//    private MockMvc mvc;
//
//    @Autowired
//    ObjectMapper mapper;
//    @Autowired
//    ShipRepository shipRepository;
//    @Autowired
//    private ShipController shipController;
//
//    @Override
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//        mvc = MockMvcBuilders.standaloneSetup(shipController).build();
//        OperationContext.setAccountId(userInfo.getAccountId());
//
//        shipRepository.deleteAll();
//    }
//
//    @Test
//    void crud() throws Exception {
//        // POST
//        var dto = new ShipDto.Req();
//        dto.setValue("ship1");
//        dto.setLength(12.0);
//        dto.setWidth(8.0);
//        dto.setDraft(3.0);
//        dto.setPhotoList(List.of("photo1"));
//
//        MvcResult result = mvc.perform(post("/api/ships")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        var response = mapper.readValue(result.getResponse().getContentAsString(), IdResponse.class);
//
//        var dtoResp = new ShipDto.Resp()
//                .setId(Long.valueOf((Integer) response.getId()));
//        syncDto(dto, dtoResp);
//
//        commitAndStartNewTransaction();
//
//        // GET
//        mvc.perform(get("/api/ships/" + dtoResp.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));
//
//        // PUT
//        dto.setValue("122");
//        dto.setPhotoList(List.of("photo1", "photo2"));
//        syncDto(dto, dtoResp);
//
//        mvc.perform(put("/api/ships/" + dtoResp.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(dto)))
//                .andExpect(status().isOk());
//        commitAndStartNewTransaction();
//
//        // GET
//        mvc.perform(get("/api/ships/" + dtoResp.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string(mapper.writeValueAsString(dtoResp)));
//
//        // DELETE
//        mvc.perform(delete("/api/ships/" + dtoResp.getId()))
//                .andExpect(status().isOk());
//        commitAndStartNewTransaction();
//
//        // GET ALL
//        mvc.perform(get("/api/ships"))
//                .andExpect(content().json((mapper.writeValueAsString(List.of()))));
//    }
//
//    private void syncDto(ShipDto.Req req, ShipDto.Resp resp) {
//        var photoList = req.getPhotoList().stream()
//                .map(photo -> MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.SHIP.value().toLowerCase(), account.getId(), photo))
//                .collect(Collectors.toList());
//
//        resp
//                .setPhotoList(photoList)
//                .setValue(req.getValue())
//                .setLength(req.getLength())
//                .setWidth(req.getWidth())
//                .setDraft(req.getDraft());
//    }
//}