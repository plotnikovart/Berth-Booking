package app.web;

//@RestController
//@RequestMapping("api/user_info")
//@RequiredArgsConstructor
//public class UserInfoController {
//
//    private final UserInfoFacade userInfoFacade;
//
//    @GetMapping
//    public UserInfoDto.Resp getCurrentUserInfo() {
//        Long userId = OperationContext.getAccountId();
//        return userInfoFacade.getUserInfo(userId);
//    }
//
//    @PostMapping
//    public void createUserInfo(@RequestBody UserInfoDto.Req dto) {
//        ValidationUtils.validateEntity(dto);
//        userInfoFacade.createUserInfo(dto);
//    }
//
//    @PutMapping
//    public void updateUserInfo(@RequestBody UserInfoDto.Req dto) {
//        ValidationUtils.validateEntity(dto);
//        userInfoFacade.updateUserInfo(dto);
//    }
//}
