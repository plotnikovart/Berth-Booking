package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.Berth;
import app.database.entity.BerthPlace;
import app.database.entity.Convenience;
import app.database.repository.BerthRepository;
import app.database.repository.ConvenienceRepository;
import app.database.repository.UserInfoRepository;
import app.service.PermissionService;
import app.web.dto.BerthDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.ConvenienceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BerthFacade {

    private final UserInfoRepository userInfoRepository;
    private final ConvenienceRepository convenienceRepository;
    private final BerthRepository berthRepository;
    private final PermissionService permissionService;

    @Transactional
    public Long createBerth(BerthDto dto) {
        var userInfo = userInfoRepository.findCurrent();
        Berth berth = new Berth(userInfo, dto);

        if (dto.getPlaceList() != null) {
            List<BerthPlace> berthPlaces = dto.getPlaceList().stream()
                    .map(placeDto -> new BerthPlace(berth, placeDto))
                    .collect(Collectors.toList());

            berth.setBerthPlaces(berthPlaces);
        }

        if (dto.getConvenienceList() != null) {
            List<Integer> ids = dto.getConvenienceList().stream().map(ConvenienceDto::getId).collect(Collectors.toList());
            List<Convenience> conveniences = convenienceRepository.findAllById(ids);

            berth.setConveniences(conveniences);
        }

        return berthRepository.save(berth).getId();
    }

    @Transactional
    public void updateBerth(BerthDto.WithId dto) {
        Berth berth = berthRepository.findById(dto.getId()).orElseThrow(NotFoundException::new);
        permissionService.changeEntity(berth);

        berth.setDto(dto);
        berthRepository.save(berth);

        if (dto.getPlaceList() != null) {
            List<BerthPlace> places = new ArrayList<>(berth.getBerthPlaces());
            List<BerthPlaceDto.WithId> placesDto = dto.getPlaceList();

            List<BerthPlace> deleted = getDeleted(places, placesDto);
            checkForDelete(deleted);

            Map<Long, BerthPlace> idToPlace = places.stream().collect(Collectors.toMap(BerthPlace::getId, Function.identity()));
            List<BerthPlace> newPlaces = placesDto.stream().map(placeDto -> createBerthPlace(berth, idToPlace, placeDto)).collect(Collectors.toList());

            berth.setBerthPlaces(newPlaces);
        }

        if (dto.getConvenienceList() != null) {
            List<Integer> ids = dto.getConvenienceList().stream().map(ConvenienceDto::getId).collect(Collectors.toList());
            List<Convenience> conveniences = convenienceRepository.findAllById(ids);

            berth.setConveniences(conveniences);
        }
    }

    @Transactional(readOnly = true)
    public BerthDto.WithId getBerth(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        return convertToDto(berth);
    }

    @Transactional(readOnly = true)
    public List<BerthDto.WithId> getBerths() {
        var userInfo = userInfoRepository.findCurrent();
        var berths = userInfo.getBerths();

        berthRepository.loadPhotos(berths);
        berthRepository.loadConveniences(berths);
        berthRepository.loadPlaces(berths);

        return berths.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteBerth(Long berthId) {
        var berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.changeEntity(berth);
        berthRepository.delete(berth);        // todo проверки на зависимости
    }

    private List<BerthPlace> getDeleted(List<BerthPlace> berthPlaces, List<BerthPlaceDto.WithId> dtos) {
        Set<Long> newPlaces = dtos.stream().map(BerthPlaceDto.WithId::getId).collect(Collectors.toSet());
        return berthPlaces.stream().filter(place -> !newPlaces.contains(place.getId())).collect(Collectors.toList());
    }

    private void checkForDelete(List<BerthPlace> berthPlaces) {
        // todo
    }

    private BerthPlace createBerthPlace(Berth berth, Map<Long, BerthPlace> idToPlace, BerthPlaceDto.WithId dto) {
        if (dto.getId() != null) {
            var place = Optional.ofNullable(idToPlace.get(dto.getId()))
                    .orElseThrow(NotFoundException::new);

            if (!dto.equals(place.getDto())) {   // значение изменилось
                place.setDto(dto);
            }

            return place;
        } else {
            return new BerthPlace(berth, dto);
        }
    }

    private BerthDto.WithId convertToDto(Berth berth) {
        List<BerthPlace> berthPlaces = berth.getBerthPlaces();
        List<Convenience> conveniences = berth.getConveniences();

        return (BerthDto.WithId) berth.getDto()
                .setPlaceList(berthPlaces.stream().map(BerthPlace::getDto).collect(Collectors.toList()))
                .setConvenienceList(conveniences.stream().map(Convenience::getDto).collect(Collectors.toList()));
    }
}
