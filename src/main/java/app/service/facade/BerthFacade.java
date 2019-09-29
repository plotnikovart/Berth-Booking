package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.Berth;
import app.database.repository.BerthRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.service.converters.impl.BerthConverter;
import app.web.dto.BerthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BerthFacade {

    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final PermissionService permissionService;
    private final BerthConverter converter;

    @Transactional
    public Long createBerth(BerthDto.Req dto) {
        var userInfo = userInfoRepository.findCurrent();

        var berth = new Berth().setUserInfo(userInfo);
        converter.convertToEntity(berth, dto);

        return berthRepository.save(berth).getId();
    }

    @Transactional
    public void updateBerth(Long berthId, BerthDto.Req dto) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.check(berth);

        converter.convertToEntity(berth, dto);
        berthRepository.save(berth);
    }

    @Transactional(readOnly = true)
    public BerthDto.Resp getBerth(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        return converter.convertToDto(berth);
    }

    @Transactional(readOnly = true)
    public List<BerthDto.Resp> getBerths() {
        var userInfo = userInfoRepository.findCurrent();
        var berths = userInfo.getBerths();

        return converter.convertToDtos(berths);
    }

    @Transactional
    public void deleteBerth(Long berthId) {
        var berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.check(berth);
        berthRepository.delete(berth);        // todo проверки на зависимости
    }
}
