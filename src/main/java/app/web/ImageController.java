package app.web;


//
//@RestController
//@RequestMapping("/api/images")
//@RequiredArgsConstructor
//public class ImageController {
//
//    private final FileStorageService fileStorageService;
//
//    @PostMapping("/{imageKind}")
//    public FileLoadResponse loadImage(@PathVariable String imageKind, @RequestPart MultipartFile file) throws IOException {
//        var image = fileStorageService.saveImage(file.getBytes(), file.getResource().getFilename(), ImageKind.get(imageKind));
//        return new FileLoadResponse(image.getName());
//    }
//
//    @GetMapping(value = "/{imageKind}/{accountId}/{fileName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
//    public FileSystemResource getImage(@PathVariable String imageKind, @PathVariable Long accountId, @PathVariable String fileName) {
//        var imageFile = fileStorageService.getImageFile(ImageKind.get(imageKind), accountId, fileName).orElseThrow(NotFoundException::new);
//        return new FileSystemResource(imageFile);
//    }
//}
