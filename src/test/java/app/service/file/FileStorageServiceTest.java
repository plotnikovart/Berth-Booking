package app.service.file;

//class FileStorageServiceTest extends AbstractAccountTest {
//
//    @Autowired
//    ShipPhotoRepository shipPhotoRepository;
//    @Autowired
//    BerthPhotoRepository berthPhotoRepository;
//    @Autowired
//    FileStorageService fileStorageService;
//
//    @Override
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//        OperationContext.setAccountId(userInfo.getAccountId());
//        Files.delete(new File(AppConfig.FILES_FOLDER_PATH));
//    }
//
//    @Test
//    void saveAndGet() throws IOException {
//        var image = new byte[]{1, 2, 120};
//        var originalFileName = "image1";
//        var imageKind = ImageKind.USER;
//
//        var file = fileStorageService.saveImage(image, originalFileName, imageKind);
//        var actual = fileStorageService.getImage(imageKind, account.getId(), file.getName());
//        ReflectionAssert.assertReflectionEquals(actual, image);
//
//        image = new byte[]{1, 2};
//        originalFileName = "image.png";
//        imageKind = ImageKind.SHIP;
//
//        file = fileStorageService.saveImage(image, originalFileName, imageKind);
//        actual = fileStorageService.getImage(imageKind, account.getId(), file.getName());
//        ReflectionAssert.assertReflectionEquals(actual, image);
//
//        Assertions.assertThrows(NotFoundException.class, () -> fileStorageService.getImage(ImageKind.SHIP, account.getId(), "1"));
//    }
//}